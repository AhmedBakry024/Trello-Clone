package messaging;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

@Startup
@Singleton
public class JMSClient {
	
	@Resource(mappedName = "java:/jms/queue/DLQ")
	private Queue queue;
	
	@Inject
	private JMSContext context;
	
	public void sendMessage(String message) {
		JMSProducer producer = context.createProducer();
		producer.send((Destination) queue, message);
		System.out.println("Message sent: " + message);
	}
	
	public String receiveMessage() {
		JMSConsumer consumer = context.createConsumer(queue);
		String message = consumer.receiveBody(String.class);
		System.out.println("Message received: " + message);
		return message;
	}

}
