package se.softhouse.garden.spotify.scraper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HtAlbumArtistScraper {

	public static InputStream getImageStream(String artist, String album)throws Throwable{
		artist=URLEncoder.encode(artist, "UTF-8");
		album=URLEncoder.encode(album, "UTF-8");
		String xml_string = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=13e5700a606f4ccceef4dcc6e9bc2180&artist="+artist+"&album="+album;
		
		URLConnection u = getImageXml(xml_string);
		String image_url = getImageUrl(u);
		
		return getImageStream(image_url);
	}

	private static URLConnection getImageXml(String image_url)throws Throwable{
		//URL url = new URL(image_url);
		URLConnection u = new URL(image_url).openConnection();
		u.connect();
		return u;
	}
	
	private static InputStream getImageStream(String image_url)throws Throwable{
		URL url = new URL(image_url);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		return httpCon.getInputStream();
	}
	
	private static String getImageUrl(URLConnection u)throws Throwable{
		String result="";
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse(u.getInputStream());
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expression = xpath.compile("//lfm/album/image");
					
		NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
		
		for(int i=0; i<nodeList.getLength();i++){
			Node n = nodeList.item(i);
			NamedNodeMap nnm = n.getAttributes();
			Node size = nnm.getNamedItem("size");
			String size_text = size.getTextContent();
			
			if(size_text.equals("mega")){
				return n.getTextContent();
			}
		}
		
		return result;
	}
}
