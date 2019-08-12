package server;

import jdk.internal.net.http.common.Log;
import model.MailRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueService {
    private static QueueService instance = null;
    private static BlockingQueue<MailRequest> mailRequestBlockingQueue = null;
    private QueueService() {}
    public static QueueService getInstance() {
        if (instance == null) {
            instance = new QueueService();
        }
        return instance;
    }
    private void initialize() {
        if (mailRequestBlockingQueue == null) {
            mailRequestBlockingQueue = new LinkedBlockingQueue<MailRequest>();
            QueueProcessor eventProcessor = new QueueProcessor();
            eventProcessor.start();
        }
    }
    public boolean putRequestInQueue(MailRequest mailRequest) {
        try {
            initialize();
            mailRequestBlockingQueue.put(mailRequest);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    class QueueProcessor extends Thread {
        @Override
        public void run() {
            for (;;) {
                MailRequest mailRequest = null;
                try {
                    mailRequest = mailRequestBlockingQueue.take();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
