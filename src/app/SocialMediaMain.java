package app;

import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.staticFiles;
import static spark.Spark.before;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import beans.Comment;
import beans.FriendshipRequest;
import beans.FriendshipRequestState;
import beans.Gender;
import beans.Message;
import beans.Photo;
import beans.Post;
import beans.Role;
import beans.SearchUserParam;
import beans.Text;
import beans.User;
import beans.UserPayload;
import dao.CommentDAO;
import dao.FriendshipRequestDAO;
import dao.MessageDAO;
import dao.PostDAO;
import dao.UserDAO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import spark.Route;

public class SocialMediaMain {
	private static String contextPath =  "./WebContent/files";
	private static UserDAO userDAO = new UserDAO();
	private static PostDAO postDAO = new PostDAO(contextPath);
	private static MessageDAO messDAO = new MessageDAO(contextPath);
	private static CommentDAO commDAO = new CommentDAO(contextPath);
	private static FriendshipRequestDAO frequestDAO = new FriendshipRequestDAO(contextPath, userDAO);
	private static Gson gson = new Gson();
	static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public static void main(String[] args) throws IOException {
		
		port(9090);
		
		
//		mockData();
		
	
		
		staticFiles.externalLocation(new File("./WebContent/static").getCanonicalPath());
		
		post("/login", (request, response) ->{
			UserPayload checkUser;
			try {
				checkUser = new Gson().fromJson(request.body(), UserPayload.class);
			}catch(Exception e) {
				return "losi parametri poslati";
			}
		
			
			User u = userDAO.find(checkUser.getUsername(), checkUser.getPassword());
			if(u == null) {
				System.out.println("nije pronasao");
				response.status(400);
				return "korisnik nije pronadjen";
			}
			String jws = Jwts.builder().setSubject(u.getUsername()).setExpiration(new Date(new Date().getTime() + 1000*60L)).setIssuedAt(new Date()).signWith(key).compact();
			checkUser.setJwt(jws);
			
			return  new Gson().toJson(checkUser);
		});
		
		
		post("/users", (request, response) -> {
			 response.type("application/json");
			    User user = new Gson().fromJson(request.body(), User.class);
			    userDAO.add(user);
			    return "success";
	          
	        });
        get("/users", (request, response) -> {
        	
        	response.type("application/json");
        	Collection<User> users =  userDAO.findAll();
            return new Gson().toJson(users);
        });
        
        get("/search-users", (request, response) -> {
        	System.out.println("SEARCUJE");
        	String name = request.queryParams("name");
        	String lastName = request.queryParams("lastName");
        	String startDate = request.queryParams("startDate");
        	String endDate = request.queryParams("endDate");
        	if(name == null || lastName == null || startDate == null || endDate == null) {
        		response.status(400);
        		return response;
        	}
        	System.out.println(name);
        	System.out.println(lastName);
        	SearchUserParam params = new  SearchUserParam();
        	try {
        		long startBirthDate = Long.parseLong(startDate);
        		params.setStartBirthDate(startBirthDate);
        	}catch(Exception e) {
        		
        	}
        	
        	try {
        		long endBirthDate = Long.parseLong(endDate);
        		params.setEndBirthDate(endBirthDate);
        	}catch(Exception e) {
        		
        	}
        	
        	
        	if(!name.equals(""))
        		params.setName(name.toLowerCase().trim());
        	if(!lastName.equals(""))
        		params.setLastName(lastName.toLowerCase().trim());


        	response.type("application/json");
        	Collection<User> users =  userDAO.searchUser(params);
        	System.out.println(users);
            return new Gson().toJson(users);
        });
        
        get("/users/:id", (request, response) -> {
        	response.type("application/json");
            return new Gson().toJson(userDAO.findById(request.params(":id")));
        });
        
        put("/users", (request, response) -> {
        	User user = new Gson().fromJson(request.body(), User.class);
        	boolean res = userDAO.updateUser(user);
            return  res == true ? "success" : "false";
        });
        
        
        post("/posts/text", (request, response) -> {
			 response.type("application/json");
			 Text text = new Gson().fromJson(request.body(), Text.class);
			 postDAO.addOneText(text);
			 
			    return "success";
	          
	        });
        
        
        
        post("/posts/photo", (request, response) -> {
        	
			 response.type("application/json");
			 Photo photo = new Gson().fromJson(request.body(), Photo.class);
			 postDAO.addOnePhoto(photo);
			 return "success"; 
	        
        });
        
        
        get("/posts/photo", (request, response) -> {
     	   String idPhoto = request.queryParams("id");
     	   if(idPhoto == null) { response.status(404); return "";}
     	   Long id = Long.parseLong(idPhoto);
     	   
     	  return new Gson().toJson(postDAO.findOnePhoto(id)); 
        });
        
        get("/posts/text", (request, response) -> {
     	   String idText = request.queryParams("id");
     	   if(idText == null) { response.status(404); return "";}
     	   Long id = Long.parseLong(idText);
     	   
     	  return new Gson().toJson(postDAO.findOneText(id)); 
        });
        
        put("/posts/text", (request, response) -> {
        	
     	   Text text = new Gson().fromJson(request.body(), Text.class);
     	   Text res = postDAO.changeText(text);
     	   return  new Gson().toJson(res);
        });
        
        put("/posts/photo", (request, response) -> {
           Photo photo = new Gson().fromJson(request.body(), Photo.class);
     	   Photo res = postDAO.changePhoto(photo);
            return  new Gson().toJson(res);
           });
       
       
       get("/posts/photos/:username", (request, response) -> {
    	  
    	  String username = request.params(":username");
    	  if(username ==null) { response.status(404);  return "";}
    	  response.type("application/json");
    	  List<Photo> posts = postDAO.findAllPhotoByUser(username);
          return new Gson().toJson(posts);
    	  
       });
       
       get("/posts/texts/:username", (request, response) -> {
     	  
     	  String username = request.params(":username");
     	  if(username ==null) { response.status(404);  return "";}
     	  response.type("application/json");
     	  List<Text> posts = postDAO.findAllTextByUser(username);
           return new Gson().toJson(posts);
     	  
        });
       
     
          
       get("/comment", (request, response) ->{
    	   String id = request.queryParams("id");
    	   if(id == null) {response.status(404); return "";}
    	   Comment c = commDAO.findOne(Long.parseLong(id));
    	   
    	   return new Gson().toJson(c);
       });
       
       
       post("/comment", (request, response) -> {
    	   Comment comment = new  Gson().fromJson(request.body(), Comment.class);
    	   if(comment == null) { response.status(404); return "";}
    	   commDAO.addOne(comment);
    	   postDAO.findOnePost(comment.getPostId()).getComments().add(comment);
    	  return ""; 
       });
       
       put("/comment", (request, response) -> {
    	   Comment comment = new  Gson().fromJson(request.body(), Comment.class);
    	   if(comment == null) { response.status(404); return "";}
    	   Comment c = commDAO.changeComment(comment);
    	  return  new Gson().toJson(c); 
       });
       
       
       get("/request", (request, response) ->{
    	   String id = request.queryParams("id");
    	   if(id == null) {response.status(404); return "";}
    	  
    	   FriendshipRequest frequest = frequestDAO.findOne(Long.parseLong(id));
    	   return new Gson().toJson(frequest);
       });
       
       get("/request/:username", (request, response) ->{
    	   String username = request.params(":username");
    	   List<FriendshipRequest> frequests = frequestDAO.findByUser(username);
    	   return new Gson().toJson(frequests);
       });
       
       post("/request", (request, response) -> {
    	   FriendshipRequest frequest = new  Gson().fromJson(request.body(), FriendshipRequest.class);
    	   if(frequest == null) { response.status(404); return "";}
    	   frequestDAO.addOne(frequest);
    	  return "success"; 
       });
       
       put("/request", (request, response) -> {
    	   FriendshipRequest frequest = new  Gson().fromJson(request.body(), FriendshipRequest.class);
    	   frequestDAO.changeStatus(frequest.getId(), frequest.getState());
    	  return  "success"; 
       });
        
        

		
		
	}
	
	

//	private static void mockData() {
//		User u = new User("nikola","nikola123","nikola99@gmail.com","Nikola","Markovic",LocalDate.of(1999, 12, 28), Gender.MALE, Role.ORDINARY,
//				"putanja neke slike", true, false);
//		userDAO.add(u);
//		User u2 = new User("marko","marko123","marko2020@gmail.com","Marko","Bojanic",LocalDate.of(1980, 8, 16), Gender.MALE, Role.ADMIN,
//				"putanja neke slike", false, false);
//		
//		User u3 = new User("petar","petar123","pero333@gmail.com","Petar","Peric",LocalDate.of(2000, 1, 8), Gender.MALE, Role.ORDINARY,
//				"putanja neke slike", false, true);
//		
//		User u4 = new User("jovana","jovana123","jole333@gmail.com","Jovana","Jovanovic",LocalDate.of(1999, 6, 26), Gender.FEMALE, Role.ORDINARY,
//				"putanja neke slike", false, false);
//		userDAO.add(u);
//		userDAO.add(u2);
//		userDAO.add(u3);
//		userDAO.add(u4);
//		
//		
//		
//		Photo p = new Photo("neka putanja","nikola", false);
//		Photo p2 = new Photo("neka putanja","petar", false);
//		Photo p3 = new Photo("neka putanja","petar", true);
//		postDAO.addOnePhoto(p);
//		postDAO.addOnePhoto(p2);
//		postDAO.addOnePhoto(p3);
//		
//		
//		Text t = new Text("Neki sadrzaj ", "nikola", false);
//		Text t2 = new Text("Neki sadrzaj ", "nikola", true);
//		Text t3 = new Text("Neki sadrzaj ", "petar", false);
//		postDAO.addOneText(t);
//		postDAO.addOneText(t2);
//		postDAO.addOneText(t3);
//		
//		
//		Message m = new Message("nikola","marko","cao",LocalDateTime.now());
//		Message m2 = new Message("marko","nikola","cao",LocalDateTime.now());
//		Message m3 = new Message("marko","nikola","sta radis?",LocalDateTime.now());
//		messDAO.add(m);
//		messDAO.add(m2);
//		messDAO.add(m3);
//		
//		
//		FriendshipRequest f = new FriendshipRequest(FriendshipRequestState.WAITING, LocalDate.now(), "nikola", "marko");
//		FriendshipRequest f2 = new FriendshipRequest(FriendshipRequestState.DENIED, LocalDate.now(), "nikola", "petar");
//		frequestDAO.addOne(f2);
//		frequestDAO.addOne(f);
//		
//		
//		Comment c = new Comment("Odlican tekst", null,false,  LocalDateTime.now(), "nikola", false, t.getId());
//		commDAO.addOne(c);
//		postDAO.findOnePost(c.getPostId()).getComments().add(c);
//		
//		userDAO.addFriendship(u.getUsername(), u3.getUsername());
//		userDAO.addFriendship(u.getUsername(), u4.getUsername());
//		userDAO.saveUsers(contextPath);
//		postDAO.savePosts(contextPath);
//		messDAO.saveMessages(contextPath);
//		frequestDAO.saveRequests("./WebContent");
//		commDAO.saveComments("./WebContent");
//		
//	}
}
