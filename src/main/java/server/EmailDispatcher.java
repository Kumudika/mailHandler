package server;

import model.SendEmailReq;

public class EmailDispatcher implements Runnable
{
	private EmailDispatchQueue emailDispatchQueue;
	private MailServerConnection mailServerConnection;
	private boolean running = true;

	public EmailDispatcher( EmailDispatchQueue emailDispatchQueue, MailServerConnection mailServerConnection, boolean running )
	{
		this.emailDispatchQueue = emailDispatchQueue;
		this.mailServerConnection = mailServerConnection;
		this.running = running;
	}

	@Override
	public void run()
	{
		while ( running )
		{
			SendEmailReq sendEmailReq = emailDispatchQueue.dequeue();
			if ( sendEmailReq != null )
			{
				mailServerConnection.sendEmail( sendEmailReq );
			}
		}
	}
}
