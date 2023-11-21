package app;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.path;
import java.util.List;

import com.google.gson.Gson;

import beans.PhotoComment;
import beans.PhotoCommentRequest;
import beans.PostComment;
import beans.PostCommentRequest;
import beans.Role;
import beans.User;
import dao.FriendshipDAO;
import dao.PhotoCommentDAO;
import dao.PhotoDAO;
import dao.PostCommentDAO;
import dao.PostDAO;
import dao.UserDAO;
import util.AppConstants;

public class Comments {

	private static PostDAO postDAO = PostDAO.getInstance(AppConstants.contextPath);
	private static PostCommentDAO postCommentDAO = PostCommentDAO.getInstance(AppConstants.contextPath);
	private static PhotoCommentDAO photoCommentDAO = PhotoCommentDAO.getInstance(AppConstants.contextPath);
	private static PhotoDAO photoDAO = PhotoDAO.getInstance(AppConstants.contextPath);
	private static FriendshipDAO friendshipDAO = FriendshipDAO.getInstance(AppConstants.contextPath);
	private static UserDAO userDAO = UserDAO.getInstance(AppConstants.contextPath, friendshipDAO);
	public static void init() {
		path("",()->{
			get("/comments", (request, response) -> {

				Long id;
				try {
					id = Long.parseLong(request.queryParams("id"));
				} catch (Exception e) {
					response.status(400);
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
		});
	}
}
