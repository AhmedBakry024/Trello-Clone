package service;

import java.io.ObjectInputFilter.Status;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

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

	    private List<Integer> retrieveCardIdsFromDatabase() {
	        String queryString = "SELECT c.cardId FROM Card c";
	        Query query = entityManager.createQuery(queryString);

	        @SuppressWarnings("unchecked")
	        List<Integer> cardIds = query.getResultList();

	        return cardIds;
	    }
	    
	    
	    
	    
	    
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

	    
}
