package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.PhotoComment;
import beans.PhotoCommentRequest;
import beans.PostCommentRequest;

public class PhotoCommentDAO {

	private Map<Long, PhotoComment> photoComments;
	private String contextPath;
	private static PhotoCommentDAO dao;
	private PhotoCommentDAO() {
		this.photoComments = new HashMap<>();
	}
	
	private PhotoCommentDAO(String contextPath) {
		this();
		this.contextPath = contextPath;
		loadPhotoComment(contextPath);
	}
	
	public static PhotoCommentDAO getInstance(String contextPath) {
		if(dao == null) {
			dao = new PhotoCommentDAO(contextPath);
		}
		return dao;
	}
	
	public PhotoComment findOne(Long id) {
		PhotoComment c = photoComments.get(id);
		if(c == null)
			return null;
		if(c.isDeleted())
			return null;
		return c;
	}
	
	
	
	public PhotoComment addOne(PhotoCommentRequest req) {
		
		PhotoComment p = new PhotoComment();
		p.setContent(req.getContent());
		p.setDeleted(false);
		p.setEditDate(null);
		p.setPublishedDate(LocalDateTime.now());
		p.setPhotoId(req.getPhotoId());
		p.setEdited(false);
		p.setUsernameCreator(req.getUsername());
		p.setId((long) (photoComments.size()+1));
		
		photoComments.put(p.getId(), p);
		savePhotoComments("./WebContent/files");
		return p;
	}
	
	public List<PhotoComment> findAllActiveCommentsByPhoto(Long photoId){
		List<PhotoComment> commentsList = new ArrayList<PhotoComment>();
		photoComments.values().stream().filter(c -> (c.getPhotoId() == photoId && (!c.isDeleted()))).forEach(c -> commentsList.add(c));
		
		return commentsList;
	}
	
	public PhotoComment changeComment(Long id, PhotoCommentRequest c) {
		PhotoComment comment = photoComments.get(id);
		if(comment == null) return null;
		if(comment.isDeleted()) return null;

		if(!comment.getContent().equals(c.getContent())) {
			comment.setEdited(true);
			comment.setEditDate(LocalDateTime.now());
			comment.setContent(c.getContent());
		}else return null;
		savePhotoComments("./WebContent/files");
		return comment;
	}
	
	public boolean deleteComment(Long id) {
		PhotoComment comment = photoComments.get(id);
		if(comment == null) return false;
		if(comment.isDeleted()) return false;
		comment.setDeleted(true);
		savePhotoComments("./WebContent/files");
		return true;
	}
	
	public void loadPhotoComment(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/photoComments.txt");
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
					Long photoId = Long.parseLong(st.nextToken().trim());
					String user = st.nextToken().trim();
					String content = st.nextToken().trim();
					LocalDateTime dt = LocalDateTime.parse(st.nextToken().trim());
					String editDate = st.nextToken().trim();
					LocalDateTime edt = editDate.equals("null") ? null : LocalDateTime.parse(editDate) ;
					boolean edited = Boolean.parseBoolean(st.nextToken().trim());
					boolean deleted = Boolean.parseBoolean(st.nextToken().trim());

					PhotoComment c = new PhotoComment(content, edt, edited, dt,id,  user, deleted, photoId);
					photoComments.put(c.getId(), c);
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
	
	public void savePhotoComments(String contextPath) {
		BufferedWriter out = null;
		try {
			File file = new File(contextPath + "/photoComments.txt");
			out = new BufferedWriter(new FileWriter(file));

			for(PhotoComment c: photoComments.values()) {
				out.write(createLine(c));
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
	
	public String createLine(PhotoComment c) {
		String s = c.getId() + ";" + c.getPhotoId() +";"+ c.getUsernameCreator() + ";" + c.getContent() + ";" +
				c.getPublishedDate() + ";" + c.getEditDate() + ";" + c.isEdited() + ";" + c.isDeleted();
		return s;
	}
	
}
