package messaging;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

import model.Card;

@Startup
@Singleton
public class JMSClient {
	
	@Resource(mappedName = "java:/jms/queue/DLQ")
	private Queue queue;
	
	@Inject
	private JMSContext jmsContext;
	
	public void sendMessage(String message) {
		JMSProducer producer = jmsContext.createProducer();
		producer.send((Destination) queue, message);
		System.out.println("Message sent: " + message);
	}
	
	 public void checkDeadlineAndSendMessages(Card card) {
		    LocalDate today = LocalDate.now();
		    LocalDate deadlineDate = LocalDate.parse(card.getDeedline(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		    // Prepare message body
		    String messageBody = "";
		    if (today.isBefore(deadlineDate) && !card.getStatus().equals("Done")) {
		        messageBody = "Reminder: Deadline for card '" + card.getTitle() + "' is approaching. Please complete it soon.";
		    } else if (today.isAfter(deadlineDate) && !card.getStatus().equals("Done")) {
		        messageBody = "Reminder: Deadline for card '" + card.getTitle() + "' has passed. Please review its status.";
		    }

		    // Prepare message header attribute for receiver
		    int receiver =0;
		    if (today.isBefore(deadlineDate) && !card.getStatus().equals("Done")) {
		        receiver = card.getAssignedTo();
		    } else if (today.isAfter(deadlineDate) && !card.getStatus().equals("Done")) {
		        receiver = card.getreporterId();
		    }
		    
		    String receiver_str=String.valueOf(receiver);
	         
		    // Send message with header attribute and body
		    jmsContext.createProducer().setProperty("receiver", receiver_str).send(queue, messageBody);
		}
	
	public String receiveMessage() {
		JMSConsumer consumer = jmsContext.createConsumer(queue);
		String message = consumer.receiveBody(String.class);
		System.out.println("Message received: " + message);
		return message;
	}

}
