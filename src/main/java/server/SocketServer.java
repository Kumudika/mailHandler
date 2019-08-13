package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketServer
{
	private int port;
	private boolean listening;
	private int noOfMailSenders;
	private int noOfRequestHandlers;
	private EmailDispatchQueue emailDispatchQueue;
	private MailServerConnection mailServerConnection;

	public SocketServer( int port, boolean listening, int noOfMailSenders, int noOfRequestHandlers, EmailDispatchQueue emailDispatchQueue, MailServerConnection mailServerConnection )
	{
		this.port = port;
		this.listening = listening;
		this.noOfMailSenders = noOfMailSenders;
		this.noOfRequestHandlers = noOfRequestHandlers;
		this.emailDispatchQueue = emailDispatchQueue;
		this.mailServerConnection = mailServerConnection;
	}

	public SocketServer()
	{
		// There parameters can be load from configs as well
		this.port = 2500;
		this.listening = true;
		this.noOfMailSenders = 5;
		this.noOfRequestHandlers = 5;

		//properties for mail server
		Properties properties = new Properties();
		properties.put( "mail.smtp.auth", false );
		properties.put( "mail.smtp.starttls.enable", "true" );
		properties.put( "mail.smtp.host", "localhost" );
		properties.put( "mail.smtp.port", "25" );

		this.mailServerConnection = new MailServerConnection( properties );
		this.emailDispatchQueue = new EmailDispatchQueue( new LinkedBlockingQueue<>() );
	}

	public void start()
	{
		ExecutorService mailHandlerExecutor = Executors.newFixedThreadPool( noOfRequestHandlers );
		try (ServerSocket serverSocket = new ServerSocket( port ))
		{
			while ( listening )
			{
				mailHandlerExecutor.execute( new SendEmailReqProducer( serverSocket.accept(), emailDispatchQueue ) );
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}

		ExecutorService mailSenderExecutor = Executors.newFixedThreadPool( noOfMailSenders );
		for ( int i = 0; i < noOfMailSenders; i++ )
		{
			mailSenderExecutor.execute( new EmailDispatcher( emailDispatchQueue, mailServerConnection ) );
		}
	}
}
