package se.softhouse.garden.spotify.scraper;


public class HtBackDropArtistScraper implements ArtistScraper 
{
	@Override
	public Result getBackdropForArtist(String artistName) 
	{
		String pictureId = findPictureId(artistName);
		
		return null;
	}

	private String findPictureId(String artistName) 
	{
		
		
		return null;
	}

}
