package com.darta.MemberLogin.service;

import com.darta.MemberLogin.model.UserAccount;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class SendGmailService {

    private final Properties props = new Properties();
    private String mailUser;
    private String mailPassword;

    public SendGmailService(String mailUser, String mailPassword) {

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", 587);
        // 此方式並不是最好的寫法
        props.put("mail.smtp.ssl.trust", "*");
        this.mailUser = mailUser;
        this.mailPassword = mailPassword;
    }

    /*
     * 開通帳戶連結
     * */
    public void validationLink(UserAccount user) {

        String code = user.getCode();
        System.out.println("code : " + code);
        try {
            String link = String.format("http://localhost:8081/verify?code=%s", code);
            String anchor = String.format("<a href='%s'>驗證郵件</a>", link);
            String html = String.format("請按 %s 啟用帳戶或複製鏈結至網址列：<br><br> %s", anchor, link);

            Message message = createMessage(mailUser, user.getEmail(), "註冊結果", html);
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * 建立信件內文
     * */
    private Message createMessage(String from, String to, String subject, String text)
            throws MessagingException, AddressException, IOException {

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUser, mailPassword);
            }
        };

        Session session = Session.getInstance(props, auth);

        Multipart multiPart = multiPart(text);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setContent(multiPart);

        return message;
    }

    /*
     * html 格式
     * */
    private Multipart multiPart(String text) throws MessagingException, UnsupportedEncodingException, IOException {

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(text, "text/html;charset=UTF-8");

        Multipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(htmlPart);

        return multiPart;
    }

    /*
     * 重設密碼連結
     * */
    public void passwordResetLink(UserAccount user) {

        try {
            String link = String.format("http://localhost:8081/resetPassword?username=%s", user.getUsername());
            String anchor = String.format("<a href='%s'>前往更改密碼</a>", link);
            String html = String.format("請按 %s 更改密碼或複製鏈結至網址列：<br><br> %s", anchor, link);

            Message message = createMessage(mailUser, user.getEmail(), "更改密碼", html);

            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
