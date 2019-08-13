package model;

import java.io.Serializable;

public class SendEmailAck implements Serializable
{
	private String requestId;
	private Status status;

	public enum Status
	{
		OK, ERROR;
	}

	public SendEmailAck( String requestId, Status status )
	{
		this.requestId = requestId;
		this.status = status;
	}

	public String getRequestId()
	{
		return requestId;
	}

	public void setRequestId( String requestId )
	{
		this.requestId = requestId;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus( Status status )
	{
		this.status = status;
	}
}

