package com.anpi.app.api.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Main {

	public static void main(String[] args) {
		List<String> paths = new ArrayList<String>();
		paths.add("F://attachments/SBEST_Order_Summary.pdf");
		paths.add("F://attachments/SBEST_Order_Summary.pdf");
		String urlString = "http://10.5.3.226:8080/fax/api/merge/files/dev";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(urlString);
//			FileBody bin1 = new FileBody(file1);
//			FileBody bin2 = new FileBody(file2);
			MultipartEntity reqEntity = new MultipartEntity();
			for(int i=0;i<paths.size();i++){
			reqEntity.addPart("file", new FileBody(new File(paths.get(i))));
			}
//			reqEntity.addPart("file", bin2);
			reqEntity.addPart("to", new StringBody("12345"));
			reqEntity.addPart("from", new StringBody("12345"));
			post.setEntity(reqEntity);
			HttpResponse response = client.execute(post);
			HttpEntity resEntity = response.getEntity();
			final String response_str = EntityUtils.toString(resEntity);
			if (resEntity != null) {
				System.out.println("RESPONSE" + response_str);
			}
		} catch (Exception ex) {
			System.out.println("Debug error: " + ex.getMessage() + ", " + ex);
		}
	}

}
