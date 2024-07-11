package sample;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class Email_Controller {


    public void Send_emailWithPdf(String recipient, String messageTxt, String fileName) throws Exception{


        Properties properties = new Properties();

        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        String myAccountEmail = "gamestoresystem@gmail.com";
        String password = "papaki123";

        Session session = Session.getInstance(properties, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(myAccountEmail,password);
            }

        });

        Message message = preparePDFMessage(session,myAccountEmail,recipient,messageTxt,fileName);

        Transport.send(message);
        System.out.println("Message sent");

    }

    public void Send_Email(String recipient, String messageTxt) throws Exception{


        Properties properties = new Properties();
        //συγκεκριμένα για gmail πρεπει να μπει true
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        String myAccountEmail = "gamestoresystem@gmail.com";
        String password = "papaki123";

        Session session = Session.getInstance(properties, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(myAccountEmail,password);
            }

        });

        Message message = prepareMessage(session,myAccountEmail,recipient , messageTxt);

        Transport.send(message);
        System.out.println("Message sent");

    }

    private static Message preparePDFMessage(Session session, String myAccountEmail, String recipient,String messageTxt,String fileName) {

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipients(Message.RecipientType.TO , new InternetAddress[]{new InternetAddress(recipient)});
            message.setSubject("Game Store System");

            Multipart emailContent = new MimeMultipart();

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.attachFile(fileName);

            emailContent.addBodyPart(textBodyPart);
            message.setText(messageTxt);
            message.setContent(emailContent);
            return message;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recipient,String messageTxt) {

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipients(Message.RecipientType.TO , new InternetAddress[]{new InternetAddress(recipient)});
            message.setSubject("Game Store System");

            Multipart emailContent = new MimeMultipart();

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(messageTxt);

            emailContent.addBodyPart(textBodyPart);
            message.setText(messageTxt);
            message.setContent(emailContent);
            return message;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}