package app;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.path;
import java.util.List;

import com.google.gson.Gson;

import beans.FriendshipRequest;
import beans.User;
import dao.FriendshipDAO;
import dao.FriendshipRequestDAO;
import dao.UserDAO;
import util.AppConstants;

public class FriendRequests {
	private static FriendshipDAO friendshipDAO = FriendshipDAO.getInstance(AppConstants.contextPath);
	private static UserDAO userDAO = UserDAO.getInstance(AppConstants.contextPath, friendshipDAO);
	private static FriendshipRequestDAO frequestDAO = FriendshipRequestDAO.getInstance(AppConstants.contextPath, friendshipDAO);
	public static void init() {
		path("",() -> {
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
		});
	}
}
