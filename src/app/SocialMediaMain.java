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
import beans.LoginRequest;
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
import ws.WsHandler;
import static spark.Spark.webSocket;

public class SocialMediaMain {
	private static String contextPath = "./WebContent/files";

	private static PostDAO postDAO = new PostDAO(contextPath);
	private static PhotoDAO photoDAO = new PhotoDAO(contextPath);
	private static MessageDAO messDAO = MessageDAO.getInstance();
	private static PostCommentDAO postCommentDAO = new PostCommentDAO(contextPath);
	private static PhotoCommentDAO photoCommentDAO = new PhotoCommentDAO(contextPath);
	private static FriendshipDAO friendshipDAO = new FriendshipDAO(contextPath);
	private static UserDAO userDAO = new UserDAO(contextPath, friendshipDAO);
	private static FriendshipRequestDAO frequestDAO = new FriendshipRequestDAO(contextPath, friendshipDAO);
	private static Gson gson = new Gson();
	static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public static void main(String[] args) throws IOException {

		port(9090);
		webSocket("/ws", WsHandler.class);
//		mockData();

		staticFiles.externalLocation(new File("./WebContent/static").getCanonicalPath());

		post("/login", (request, response) -> {
			if (request.session().attribute("currentUser") != null) {
				response.status(406);
				return "already logged in";
			}
			LoginRequest l = new Gson().fromJson(request.body(), LoginRequest.class);
			if (l == null) {
				response.status(400);
				return "Bad request";
			} else if (l.getUsername() == null || l.getPassword() == null) {
				response.status(400);
				return "Bad request";
			}
			User u = userDAO.find(l.getUsername(), l.getPassword());
			if (u == null) {
				response.status(401);
				return "Bad credentials";
			} else {
				request.session().attribute("currentUser", u);
			}
			return "successful login";
		});

		get("/currentUser", (request, response) -> {
			response.type("application/json");
			User u = (User) request.session().attribute("currentUser");
			response.type("application/json");
			return new Gson().toJson(u);
		});

		put("/logout", (request, response) -> {
			request.session().invalidate();
			return "logout successfully";
		});



		/*
		 * USERS
		 *
		 */

		post("/users", (request, response) -> {
			response.type("application/json");
			User user = new Gson().fromJson(request.body(), User.class);
			if (user == null) {
				response.status(400);
				return "Bad arguments";
			}
			user.setRole(Role.ORDINARY);
			user.setBlocked(false);
			user.setPrivateAccount(false);
			if(user.getUsername() != null) {
				if(userDAO.findById(user.getUsername())!=null) {
					response.status(400);
					return "user with specified username already exist";
				}
				
			}
			System.out.println(user);
			boolean added = userDAO.add(user);
			if (!added) {
				response.status(400);
				return "Bad arguments";
			}
			response.status(201);
			return "success";

		});
		
		get("/mutualFriends", (request, response)->{
			String firstUser = request.queryParams("firstUser");
			String secondUser = request.queryParams("secondUser");
			if (firstUser == null || secondUser == null) {
				response.status(400);
				return "Bad arguments";
			}
			
			return new Gson().toJson(userDAO.getMutualFriends(firstUser, secondUser));
		});

		get("/users", (request, response) -> {
//			User u = request.session().attribute("currentUser");
//			;
//			if (u == null) {
//				response.status(401);
//				return "access is denied";
//			} else if (u.getRole() != Role.ADMIN) {
//				response.status(401);
//				return "access is denied";
//			}
			response.type("application/json");
			Collection<User> users = userDAO.findAll();
			return new Gson().toJson(users);
		});

		get("/search-users", (request, response) -> {

			String name = request.queryParams("name");
			String lastName = request.queryParams("lastName");
			String startDate = request.queryParams("startDate");
			String endDate = request.queryParams("endDate");
			if (name == null || lastName == null || startDate == null || endDate == null) {
				response.status(400);
				return "Bad arguments";
			}

			SearchUserParam params = new SearchUserParam();
			try {

				long startBirthDate = Long.parseLong(startDate);
				params.setStartBirthDate(startBirthDate);

			} catch (Exception e) {

			}

			try {

				long endBirthDate = Long.parseLong(endDate);
				params.setEndBirthDate(endBirthDate);

			} catch (Exception e) {

			}

			if (!name.equals(""))
				params.setName(name.toLowerCase().trim());
			if (!lastName.equals(""))
				params.setLastName(lastName.toLowerCase().trim());

			response.type("application/json");

			Collection<User> users = userDAO.searchUser(params);

			return new Gson().toJson(users);
		});

		get("/users/:user", (request, response) -> {

			response.type("application/json");
			return new Gson().toJson(userDAO.findById(request.params(":user")));
		});
		/*
		 * SORT USERS
		 */
		get("/users-sort", (request, response) -> {

			String name = request.queryParams("name");
			String lastName = request.queryParams("lastName");
			String startDate = request.queryParams("startDate");
			String endDate = request.queryParams("endDate");
			String sortParams = request.queryParams("sortBy");
			String orderParam = request.queryParams("orderBy");
			if (name == null || lastName == null || startDate == null || endDate == null || sortParams == null
					|| orderParam == null
					|| (orderParam.toLowerCase().equals("desc") && orderParam.toLowerCase().equals("asc"))) {
				response.status(400);
				return "Bad arguments";
			}

			SearchUserParam params = new SearchUserParam();
			try {
				long startBirthDate = Long.parseLong(startDate);
				params.setStartBirthDate(startBirthDate);
			} catch (Exception e) {

			}

			try {
				long endBirthDate = Long.parseLong(endDate);
				params.setEndBirthDate(endBirthDate);
			} catch (Exception e) {

			}

			if (!name.equals(""))
				params.setName(name.toLowerCase().trim());
			if (!lastName.equals(""))
				params.setLastName(lastName.toLowerCase().trim());

			List<String> sortParamss = new ArrayList<String>();
			for (String s : sortParams.split(",")) {
				if (s.trim().equals("name") || s.trim().equals("lastName") || s.trim().equals("birthDate")) {
					sortParamss.add(s.trim());
				}
			}

			response.type("application/json");
			List<User> users = userDAO.searchUser(params);

			userDAO.sortUser(users, sortParamss, orderParam);

			return new Gson().toJson(users);

		});

		put("/users", (request, response) -> {
			User u = request.session().attribute("currentUser");
			;
			if (u == null) {
				response.status(401);
				return "access is denied";
			}
			User user = new Gson().fromJson(request.body(), User.class);
			if (user == null) {
				response.status(400);
				return "Bad request";
			}
			if (!u.getUsername().equals(user.getUsername()) && u.getRole() != Role.ADMIN) {
				response.status(403);
				return "Can not change data";
			}
			boolean res = userDAO.updateUser(user);
			return res == true ? "success" : "false";
		});

		/*
		 * MESSAGES
		 * 
		 * */
		
		get("/message",(request, response)->{
			String receiver = request.queryParams("receiver");
			String sender = request.queryParams("sender");
			if(receiver == null || sender == null) {
				response.status(400);
				return "Bad request";
			}
			
			return    new Gson().toJson(messDAO.findByTwoUsers(receiver, sender));
		});
		
		/*
		 * POSTS
		 *
		 */
		get("/posts", (request, response) -> {
			User u = request.session().attribute("currentUser");
			
			
			String id = request.queryParams("user");
			List<Post> posts = postDAO.findAllActivePostByUser(id);

			return new Gson().toJson(posts);
		});

		get("/post",(request, response) ->{
			Long id;
			try {
				id = Long.parseLong(request.queryParams("id"));
			} catch (Exception e) {
				response.status(400);
				return null;
			}
			if (id == null) {
				response.status(400);
				return "Bad request";
			}
			
			Post p = postDAO.findOnePost(id);
			if(p == null) {
				response.status(404);
				return "post not found";
			}
			return new Gson().toJson(p);
		});
		get("/posts/allActivePosts", (request, response) -> {
			User u = request.session().attribute("currentUser");
			;
			if (u == null) {
				response.status(401);
				return "access is denied";
			} else if (u.getRole() != Role.ADMIN) {
				response.status(403);
				return "access is denied";
			}
			List<Post> posts = postDAO.findAllActivePost();
			return new Gson().toJson(posts);
		});

		get("/posts/all", (request, response) -> {
			User u = request.session().attribute("currentUser");
			;
			if (u == null) {
				response.status(401);
				return "access is denied";
			} else if (u.getRole() != Role.ADMIN) {
				response.status(401);
				return "access is denied";
			}
			List<Post> posts = postDAO.findAllPost();// pronalazi sve i izbrisane
			return new Gson().toJson(posts);
		});

		post("/posts", (request, response) -> {

			response.type("application/json");
			User u = request.session().attribute("currentUser");
			;
			if (u == null) {
				response.status(401);
				return "access is denied";
			}
			PostRequest p = new Gson().fromJson(request.body(), PostRequest.class);
			p.setUsername(u.getUsername());
			if (p == null) {
				response.status(400);
				return "Bad arguments";
			}

			boolean res = postDAO.addPost(p);
			if (!res) {
				response.status(400);
				return "Bad arguments";
			} else {
				response.status(201);
			}
			return "Post successful created";
		});

		put("/posts/:id", (request, response) -> {
			response.type("application/json");
			User u = request.session().attribute("currentUser");
			;
			if (u == null) {
				response.status(401);
				return "access is denied";
			}
			Long id;
			try {
				id = Long.parseLong(request.params(":id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments-id must be number";
			}

			PostRequest p = new Gson().fromJson(request.body(), PostRequest.class);
			if (p == null) {
				response.status(400);
				return "Bad request";
			}

			if (postDAO.findOnePost(id) == null) {
				response.status(400);
				return "Bad request - post with specified id does not exist";
			}
			if (!postDAO.findOnePost(id).getUsernameCreator().equals(u.getUsername())) {
				response.status(403);
				return "forbidden- user not owner of post";
			}
			boolean res = postDAO.changePost(id, p);
			if (!res) {
				response.status(400);
				return "Bad arguments";
			} else {
				response.status(200);
			}
			return "Post successful changed";
		});

		delete("/posts/:id", (request, response) -> {
			response.type("application/json");
			User u = request.session().attribute("currentUser");
			;
			if (u == null) {
				response.status(401);
				return "access is denied";
			}
			Long id;
			try {
				id = Long.parseLong(request.params(":id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad request";
			}
			if (postDAO.findOnePost(id) == null) {
				response.status(400);
				return "Bad request - post with specified id does not exist";
			}
			if (!postDAO.findOnePost(id).getUsernameCreator().equals(u.getUsername())) {
				response.status(403);
				return "forbidden - user not owner of post";
			}
			boolean success = postDAO.deletePost(id);
			if (success)
				response.status(204);
			else {
				response.status(400);
				return "Bad arguments";
			}

			return "success";
		});
		get("/users/:user", (request, response) -> {
			response.type("application/json");
			User u = request.session().attribute("currentUser");
			;
			if (u == null) {
				response.status(401);
				return "access is denied";
			} else if (u.getRole() != Role.ADMIN) {
				response.status(401);
				return "access is denied";
			}

			return new Gson().toJson(userDAO.findById(request.params(":user")));
		});
		/*
		 * POST COMMENTS
		 *
		 */

		get("/comments", (request, response) -> {

			Long id;
			try {
				id = Long.parseLong(request.queryParams("id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments";
			}
			if (id == null) {
				response.status(404);
				return "Bad arguments";
			}
			PostComment c = postCommentDAO.findOne(id);
			if (c == null) {
				response.status(404);
				return "Bad arguments";
			}
			return new Gson().toJson(c);
		});

		get("/comments/findByPost", (request, response) -> {
			Long id;
			try {
				id = Long.parseLong(request.queryParams("postId"));
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments";
			}

			if (id == null) {
				response.status(404);
				return "Bad arguments";
			}
			List<PostComment> comments = postCommentDAO.findAllActiveCommentsByPost(id);

			return new Gson().toJson(comments);
		});

		post("/comments", (request, response) -> {
			response.type("application/json");
			User u = request.session().attribute("currentUser");
			;
			if (u == null) {
				response.status(401);
				return "access is denied";
			}
			PostCommentRequest req = new Gson().fromJson(request.body(), PostCommentRequest.class);
			req.setUsername(u.getUsername());
			if (req.getContent() == null || req.getPostId() == null) {
				response.status(400);
				return "Bad arguments";
			}

			if (userDAO.findById(req.getUsername()) == null || postDAO.findOnePost(req.getPostId()) == null) {
				response.status(404);
				return "Entity with specified id does not exist";
			}
			PostComment p = postCommentDAO.addOne(req);
			response.status(200);
			return new Gson().toJson(p);
		});

		put("/comments/:id", (request, response) -> {
			response.type("application/json");
			User u = request.session().attribute("currentUser");
			if (u == null) {
				response.status(401);
				return "access is denied";
			}
			Long id;
			try {
				id = Long.parseLong(request.params(":id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments";
			}
			response.type("application/json");
			PostCommentRequest req = new Gson().fromJson(request.body(), PostCommentRequest.class);
			if (req == null) {
				response.status(400);
				return "Bad arguments";
			}
			if (req.getContent() == null) {
				response.status(400);
				return "Bad arguments";
			}

			if (postCommentDAO.findOne(id) == null) {
				response.status(404);
				return "Post comment with specified id does not exist";
			}
			if (!postCommentDAO.findOne(id).getUsernameCreator().equals(u.getUsername())) {
				response.status(403);
				return "forbidden - user is not owner of comment";
			}
			PostComment p = postCommentDAO.changeComment(id, req);
			response.status(200);
			return new Gson().toJson(p);
		});

		delete("/comments/:id", (request, response) -> {
			response.type("application/json");
			User u = request.session().attribute("currentUser");
			
			if (u == null) {
				response.status(401);
				return "access is denied";
			}
			Long id;
			try {
				id = Long.parseLong(request.params(":id"));
			} catch (Exception e) {
				response.status(400);
				return "id must be number";
			}
			
			if (postCommentDAO.findOne(id) == null) {
				response.status(404);
				return "Post comment with specified id does not exist";
			}
			
			if (!postCommentDAO.findOne(id).getUsernameCreator().equals(u.getUsername()) && u.getRole() != Role.ADMIN) {
				response.status(403);
				return "forbidden - user is not owner of comment";
			}
			
			boolean success = postCommentDAO.deleteComment(id);
			if (!success) {
				response.status(400);
				return "Comment with specified id does not exist";
			}

			response.status(200);
			return "Comment successful deleted";
		});

		/*
		 * PHOTO COMMENTS
		 *
		 */
		get("/photoComments", (request, response) -> {

			Long id;
			try {
				id = Long.parseLong(request.queryParams("id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments";
			}
			if (id == null) {
				response.status(400);
				return "Bad arguments";
			}
			PhotoComment c = photoCommentDAO.findOne(id);
			if (c == null) {
				response.status(404);
				return "photo comment with specified id does not exist";
			}
			return new Gson().toJson(c);
		});

		get("/photoComments/findByPhoto", (request, response) -> {
			Long id;
			try {
				id = Long.parseLong(request.queryParams("photoId"));
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments";
			}

			if (id == null) {
				response.status(400);
				return "Bad arguments";
			}
			List<PhotoComment> comments = photoCommentDAO.findAllActiveCommentsByPhoto(id);

			return new Gson().toJson(comments);
		});

		post("/photoComments", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			PhotoCommentRequest req = new Gson().fromJson(request.body(), PhotoCommentRequest.class);
			req.setUsername(u.getUsername());
			if (req == null) {
				response.status(400);
				return "Bad arguments";
			}
			if (req.getContent() == null || req.getPhotoId() == null || req.getUsername() == null) {
				response.status(400);
				return "Bad arguments";
			}

			if (userDAO.findById(req.getUsername()) == null || photoDAO.getPhotoById(req.getPhotoId()) == null) {
				response.status(404);
				return "Entity with specified id does not exist";
			}
			PhotoComment p = photoCommentDAO.addOne(req);
			response.status(200);
			return new Gson().toJson(p);
		});

		put("/photoComments/:id", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			Long id;
			try {
				id = Long.parseLong(request.params(":id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments";
			}
			response.type("application/json");
			PhotoCommentRequest req = new Gson().fromJson(request.body(), PhotoCommentRequest.class);
			if (req == null) {
				response.status(400);
				return "Bad arguments";
			}
			if (req.getContent() == null) {
				response.status(400);
				return "Bad arguments";
			}

			if (photoCommentDAO.findOne(id) == null) {
				response.status(404);
				return "Photo comment with specified id does not exist";
			}
			
			if(!photoCommentDAO.findOne(id).getUsernameCreator().equals(u.getUsername())){
				response.status(403);
				return "Forbidden - user not owner of comment";
			}
			
			
			PhotoComment p = photoCommentDAO.changeComment(id, req);
			response.status(200);
			return new Gson().toJson(p);
		});

		delete("/photoComments/:id", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			Long id;
			try {
				id = Long.parseLong(request.params(":id"));
			} catch (Exception e) {
				response.status(400);
				return "id must be number";
			}
			if(photoCommentDAO.findOne(id) == null) {
				response.status(400);
				return "comment with specified id does not exist";
			}
			
			if(!photoCommentDAO.findOne(id).getUsernameCreator().equals(u.getUsername()) && u.getRole() != Role.ADMIN) {
				response.status(403);
				return "Forbidden - user not owner of comment";
			}
			boolean success = photoCommentDAO.deleteComment(id);
			if (!success) {
				response.status(404);
				return "Comment with specified id does not exist";
			}

			response.status(200);
			return "Comment successful deleted";
		});

		/*
		 * FRIEND REQUEST
		 *
		 */

		get("/request", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			String id = request.queryParams("id");
			if (id == null) {
				response.status(400);
				return "Bad arguments";
			}

			Long reqId;
			try {
				reqId = Long.parseLong(id);
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments";
			}
			FriendshipRequest frequest = frequestDAO.findOne(reqId);
			if (frequest == null) {
				response.status(404);
				return "Friendship request with specified id does not exist";
			}
			if(!frequest.getReceiver().equals(u.getUsername())) {
				response.status(403);
				return "user not owner of friendship request";
			}
			return new Gson().toJson(frequest);
		});

		get("/request/check", (request, response) -> {
			
			String firstUser = request.queryParams("firstUser");
			String secondUser = request.queryParams("secondUser");
			if (firstUser == null || secondUser == null) {
				response.status(400);
				return "Bad arguments";
			}
			return frequestDAO.checkFriendRequestExists(firstUser, secondUser);
		});
		
		get("/request/getBetweenUsers",(request, response) ->{
			
			String firstUser = request.queryParams("firstUser");
			String secondUser = request.queryParams("secondUser");
			if (firstUser == null || secondUser == null) {
				response.status(400);
				return "Bad arguments";
			}
			return new Gson().toJson(frequestDAO.checkFriendRequest(firstUser, secondUser));
			
		});

		get("/request/:username", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			String username = request.params(":username");
			if(username == null) {
				response.status(400);
				return "Bad request";
			}
			List<FriendshipRequest> frequests = frequestDAO.findByUser(username);
			return new Gson().toJson(frequests);
		});

		post("/request", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			FriendshipRequest frequest = new Gson().fromJson(request.body(), FriendshipRequest.class);
			if (frequest == null) {
				response.status(400);
				return "Bad request";
			}
			if (frequest.getReceiver() == null || frequest.getSender() == null) {
				response.status(400);
				return "Bad request";
			}
			if(!frequest.getSender().equals(u.getUsername())) {
				response.status(403);
				return "forbidden - sender is not current logged user";
			}
			if (userDAO.findById(frequest.getReceiver()) == null || userDAO.findById(frequest.getSender()) == null) {
				response.status(404);
				return "User with specified id does not exist";
			}
			if (frequestDAO.addOne(frequest) == null) {
				response.status(400);
				return "Friendship request or friendship between these two users already exists";
			}
			;
			return "success";
		});

		put("/request", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			FriendshipRequest frequest = new Gson().fromJson(request.body(), FriendshipRequest.class);
			if (frequest == null) {
				response.status(400);
				return "Bad request";
			}
			if (frequest.getId() == null || frequest.getState() == null) {
				response.status(400);
				return "Bad request";
			}
			if (frequestDAO.findOne(frequest.getId()) == null) {
				response.status(404);
				return "Friendship request with specified id does not exist";
			}
			frequestDAO.changeStatus(frequest.getId(), frequest.getState());
			return "success";
		});

		/*
		 * Friendship
		 *
		 */
		get("/friendship", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			response.type("application/json");
			Long id;
			try {
				id = Long.parseLong(request.queryParams("id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments";
			}
			Friendship f = friendshipDAO.getById(id);
			if (f == null) {
				response.status(404);
				return "Bad request";
			}
			return f;
		});

		get("/friendship/checkFriendship", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			String firstUser = request.queryParams("firstUser");
			String secondUser = request.queryParams("secondUser");
			if (firstUser == null || secondUser == null) {
				response.status(400);
				return "Bad request";
			}
			return friendshipDAO.checkFriendshipExist(firstUser, secondUser);
		});
		
		
		get("/friendship/getBetweenUsers", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			String firstUser = request.queryParams("firstUser");
			String secondUser = request.queryParams("secondUser");
			if (firstUser == null || secondUser == null) {
				response.status(400);
				return "Bad request";
			}
			return new Gson().toJson( friendshipDAO.checkFriendship(firstUser, secondUser));
		});

		get("/friendship/:user", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			response.type("application/json");
			String user = request.params(":user");

			if (userDAO.findById(user) == null) {
				response.status(404);
				return "user with specified id does not exist";
			}
			List<Friendship> f = friendshipDAO.getAllByUser(user);

			return new Gson().toJson(f);
		});

		post("/friendship", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			FriendshipRequest f = new Gson().fromJson(request.body(), FriendshipRequest.class);
			if (f == null) {
				response.status(400);
				return "Bad arguments";
			}
			if (f.getId() == null) {
				response.status(400);
				return "Bad arguments";
			}

			if (frequestDAO.findOne(f.getId()) == null) {
				response.status(404);
				return "Friendship request with specified id does not exist";
			}
			if (frequestDAO.findOne(f.getId()).getState() != FriendshipRequestState.ACCEPTED) {
				response.status(400);
				return "Friendship request is not accepted";
			}

			Friendship friendship = friendshipDAO.addOne(frequestDAO.findOne(f.getId()));
			return friendship;
		});
		
		delete("friendship/:id", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			Long id;
			try {
				id = Long.parseLong(request.params(":id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad arguments";
			}
			Friendship f = friendshipDAO.getById(id);
			if (f == null) {
				response.status(404);
				return "Friendship with specified id does not exist";
			}
			
			if(!f.getFirstUser().equals(u.getUsername()) && !f.getSecondUser().equals(u.getUsername())) {
				response.status(403);
				return "user not owner of friendship";
			}

			friendshipDAO.deleteFriendship(id);
			return "Friendship successful deleted";
		});

		/*
		 * PHOTO
		 *
		 */

		post("/photo", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			PhotoRequest p = new Gson().fromJson(request.body(), PhotoRequest.class);
			p.setUsername(u.getUsername());
			if (p == null) {
				response.status(400);
				return "Bad arguments";
			}
			if (p.getContent() == null) {
				response.status(400);
				return "Bad arguments";
			}

			if (userDAO.findById(p.getUsername()) == null) {
				response.status(404);
				return "User with specified id does not exists";
			}

			return new Gson().toJson(photoDAO.createPhoto(p));
		});

		get("/photo", (request, response) -> {
			Long id;
			try {
				id = Long.parseLong(request.queryParams("id"));
			} catch (Exception e) {
				response.status(400);
				return null;
			}
			if (id == null) {
				response.status(400);
				return "Bad request";
			}

			Photo p = photoDAO.getPhotoById(id);
			if (p == null)
				response.status(404);
			return new Gson().toJson(p);
		});

		get("/photo/getByUser", (request, response) -> {
			String username = request.queryParams("username");
			if (username == null) {
				response.status(400);
				return "Bad request";
			}
			List<Photo> photos = photoDAO.getAllPhotoByUser(username);
			return new Gson().toJson(photos);
		});

		/*
		 * moze se mijenjati samo tekst tj opis slike
		 */

		put("/photo/:id", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			Long id;
			try {
				id = Long.parseLong(request.params(":id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad request";
			}
			Photo p = photoDAO.getPhotoById(id);
			if (p == null) {
				response.status(404);
				return "Bad request";
			}
			
			if(!p.getUsernameCreate().equals(u.getUsername())) {
				response.status(403);
				return "Forbidden - user not owner of photo";
			}
			PhotoRequest req = new Gson().fromJson(request.body(), PhotoRequest.class);
			if (req == null) {
				response.status(400);
				return "Bad request";
			}
			if (req.getText() == null) {

				response.status(400);
				return "Bad request";
			}
			
			
			return photoDAO.changePhoto(id, req);
		});

		delete("/photo/:id", (request, response) -> {
			response.type("application/json");
       	 	User u = request.session().attribute("currentUser");;
			if(u == null) {
				response.status(401);
				return "access is denied";
			}
			
			Long id;
			try {
				id = Long.parseLong(request.params(":id"));
			} catch (Exception e) {
				response.status(400);
				return "Bad request";
			}
			Photo p = photoDAO.getPhotoById(id);
			if (p == null) {
				response.status(404);
				return "photo with specified id does not exist";
			}
			
			if(!p.getUsernameCreate().equals(u.getUsername())) {
				response.status(403);
				return "Forbidden - user not owner of photo";
			}
			photoDAO.deletePhoto(id);
			return "photo successful deleted";
		});

	}

}
