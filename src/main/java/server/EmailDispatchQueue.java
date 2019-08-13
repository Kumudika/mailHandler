package server;

import model.SendEmailAck;
import model.SendEmailReq;

import java.util.concurrent.BlockingQueue;

public class EmailDispatchQueue
{
	BlockingQueue<SendEmailReq> queue;

	public EmailDispatchQueue( BlockingQueue<SendEmailReq> queue )
	{
		this.queue = queue;
	}

	public BlockingQueue<SendEmailReq> getQueue()
	{
		return queue;
	}

	public void setQueue( BlockingQueue<SendEmailReq> queue )
	{
		this.queue = queue;
	}

	public SendEmailAck enqueue( SendEmailReq sendEmailReq )
	{
		try
		{
			this.queue.put( sendEmailReq );
			return new SendEmailAck( sendEmailReq.getRequestId(), SendEmailAck.Status.OK );
		}
		catch ( InterruptedException e )
		{
			e.printStackTrace();
		}

		return null;
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
			return null;
		}
	}
}
