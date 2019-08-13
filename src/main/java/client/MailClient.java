package client;

import model.SendEmailReq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MailClient
{
	public static void main( String[] args )
	{
		if ( true )
//		if ( args.length == 2 )
		{
//			int requests = Integer.parseInt( args[0] );
			int requests = 20;
//			int threads = Integer.parseInt( args[1] );
			int threads = 2;
			ExecutorService executorService = Executors.newFixedThreadPool( threads );

			for ( int i = 0; i < requests; i++ )
			{
				executorService.execute( new ClientMailSender( getRq( String.valueOf( i ) ) ) );
			}

			System.out.println("end");

		}
	}

	public static SendEmailReq getRq( String requestId )
	{
		SendEmailReq emailReqMsg = new SendEmailReq();
		emailReqMsg.setRequestId( requestId );
		emailReqMsg.setBody( "Body" );
		emailReqMsg.setReceipentEmail( "janithalokuge92@gmail.com" );
		emailReqMsg.setSubject( requestId );
		emailReqMsg.setSenderName( "janitha" );
		return emailReqMsg;
	}

}
