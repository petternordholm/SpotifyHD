package se.softhouse.garden.spotify.scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class HtBackDropArtistScraper implements ArtistScraper 
{
	@Override
	public Result getBackdropForArtist(String artistName) 
	{
		String pictureId = findPictureId(artistName);
		
		if (pictureId == null) return null;
		
		Result res = new Result();
		
		try 
		{
			URLConnection connection = new URL("http://htbackdrops.com/api/fe530c198265f74fc3e655ad87702028/download/" + pictureId + "/fullsize").openConnection();
			connection.connect();
			Map<String, List<String>> headerFields = connection.getHeaderFields();
			for(Entry<String, List<String>> header : headerFields.entrySet())
			{
				if (header.getValue() == null)continue;
				res.getResponseHeaders().put(header.getKey(),header.getValue().get(0));
			}
			res.setResponseCode(HttpServletResponse.SC_OK);			
			res.setInputStream(connection.getInputStream());
		} 
		catch (MalformedURLException e) 
		{
			throw new RuntimeException(e);
		} 
		catch (IOException e) 
		{
			throw new RuntimeException(e);
		}
		
		return res;
	}

	private String findPictureId(String artistName) 
	{
		try 
		{
			String url = "http://htbackdrops.com/api/fe530c198265f74fc3e655ad87702028/searchXML?keywords=" + URLEncoder.encode(artistName, "UTF-8");
		
			URLConnection u = new URL(url).openConnection();
			u.connect();
			
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(u.getInputStream());
			
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expression = xpath.compile("//images/image/dimensions");
						
			NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
			
			String preferedImgNo = null;
			
			for (int index = 0; index < nodeList.getLength(); index++)
			{
				Node n = nodeList.item(index);
				String dim = n.getTextContent();
				
				if ( preferedImgNo == null )
				{
					Node pictureId = getNodeWithName(n.getParentNode(),"id");
					if (pictureId == null)continue;
					preferedImgNo = pictureId.getTextContent();
				}
				
				else if (dim.equalsIgnoreCase("1920x1080"))
				{
					Node pictureId = getNodeWithName(n.getParentNode(),"id");
					if (pictureId == null)continue;
					preferedImgNo = pictureId.getTextContent();
					break;
				}
				else if (dim.equalsIgnoreCase("1280x720"))
				{
					Node pictureId = getNodeWithName(n.getParentNode(),"id");
					if (pictureId == null)continue;
					preferedImgNo = pictureId.getTextContent();					
				}				
			}
			return preferedImgNo;
			
		} 
		catch (MalformedURLException e) 
		{
			throw new RuntimeException(e);
		} 
		catch (IOException e) 
		{
			return null;
		} 
		catch (ParserConfigurationException e) 
		{
			throw new RuntimeException(e);
		} 
		catch (SAXException e) 
		{
			throw new RuntimeException(e);
		} 
		catch (XPathExpressionException e) 
		{
			throw new RuntimeException(e);
		}
	}

	private Node getNodeWithName(Node parentNode, String name) 
	{
		NodeList nl = parentNode.getChildNodes();
		for (int index = 0; index< nl.getLength(); index++)
		{
			Node n = nl.item(index);
			if (n.getNodeName().equalsIgnoreCase(name))
			{
				return n;
			}
		}
		return null;
	}

}
