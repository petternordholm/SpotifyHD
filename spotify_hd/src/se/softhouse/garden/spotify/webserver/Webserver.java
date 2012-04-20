package se.softhouse.garden.spotify.webserver;

import org.eclipse.jetty.server.Server;

public class Webserver {

	private Server server;
	
	private void start(){
		try{
			server = new Server(9988);
		    server.setHandler(new ApiHandler());
		    server.start();
		    System.out.println("webserver running");
		    server.join();
		}catch(Throwable t){
			
		}
	}
	
	
	public static void main(String[] args) {
		Webserver ws = new Webserver();
		ws.start();
	}

}
