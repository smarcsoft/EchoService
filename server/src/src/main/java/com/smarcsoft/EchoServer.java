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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.Empty;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class EchoServer {
  private static final Logger logger = Logger.getLogger(EchoServer.class.getName());

  public static final String VERSION = "v1.2";

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
      logger.log(Level.INFO, "Service request " + request.getName());
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
  }
}
