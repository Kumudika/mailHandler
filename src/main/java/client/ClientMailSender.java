package client;

import com.google.gson.Gson;
import model.SendEmailAck;
import model.SendEmailReq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ClientMailSender implements Runnable
{
	private InetAddress host;
	private int port;
	private SendEmailReq sendEmailReq;
	private Gson gson = new Gson();

	private static final String SENDER_ADDRESS = "janitha@test.com";
	private static final String RECIPIENT_ADDRESS = "janithalokuge92@gmail.com";

	public ClientMailSender( SendEmailReq sendEmailReq )
	{
		try
		{
			host = InetAddress.getLocalHost();
			port = 2500;
			this.sendEmailReq = sendEmailReq;
		}
		catch ( UnknownHostException e )
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try (
				Socket socket = new Socket( host, port );
				PrintWriter printWriter =
						new PrintWriter( socket.getOutputStream(), true );
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader( socket.getInputStream() ) )
		)
		{
			String outputString = gson.toJson( sendEmailReq, SendEmailReq.class );
			printWriter.println( outputString );

			String inputString = bufferedReader.readLine();
			SendEmailAck resMsg = gson.fromJson( inputString, SendEmailAck.class );
			System.out.println( resMsg );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public static SendEmailReq getEmailReq( String requestId )
	{
		SendEmailReq emailReqMsg = new SendEmailReq();
		emailReqMsg.setRequestId( requestId );
		emailReqMsg.setBody( "Body" );
		emailReqMsg.setReceipentEmail( RECIPIENT_ADDRESS );
		emailReqMsg.setSubject( requestId );
		emailReqMsg.setSenderName( SENDER_ADDRESS );
		return emailReqMsg;
	}

}
