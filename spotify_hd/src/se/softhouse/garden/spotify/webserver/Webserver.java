package se.softhouse.garden.spotify.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Webserver {

	private Server server;
	
	private void start() throws Exception
	{
		server = new Server(9988);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		
		context.addServlet(new ServletHolder(ArtistServlet.class),"/artist/*");
		context.addServlet(new ServletHolder(AlbumServlet.class),"/album/*");
		server.start();
		server.join();
	}
	
	
	public static void main(String[] args) throws Exception 
	{
		Webserver ws = new Webserver();
		ws.start();
	}
}
