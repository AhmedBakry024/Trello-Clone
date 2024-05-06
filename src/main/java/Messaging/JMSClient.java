
package Messaging;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;

@Startup
@Singleton
public class JMSClient {

	//Map the destination queue
	@Resource(mappedName="java:jboss/jms/queue/notifications")
	private static Queue notifications;
	
	//Inject a JMSContext to get a Connection and Session to the embedded broker
	@Inject
	JMSContext context;
	
	public void sendMessage(String msg) {
		//"Reminder: Deadline approaching for your task"
		
		try{
			//Create a JMSProducer
			JMSProducer producer = context.createProducer();
			
			//Create a TextMessage
			TextMessage message = context.createTextMessage(msg);
			
			//Send the message
			producer.send(notifications, message);
			
			System.out.println("Sent Message: "+ msg);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void sendMessage(String msg, String receiverId) {
		  try {
		    JMSProducer producer = context.createProducer();
		    String messageBody = msg + " [" + receiverId + "]";
		    TextMessage message = context.createTextMessage(messageBody);
		    producer.send(notifications, message);
		    System.out.println("Sent Message: " + messageBody);
		    processMessage(messageBody);
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
	}
	
	public String getMessage() {

		//Create a JMS Consumer
		JMSConsumer consumer = context.createConsumer(notifications);

		try {
			//receive a message without waiting
			TextMessage msg = (TextMessage) consumer.receiveNoWait();
			if(msg != null) {
				System.out.println("Received Message: "+ msg);
				return msg.getBody(String.class);
			}else {
				return null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			//close the consumer
			consumer.close();
		}
	}
	
	
	public void processMessage(String message) {
		  // Extract receiver ID from message body (assuming format from sendMessage)
		  int indexOfBracketOpen = message.indexOf('[');
		  int indexOfBracketClose = message.indexOf(']');
		  if (indexOfBracketOpen > 0 && indexOfBracketClose > indexOfBracketOpen) {
		    String receiverId = message.substring(indexOfBracketOpen + 1, indexOfBracketClose);
		    // Process message based on receiverId
		    System.out.println("Received message for receiver: " + receiverId);
		    // ... your message processing logic here ...
		  } else {
		    System.out.println("Received message without receiver ID: " + message);
		  }
		}
	
	
	
	
}

