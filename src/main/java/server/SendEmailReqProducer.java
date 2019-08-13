package server;

import com.google.gson.Gson;
import model.SendEmailAck;
import model.SendEmailReq;

import java.io.*;
import java.net.Socket;

public class SendEmailReqProducer implements Runnable
{
	private Socket socket;
	private EmailDispatchQueue emailDispatchQueue;
	Gson gson = new Gson();

	public SendEmailReqProducer( Socket socket, EmailDispatchQueue emailDispatchQueue )
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
			//			CommonLogger.logErrorMessage(this, e);
			e.printStackTrace();
		}
	}
}
