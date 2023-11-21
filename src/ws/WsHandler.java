package ws;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import beans.Message;
import dao.MessageDAO;

@WebSocket
public class WsHandler extends WebSocketHandler {

	private static final Map<Session, String> allSessions = new ConcurrentHashMap<>();
	
	@OnWebSocketConnect
	public void connected(Session session) {
		String userKey = session.getUpgradeRequest().getRequestURI().getQuery();
		allSessions.put(session,userKey.split("=")[1]);
		System.out.println("connected...");
		System.out.println(userKey);
		System.out.println(userKey.split("=")[1]);
	}

	@OnWebSocketClose
	public void closed(Session session, int statusCode, String reason) {
		allSessions.remove(session);
		System.out.println("closing..");
	}

	@OnWebSocketError
	public void error(Session session, Throwable t) {
		allSessions.remove(session);
	}

	@OnWebSocketMessage
	public void message(Session session, String message) throws IOException {
		JsonParser jsonParser = new JsonParser();
		JsonObject obj = jsonParser.parse(message).getAsJsonObject();
		System.out.println("Got: " + message); // Print message
		postMessage(obj.get("context").getAsString(), session, obj);
		System.out.println("message");

		
	}
	
	public static void postMessage(String text, Session sess, JsonObject obj) {
//		for (Session s : allSessions.keySet()) {
//			try {
//				if (s.hashCode() != sess.hashCode())
//					s.getRemote().sendString(text);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		String receiver = obj.get("receiver").getAsString();
		String sender = obj.get("sender").getAsString();
		Message m = new Message();
		m.setContext(obj.get("context").getAsString());
		m.setDate(System.currentTimeMillis());
		m.setReceiverUsername(receiver);
		m.setSenderUsername(sender);
		MessageDAO mDAO = MessageDAO.getInstance();
		
		
		mDAO.add(m);

		JsonObject newObj = new JsonObject();
		newObj.addProperty("context", m.getContext());
		newObj.addProperty("receiverUsername", m.getReceiverUsername());
		newObj.addProperty("senderUsername", m.getSenderUsername());
		newObj.addProperty("date", m.getDate());
		allSessions.keySet().stream().filter(k -> allSessions.get(k).equals(receiver)).forEach(k -> {
			try {
				k.getRemote().sendString(String.valueOf(newObj));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setIdleTimeout(1000*60*60);
	}
	

}
