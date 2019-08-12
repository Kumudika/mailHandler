package client;

import com.google.gson.Gson;
import model.MailRequest;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ClientMailSender implements Runnable {
    private InetAddress host;
    ObjectMapper
    private int port;
    MailRequest mailRequest;
    private String requestId;
    Random random;

    public ClientMailSender(MailRequest mailRequest) {
        try {
            host = InetAddress.getLocalHost();
            port = 9876;
            this.mailRequest = mailRequest;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try(
                Socket socket = new Socket(host, port);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ){
            String writeJson = objectMapper.writeValueAsString(mailRequest);
            out.println(writeJson);
            SocketMailLogger.logInfoMessage( "Mail request sent : " + emailRequest.getRequestId() );

            String readValue = in.readLine();
            SendEmailAck emailAck = objectMapper.readValue(readValue, SendEmailAck.class);
            SocketMailLogger.logInfoMessage( "Ack received " + emailAck.getRequestId() + " : " + emailAck.getStatus() );

            int sleepTime = random.nextInt(450) + 50;
            Thread.sleep(sleepTime);
        }
        catch(JsonProcessingException e)
        {
            SocketMailLogger.logErrorMessage( "Error occurred while JSON parsing: " + e.getMessage(), e );
        }
        catch(IOException e)
        {
            SocketMailLogger.logErrorMessage( "Error occurred in socket connection: " + e.getMessage(), e );
        }
        catch(InterruptedException e)
        {
            SocketMailLogger.logErrorMessage( "Error occurred in requestSender thread running : " + e.getMessage(), e );
            Thread.currentThread().interrupt();
        }
    }
}
