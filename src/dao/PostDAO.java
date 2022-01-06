package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Gender;
import beans.Photo;
import beans.Post;
import beans.Role;
import beans.Text;
import beans.User;

public class PostDAO {
	
	
	private Map<Long, Text> texts;
	private Map<Long, Photo> photos;
	private Map<String, List<Photo>> userPhotos;
	private Map<String, List<Text>> userTexts;
	public PostDAO() {
		
	
		texts = new HashMap<Long, Text>();
		photos = new HashMap<Long, Photo>();
		userPhotos = new HashMap<String, List<Photo>>();
		userTexts = new HashMap<String, List<Text>>();

		
	}
	
	public PostDAO(String path) {
		this();
		loadPosts(path);
	}
	
	public Text findOneText(Long id) {
		return texts.get(id);	
	}
	
	public Photo findOnePhoto(Long id) {
		return photos.get(id);
	}
	
	public void addOnePhoto(Photo p) {
		p.setId((long) photos.size() + texts.size());
		photos.put(p.getId(), p);
		if(userPhotos.containsKey(p.getUsernameCreator())) {
			userPhotos.get(p.getUsernameCreator()).add(p);
		}else {
			userPhotos.put(p.getUsernameCreator(), new ArrayList<Photo>());
			userPhotos.get(p.getUsernameCreator()).add(p);
		}
	}
	
	public void addOneText(Text t) {
		
		t.setId((long) texts.size() + photos.size());
		texts.put((long) t.getId(), t);
		if(userTexts.containsKey(t.getUsernameCreator())) {
			userTexts.get(t.getUsernameCreator()).add(t);
		}else {
			userTexts.put(t.getUsernameCreator(), new ArrayList<Text>());
			userTexts.get(t.getUsernameCreator()).add(t);
		}
	}
	
	public void loadPosts(String contextPath) {
		
		BufferedReader in = null;
		BufferedReader in2 = null;
		try {
			File file = new File(contextPath + "/texts.txt");
			File file2 = new File(contextPath + "/photos.txt");
			in = new BufferedReader(new FileReader(file));
			in2 = new BufferedReader(new FileReader(file2));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				//t.getId() + ";" + t.getUsernameCreator() + ";" + t.getContext() +";"+ t.isDeleted();
				while (st.hasMoreTokens()) {
					Long id = Long.parseLong(st.nextToken().trim());
					String usernameCreator = st.nextToken().trim();
					String context = st.nextToken().trim();
					boolean isDeleted = Boolean.parseBoolean(st.nextToken().trim());
					Text t = new Text(context, usernameCreator, isDeleted);
					t.setId(id);
					texts.put(id, t);
					if(userTexts.containsKey(t.getUsernameCreator())) {
						userTexts.get(t.getUsernameCreator()).add(t);
					}else {
						userTexts.put(t.getUsernameCreator(), new ArrayList<Text>());
						userTexts.get(t.getUsernameCreator()).add(t);
					}
				}
				
			}
			
			while ((line = in2.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				// p.getId() + ";" + p.getUsernameCreator() + ";" + p.getPath() +";"+ p.isDeleted();
				while (st.hasMoreTokens()) {
					Long id = Long.parseLong(st.nextToken().trim());
					String usernameCreator = st.nextToken().trim();
					String path = st.nextToken().trim();
					boolean isDeleted = Boolean.parseBoolean(st.nextToken().trim());
					Photo p = new Photo(path, usernameCreator, isDeleted);
					p.setId(id);
					photos.put(id, p);
					if(userPhotos.containsKey(p.getUsernameCreator())) {
						userPhotos.get(p.getUsernameCreator()).add(p);
					}else {
						userPhotos.put(p.getUsernameCreator(), new ArrayList<Photo>());
						userPhotos.get(p.getUsernameCreator()).add(p);
					}
				}
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
		
		
	}
	
public void savePosts(String contextPath) {
		
		BufferedWriter out = null;
		BufferedWriter out2 = null;
		try {
			File file = new File(contextPath + "/texts.txt");
			File file2 = new File(contextPath + "/photos.txt");
			out = new BufferedWriter(new FileWriter(file));
			out2 = new BufferedWriter(new FileWriter(file2));
			
			for(Text t: texts.values()) {
				out.write(createLineText(t));
				out.newLine();
			}
			
			for(Photo p: photos.values()) {
				out2.write(createLinePhoto(p));
				out2.newLine();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				}
				catch (Exception e) { }
			}
			if (out2 != null) {
				try {
					out2.close();
				}
				catch (Exception e) { }
			}
		}
		
	}
	public Post findOnePost(Long id) {
		return texts.containsKey(id) ? texts.get(id) : photos.get(id);
	}
	
	public Text changeText(Text t) {
		Text text = texts.get(t.getId());
		if(text == null) return null;
		text.setDeleted(t.isDeleted());
		text.setContext(t.getContext());
		return text;
	}
	
	public Photo changePhoto(Photo p) {
		Photo photo = photos.get(p.getId());
		if(photo == null) return null;
		photo.setDeleted(p.isDeleted());
		photo.setPath(p.getPath());
		return photo;
	}
	
	private String createLineText(Text t) {
		String s = t.getId() + ";" + t.getUsernameCreator() + ";" + t.getContext() +";"+ t.isDeleted();
		return s;
	}
	
	private String createLinePhoto(Photo p) {
		String s = p.getId() + ";" + p.getUsernameCreator() + ";" + p.getPath() +";"+ p.isDeleted();
		return s;
	}

	@Override
	public String toString() {
		return "PostDAO [texts=" + texts + ", photos=" + photos + ", userPhotos=" + userPhotos + "]";
	}
	
	public List<Photo> findAllPhotoByUser(String username){
		return  userPhotos.get(username) ;
	}
	
	public List<Text> findAllTextByUser(String username){
		return  userTexts.get(username) ;
	}
	
	
	
	
	

}
