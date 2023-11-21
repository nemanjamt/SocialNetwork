package app;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.path;
import com.google.gson.Gson;

import beans.LoginRequest;
import beans.User;
import dao.FriendshipDAO;
import dao.UserDAO;
import util.AppConstants;

public class Auth {
	private static FriendshipDAO friendshipDAO = FriendshipDAO.getInstance(AppConstants.contextPath);
	private static UserDAO userDAO = UserDAO.getInstance(AppConstants.contextPath, friendshipDAO);
	public static void init() {
		path("",() -> {
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
				} else if(u.isBlocked()) {
					response.status(401);
					return "blocked";
				}else {
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
		});
	}
}
