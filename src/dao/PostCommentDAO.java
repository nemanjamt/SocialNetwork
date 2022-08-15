package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.PostComment;
import beans.PostCommentRequest;
import beans.FriendshipRequest;
import beans.FriendshipRequestState;
public class PostCommentDAO {


	private Map<Long, PostComment> postComments;
	public PostCommentDAO() {
		postComments = new HashMap<Long, PostComment>();
	}
	
	public PostCommentDAO(String contextPath) {
		this();
		loadComments(contextPath);
		
	}
	
	public PostComment findOne(Long id) {
		PostComment c = postComments.get(id);
		if(c == null)
			return null;
		if(c.isDeleted())
			return null;
		return c;
	}
	
	
	
	public PostComment addOne(PostCommentRequest req) {
		
		PostComment p = new PostComment();
		p.setContent(req.getContent());
		p.setDeleted(false);
		p.setEditDate(null);
		p.setPublishedDate(System.currentTimeMillis());
		p.setPostId(req.getPostId());
		p.setEdited(false);
		p.setUsernameCreator(req.getUsername());
		p.setId((long) (postComments.size()+1));
		
		postComments.put(p.getId(), p);
		saveComments("./WebContent/files");
		return p;
	}
	
	public List<PostComment> findAllActiveCommentsByPost(Long postId){
		List<PostComment> commentsList = new ArrayList<PostComment>();
		postComments.values().stream().filter(c -> (c.getPostId() == postId && (!c.isDeleted()))).forEach(c -> commentsList.add(c));
		
		return commentsList;
	}
	
	public PostComment changeComment(Long id, PostCommentRequest c) {
		PostComment comment = postComments.get(id);
		if(comment == null) return null;
		if(comment.isDeleted()) return null;

		if(!comment.getContent().equals(c.getContent())) {
			comment.setEdited(true);
			comment.setEditDate(System.currentTimeMillis());
			comment.setContent(c.getContent());
		}else return null;
		saveComments("./WebContent/files");
		return comment;
	}
	
	public boolean deleteComment(Long id) {
		PostComment comment = postComments.get(id);
		if(comment == null) return false;
		if(comment.isDeleted()) return false;
		comment.setDeleted(true);
		saveComments("./WebContent/files");
		return true;
	}
	
	public void loadComments(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/postComments.txt");
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
					Long postId = Long.parseLong(st.nextToken().trim());
					String user = st.nextToken().trim();
					String content = st.nextToken().trim();
					Long dt = Long.valueOf(st.nextToken().trim());

					Long edt = Long.valueOf(st.nextToken().trim()) ;
					boolean edited = Boolean.parseBoolean(st.nextToken().trim());
					boolean deleted = Boolean.parseBoolean(st.nextToken().trim());
					PostComment c = new PostComment(content, edt, edited, dt,id,  user, deleted, postId);
					
					postComments.put(c.getId(), c);
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
	
	public void saveComments(String contextPath) {
		BufferedWriter out = null;
		try {
			File file = new File(contextPath + "/postComments.txt");
			out = new BufferedWriter(new FileWriter(file));

			for(PostComment c: postComments.values()) {
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
	
	public String createLine(PostComment c) {
		String s = c.getId() + ";" + c.getPostId() +";"+ c.getUsernameCreator() + ";" + c.getContent() + ";" +
	c.getPublishedDate() + ";" + c.getEditDate() + ";" + c.isEdited() + ";" + c.isDeleted();
		return s;
	}
	
	public List<PostComment> getCommentsByPostId(Long id){
		List<PostComment> list = new ArrayList<PostComment>();
		for(PostComment c: postComments.values()) {
			if(c.getPostId() == id) {
				list.add(c);
			}
		}
		return list;
	}
}
