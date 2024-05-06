package controller;

import Messaging.JMSClient;

public class JMSClientMain {

	  public static void main(String[] args) {
	    JMSClient jmsClient = new JMSClient(); // Assuming no-argument constructor

	    String message = "Test message from standalone client";
	    String receiverId = "userABC";

	    // Send a message with receiver ID
	    jmsClient.sendMessage(message, receiverId);

	    // Optionally, receive a message (if your JMSClient supports it)
	    String receivedMessage = jmsClient.getMessage();
	    if (receivedMessage != null) {
	      System.out.println("Received message: " + receivedMessage);
	    } else {
	      System.out.println("No message received currently.");
	    }

	    System.exit(0); // Indicate successful termination
	  }
	}