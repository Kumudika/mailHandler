package model;

import java.io.Serializable;

public class RequestAck implements Serializable {
    private String requestId;
    private String status;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RequestAck(String requestId, boolean isSuccess)
    {
        this.requestId = requestId;
        if(isSuccess)
        {
            this.status = "OK";
        } else
        {
            this.status = "ERROR";
        }
    }
}
