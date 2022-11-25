package bd.com.squarehealth.corelibrary.common.mail;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;
import java.util.Properties;

@Getter
public class MailerImpl implements Mailer {

    private final String host;
    private final int port;
    private final String username;

    @Getter(AccessLevel.NONE)
    private final JavaMailSender mailSender;

    public MailerImpl(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        mailSender = createJavaMailSender(host, port, username, password);
    }

    @Override
    public boolean sendTextMessage(String recipient, String subject, String message) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(username);
            simpleMailMessage.setTo(recipient);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);

            mailSender.send(simpleMailMessage);

            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean sendHtmlMessage(String recipient, String subject, String templateName, Map<String, Object> data) {
        throw new RuntimeException("Method not implemented");
    }

    private static JavaMailSender createJavaMailSender(String host, int port, String username, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);                   // "smtp.gmail.com"
        mailSender.setPort(port);                   // 587
        mailSender.setUsername(username);           // my.gmail@gmail.com
        mailSender.setPassword(password);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return mailSender;
    }
}
