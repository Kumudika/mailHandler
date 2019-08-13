package client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MailClient
{
	public static void main( String[] args )
	{
		int requests = 100;
		int threads = 10;

		if ( args.length == 2 )
		{
			requests = Integer.parseInt( args[0] );
			threads = Integer.parseInt( args[1] );
		}
		ExecutorService executorService = Executors.newFixedThreadPool( threads );

		for ( int i = 0; i < requests; i++ )
		{
			executorService.execute( new ClientMailSender( ClientMailSender.getEmailReq( String.valueOf( i ) ) ) );
		}

		executorService.shutdown();
		System.out.println( "Email sending task completed" );
	}
}


