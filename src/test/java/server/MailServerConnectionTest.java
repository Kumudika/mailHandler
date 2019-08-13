package server;

import client.ClientMailSender;
import model.SendEmailReq;
import org.junit.Test;

import static org.junit.Assert.*;

public class MailServerConnectionTest {

    @Test
    public void sendEmail() {
        MailServerConnection mailServerConnection = new MailServerConnection();
        SendEmailReq emailReq = ClientMailSender.getEmailReq("1");
        boolean sentStatus = mailServerConnection.sendEmail(emailReq);
        assertTrue(sentStatus);
    }
}