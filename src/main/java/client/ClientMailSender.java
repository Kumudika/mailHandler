package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import model.SendEmailReq;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ClientMailSender implements Runnable {
    private InetAddress host;
    ObjectMapper objectMapper;
    private int port;
    SendEmailReq mailRequest;
    private String requestId;
    Random random;
    private  int rr;

    public ClientMailSender( SendEmailReq mailRequest) {
        try {
            objectMapper = new ObjectMapper();
            host = InetAddress.getLocalHost();
            port = 2500;
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

            String readValue = in.readLine();

            int sleepTime = random.nextInt(450) + 50;
            Thread.sleep(sleepTime);
        } catch(IOException e)
        {
            e.printStackTrace();
        } catch(InterruptedException e)
        {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
