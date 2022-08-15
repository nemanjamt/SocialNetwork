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

import beans.PostComment;
import beans.Gender;
import beans.Photo;
import beans.Post;
import beans.PostRequest;
import beans.Role;
import beans.User;
import javaxt.utils.Base64;

public class PostDAO {
	
	
	private Map<Long, Post> posts;
	private Map<String, List<Post>> usersPost;
	public PostDAO() {
		
	
		posts = new HashMap<>();
		usersPost = new HashMap<String, List<Post>>();
		
	}
	
	public PostDAO(String path) {
		this();
		loadPosts(path);
		System.out.println(posts);
	}
	
//	public Text findOneText(Long id) {
//		return texts.get(id);	
//	}
//	
//	public Photo findOnePhoto(Long id) {
//		return photos.get(id);
//	}
//	
//	public void addOnePhoto(Photo p) {
//		p.setId((long) photos.size() + texts.size());
//		photos.put(p.getId(), p);
//		if(userPhotos.containsKey(p.getUsernameCreator())) {
//			userPhotos.get(p.getUsernameCreator()).add(p);
//		}else {
//			userPhotos.put(p.getUsernameCreator(), new ArrayList<Photo>());
//			userPhotos.get(p.getUsernameCreator()).add(p);
//		}
//	}
//	
//	public void addOneText(Text t) {
//		
//		t.setId((long) texts.size() + photos.size());
//		texts.put((long) t.getId(), t);
//		if(userTexts.containsKey(t.getUsernameCreator())) {
//			userTexts.get(t.getUsernameCreator()).add(t);
//		}else {
//			userTexts.put(t.getUsernameCreator(), new ArrayList<Text>());
//			userTexts.get(t.getUsernameCreator()).add(t);
//		}
//	}
	
	public void loadPosts(String contextPath) {
		
		BufferedReader in = null;
		BufferedReader in2 = null;
		try {
			File file = new File(contextPath + "/posts.txt");
//			File file2 = new File(contextPath + "/photos.txt");
			in = new BufferedReader(new FileReader(file));
//			in2 = new BufferedReader(new FileReader(file2));
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
					Long date = Long.parseLong(st.nextToken().trim());
					String usernameCreator = st.nextToken().trim();
					String imageName = st.nextToken().trim();
					String text = st.nextToken().trim();
					
					boolean isDeleted = Boolean.parseBoolean(st.nextToken().trim());

//					Post(String usernameCreator, Long id, boolean deleted, String name, String postText)
					Post p = new Post(usernameCreator,id,isDeleted,imageName, text, date);
					posts.put(id, p);
					if(usersPost.containsKey(p.getUsernameCreator())) {
						usersPost.get(p.getUsernameCreator()).add(p);
					}else {
						usersPost.put(p.getUsernameCreator(), new ArrayList<Post>());
						usersPost.get(p.getUsernameCreator()).add(p);
					}
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
	
public void savePosts(String contextPath) {
		
		BufferedWriter out = null;
		
		try {
			File file = new File(contextPath + "/posts.txt");

			out = new BufferedWriter(new FileWriter(file));

			
			for(Post p: posts.values()) {
				out.write(createLinePost(p));
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

	public Post findOnePost(Long id) {
		return posts.get(id);
	}
	
	
	public boolean addPost(PostRequest req) {
		if(req.getDate() == null || req.getText() == null || req.getUsername() == null)
			return false;
		Post p = new Post();
		p.setDate(req.getDate());
		if(req.getImagePath() != null) {
			System.out.println(req.getImagePath());
			byte[] imgBytes = Base64.decode(req.getImagePath());
			Long name = 100L;
			File f = new File("WebContent/static/userImages//" + 100 + ".png");
			
			while(f.exists()) {
				name++;
				f = new File("WebContent/static/userImages//" + name + ".png");
			}
			
			FileOutputStream osf;
			try {
				osf = new FileOutputStream(f);
				osf.write(imgBytes);
				osf.flush();
				osf.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			req.setImagePath(name.toString()+".png");
		}
		p.setPictureName(req.getImagePath());
		p.setPostText(req.getText());
		p.setUsernameCreator(req.getUsername());
		p.setComments(new ArrayList<PostComment>());
		p.setDeleted(false);
		p.setId((long) (posts.size()+1));
		posts.put(p.getId(), p);
		savePosts("./WebContent/files");
		return true;
		
	}


	private String createLinePost(Post p) {
		String s = p.getId() + ";" + p.getDate()+";"+ p.getUsernameCreator() + ";" +p.getPictureName()+";"+ p.getPostText() +";"+ p.isDeleted();
		return s;
	}

	
	public List<Post> findAllPostUser(String username){
		return usersPost.get(username) ;
	}
	
	public List<Post> findAllActivePostByUser(String username){
		if(!usersPost.containsKey(username)) {
			return null;
		}
		List<Post> posts = new ArrayList<>();
		for(Post p: usersPost.get(username)) {
			if(!p.isDeleted())
				posts.add(p);
		}
		if(posts.isEmpty())
			return null;
		return posts;
	}
	
	public List<Post> findAllPost(){
		List<Post> allPosts = new ArrayList<>();
		usersPost.values().forEach(p -> p.forEach(post -> allPosts.add(post)));
		return allPosts;
	}
	
	public List<Post> findAllActivePost(){
		List<Post> allPosts = new ArrayList<>();
		usersPost.values().forEach(p -> p.stream().filter(post -> !post.isDeleted()).forEach(post -> allPosts.add(post)));
		return allPosts;
	}
	
	public boolean deletePost(Long postId) {
		if(!posts.containsKey(postId))
			return false;
		posts.get(postId).setDeleted(true);
		savePosts("./WebContent/files");
		return true;
	}
	
	public boolean changePost(Long postId, PostRequest req) {
		if(!posts.containsKey(postId))
			return false;
		Post p = posts.get(postId);
		p.setPostText(req.getText());
		savePosts("./WebContent/files");
		return true;
	}

	
	
	
	
	
	

}
