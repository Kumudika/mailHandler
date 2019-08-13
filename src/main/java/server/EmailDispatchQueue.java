package server;

import model.SendEmailAck;
import model.SendEmailReq;

import java.util.concurrent.LinkedBlockingQueue;

public class EmailDispatchQueue
{
	private LinkedBlockingQueue<SendEmailReq> queue;

	public EmailDispatchQueue( LinkedBlockingQueue<SendEmailReq> queue )
	{
		this.queue = queue;
	}

	public LinkedBlockingQueue<SendEmailReq> getQueue()
	{
		return queue;
	}

	public void setQueue( LinkedBlockingQueue<SendEmailReq> queue )
	{
		this.queue = queue;
	}

	public SendEmailAck enqueue( SendEmailReq sendEmailReq )
	{
		SendEmailAck sendEmailAck;
		try
		{
			this.queue.put( sendEmailReq );
			sendEmailAck = new SendEmailAck( sendEmailReq.getRequestId(), SendEmailAck.Status.OK );
		}
		catch ( InterruptedException e )
		{
			e.printStackTrace();
			sendEmailAck = new SendEmailAck( sendEmailReq.getRequestId(), SendEmailAck.Status.ERROR );
		}

		return sendEmailAck;
	}

	public SendEmailReq dequeue()
	{
		try
		{
			return this.queue.take();
		}
		catch ( InterruptedException e )
		{
			e.printStackTrace();
		}
		return null;
	}
}
