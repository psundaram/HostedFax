package com.anpi.app.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;

import com.anpi.app.api.util.FakeSSLSocketFactory;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MergePdfService {
	
	public boolean deleteFiles(List<String> filePaths) {
		System.out.println("Entering deleteGeneratedFiles");
		File f = null;
		boolean successValue = false;
		for (String path : filePaths) {
			f = new File(path);
			System.out.println("path -- >"+path);
			boolean isdeleted = f.delete();
			if (isdeleted) {
				System.out.println("path -- >"+path + " deleted");
			} else {
			}
		}
		System.out.println("Exiting deleteGeneratedFiles --> response: " + successValue);
		return successValue;
	}

	@SuppressWarnings("deprecation")
	private static ClientConnectionManager getSchemeFactory() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, new FakeSSLSocketFactory()));
        ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
        return cm;
    }
	
	
	
	public static String uploadFile(String fileToUpload) throws IOException, UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		System.out.print("Entering uploadFile api");
		System.out.println("fileToUpload:"+fileToUpload);
		// Context Path will be of the form : "emails_sent?partner_id=ANPI"
		String postUrl = "http://10.7.3.207:5001/drepository/documents/"+"upload?partner_id=1";
		DefaultHttpClient httpclient = new DefaultHttpClient(getSchemeFactory());
		HttpPost httppost = new HttpPost(postUrl);
		MultipartEntity mpEntity = new MultipartEntity();
		File file = new File(fileToUpload);
		ContentBody cbFile = new FileBody(file);
		mpEntity.addPart("user_file", cbFile);
		httppost.setEntity(mpEntity);
		System.out.println("httppost =>"+httppost.toString());
		CloseableHttpResponse resposne = httpclient.execute(httppost);
        String response = EntityUtils.toString(resposne.getEntity());
    	System.out.print("Exiting uploadFile api");
    	System.out.println("response-->"+response);
    	
    	// parse the json response
    	JsonParser parser = new JsonParser();
        JsonObject jo = new JsonObject();
        jo = (JsonObject) parser.parse(response);
        JsonElement uuid = jo.get("uuid");
    	String uuidStr = uuid.getAsString();
    	System.out.println("uuidStr-->"+uuidStr);
    	System.out.println("Exiting upload file");
        return "http://10.7.3.207:5001/drepository/documents/"+uuidStr;
    }
	
	public static List downloadFiles(String filePaths) throws MalformedURLException, IOException {
		System.out.println("Entering downloadFiles");
		List fileList = new ArrayList();
		String[] filePathsArr = filePaths.split(",");
		TrustManager[] trustAllCerts = new TrustManager[] { 
			    new X509TrustManager() {     
			        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
			            return new X509Certificate[0];
			        } 
			        public void checkClientTrusted( 
			            java.security.cert.X509Certificate[] certs, String authType) {
			            } 
			        public void checkServerTrusted( 
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			    } 
			}; 

			// Install the all-trusting trust manager
			try {
			    SSLContext sc = SSLContext.getInstance("SSL"); 
			    sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
			    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (GeneralSecurityException e) {
			} 
			
			
		for (int i = 0; i < filePathsArr.length; i++) {
			 String fileName = "";
			 URL url = new URL(filePathsArr[i]);
		        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		        int responseCode = httpConn.getResponseCode();
		        // always check HTTP response code first
		        if (responseCode == HttpURLConnection.HTTP_OK) {
		           
		            String disposition = httpConn.getHeaderField("Content-Disposition");
		            String contentType = httpConn.getContentType();
		            int contentLength = httpConn.getContentLength();
		            if (disposition != null) {
		                // extracts file name from header field
		                int index = disposition.indexOf("filename=");
		                if (index > 0) {
		                    fileName = disposition.substring(index + 10,
		                            disposition.length() - 1);
		                }
		            } else {
		                // extracts file name from URL
		            	String fileURL = filePathsArr[i];
		                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
		                        fileURL.length());
		            }
		 
		            System.out.println("Content-Type = " + contentType);
		            System.out.println("Content-Disposition = " + disposition);
		            System.out.println("Content-Length = " + contentLength);
		            System.out.println("fileName = " + fileName);
		 
		            // opens input stream from the HTTP connection
		            InputStream inputStream = httpConn.getInputStream();
		            String saveFilePath = System.getProperty("user.dir")+"/"+ fileName;
		             
		            // opens an output stream to save into file
		            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
		 
		            int bytesRead = -1;
		            byte[] buffer = new byte[4096];
		            while ((bytesRead = inputStream.read(buffer)) != -1) {
		                outputStream.write(buffer, 0, bytesRead);
		            }
		 
		            outputStream.close();
		            inputStream.close();
		 
		            System.out.println("File downloaded");
		        } else {
		            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		        }
		        httpConn.disconnect();
			fileList.add(fileName);
//			filesToBeDeleted.add(fileName);
		}
		System.out.println("Exiting downloadFiles");
		return fileList;
	}

}
