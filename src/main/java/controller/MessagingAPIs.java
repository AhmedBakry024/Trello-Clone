package controller;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import messaging.JMSClient;
import model.Card;

@Stateless
@Path("/messaging")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessagingAPIs {
	
	@Inject
    private JMSClient jmsClient;
	
	
	@PersistenceContext(unitName = "database")
    private EntityManager em;
	
	
	@GET
	@Path("/gett")
	public Response getExample() {
	    int id = 123;
	    return Response.ok(id).build();
	}
	
	
	

	@GET
	@Path("/check-Deadline-And-Send-Messages/{id}")
	public Response checkDeedline(@PathParam("id") int id) {
	    String jpql = "SELECT c FROM Card c WHERE c.cardId = :id";
	    TypedQuery<Card> query = em.createQuery(jpql, Card.class);
	    query.setParameter("id", id); 
	    Card card = null;
	    try {
	        card = query.getSingleResult(); 
	    } catch (NoResultException e) {
	        // Entity not found for the given ID
	        return Response.status(Response.Status.NOT_FOUND)
	                       .entity("No card found with ID: " + id)
	                       .build();
	    }
	    
	    jmsClient.checkDeadlineAndSendMessages(card);
	    
	    return Response.ok().entity("Card Deadline Checked Successfully").build();
	}

	

    @GET
    @Path("/notification/{userId}")
    public Response searchByDescription(@PathParam("userId") String userId) {
        StringBuilder userMessages = new StringBuilder();

        // Receive messages from the queue and filter messages related to the specified user
        String message;
        while ((message = jmsClient.receiveMessage()) != null) {
            if (message.contains(userId)) {
                userMessages.append(message).append("\n");
            }
        }

        // Check if any messages related to the user were found
        if (userMessages.length() > 0) {
            return Response.ok().entity(userMessages.toString()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No messages found for user " + userId).build();
        }
    }
	
	
	
}
