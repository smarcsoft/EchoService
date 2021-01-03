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
    logger.info("Will try to greet " + name + " ...");
    try {
      Reply v = blockingStub.version(com.google.protobuf.Empty.getDefaultInstance());
      String version = v.getMessage();
      logger.info("Contacted server version " + version + " successfully...");
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
    logger.info("Launch CPU cycles for " + secs + " seconds...");
    try {
      Iterations its = blockingStub.cpu(Seconds.newBuilder().setSecs(secs).build());
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
    // Access a service running on the local machine on port 50051
    String target = "localhost:50051";

    int op =OP_ECHO;
    // Allow passing in the user and target strings as command line arguments
    if (args.length > 0) {
      if ("--help".equals(args[0])) {
        printhelp(user, secs, target);
        System.exit(1);
      }
      if("echo".equals(args[0]))
      {
        if(args.length >1)
          user=args[1];
      } else
      if("cpu".equals(args[0]))
      {
        op = OP_CPU;
        if(args.length >1)
          secs=Integer.parseInt(args[1]);
      } else 
      {
        printhelp(user, secs, target);
        System.exit(1);
      }
      if(args.length >2)
      target=args[2];
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
      switch(op)
      {
        case OP_ECHO:
          client.greet(user);
        break;
        case OP_CPU:
          client.cpu(secs);
        break;
      }
    } finally {
      // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
      // resources the channel should be shut down when it will no longer be used. If it may be used
      // again leave it running.
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
  }

  private static void printhelp(String user, int secs, String target) {
    System.err.println("Usage: [echo | cpu ] [args] ");
    System.err.println("");
    System.err.println("  echo    [name target]. Defaults to " + user);
    System.err.println("  cpu     [secs target]. Number of seconds on which the cpu will be pegged. Default to " + secs +" seconds");
    System.err.println("target is the host:port of the server. Default to " + target);
  } 
}

