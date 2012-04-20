package se.softhouse.garden.spotify.scraper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public interface ArtistScraper 
{
	public class Result
	{
		public Map<String, String> getResponseHeaders()
		{
			return myResponseHeaders;
		}
		
		public InputStream getInputStream()
		{
			return myInputStream;
		}
		
		public int getResponseCode() 
		{
			return myResponseCode;
		}

		public void setResponseCode(int responseCode)
		{
			myResponseCode = responseCode;
		}
		
		public void setInputStream(InputStream inputStream) 
		{
			myInputStream = inputStream;
		}

		private Map<String, String> myResponseHeaders = new HashMap<String, String>();
		private InputStream         myInputStream;
		private int myResponseCode;
	}
	
	
	public Result getBackdropForArtist(String artistName);
}
