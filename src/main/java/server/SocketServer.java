package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class SocketServer
{
	private int port;
	private boolean listening;
	private int noOfMailSenders;
	private int noOfRequestHandlers;
	private EmailDispatchQueue emailDispatchQueue;
	private MailServerConnection mailServerConnection;
	public  static Logger logger = Logger.getLogger("server");

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
		this.port = 9999;
		this.listening = true;
		this.noOfMailSenders = 2;
		this.noOfRequestHandlers = 2;

		// Mail serve created with default configs. Customize mail server can be created by using the constructor which requires Properties as an argument.
		this.mailServerConnection = new MailServerConnection();
		this.emailDispatchQueue = new EmailDispatchQueue( new LinkedBlockingQueue<>(1000) );
	}

	public void start()
	{
		ExecutorService mailSenderExecutor = Executors.newFixedThreadPool( noOfMailSenders );
		for ( int i = 0; i < noOfMailSenders; i++ )
		{
			mailSenderExecutor.execute( new EmailDispatcher( emailDispatchQueue, mailServerConnection ) );
		}


		ExecutorService mailHandlerExecutor = Executors.newFixedThreadPool( noOfRequestHandlers );
		try (ServerSocket serverSocket = new ServerSocket( port ))
		{
			logger.info("The Email Server is running on port = " );
			while ( listening )
			{
				mailHandlerExecutor.execute( new SendEmailReqProducer( serverSocket.accept(), emailDispatchQueue ) );
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}


	}
}
