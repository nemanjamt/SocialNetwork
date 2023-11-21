package app;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.List;

import com.google.gson.Gson;

import beans.Post;
import beans.PostRequest;
import beans.Role;
import beans.User;
import dao.FriendshipDAO;
import dao.MessageDAO;
import dao.PostDAO;
import dao.UserDAO;
import util.AppConstants;

public class Posts {
	private static FriendshipDAO friendshipDAO = FriendshipDAO.getInstance(AppConstants.contextPath);
	private static UserDAO userDAO = UserDAO.getInstance(AppConstants.contextPath, friendshipDAO);
	private static PostDAO postDAO = PostDAO.getInstance(AppConstants.contextPath);
	public static void init() {
		path("", () -> {
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
				List<Post> posts = postDAO.findAllPost();
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
				
				if (p == null) {
					response.status(400);
					return "Bad arguments";
				}
				p.setUsername(u.getUsername());
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
				if (!postDAO.findOnePost(id).getUsernameCreator().equals(u.getUsername()) && u.getRole() != Role.ADMIN) {
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
		});
		
	}
}
