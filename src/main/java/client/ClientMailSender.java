package client;

import com.google.gson.Gson;
import model.SendEmailAck;
import model.SendEmailReq;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ClientMailSender implements Runnable
{
	private InetAddress host;
	private int port;
	SendEmailReq sendEmailReq;
	private String requestId;
	Random random;
	private int rr;
	private Gson gson = new Gson();

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

	public void run()
	{
		try (
				Socket socket = new Socket( host, port );
				PrintWriter out =
						new PrintWriter( socket.getOutputStream(), true );
				BufferedReader in = new BufferedReader(
						new InputStreamReader( socket.getInputStream() ) )
		)
		{
			String writeJson = gson.toJson( sendEmailReq, SendEmailReq.class );
			out.println( writeJson );
			String readValue = in.readLine();
			SendEmailAck resMsg = gson.fromJson( readValue, SendEmailAck.class );
			System.out.println(resMsg);

			int sleepTime = random.nextInt( 450 ) + 50;
			Thread.sleep( sleepTime );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		catch ( InterruptedException e )
		{
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
}
