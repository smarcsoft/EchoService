package com.smarcsoft;

/*
 * Copyright 2015 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.lang.model.util.ElementScanner6;

/**
 * A simple client that requests a greeting from the {@link EchoServer}.
 */
public class EchoClient {
  private static final Logger logger = Logger.getLogger(EchoClient.class.getName());
  private static final int OP_ECHO = 0;
  private static final int OP_CPU = 1;
  private static final int OP_CPUJOB = 2;

  private final EchoGrpc.EchoBlockingStub blockingStub;

  /** Construct client for accessing Echo server using the existing channel. */
  public EchoClient(Channel channel) {
    // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
    // shut it down.

    // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
    blockingStub = EchoGrpc.newBlockingStub(channel);
  }

  /** Say hello to server. */
  public void greet(String name) {
    logger.log(Level.INFO, "Will try to greet {0} ...", name);
    try {
      Reply v = blockingStub.version(com.google.protobuf.Empty.getDefaultInstance());
      String version = v.getMessage();
      logger.log(Level.INFO,"Contacted server version {0} successfully...", version);
      Request request = Request.newBuilder().setName(name).build();
      Reply response = blockingStub.say(request);
      logger.info("Greeting: " + response.getMessage());
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
  }

   /** Say hello to server. */
   public void cpu(int secs) {
    logger.log(Level.INFO, "Launch CPU cycles for {0} seconds...", secs);
    try {
      Iterations its = blockingStub.cpu(Seconds.newBuilder().setSecs(secs).build());
      long it = its.getIterations();
      logger.info("We have iterated " + it + " time for " + secs +" seconds...");
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
    }
  }

  /** Say hello to server. */
  public void cpuJob(int secs) {
    logger.log(Level.INFO, "Launch CPU cycles for {0} seconds...", secs);
    try {
      Iterations its = blockingStub.cpuJob(Seconds.newBuilder().setSecs(secs).build());
      long it = its.getIterations();
      logger.info("We have iterated " + it + " time for " + secs +" seconds...");
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
    }
  }

  /**
   * Greet server. If provided, the first element of {@code args} is the name to use in the
   * greeting. The second argument is the target server.
   */
  public static void main(String[] args) throws Exception {
    String user = "world";
    int secs = 30;
    int batch_size = 1;
    // Access a service running on the local machine on port 50051
    String target = "localhost:50051";

    int op =OP_ECHO;

    int arg_number = args.length;
    int arg_current = 0;
    logger.log(Level.INFO,"Processing arguments...");
    while (arg_number >0)
    {
      // Allow passing in the user and target strings as command line arguments
      if ("--help".equals(args[arg_current])) {
        printhelp(user, secs, target);
        System.exit(1);
      }
      if("echo".equals(args[arg_current]))
      {
        arg_number--; op = OP_ECHO;arg_current++;
        logger.log(Level.INFO, "Echo...");
        if(arg_number >1) { user=args[arg_current]; arg_current++;arg_number--; logger.log(Level.INFO, "on {0}...", user); } else {printhelp(user, secs, target);System.exit(1); }
      }
      if("cpujob".equals(args[arg_current]))
      {
        logger.log(Level.INFO, "CPUJOB...");
        arg_number--; arg_current++; op = OP_CPUJOB;
        if(arg_number >1) { secs=Integer.parseInt(args[arg_current]); arg_number--; logger.log(Level.INFO, "For {0} seconds.", secs);arg_current++;} else {printhelp(user, secs, target);System.exit(1); }
      } else 
      if("cpu".equals(args[arg_current]))
      {
        logger.log(Level.INFO, "CPU...");
        arg_number--; op = OP_CPU;arg_current++;
        if(arg_number >1) { secs=Integer.parseInt(args[arg_current]);arg_number--; logger.log(Level.INFO, "For {o} seconds", secs);} else {printhelp(user, secs, target);System.exit(1); }
      } else
      if("batch".equals(args[arg_current]))
      {
        logger.log(Level.INFO, "Batched for ...");
        arg_number--; arg_current++;
        if(arg_number >1) { batch_size=Integer.parseInt(args[arg_current]);arg_number--;logger.log(Level.INFO, "{0} jobs...", batch_size); } else {printhelp(user, secs, target);System.exit(1); }
      } 
      if(arg_number == 1) {target=args[args.length-1];arg_number--;logger.log(Level.INFO, "On service {0}...", target);}
    }
   
    // Create a communication channel to the server, known as a Channel. Channels are thread-safe
    // and reusable. It is common to create channels at the beginning of your application and reuse
    // them until the application shuts down.
    ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext()
        .build();
    try {
      EchoClient client = new EchoClient(channel);
      for(int i=0;i< batch_size;i++)
        switch(op)
        {
          case OP_ECHO:
            logger.log(Level.INFO, "Greeting {0} time",i);
            client.greet(user);
          break;
          case OP_CPU:
            logger.log(Level.INFO, "Greeting CPU for {0} second {1} time",new Object[]{secs, i});
            client.cpu(secs);
          break;
          case OP_CPUJOB:
            logger.log(Level.INFO, "Launching CPU job for {0} second {1} time", new Object[]{secs, i});
            client.cpuJob(secs);
            break;
          default:
              logger.log(Level.SEVERE, "Operation {0} handled", op);
        }
    } finally {
      // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
      // resources the channel should be shut down when it will no longer be used. If it may be used
      // again leave it running.
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
  }

  private static void printhelp(String user, int secs, String target) {
    System.err.println("Usage: [echo | cpu ] [args] [batch [batch_size]]");
    System.err.println("");
    System.err.println("  echo    [name target]. Defaults to " + user);
    System.err.println("  cpu     [secs target]. Number of seconds on which the cpu will be pegged. Default to " + secs +" seconds");
    System.err.println("target is the host:port of the server. Default to " + target);
  } 
}

