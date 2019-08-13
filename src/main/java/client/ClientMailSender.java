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
			port = 9999;
			this.sendEmailReq = sendEmailReq;
			random = new Random();
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
				Socket socket = new Socket(host, port);
				PrintWriter printWriter =
						new PrintWriter(socket.getOutputStream(), true);
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()))
		)
		{
			System.out.println("1");
			String outputString = gson.toJson( sendEmailReq, SendEmailReq.class );
			printWriter.println(outputString);

			String inputString =  bufferedReader.readLine();
			SendEmailAck resMsg = gson.fromJson( inputString, SendEmailAck.class );
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
