package bd.com.squarehealth.corelibrary.common.mail;

import java.util.Map;

public interface Mailer {

    /**
     * This method shall be used to send plain text email messages.
     * @param recipient Email address of the recipient to whom the message shall be sent.
     * @param subject Subject of the email.
     * @param message Message that shall be sent.
     * @return Returns true if the message is sent successfully. Otherwise, returns false.
     */
    boolean sendTextMessage(String recipient, String subject, String message);

    /**
     * This method shall be used to send styled email messages.
     * Note: This method is not implemented.
     * @param recipient Email address of the recipient to whom the message shall be sent.
     * @param subject Subject of the email.
     * @param templateName Name of the HTML template.
     * @param data Data to fill-up the placeholders inside the template.
     * @return Returns true if the message is sent successfully. Otherwise, returns false.
     */
    boolean sendHtmlMessage(String recipient, String subject, String templateName, Map<String, Object> data);
}
