package app;

import static spark.Spark.get;
import static spark.Spark.path;

import com.google.gson.Gson;

import dao.MessageDAO;

public class Messages {

	private static MessageDAO messDAO = MessageDAO.getInstance();
	public static void init() {
		path("", () -> {
			get("/message",(request, response)->{
				String receiver = request.queryParams("receiver");
				String sender = request.queryParams("sender");
				if(receiver == null || sender == null) {
					response.status(400);
					return "Bad request";
				}
				
				return    new Gson().toJson(messDAO.findByTwoUsers(receiver, sender));
			});
		});
		
	}
}
