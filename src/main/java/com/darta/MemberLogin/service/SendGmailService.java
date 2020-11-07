package com.darta.MemberLogin.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.darta.MemberLogin.model.UserAccount;

public class SendGmailService {

	private final Properties props = new Properties();
	private String mailUser;
	private String mailPassword;

	public SendGmailService(String mailUser, String mailPassword) {

		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", 587);
		this.mailUser = mailUser;// WedTesthw01@gmail.com
		this.mailPassword = mailPassword;// aa784512
	}

	public void validationLink(UserAccount user) {

		try {
			String link = String.format("http://localhost:8080/verify?email=%s&password=%s",
					user.getEmail(), user.getPassword());

			String anchor = String.format("<a href='%s'>驗證郵件</a>", link);

			String html = String.format("請按 %s 啟用帳戶或複製鏈結至網址列：<br><br> %s", anchor, link);

			Message message = createMessage(mailUser, user.getEmail(), "註冊結果", html);
			Transport.send(message);
		} catch (MessagingException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Message createMessage(String from, String to, String subject, String text)
			throws MessagingException, AddressException, IOException {

		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailUser, mailPassword);
			}
		};
		
		Session session = Session.getDefaultInstance(props, auth);

		Multipart multiPart = multiPart(text);

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject(subject);
		message.setSentDate(new Date());
		message.setContent(multiPart);

		return message;
	}

	private Multipart multiPart(String text) throws MessagingException, UnsupportedEncodingException, IOException {

		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(text, "text/html;charset=UTF-8");

		Multipart multiPart = new MimeMultipart();
		multiPart.addBodyPart(htmlPart);

		return multiPart;
	}

	public void passwordResetLink(UserAccount user) {

		try {
			String link = String.format("http://localhost:8080/resetpassword?username=%s&email=%s",
					user.getUserName(), user.getEmail());

			String anchor = String.format("<a href='%s'>前往更改密碼</a>", link);

			String html = String.format("請按 %s 更改密碼或複製鏈結至網址列：<br><br> %s", anchor, link);

			Message message = createMessage(mailUser, user.getEmail(), "更改密碼", html);

			Transport.send(message);

		} catch (MessagingException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
