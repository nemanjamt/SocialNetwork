package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.google.gson.Gson;

import beans.Photo;
import beans.PhotoRequest;
import beans.Post;
import javaxt.utils.Base64;

public class PhotoDAO {

	private Map<Long, Photo> photos;
	private String contextPath;
	public PhotoDAO() {
		this.photos = new HashMap<>();
	}
	
	public PhotoDAO(String contextPath) {
		this();
		this.contextPath = contextPath;
		loadPhotos(contextPath);
		System.out.println(photos);
	}
	
	
	public void addPhoto(Photo p) {
		photos.put(p.getId(), p);
	}
	
	public Photo getPhotoById(Long id) {
		Photo p = photos.get(id);
		if(p == null) return null;
		if(p.isDeleted()) return null;
		return p;
	}
	
	public List<Photo> getAllPhotoByUser(String username){
		List<Photo> userPhotos = new ArrayList<>();
		photos.values().stream().filter(p -> (!p.isDeleted()) && p.getUsernameCreate().equals(username)).forEach(p -> userPhotos.add(p));
		return userPhotos;
	}
	
	public Photo createPhoto(PhotoRequest req) {

 	  
 	   Photo p = new Photo();
 	   p.setDate(System.currentTimeMillis());
 	   p.setDeleted(false);
 	   p.setText(req.getText());
 	   p.setUsernameCreate(req.getUsername());
 	   p.setId((long) (photos.size()+1));
		byte[] imgBytes = Base64.decode(req.getContent());
		long name = 100;
		String fileName = p.getId().toString().concat(".png");
		File f = new File("WebContent/static/userImages//".concat(fileName));
		p.setPath(fileName);

		FileOutputStream osf;
		try {
			osf = new FileOutputStream(f);
			osf.write(imgBytes);
			osf.flush();
			osf.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		
		photos.put(p.getId(), p);
		savePhotos(this.contextPath);
		return p;
	}
	
	public Photo changePhoto(Long id,PhotoRequest req) {
		Photo p = photos.get(id);
		if(!p.getText().equals(req.getText())) {
			p.setText(req.getText());
		}
		savePhotos(this.contextPath);
		return p;
	}
	
	public void deletePhoto(Long id) {
		Photo p = photos.get(id);
		p.setDeleted(true);
		savePhotos(this.contextPath);
		
	}
	
	
	public void loadPhotos(String contextPath) {
		
		BufferedReader in = null;

		try {
			File file = new File(contextPath + "/photos.txt");

			in = new BufferedReader(new FileReader(file));

			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				
				while (st.hasMoreTokens()) {
					Long id = Long.parseLong(st.nextToken().trim());
					String path = st.nextToken().trim();
					String username = st.nextToken().trim();
					String text = st.nextToken().trim();
					boolean deleted = Boolean.parseBoolean(st.nextToken().trim());
					Long date = Long.parseLong(st.nextToken().trim());
					
					Photo p = new Photo(id,path,username,deleted,date,text);
					photos.put(p.getId(), p);

					
					System.out.println(p);
					
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
	
	public void savePhotos(String contextPath) {
		BufferedWriter out = null;
		
		try {
			File file = new File(contextPath + "/photos.txt");

			out = new BufferedWriter(new FileWriter(file));

			
			for(Photo p: photos.values()) {
				out.write(createLine(p));
				out.newLine();
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
			
		}
	}
	
	public String createLine(Photo p) {
		return p.getId()+";"+p.getPath()+";"+p.getUsernameCreate()+";"+p.getText()+";"+p.isDeleted()+";"+p.getDate();
	}
}
