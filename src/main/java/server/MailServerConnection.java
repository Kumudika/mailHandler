package server;

import model.SendEmailReq;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailServerConnection
{

	private Session session;

	public MailServerConnection( Properties properties )
	{
		this.session = Session.getInstance( properties, new Authenticator()
		{
			// no need to override password authentication since it is not required.
		} );
	}

	public void sendEmail( SendEmailReq sendEmailReq )
	{
		try
		{
			Message message = new MimeMessage( session );
			message.setFrom( new InternetAddress( sendEmailReq.getSenderName() ) );
			message.setRecipients(
					Message.RecipientType.TO, InternetAddress.parse( sendEmailReq.getReceipentEmail() ) );
			message.setSubject( sendEmailReq.getSubject() );

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent( sendEmailReq.getBody(), "text/html" );

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart( mimeBodyPart );

			message.setContent( multipart );
			Transport.send( message );

		}
		catch ( MessagingException e )
		{
			e.printStackTrace();
		}
	}

}
