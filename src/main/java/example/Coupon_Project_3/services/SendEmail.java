package example.Coupon_Project_3.services;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendEmail {

	public String to = "";
	@Value("${EMAIL_FROM}")
	public String from;
	public String host = "smtp.gmail.com";
	@Value("${EMAIL_PASS}")
	private String pass;
	
	Properties properties = System.getProperties();

	
	public void sendEmailVerify(String VerifyURL, String Email){
	to = Email;
	properties.put("mail.smtp.host", host);
//	properties.put("mail.smtp.port", "465"); // not working with this port
//	properties.put("mail.smtp.ssl.enable", "true"); // not working with this line
	
	properties.put("mail.smtp.port", "587");
	properties.put("mail.smtp.starttls.enable","true");
	properties.put("mail.smtp.starttls.required","true");
	properties.put("mail.smtp.auth", "true");
	properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	
	
	Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(from, pass);
		}
	});


	try {
		 // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(from));

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        // Set Subject: header field
        message.setSubject("worldOfCoupons.com - Active account");

        // Now set the actual message
        message.setContent("please click this link to active your account:  "+
        "<br /><a style =\"color:white; border: 1.5px solid black; border-radius: 10px; padding: 2px 5px; background-color: gainsboro; text-decoration: none;\" href="+VerifyURL+">Active Account</a>","text/html");

        System.out.println("sending...");
        
        // Send message
        Transport.send(message);
        System.out.println("Sent message successfully....");
	}catch (MessagingException  e) {
		e.printStackTrace();
		System.out.println("error pordiossss");
	}
	
	}
}
