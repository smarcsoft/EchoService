package com.smarcsoft;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.TimeUnit;

public class EchoClientThread implements Runnable {

    private static final Logger logger = Logger.getLogger(EchoClientThread.class.getName());
    private int batch_size;
    private int op;
    private String user;
    private int secs;
    private String target;

    EchoClientThread(int batch_size, int operation, String user, int secs, String target) {
        this.batch_size = batch_size;
        op = operation;
        this.user = user;
        this.secs = secs;
        this.target = target;
    }

    public void run() {
        logger.info("Running new thread...");
        // Create a communication channel to the server, known as a Channel. Channels
        // are thread-safe
        // and reusable. It is common to create channels at the beginning of your
        // application and reuse
        // them until the application shuts down.
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS
                // to avoid
                // needing certificates.
                .usePlaintext().build();
        try {
            EchoClient client = new EchoClient(channel);
            for (int i = 0; i < batch_size; i++)
                switch (op) {
                    case EchoClient.OP_ECHO:
                        logger.log(Level.INFO, "Greeting {0}/{1} time", new Object[] { i, batch_size });
                        client.greet(user);
                        break;
                    case EchoClient.OP_CPU:
                        logger.log(Level.INFO, "Greeting CPU for {0} seconds {1}/{2} time(s)",
                                new Object[] { secs, i, batch_size });
                        client.cpu(secs);
                        break;
                    case EchoClient.OP_CPUJOB:
                        logger.log(Level.INFO, "Launching CPU job for {0} second {1}/{2} time(s)",
                                new Object[] { secs, i, batch_size });
                        client.cpuJob(secs);
                        break;
                    default:
                        logger.log(Level.SEVERE, "Operation {0} unknown", op);
                }
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent
            // leaking these
            // resources the channel should be shut down when it will no longer be used. If
            // it may be used
            // again leave it running.
            try {
                channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            logger.info("Finishing thread...");
}
    }

}
