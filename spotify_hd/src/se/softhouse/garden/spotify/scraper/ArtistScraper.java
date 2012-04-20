package se.softhouse.garden.spotify.scraper;

import java.io.InputStream;
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
		
		private Map<String, String> myResponseHeaders;
		private InputStream         myInputStream;
		private int myResponseCode;
	}
	
	
	public Result getBackdropForArtist(String artistName);
}
