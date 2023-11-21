package app;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.path;
import java.util.List;

import com.google.gson.Gson;

import beans.Friendship;
import beans.FriendshipRequest;
import beans.FriendshipRequestState;
import beans.User;
import dao.FriendshipDAO;
import dao.FriendshipRequestDAO;
import dao.PhotoCommentDAO;
import dao.PhotoDAO;
import dao.PostCommentDAO;
import dao.PostDAO;
import dao.UserDAO;
import util.AppConstants;

public class Friendships {
	private static FriendshipDAO friendshipDAO = FriendshipDAO.getInstance(AppConstants.contextPath);
	private static UserDAO userDAO = UserDAO.getInstance(AppConstants.contextPath, friendshipDAO);
	private static FriendshipRequestDAO frequestDAO = FriendshipRequestDAO.getInstance(AppConstants.contextPath, friendshipDAO);
	public static void init() {
		path("", () -> {
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

		});
	}
}
