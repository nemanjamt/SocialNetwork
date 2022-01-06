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

import beans.Comment;
import beans.FriendshipRequest;
import beans.FriendshipRequestState;
public class CommentDAO {

//	private String content;
//	private LocalDate editDate;
//	private boolean edited;
//	private LocalDate publishedDate;
//	private Long id;
//	private String usernameCreator;
//	private boolean deleted;
	private Map<Long, Comment> comments;
	public CommentDAO() {
		comments = new HashMap<Long, Comment>();
	}
	
	public CommentDAO(String contextPath) {
		this();
		loadComments(contextPath);
		
	}
	
	public Comment findOne(Long id) {
		return comments.get(id);
	}
	
	public void addOne(Comment c) {
		c.setId((long) comments.size());
		comments.put(c.getId(), c);
	}
	
	public List<Comment> findAllByPost(Long postId){
		List<Comment> commentsList = new ArrayList<Comment>();
		for (Comment comment : comments.values()) {
			if(comment.getPostId() == postId) {
				commentsList.add(comment);
			}
		}
		return commentsList;
	}
	
	public Comment changeComment(Comment c) {
		Comment comment = comments.get(c.getId());
		if(comment == null) return null;
		
		comment.setDeleted(c.isDeleted());
		if(!comment.getContent().equals(c.getContent())) {
			comment.setEdited(true);
			comment.setEditDate(LocalDateTime.now());
			comment.setContent(c.getContent());
		}
		
		return comment;
	}
	
	public void loadComments(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/comments.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
/*
 * String s = c.getId() + ";" + c.getUsernameCreator() + ";" + c.getContent() + ";" +
	c.getPublishedDate() + ";" + c.getEditDate() + ";" + c.isEdited() + ";" + c.isDeleted();*/

				while (st.hasMoreTokens()) {
					//0;9;nikola;Odlican tekst;2022-01-04T11:43:32.564047;null;false;false
					Long id = Long.parseLong(st.nextToken().trim());
					Long postId = Long.parseLong(st.nextToken().trim());
					String user = st.nextToken().trim();
					String content = st.nextToken().trim();
					LocalDateTime dt = LocalDateTime.parse(st.nextToken().trim());
					String editDate = st.nextToken().trim();
					LocalDateTime edt = editDate.equals("null") ? null : LocalDateTime.parse(editDate) ;
					boolean edited = Boolean.parseBoolean(st.nextToken().trim());
					boolean deleted = Boolean.parseBoolean(st.nextToken().trim());
					Comment c = new Comment(content, edt, edited, dt,  user, deleted, postId);
					c.setId((long) comments.size());
					comments.put(c.getId(), c);
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
			File file = new File(contextPath + "/comments.txt");
			out = new BufferedWriter(new FileWriter(file));

			

			
			for(Comment c: comments.values()) {
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
	
	public String createLine(Comment c) {
		String s = c.getId() + ";" + c.getPostId() +";"+ c.getUsernameCreator() + ";" + c.getContent() + ";" +
	c.getPublishedDate() + ";" + c.getEditDate() + ";" + c.isEdited() + ";" + c.isDeleted();
		return s;
	}
	
	public List<Comment> getCommentsByPostId(Long id){
		List<Comment> list = new ArrayList<Comment>();
		for(Comment c: comments.values()) {
			if(c.getPostId() == id) {
				list.add(c);
			}
		}
		return list;
	}
}
