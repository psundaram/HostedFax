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
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendFax extends Thread{
	
//	Fax fax = null;
	
	public SendFax(){
//		this.fax = fax;
	}

	private static Session session = null;
	
	public void run() {
		Store store = null;
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
			store = session.getStore("pop3");
			store.connect("relay.anpiservices.com", "relay", "8np!R3l8y");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		Message message = null;
		try {
			message = identifyElements();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
         try {
			Transport.send(message);
			System.out.println("Mail sent successfully");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	
	}
	
	public static void main(String[] args) {
		SendFax sendFax = new SendFax();
		sendFax.start();
	}

	private Message createMessage() {
		Message messageWithAttachment = new MimeMessage(session);
		try {
			messageWithAttachment.setFrom(new InternetAddress("psundaram@anpi.com"));
		messageWithAttachment.addRecipient(Message.RecipientType.TO, new InternetAddress("e2fuser@email2faxprod.anpiservices.com"));
		messageWithAttachment.setRecipient(Message.RecipientType.CC, new InternetAddress("psundaram@anpi.com"));
		//messageWithAttachment.setReplyTo(message.getReplyTo());
//		messageWithAttachment.setSubject("Hai");
		String newContent = "";
		
		messageWithAttachment.setSubject("Fax Attachment");
		/*if(!Strings.isNullOrEmpty(fax.getFromDid())){
			newContent = "SIPFROM:"+fax.getFromDid();
		}
		if(!Strings.isNullOrEmpty(fax.getToDid())){
			newContent += "\nSIPTO:"+fax.getToDid();
		}
		*/
		
		// Creates a local file
		
		 BodyPart messageBodyPart = new MimeBodyPart();

		 System.out.println("Content:"+newContent);
         // Now set the actual message
         messageBodyPart.setText("Hai ");
         
		 Multipart multipart = new MimeMultipart();

         // Set text message part
         multipart.addBodyPart(messageBodyPart);

         // Part two is attachment
         messageBodyPart = new MimeBodyPart();
         DataSource source = new FileDataSource("F://eclipse/1432720186832.pdf");
         messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName("abc.pdf");
         multipart.addBodyPart(messageBodyPart);

         // Send the complete message parts
         messageWithAttachment.setContent(multipart);
		
	}catch (AddressException e) {
		e.printStackTrace();
	} catch (MessagingException e) {
		e.printStackTrace();
	}
		return messageWithAttachment;
	}

			 
			 private static Message identifyElements() throws Exception {
				 String newContent = "";
				Message formMessage = new MimeMessage(session);
//				String toAddress = "e2fuser@email2faxprod.anpiservices.com";
				String toAddress ="psundaram@anpi.com";
				formMessage.setFrom(new InternetAddress("nsathiaraj@anpi.com"));
				formMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
//				formMessage.setRecipient(Message.RecipientType.CC, new InternetAddress("psundaram@anpi.com"));
				formMessage.setSubject("Fax Attachment");
				/*if(elementsForMessage.containsKey("FROM")){
					newContent = "SIPFROM:"+elementsForMessage.get("FROM");
				}
				if(elementsForMessage.containsKey("TO")){
					newContent += "\nSIPTO:"+elementsForMessage.get("TO");
				}
				if(elementsForMessage.containsKey("SUBJECT")){
					newContent += "\nSUBJECT:"+elementsForMessage.get("SUBJECT");
					formMessage.setSubject(elementsForMessage.get("SUBJECT"));
				}
				if(elementsForMessage.containsKey("REPLY_TO")){
					newContent += "\nREPLYTO :"+elementsForMessage.get("REPLY_TO");
				}
				if(elementsForMessage.containsKey("ID")){
					newContent += "\nID :"+elementsForMessage.get("ID");
				}
				if(elementsForMessage.containsKey("COVER")){
					newContent += "\nCOVER :"+elementsForMessage.get("COVER");
				}else{
					newContent +="\nCOVER:NO";
				}*/
				System.out.println("newContent:"+newContent);
				formMessage.setContent("Hai","text/html");
				System.out.println("Message:"+formMessage);
				return formMessage;
			}
			 


			

}
