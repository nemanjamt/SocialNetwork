package app;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.path;
import java.util.List;

import com.google.gson.Gson;

import beans.Photo;
import beans.PhotoRequest;
import beans.Role;
import beans.User;
import dao.FriendshipDAO;
import dao.PhotoDAO;
import dao.UserDAO;
import util.AppConstants;

public class Photos {
	private static PhotoDAO photoDAO = PhotoDAO.getInstance(AppConstants.contextPath);
	private static FriendshipDAO friendshipDAO = FriendshipDAO.getInstance(AppConstants.contextPath);
	private static UserDAO userDAO = UserDAO.getInstance(AppConstants.contextPath, friendshipDAO);
	public static void init() {
		path("",()->{
			post("/photo", (request, response) -> {
				response.type("application/json");
	       	 	User u = request.session().attribute("currentUser");;
				if(u == null) {
					response.status(401);
					return "access is denied";
				}
				PhotoRequest p = new Gson().fromJson(request.body(), PhotoRequest.class);
				
				if (p == null) {
					response.status(400);
					return "Bad arguments";
				}
				p.setUsername(u.getUsername());
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
				
				if(!p.getUsernameCreate().equals(u.getUsername()) && u.getRole() != Role.ADMIN) {
					response.status(403);
					return "Forbidden - user not owner of photo";
				}
				photoDAO.deletePhoto(id);
				return "photo successful deleted";
			});
		});
	}
}
