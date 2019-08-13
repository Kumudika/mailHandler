package server;

import com.google.gson.Gson;
import model.SendEmailAck;
import model.SendEmailReq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EmailReqProducer implements Runnable
{
	private Socket socket;
	private EmailDispatchQueue emailDispatchQueue;
	private Gson gson = new Gson();

	public EmailReqProducer( Socket socket, EmailDispatchQueue emailDispatchQueue )
	{
		this.socket = socket;
		this.emailDispatchQueue = emailDispatchQueue;
	}

	@Override
	public void run()
	{
		try (
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader( socket.getInputStream() ) );
				PrintWriter printWriter =
						new PrintWriter( socket.getOutputStream(), true );
		)
		{
			String data = bufferedReader.readLine();
			SendEmailReq sendEmailReq = gson.fromJson( data, SendEmailReq.class );
			SendEmailAck sendEmailAck = emailDispatchQueue.enqueue( sendEmailReq );
			printWriter.println( gson.toJson( sendEmailAck ) );
			socket.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}
}
