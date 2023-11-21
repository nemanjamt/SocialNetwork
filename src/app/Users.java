package app;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;

import beans.Role;
import beans.SearchUserParam;
import beans.User;
import dao.FriendshipDAO;
import dao.UserDAO;
import util.AppConstants;

public class Users {
	private static FriendshipDAO friendshipDAO = FriendshipDAO.getInstance(AppConstants.contextPath);
	private static UserDAO userDAO = UserDAO.getInstance(AppConstants.contextPath, friendshipDAO);
	public static void init() {
		path("", () -> {
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

		});
	}
}
