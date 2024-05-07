package service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;

import model.*;

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
	   // retrieve Cards Id that are in the  to do or in progress  list From Database 
	   public List<Integer> retrieveCardIdsFromDatabase() {
		   
		    String queryString = "SELECT c FROM Card c"; // Select the whole Card object
		   
		    Query query = entityManager.createQuery(queryString);
		    	    
		    @SuppressWarnings("unchecked")
		   // Retrieve all Card objects
		    List<Card> cards = query.getResultList(); 
		    
		    // Create a list to store listIds of retrieved cards
		    List<Integer> listIds = new ArrayList<>();
		    for (Card card : cards) {
		    	
		    	// check the listID if it is = 1 ( to-do ) or = 2 ( in progress ) and add them the the list 
		    	// if it is = 3 ( done ), no need to add this card to the new sprint 
		    	if ( adjustCardId(card.getListId()) == 1 || adjustCardId(card.getListId()) == 2     ) {
		    	  
		    		// Add listId of each eligible card to the list
		    		listIds.add(card.getCardId()); 
			    }
		    }
		    	
		    return listIds;
		}
	    
	   // get cards that are To-Do or inProgress only
	   private int adjustCardId(int cardId) {
		   
		   // the while loop because each board has 3 lists with different ID 
		   // so board 1 has 3 lists with Id 1,2,3 ( to-do , inProgress , done )
		   // board 2 has 3 lists with Id 4,5,6  ( to-do , inProgress , done )
		   // and we must include all boards to the new sprint
		   while (cardId > 3) {
			   cardId -= 3 ;
		   }
		   return cardId;
		   
		}

	    
	   
	   // **********************
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

	            List<Card> cards = new ArrayList<>();
	            Map<Integer, Integer> listIdCounts = new HashMap<>();

	            
	            
	            for (Integer cardId : cardIds) {
	                Card card = entityManager.find(Card.class, cardId); // Retrieve Card by cardId
	                if (card != null) {
	                 
	                	 int listId =adjustCardId( card.getListId() ) ;
	                     listIdCounts.put(listId, listIdCounts.getOrDefault(listId, 0) + 1); // Increment count for the listId
	                 
	                	
	                	cards.add(card); // Add retrieved Card to the list
	                }
	            }

	         // Calculate sum of card counts for each listId
	            int totalCount = 0;
	            StringBuilder responseMessage = new StringBuilder();
	            responseMessage.append("Card counts in Sprint ").append(sprintId).append(":\n");
	            for (Map.Entry<Integer, Integer> entry : listIdCounts.entrySet()) {
	                int listId = entry.getKey();
	                int count = entry.getValue();
	                responseMessage.append("List ID ").append(listId).append(": ").append(count).append(" card(s)\n");
	                totalCount += count;
	            }
	            responseMessage.append("Total count: ").append(totalCount);

	            // Return response with card counts per listId and total count
	            return Response.ok(responseMessage.toString()).build();
	        } catch (NoResultException e) {
	            return Response.status(404).entity("Sprint with ID " + sprintId + " not found").build();
	        } catch (Exception e) {
	            return Response.status(500).entity("Internal Server Error").build();
	        }
	    } 
	            
	            
	            
	           

	   
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
}
