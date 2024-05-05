package service;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response.Status;

import model.Card;


@Stateless
public class searchService {
	@PersistenceContext(unitName = "database")
	private static EntityManager em;
	
	//----------------------------------------------------------
	// Search for cards with Card Title matches with Keywords
	//----------------------------------------------------------
	public static List<Card> searchCardsByTitle(String keywords) {
		// Split the input keywords string into individual words
        List<String> keywordList = Arrays.asList(keywords.split("\\s+"));
        
        // Construct JPQL query to search for cards with title containing any of the keywords
        String jpql = "SELECT c FROM Card c WHERE ";
        jpql += keywordList.stream()
                .map(keyword -> "c.title LIKE '%" + keyword + "%'")
                .collect(Collectors.joining(" OR "));
        
        // Create TypedQuery with the JPQL query
        TypedQuery<Card> query = em.createQuery(jpql, Card.class);

        // Execute the query and return the result
        return query.getResultList();
	}
	
	//--------------------------------------------------------------
	// Search for cards with Card Description matches with Keywords
	//--------------------------------------------------------------
	public static List<Card> searchCardsByDescription(String keywords) {
        // Split the input keywords string into individual words
        List<String> keywordList = Arrays.asList(keywords.split("\\s+"));
        
        // Construct JPQL query to search for cards with description containing any of the keywords
        String jpql = "SELECT c FROM Card c WHERE ";
        jpql += keywordList.stream()
                .map(keyword -> "c.description LIKE '%" + keyword + "%'")
                .collect(Collectors.joining(" OR "));
        
        // Create TypedQuery with the JPQL query
        TypedQuery<Card> query = em.createQuery(jpql, Card.class);

        // Execute the query and return the result
        return query.getResultList();
    }
	
	
	//----------------------------------------------------------
	// Search for cards with Card status matches with Keywords
	// expect one keyword  ( ToDo - Doing - Test - Done )
	//----------------------------------------------------------
	public static List<Card> searchCardsByStatus(String keywords) {
        // Construct JPQL query to search for cards with status matching the specified keyword
        String jpql = "SELECT c FROM Card c WHERE c.status = :status";
        
        // Create TypedQuery with the JPQL query
        TypedQuery<Card> query = em.createQuery(jpql, Card.class);
        
        // Set the parameter
        // Assuming keywords contain a single status value
        query.setParameter("status", Status.valueOf(keywords.toUpperCase()));

        // Execute the query and return the result
        return query.getResultList();
    }
	
	
	//------------------------------------------------------------------
	// Search for cards with Card assignee Id matches with assignedToId
	// Note : Id is int 
	//------------------------------------------------------------------
	public static List<Card> searchCardsByAssignedTo(int assignedToId) {
        // Construct JPQL query to search for cards with assignedTo matching the specified ID
        String jpql = "SELECT c FROM Card c WHERE c.assignedTo = :assignedToId";
        
        // Create TypedQuery with the JPQL query
        TypedQuery<Card> query = em.createQuery(jpql, Card.class);
        
        // Set the parameter
        query.setParameter("assignedToId", assignedToId);

        // Execute the query and return the result
        return query.getResultList();
    }
	
	//------------------------------------------------------------------
	// Search for cards with Card Reporter Id matches with ReporterId
	// Note : Id is int 
	//------------------------------------------------------------------
	public static List<Card> searchCardsByReporterId(int reporterId) {
        // Construct JPQL query to search for cards with reporterId matching the specified ID
        String jpql = "SELECT c FROM Card c WHERE c.reporterId = :reporterId";
        
        // Create TypedQuery with the JPQL query
        TypedQuery<Card> query = em.createQuery(jpql, Card.class);
        
        // Set the parameter
        query.setParameter("reporterId", reporterId);

        // Execute the query and return the result
        return query.getResultList();
    }
	
	
	
	public static List<Card> searchCardsByCreationDate(Date creationDate) {
        // Construct JPQL query to search for cards with creationDate matching the specified date
        String jpql = "SELECT c FROM Card c WHERE c.creationDate = :creationDate";

        // Create TypedQuery with the JPQL query
        TypedQuery<Card> query = em.createQuery(jpql, Card.class);

        // Set the parameter
        query.setParameter("creationDate", creationDate, TemporalType.TIMESTAMP);

        // Execute the query and return the result
        return query.getResultList();
    }
	


}
