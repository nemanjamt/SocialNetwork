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
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import beans.PostComment;
import beans.PostCommentRequest;
import beans.Friendship;
import beans.FriendshipRequest;
import beans.FriendshipRequestState;
import beans.Gender;
import beans.Message;
import beans.Photo;
import beans.PhotoComment;
import beans.PhotoCommentRequest;
import beans.PhotoRequest;
import beans.Post;
import beans.PostRequest;
import beans.Role;
import beans.SearchUserParam;
import beans.User;
import beans.UserPayload;
import dao.PostCommentDAO;
import dao.FriendshipDAO;
import dao.FriendshipRequestDAO;
import dao.MessageDAO;
import dao.PhotoCommentDAO;
import dao.PhotoDAO;
import dao.PostDAO;
import dao.UserDAO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javaxt.utils.Base64;
import spark.Route;

public class SocialMediaMain {
	private static String contextPath =  "./WebContent/files";
	private static UserDAO userDAO = new UserDAO();
	private static PostDAO postDAO = new PostDAO(contextPath);
	private static PhotoDAO photoDAO = new PhotoDAO(contextPath);
	private static MessageDAO messDAO = new MessageDAO(contextPath);
	private static PostCommentDAO postCommentDAO = new PostCommentDAO(contextPath);
	private static PhotoCommentDAO photoCommentDAO = new PhotoCommentDAO(contextPath);
	private static FriendshipDAO friendshipDAO = new FriendshipDAO(contextPath);
	private static FriendshipRequestDAO frequestDAO = new FriendshipRequestDAO(contextPath, friendshipDAO);
	private static Gson gson = new Gson();
	static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public static void main(String[] args) throws IOException {
		
		port(9090);
		
		
//		mockData();
		
	
		
		staticFiles.externalLocation(new File("./WebContent/static").getCanonicalPath());
		
//		post("/login", (request, response) ->{
//			UserPayload checkUser;
//			try {
//				checkUser = new Gson().fromJson(request.body(), UserPayload.class);
//			}catch(Exception e) {
//				return "losi parametri poslati";
//			}
//		
//			
//			User u = userDAO.find(checkUser.getUsername(), checkUser.getPassword());
//			if(u == null) {
//				System.out.println("nije pronasao");
//				response.status(400);
//				return "korisnik nije pronadjen";
//			}
//			String jws = Jwts.builder().setSubject(u.getUsername()).setExpiration(new Date(new Date().getTime() + 1000*60L)).setIssuedAt(new Date()).signWith(key).compact();
//			checkUser.setJwt(jws);
//			
//			return  new Gson().toJson(checkUser);
//		});
		
		/*
		 *                         USERS 
		 *
		 */
		
		post("/users", (request, response) -> {
			 response.type("application/json");
			    User user = new Gson().fromJson(request.body(), User.class);
			    if(user == null) {
			    	response.status(400);
			    	return "Bad arguments";
			    }
			    
			    boolean added = userDAO.add(user);
			    if(!added) {
			    	response.status(400);
			    	return "Bad arguments";
			    }
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
        		return "Bad arguments";
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
        
        get("/users/:user", (request, response) -> {
        	
        	response.type("application/json");
            return new Gson().toJson(userDAO.findById(request.params(":user")));
        });
        /*
         * SORT USERS
         * */
        get("/users-sort", (request, response) -> {

        	String name = request.queryParams("name");
        	String lastName = request.queryParams("lastName");
        	String startDate = request.queryParams("startDate");
        	String endDate = request.queryParams("endDate");
        	String sortParams = request.queryParams("sortBy");
        	String orderParam = request.queryParams("orderBy");
        	if(name == null || lastName == null || startDate == null || endDate == null 
        			|| sortParams == null || orderParam == null || 
        			(orderParam.toLowerCase().equals("desc") && orderParam.toLowerCase().equals("asc"))) {
        		response.status(400);
        		return "Bad arguments";
        	}
        	System.out.println(sortParams);
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

        	List<String> sortParamss = new ArrayList<String>();
        	for(String s: sortParams.split(",")) {
        		if(s.trim().equals("name") || s.trim().equals("lastName") || s.trim().equals("birthDate")) {
        			sortParamss.add(s.trim());
        		}
        	}
        	
        	response.type("application/json");
        	List<User> users =  userDAO.searchUser(params);
        	System.out.println(users);
        	userDAO.sortUser(users, sortParamss, orderParam);
        	System.out.println(users);
            return new Gson().toJson(users);
        	
        });
        
        
        put("/users", (request, response) -> {
        	User user = new Gson().fromJson(request.body(), User.class);
        	boolean res = userDAO.updateUser(user);
            return  res == true ? "success" : "false";
        });
        
        

        /*
		 *                         POSTS
		 *
		 */
        get("/posts", (request, response) ->{
     	   String id = request.queryParams("user");
     	   if(id == null) {response.status(404); return response;}
     	   List<Post> posts = postDAO.findAllActivePostByUser(id);
     	   
     	   return new Gson().toJson(posts);
        });
        
        get("/posts/allActivePosts", (request, response) ->{
     	   List<Post> posts = postDAO.findAllActivePost();
     	   return new Gson().toJson(posts);
        });
        
        get("/posts/all", (request, response) ->{
      	   List<Post> posts = postDAO.findAllPost();//pronalazi sve i izbrisane
      	   return new Gson().toJson(posts);
         });
        
        post("/posts", (request, response) ->{
        	 response.type("application/json");
     	   PostRequest p = new Gson().fromJson(request.body(), PostRequest.class);
     	   if(p == null) {
     		   response.status(400);
     		   return "Bad arguments";
     	   }
     	   
     	   System.out.println(p);

     	   boolean res = postDAO.addPost(p);
     	   if(!res) {
     		   response.status(400);
     		   return "Bad arguments";
     	   }else {
     		   response.status(201);
     	   }
     	   return "Post successful created";
        });
        
        put("/posts/:id", (request, response) ->{
       	 	response.type("application/json");
	       	Long id;
	    	try {
	    		id = Long.parseLong(request.params(":id"));
	    	}catch(Exception e) {
	    		response.status(400);
	    		return "Bad arguments-id must be number";
	    	}
	    	
		   PostRequest p = new Gson().fromJson(request.body(), PostRequest.class);
		   if(p == null) {
			   response.status(400);
			   return response;
		   }
		   
		   
	
		   boolean res = postDAO.changePost(id,p);
		   if(!res) {
			   response.status(400);
			   return "Bad arguments";
		   }else {
			   response.status(200);
		   }
		   return "Post successful changed";
       });
        
        delete("/posts/:id", (request, response) ->{
        	Long id;
        	try {
        		id = Long.parseLong(request.params(":id"));
        	}catch(Exception e) {
        		response.status(400);
        		return response;
        	}
       	   boolean success = postDAO.deletePost(id);
       	   if(success)
       		   response.status(204);
       	   else {
       		 response.status(400);
       		 return "Bad arguments";
       	   }
       		  
       	   return "success";
          });
        get("/users/:user", (request, response) -> {
        	
        	response.type("application/json");
            return new Gson().toJson(userDAO.findById(request.params(":user")));
        });
        /*
		 *                         POST COMMENTS
		 *
		 */
          
       get("/comments", (request, response) ->{
    	   
    	   Long id;
    	   try {
    		   id = Long.parseLong(request.queryParams("id"));   
    	   }catch(Exception e) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   if(id == null) {response.status(404); return "Bad arguments";}
    	   PostComment c = postCommentDAO.findOne(id);
    	   if(c == null) {response.status(404);return "Bad arguments";}
    	   return new Gson().toJson(c);
       });
       
       get("/comments/findByPost", (request, response) ->{
    	   Long id;
    	   try {
    		   id = Long.parseLong(request.queryParams("postId"));   
    	   }catch(Exception e) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   
    	   if(id == null) {response.status(404); return "Bad arguments";}
    	   List<PostComment> comments = postCommentDAO.findAllActiveCommentsByPost(id);
    	   
    	   return new Gson().toJson(comments);
       });
       
      
       
       post("/comments", (request, response) ->{
    	   response.type("application/json");
    	   PostCommentRequest req = new Gson().fromJson(request.body(), PostCommentRequest.class);
    	   if(req.getContent() == null || req.getPostId() == null || req.getUsername() == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   
    	   if(userDAO.findById(req.getUsername()) == null || postDAO.findOnePost(req.getPostId()) == null) {
    		   response.status(404);
    		   return "Entity with specified id does not exist";
    	   }
    	   PostComment p = postCommentDAO.addOne(req);
    	   response.status(200);
    	  return new Gson().toJson(p);
       });
       
       put("/comments/:id", (request, response) ->{
    	   response.type("application/json");
	       	Long id;
	    	try {
	    		id = Long.parseLong(request.params(":id"));
	    	}catch(Exception e) {
	    		response.status(400);
	    		return "Bad arguments";
	    	}
    	   response.type("application/json");
    	   PostCommentRequest req = new Gson().fromJson(request.body(), PostCommentRequest.class);
    	   if(req == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   if(req.getContent() == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   
    	   if(postCommentDAO.findOne(id) == null) {
    		   response.status(404);
    		   return "Post comment with specified id does not exist";
    	   }
    	   PostComment p = postCommentDAO.changeComment(id,req);
    	   response.status(200);
    	  return new Gson().toJson(p);
       });
       
       delete("/comments/:id", (request, response) ->{
       	Long id;
       	try {
       		id = Long.parseLong(request.params(":id"));
       	}catch(Exception e) {
       		response.status(400);
       		return "id must be number";
       	}
      	   boolean success = postCommentDAO.deleteComment(id);
      	   if(!success) {
      		   response.status(400);
      		   return "Comment with specified id does not exist";
      	   }
      		   
      	   
      	   response.status(200);
      	   return "Comment successful deleted";
         });
       
       /*
		 *                         PHOTO COMMENTS
		 *
		 */
       get("/photoComments", (request, response) ->{
    	   
    	   Long id;
    	   try {
    		   id = Long.parseLong(request.queryParams("id"));   
    	   }catch(Exception e) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   if(id == null) {response.status(400); return "Bad arguments";}
    	   PhotoComment c = photoCommentDAO.findOne(id);
    	   if(c == null) {response.status(404); return "photo comment with specified id does not exist";}
    	   return new Gson().toJson(c);
       });
       
       get("/photoComments/findByPhoto", (request, response) ->{
    	   Long id;
    	   try {
    		   id = Long.parseLong(request.queryParams("photoId"));   
    	   }catch(Exception e) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   
    	   if(id == null) {response.status(400); return "Bad arguments";}
    	   List<PhotoComment> comments = photoCommentDAO.findAllActiveCommentsByPhoto(id);
    	   
    	   return new Gson().toJson(comments);
       });
       
      
       
       post("/photoComments", (request, response) ->{
    	   response.type("application/json");
    	   PhotoCommentRequest req = new Gson().fromJson(request.body(), PhotoCommentRequest.class);
    	   if(req == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   if(req.getContent() == null || req.getPhotoId() == null || req.getUsername() == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   System.out.println(userDAO.findById(req.getUsername()));
    	   if(userDAO.findById(req.getUsername()) == null || photoDAO.getPhotoById(req.getPhotoId()) == null) {
    		   response.status(404);
    		   return "Entity with specified id does not exist";
    	   }
    	   PhotoComment p = photoCommentDAO.addOne(req);
    	   response.status(200);
    	  return new Gson().toJson(p);
       });
       
       put("/photoComments/:id", (request, response) ->{
    	   response.type("application/json");
	       	Long id;
	    	try {
	    		id = Long.parseLong(request.params(":id"));
	    	}catch(Exception e) {
	    		response.status(400);
	    		return "Bad arguments";
	    	}
    	   response.type("application/json");
    	   PhotoCommentRequest req = new Gson().fromJson(request.body(), PhotoCommentRequest.class);
    	   if(req == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   if(req.getContent() == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   
    	   if(photoCommentDAO.findOne(id) == null) {
    		   response.status(404);
    		   return "Photo comment with specified id does not exist";
    	   }
    	   PhotoComment p = photoCommentDAO.changeComment(id,req);
    	   response.status(200);
    	  return new Gson().toJson(p);
       });
       
       delete("/photoComments/:id", (request, response) ->{
       	Long id;
       	try {
       		id = Long.parseLong(request.params(":id"));
       	}catch(Exception e) {
       		response.status(400);
       		return "id must be number";
       	}
      	   boolean success = photoCommentDAO.deleteComment(id);
      	   if(!success) {
      		   response.status(404);
      		   return "Comment with specified id does not exist";
      	   }
      		   
      	   
      	   response.status(200);
      	   return "Comment successful deleted";
         });
       
       

       
       /*
		 *                         FRIEND REQUEST 
		 *
		 */
       
       get("/request", (request, response) ->{
    	   String id = request.queryParams("id");
    	   if(id == null) {response.status(400); return "Bad arguments";}
    	  
    	   Long reqId;
    	   try {
    		   reqId = Long.parseLong(id);
    	   }catch(Exception e) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   FriendshipRequest frequest = frequestDAO.findOne(reqId);
    	   if(frequest == null) {response.status(404); return "Friendship request with specified id does not exist";}
    	   return new Gson().toJson(frequest);
       });
       
       get("/request/check", (request, response) ->{
    	   String firstUser = request.queryParams("firstUser");
    	   String secondUser = request.queryParams("secondUser");
    	   if(firstUser == null || secondUser == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   return frequestDAO.checkFriendRequestExists(firstUser, secondUser);
       });
       
       get("/request/:username", (request, response) ->{
    	   String username = request.params(":username");
    	   List<FriendshipRequest> frequests = frequestDAO.findByUser(username);
    	   return new Gson().toJson(frequests);
       });
       
       post("/request", (request, response) -> {
    	   FriendshipRequest frequest = new  Gson().fromJson(request.body(), FriendshipRequest.class);
    	   if(frequest == null) { response.status(404); return "";}
    	   if(frequest.getReceiver() == null || frequest.getSender() == null) {
    		   response.status(400);
    		   return "Bad request";
    	   }
    	   if(userDAO.findById(frequest.getReceiver()) == null || userDAO.findById(frequest.getSender()) == null) {
    		   response.status(404);
    		   return "User with specified id does not exist";
    	   }
    	   if(frequestDAO.addOne(frequest) == null) {
    		   response.status(400);
    		   return "Friendship request or friendship between these two users already exists";
    	   };
    	  return "success"; 
       });
       
       put("/request", (request, response) -> {
    	   FriendshipRequest frequest = new  Gson().fromJson(request.body(), FriendshipRequest.class);
    	   if(frequest == null) {
    		   response.status(400);
    		   return "Bad request";
    	   }
    	   if(frequest.getId() == null || frequest.getState() == null) {
    		   response.status(400);
    		   return "Bad request";
    	   }
    	   if(frequestDAO.findOne(frequest.getId()) == null) {
    		   response.status(404);
    		   return "Friendship request with specified id does not exist";
    	   }
    	   frequestDAO.changeStatus(frequest.getId(), frequest.getState());
    	  return  "success"; 
       });
       
       /*
		 *                         Friendship
		 *
		 */
       get("/friendship", (request, response) ->{
    	   response.type("application/json");
    	   Long id;
    	   try {
    		   id = Long.parseLong(request.queryParams("id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments";
			}
    	   Friendship f = friendshipDAO.getById(id);
    	   if(f == null) {
    		   response.status(404);
    		   return response;
    	   }
    	   return f;
       });
       
       get("/friendship/checkFriendship", (request, response) ->{
     	  String firstUser = request.queryParams("firstUser");
     	  String secondUser = request.queryParams("secondUser");
     	  if(firstUser == null || secondUser == null) {
     		  response.status(400);
     		  return "Bad request";
     	  }
     	  return friendshipDAO.checkFriendship(firstUser, secondUser);
        });
       
       get("/friendship/:user", (request, response) ->{
    	   response.type("application/json");
    	   String user = request.params(":user");
    	   
    	   if(userDAO.findById(user) == null) {
    		   response.status(404);
    		   return response;
    	   }
    	   List<Friendship> f = friendshipDAO.getAllByUser(user);
    	   
    	   return f;
       });
       
       post("/friendship", (request, response) -> {
    	   response.type("application/json");
    	   FriendshipRequest f = new Gson().fromJson(request.body(), FriendshipRequest.class);
    	   if(f == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   if(f.getId() == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   
    	   if(frequestDAO.findOne(f.getId()) == null) {
    		   response.status(404);
    		   return "Friendship request with specified id does not exist";
    	   }
    	   if(frequestDAO.findOne(f.getId()).getState() != FriendshipRequestState.ACCEPTED) {
    		   response.status(400);
    		   return "Friendship request is not accepted";
    	   }
    	   
    	   Friendship friendship = friendshipDAO.addOne(frequestDAO.findOne(f.getId()));
    	   return friendship;
       });
       
       delete("friendship/:id", (request, response) -> {
    	   response.type("application/json");
    	   Long id;
    	   try {
    		   id = Long.parseLong(request.params(":id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments";
			}
    	   if(friendshipDAO.getById(id) == null) {
    		   response.status(404);
    		   return "Friendship with specified id does not exist";
    	   }
    	   
    	   friendshipDAO.deleteFriendship(id);
    	   return "Friendship successful deleted"; 
       });
       
       /*
		 *                         PHOTO
		 *
		 */
       
       
       post("/photo",(request,response) ->{
    	   System.out.println("POGODIO");
    	   System.out.println(System.currentTimeMillis());
    	   PhotoRequest p = new Gson().fromJson(request.body(), PhotoRequest.class);
    	   if(p == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   if(p.getContent() == null  || p.getUsername() == null) {
    		   response.status(400);
    		   return "Bad arguments";
    	   }
    	   
    	   if(userDAO.findById(p.getUsername()) == null) {
    		   response.status(404);
    		   return "User with specified id does not exists";
    	   }
			
			return new Gson().toJson(photoDAO.createPhoto(p));
		});
        
       get("/photo", (request, response) ->{
    	   Long id ;
    	   try {
    		   id = Long.parseLong( request.queryParams("id"));
    	   }catch(Exception e) {
    		   response.status(400);
    		   return null;
    	   }
    	   if(id == null) {response.status(400); return "";}
    	  
    	   Photo p = photoDAO.getPhotoById(id);
    	   if(p == null)
    		   response.status(404);
    	   return new Gson().toJson(p);
       });
       
       get("/photo/getByUser", (request, response) ->{
    	   String username = request.queryParams("username");
    	   if(username == null) {
    		   response.status(400);
    		   return "";
    	   }
    	   List<Photo> photos = photoDAO.getAllPhotoByUser(username);
    	   return new Gson().toJson(photos);
       });
       
       /*moze se mijenjati samo tekst tj opis slike
        * */
       
       put("/photo/:id", (request, response) ->{
    	   System.out.println("USLO");
    	   response.type("application/json");
    	   Long id ;
    	   try {
    		   id = Long.parseLong( request.params(":id"));
    	   }catch(Exception e) {
    		   response.status(400);
    		   return null;
    	   }
    	   if(photoDAO.getPhotoById(id) == null) {
    		  
    		   response.status(404);
    		   return null;
    	   }
    	   PhotoRequest req = new  Gson().fromJson(request.body(), PhotoRequest.class);
    	   if(req == null) {
    		   response.status(400);
    		   return null;
    	   }
    	   if(req.getText() == null) {
    		   System.out.println("OVDE JE");
    		   response.status(400);
    		   return null;
    	   }
    	   return photoDAO.changePhoto(id, req);
       });
       
       delete("/photo/:id", (request,response) ->{
    	   response.type("application/json");
    	   Long id ;
    	   try {
    		   id = Long.parseLong( request.params(":id"));
    	   }catch(Exception e) {
    		   response.status(400);
    		   return null;
    	   }
    	   if(photoDAO.getPhotoById(id) == null) {
    		  
    		   response.status(404);
    		   return null;
    	   }
    	   photoDAO.deletePhoto(id);
    	  return "photo successful deleted"; 
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
