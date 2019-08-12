package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MailServer {
    public static void main(String[] args) {
        int port = 9999;
        boolean listening = true;
        int noOfMailSenders = 2;
        int noOfRequestHandlers = 5;
        QueueService mailQueue = QueueService.getInstance();

        ExecutorService mailSenderExecutor = Executors.newFixedThreadPool(noOfMailSenders);
        for(int i = 0; i < noOfMailSenders; i++)
        {
            mailSenderExecutor.execute(new EmailSender(mailQueue));
        }

        ExecutorService mailHandlerExecutor = Executors.newFixedThreadPool(noOfRequestHandlers);
        try(ServerSocket serverSocket = new ServerSocket(port))
        {
            while(listening)
            {
                mailHandlerExecutor.execute(new EmailRequestHandler(serverSocket.accept(), mailQueue));
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
