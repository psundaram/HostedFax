package com.anpi.app.api.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.anpi.app.api.domain.Fax;
import com.google.common.base.Strings;

public class SendMail extends Thread{
	private static Session session = null;
	Fax fax = new Fax();
	boolean isProd = false;
	
	public SendMail(Fax fax,boolean isProd) {
		this.fax = fax;
		this.isProd = isProd;
	}
	
	public static void main(String[] args) {
		/*SendMail sendMail = new SendMail();
		sendMail.start();*/
		  
	}
	
	public void run(){
		String to = null;
		if(isProd)
		  to = "e2fuser@email2faxprod.anpiservices.com";
		else
			to = "e2fuser@email2faxlab.anpiservices.com";

	      // Sender's email ID needs to be mentioned
	      String from = "psundaram@anpi.com";



	      Properties props = System.getProperties();
			props.put("mail.smtp.host", "smtp.anpi.com");
			props.put("mail.smtps.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			
			/****** IMAP Server ********/
			props.setProperty("mail.store.protocol", "pop3");
			session = Session.getDefaultInstance(props, null);
			props.setProperty("mail.store.protocol", "pop3");
			session = Session.getDefaultInstance(props, null);

	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(to));
	         
	         message.setRecipient(Message.RecipientType.BCC, new InternetAddress("psundaram@anpi.com"));

	         // Set Subject: header field
	         message.setSubject("Testing Subject");

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         
	         
//	 		fax.setFromDid("12097574934@172.31.16.33");
//	 		fax.setToDid("12097574934@172.31.16.33");
	 		
	 		String newContent = "";
	         
	 		if(!Strings.isNullOrEmpty(fax.getFromDid())){
				newContent = "SIPFROM:"+fax.getFromDid();
			}
			if(!Strings.isNullOrEmpty(fax.getToDid())){
				newContent += "\nSIPTO:"+fax.getToDid();
			}
	         messageBodyPart.setText(newContent);

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         DataSource source = new FileDataSource(System.getProperty("user.dir") + "/" + fax.getFileName());
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(fax.getFileName());
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");
	  
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }
	}
}
