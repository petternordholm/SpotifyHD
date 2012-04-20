package se.softhouse.garden.spotify.webserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.softhouse.garden.spotify.scraper.HtAlbumArtistScraper;

import com.google.common.io.ByteStreams;

@SuppressWarnings("serial")
public class AlbumServlet extends HttpServlet 
{
	private static String[] uriPath;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try{
			setUriPath(req.getRequestURI());
			if(AlbumServlet.uriPath.length<3){
				resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "invalid number of params.");
			}
			
			String artist = URLDecoder.decode(AlbumServlet.uriPath[1], "UTF-8");
			String album = URLDecoder.decode(AlbumServlet.uriPath[2], "UTF-8");
			
			//resp.setContentType("text/html; charset=utf-8");
			resp.setContentType("image/png");
	        resp.setStatus(200);
	        byte[] image = getImage();
	        resp.setContentLength(image.length);
	        
	        InputStream is = HtAlbumArtistScraper.getImageStream(artist, album);
	        
	        ByteStreams.copy(is, resp.getOutputStream());
	        /*
	        ServletOutputStream sos = resp.getOutputStream();
	        sos.write(image);
	        sos.flush();
	        sos.close();
	        */
	        
	        //resp.getWriter().print(getThumbnail(artist, album));
			
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	private void setUriPath(String uri)throws Throwable{
		if(uri!=null){
			if(uri.indexOf("/")>-1){
				if(uri.startsWith("/")){ 
					uri = uri.substring(1, uri.length());
				}
				AlbumServlet.uriPath = uri.split("/");
			}else{
				AlbumServlet.uriPath = new String[]{uri};
			}
		}else{
			AlbumServlet.uriPath = new String[0];
		}
	}
	
	private String getThumbnail(String artist, String album){
		String response = "";
		try{
			artist=URLEncoder.encode(artist, "UTF-8");
			album=URLEncoder.encode(album, "UTF-8");
			String url_string = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=13e5700a606f4ccceef4dcc6e9bc2180&artist="+artist+"&album="+album;
			//url_string = URLEncoder.encode(url_string, "UTF-8");
			URL url = new URL(url_string);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
			String line=null;
			while((line=br.readLine())!=null){
				response+=line;
			}
			br.close();
		}catch(Throwable t){
			t.printStackTrace();
		}
		return response;
	}
	
	private InputStream getImageStream()throws Throwable{
		URL url = new URL("http://userserve-ak.last.fm/serve/300x300/72903330.png");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		return httpCon.getInputStream();
	}
	
	private byte[] getImage(){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] result = null;
		try{
			URL url = new URL("http://userserve-ak.last.fm/serve/300x300/72903330.png");
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			InputStreamReader isr = new InputStreamReader(httpCon.getInputStream());
			int next = isr.read();
		    while (next > -1){
		        baos.write(next);
		        next = isr.read();
		    }

		    result = baos.toByteArray();
		    baos.flush();
		    isr.close();
		    return result;
		}catch(Throwable t){
			t.printStackTrace();
		}
		return result;
	}
	
}
