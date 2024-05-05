package service;

import java.io.ObjectInputFilter.Status;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import model.Card;
import model.ListOfCards;
import model.sprint;

@Stateless

public class SprintService {

	   @PersistenceContext(unitName = "database")
	    private EntityManager entityManager;

	   
	   public Response getAllSprintCardIds() {
	        try {
	            List<List<Integer>> sprintCardIdsLists = new ArrayList<>();

	            // Retrieve all sprints
	            List<sprint> sprints = entityManager.createQuery("SELECT s FROM sprint s", sprint.class).getResultList();

	            // Collect cardID lists from each sprint
	            for (sprint sprint : sprints) {
	                List<Integer> sprintCardIds = sprint.getCardId();
	                sprintCardIdsLists.add(sprintCardIds);
	            }

	            return Response.status(200).entity(sprintCardIdsLists).build();
	        } catch (Exception e) {
	            return Response.status(500)
	                           .entity("Error retrieving sprint card IDs: " + e.getMessage())
	                           .build();
	        }
	    }

	   // **********************
	   // need timer 
	   // Start a new Sprint 
	   public Response createSprint() {
	        try {
	            sprint sprint = new sprint();
	            entityManager.persist(sprint);
	            
	            
	            // Retrieve card IDs from the database
	            List<Integer> cardIds = retrieveCardIdsFromDatabase();

	            // Add retrieved card IDs to the sprint's cardsID list
	            for (Integer cardId : cardIds) {
	                sprint.addToCardid(cardId);
	            }

	            return Response.status(200).entity("Cards added to sprint successfully").build();
	        } catch (Exception e) {
	            return Response.status(503)
	                           .entity("Error adding cards to sprint: " + e.getMessage())
	                           .build();
	        }
	    }
	   
	   // **********************
	   // to be continued 
	   // it must get all cards that are not in the done ( has listID 3 )
	   // till now Get all cards ID in the database 
	   // Get all cards from the database 
	    private List<Integer> retrieveCardIdsFromDatabase() {
	        String queryString = "SELECT c.cardId FROM Card c";
	        Query query = entityManager.createQuery(queryString);

	        @SuppressWarnings("unchecked")
	        List<Integer> cardIds = query.getResultList();

	        return cardIds;
	    }
	    
	    
	    
	    
	    // Get Sprint by ID 
	    public Response getSprintById(int sprintId) {
	        try {
	            sprint sprint = entityManager.createQuery("SELECT s FROM sprint s LEFT JOIN FETCH s.cardsID WHERE s.id = :sprintId", sprint.class)
	                                        .setParameter("sprintId", sprintId)
	                                        .getSingleResult();

	            if (sprint != null) {
	                return Response.status(200).entity(sprint).build();
	            } else {
	                return Response.status(404).entity("Sprint with ID " + sprintId + " not found").build();
	            }
	        } catch (NoResultException e) {
	            return Response.status(404).entity("Sprint with ID " + sprintId + " not found").build();
	        } catch (Exception e) {
	            return Response.status(500).entity("Error retrieving sprint: " + e.getMessage()).build();
	        }
	    }

	    
	    
	    
	    // get the statistics of each sprint 
	    // AKA generating a report based on a sprint ID 
	    public Response getCardCountInSprintLists(int sprintId) {
	    	try {
	            // Find the sprint by sprintId
	    		sprint sprint = entityManager.createQuery("SELECT s FROM sprint s LEFT JOIN FETCH s.cardsID WHERE s.id = :sprintId", sprint.class)
                        .setParameter("sprintId", sprintId)
                        .getSingleResult();

	            if (sprint == null) {
	                return Response.status(404).entity("Sprint with ID " + sprintId + " not found").build();
	            }

	            // Retrieve the list of card IDs associated with this sprint
	            List<Integer> cardIds = sprint.getCardId();

	            // Count the cards in each list
	            Map<String, Integer> cardCountInLists = new HashMap<>();
	            for (Integer cardId : cardIds) {
	                // Find the card by cardId
	                Card card = entityManager.find(Card.class, cardId);

	                if (card != null) {
	                    // Retrieve the listId associated with the card from the database
	                    int listId = card.getListId();

	                    // Determine the list name based on listId
	                    String listName = determineListName(listId);

	                    // Update the count for the list
	                    if (cardCountInLists.containsKey(listName)) {
	                        cardCountInLists.put(listName, cardCountInLists.get(listName) + 1);
	                    } else {
	                        cardCountInLists.put(listName, 1);
	                    }
	                }
	            }

	            return Response.status(200).entity(cardCountInLists).build();
	        } catch (Exception e) {
	            return Response.status(500).entity("Error retrieving card counts for sprint: " + e.getMessage()).build();
	        }
	    }
	    
	    // Get the list name 
	    private String determineListName(int listId) {
	        switch (listId) {
	            case 1:
	                return "To-Do";
	            case 2:
	                return "Progress";
	            case 3:
	                return "Done";
	            default:
	                return "Not assigned";
	        }
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
}
