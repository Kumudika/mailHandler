package server;

import com.google.gson.Gson;
import model.SendEmailAck;
import model.SendEmailReq;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
				ObjectInputStream inputStream = new ObjectInputStream( socket.getInputStream() );
				ObjectOutputStream outputStream = new ObjectOutputStream( socket.getOutputStream() )
		)
		{
			//  read the mail
			String data = ( String ) inputStream.readObject();
			SendEmailReq sendEmailReq = gson.fromJson( data, SendEmailReq.class );
			SendEmailAck sendEmailAck = emailDispatchQueue.enqueue( sendEmailReq );
			outputStream.writeObject( gson.toJson( sendEmailAck ) );
			socket.close();
		}
		catch ( IOException | ClassNotFoundException e )
		{
			//			CommonLogger.logErrorMessage(this, e);
		}
	}
}
