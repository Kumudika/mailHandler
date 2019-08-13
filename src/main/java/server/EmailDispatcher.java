package server;

import model.SendEmailReq;

public class EmailDispatcher implements Runnable
{
	EmailDispatchQueue emailDispatchQueue;
	MailServerConnection mailServerConnection;

	public EmailDispatcher( EmailDispatchQueue emailDispatchQueue, MailServerConnection mailServerConnection )
	{
		this.emailDispatchQueue = emailDispatchQueue;
		this.mailServerConnection = mailServerConnection;
	}

	@Override
	public void run()
	{
		while ( true )
		{
			SendEmailReq sendEmailReq = emailDispatchQueue.dequeue();
			mailServerConnection.sendEmail( sendEmailReq );
		}
	}
}
