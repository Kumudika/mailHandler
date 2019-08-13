package server;

import model.SendEmailReq;

public class EmailDispatcher implements Runnable
{
	private EmailDispatchQueue emailDispatchQueue;
	private MailServerConnection mailServerConnection;

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
			if ( sendEmailReq != null )
			{
				mailServerConnection.sendEmail( sendEmailReq );
			}
		}
	}
}
