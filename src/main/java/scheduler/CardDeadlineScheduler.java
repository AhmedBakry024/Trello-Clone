package scheduler;

import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import messaging.JMSClient;
import model.Card;

@Singleton
public class CardDeadlineScheduler {
     
	@Inject
    private JMSClient jmsClient;
    //@PersistenceContext
    @PersistenceContext(unitName = "database")
    private EntityManager entityManager; // Inject the Entity Manager
    
    @Schedule(hour = "*", minute = "0", second = "0", persistent = false) // Runs every hour at minute 0
    public void checkCardDeadlines() {
        // Retrieve all cards from the database using the Entity Manager
        // Adjust the query according to your entity structure and requirements
        List<Card> cards = entityManager.createQuery("SELECT c FROM Card c", Card.class).getResultList();
        
        // Iterate over each card and check its deadline
        for (Card card : cards) {
        	jmsClient.checkDeadlineAndSendMessages(card);
        }
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
