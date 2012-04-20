package se.softhouse.garden.spotify.webserver;

import java.io.IOException;
import java.net.HttpRetryException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ArtistServlet extends HttpServlet 
{

	Pattern REGEXP = Pattern.compile("^/(.*?)/(.*?)/(.*?)$"); 
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		String uri = req.getPathInfo();
		Matcher matcher = REGEXP.matcher(uri);
		if(matcher.matches())
		{
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		System.out.println("Artist is: " + URLDecoder.decode(matcher.group(1),"UTF-8"));
		System.out.println("Album is: " + URLDecoder.decode(matcher.group(2),"UTF-8"));
		System.out.println("Image is: " + URLDecoder.decode(matcher.group(3),"UTF-8"));
		super.doGet(req, resp);
	}
	
	
	
}
