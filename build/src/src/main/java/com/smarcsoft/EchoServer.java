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

package com.smarcsoft;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.Empty;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class EchoServer {
  private static final Logger logger = Logger.getLogger(EchoServer.class.getName());

  public static final String VERSION = "v2.1";

  private Server server;

  private void start() throws IOException {
    /* The port on which the server should run */
    int port = 50051;
    server = ServerBuilder.forPort(port).addService(new EchoImpl()).build().start();
    logger.log(Level.INFO, "Server started, listening on {0}", port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown
        // hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        try {
          EchoServer.this.stop();
        } catch (InterruptedException e) {
          e.printStackTrace(System.err);
          Thread.currentThread().interrupt();
        }
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() throws InterruptedException {
    if (server != null) {
      server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon
   * threads.
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    final EchoServer server = new EchoServer();
    server.start();
    server.blockUntilShutdown();
  }

  static class EchoImpl extends EchoGrpc.EchoImplBase {

    @Override
    public void say(Request request, StreamObserver<Reply> responseObserver) {
      logger.log(Level.INFO, "Service request {0}", request.getName());
      String response;
      try {
        response = "Hello " + request.getName() + " from " + InetAddress.getLocalHost().getHostName();
      } catch (UnknownHostException e) {
        response = "Hello " + request.getName() + " from unknown host";
      }
      Reply reply = Reply.newBuilder().setMessage(response).build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }

    @Override
    public void version(Empty request, StreamObserver<Reply> responseObserver) {
      Reply reply = Reply.newBuilder().setMessage("Version " + VERSION).build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }

    @Override
    public void cpu(com.smarcsoft.Seconds request,
        io.grpc.stub.StreamObserver<com.smarcsoft.Iterations> responseObserver) {
          logger.log(Level.INFO, "cpu invoked (version {0})", VERSION);
          int secs = request.getSecs();
          logger.log(Level.INFO, "asked to run cpu for {0} seconds", secs);
          int r = new Random().nextInt(500);
          String command="kubectl run cpu-"+r+" --image=sebmarc/cpu --restart=Never --attach=true --quiet=true --rm=true -- "+secs;
          try{
            logger.log(Level.INFO, "invoking: {0}",  command);
            Process p = Runtime.getRuntime().exec(command);
            InputStream stream = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(isr);
            String tmp;
            String output="";
            while((tmp = br.readLine()) != null)
            {
              output = output + tmp;
            }
            logger.log(Level.INFO, "output read from the process invokation: {0}", output);
            int exitCode = p.waitFor();
            if(exitCode == 0)
            {
              logger.log(Level.INFO, "kubectl returned successfully");
              long it_returned = Integer.parseInt(output);
              logger.log(Level.INFO, "command returned with  {0} iterations", it_returned);
              Iterations reply = Iterations.newBuilder().setIterations(it_returned).build();
              responseObserver.onNext(reply);
            }
            else
            {
              logger.log(Level.SEVERE, "kubectly could not be invoked successfully. Returned code = {0}", exitCode);
              Iterations reply = Iterations.newBuilder().setIterations(0).build();
              responseObserver.onNext(reply);
            }
          }
          catch(IOException e)
          {
            logger.log(Level.SEVERE, "kubectly could not be invoked successfully. IOException: ({0})", e.getLocalizedMessage());
            Iterations reply = Iterations.newBuilder().setIterations(0).build();
            responseObserver.onNext(reply);
          }
          catch(InterruptedException e)
          {
            logger.log(Level.SEVERE, "kubectly could not be invoked successfully. InterruptedException: ({0})", e.getLocalizedMessage());
            Iterations reply = Iterations.newBuilder().setIterations(0).build();
            responseObserver.onNext(reply);
          }
          catch(SecurityException e)
          {
            logger.log(Level.SEVERE, "kubectly could not be invoked successfully. SecurityException: ({0})", e.getLocalizedMessage());
            Iterations reply = Iterations.newBuilder().setIterations(0).build();
            responseObserver.onNext(reply);
          }
          finally
          {
            logger.log(Level.INFO, "kubectly invocation end.");
          }

          responseObserver.onCompleted();
        }
  }
}
