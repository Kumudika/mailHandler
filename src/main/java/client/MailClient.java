package client;

import model.MailRequest;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MailClient {
    public static void main(String[] args) {
        while (args.length == 2) {
            int requests = Integer.parseInt(args[0]);
            int threads = Integer.parseInt(args[1]);
            ExecutorService executorService = Executors.newFixedThreadPool(threads);

            for (int i = 0; i < requests; i++) {
                executorService.execute( new ClientMailSender(getRq(String.valueOf(i))) );
            }

        }
    }
    public static MailRequest getRq(String requestId){
        MailRequest emailReqMsg = new MailRequest(  );
        emailReqMsg.setRequestId( requestId);
        emailReqMsg.setBody( "Body" );
        emailReqMsg.setReceipentEmail( "janithalokuge92@gmail.com" );
        emailReqMsg.setSubject( requestId );
        return  emailReqMsg;
    }

}
