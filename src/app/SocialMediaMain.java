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
	

	public static void main(String[] args) throws IOException {
		port(9090);
		
		webSocket("/ws", WsHandler.class);
		
		staticFiles.externalLocation(new File("./WebContent/static").getCanonicalPath());
		
		Auth.init();
		
		Users.init();

		Messages.init();

		Posts.init();

		Comments.init();

		FriendRequests.init();

		Friendships.init();
		
		Photos.init();
		

	}

}
