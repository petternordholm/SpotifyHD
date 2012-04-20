package se.softhouse.garden.spotify.webserver;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.softhouse.garden.spotify.scraper.ArtistScraper;
import se.softhouse.garden.spotify.scraper.ArtistScraper.Result;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.common.net.HttpHeaders;

@SuppressWarnings("serial")
public class ArtistServlet extends HttpServlet 
{
	Pattern REGEXP = Pattern.compile("^/(.*?)/(.*?)$"); 
	
	String BACKDROP = "backdrop.jpg";
	
	ArtistScraper myArtistScraper;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		String uri = req.getPathInfo();
		Matcher matcher = REGEXP.matcher(uri);
		if(!matcher.matches())
		{
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		String artist = URLDecoder.decode(matcher.group(1),"UTF-8");
		String type =   URLDecoder.decode(matcher.group(2),"UTF-8");
		
		
		Result data = myArtistScraper.getBackdropForArtist(artist);
		
		if (data.getResponseCode() != HttpServletResponse.SC_OK)
		{
			resp.sendError(data.getResponseCode());
		}
		
		String contentType = data.getResponseHeaders().get(HttpHeaders.CONTENT_TYPE);
		String contentLength = data.getResponseHeaders().get(HttpHeaders.CONTENT_LENGTH);
		
		if (!Strings.isNullOrEmpty(contentLength))
		{
			resp.setHeader(HttpHeaders.CONTENT_LENGTH, contentLength);
		}
		
		if (Strings.isNullOrEmpty(HttpHeaders.CONTENT_TYPE))
		{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		
		resp.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
		
		ByteStreams.copy(data.getInputStream(), resp.getOutputStream());
	}
	
	
	
}
