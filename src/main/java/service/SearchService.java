package service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.Card;


@Stateless
public class SearchService {
	@PersistenceContext(unitName = "database")
	private EntityManager em;
	
	//----------------------------------------------------------
	// Search for cards with Card Title matches with Keywords
	//----------------------------------------------------------
	public List<Card> searchCardsByTitle(String keywords) {
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
	public List<Card> searchCardsByDescription(String keywords) {
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
	public List<Card> searchCardsByStatus(String keyword) {
	    // Construct JPQL query to search for cards with status matching the specified keyword
	    String jpql = "SELECT c FROM Card c WHERE c.cardStatus = :keyword";

	    // Create TypedQuery with the JPQL query
	    TypedQuery<Card> query = em.createQuery(jpql, Card.class);

	    // Set the parameter
	    query.setParameter("keyword", keyword);

	    // Execute the query and return the result
	    return query.getResultList();
	}
	
	
	//------------------------------------------------------------------
	// Search for cards with Card assignee Id matches with assignedToId
	// Note : Id is int 
	//------------------------------------------------------------------
	public List<Card> searchCardsByAssignedTo(int assignedToId) {
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
	public List<Card> searchCardsByReporterId(int reporterId) {
        // Construct JPQL query to search for cards with reporterId matching the specified ID
        String jpql = "SELECT c FROM Card c WHERE c.reporterId = :reporterId";
        
        // Create TypedQuery with the JPQL query
        TypedQuery<Card> query = em.createQuery(jpql, Card.class);
        
        // Set the parameter
        query.setParameter("reporterId", reporterId);

        // Execute the query and return the result
        return query.getResultList();
    }
	
	
	
	public List<Card> searchCardsByCreationDate(String creationDateStr) {
	    try {
	        // Parse the date string into a LocalDate object
	        LocalDate creationDate = LocalDate.parse(creationDateStr);

	        // Construct the JPQL query to search for cards with the given creation date
	        String jpql = "SELECT c FROM Card c WHERE c.creationDate = :creationDate";

	        // Create TypedQuery with the JPQL query
	        TypedQuery<Card> query = em.createQuery(jpql, Card.class);

	        // Set the parameter
	        query.setParameter("creationDate", creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

	        // Execute the query and return the result
	        return query.getResultList();
	    } catch (DateTimeParseException e) {
	        // Handle parsing exception
	        return null;
	    }
	}
	
	
	
	
	public List<Card> searchCardsBySpecificDeedline(String deedlineStr) {
	    try {
	        // Parse the date string into a LocalDate object
	        LocalDate deedline = LocalDate.parse(deedlineStr);

	        // Construct the JPQL query to search for cards with the given deedline
	        String jpql = "SELECT c FROM Card c WHERE c.deedline = :deedline";

	        // Create TypedQuery with the JPQL query
	        TypedQuery<Card> query = em.createQuery(jpql, Card.class);

	        // Set the parameter
	        query.setParameter("deedline", deedline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

	        // Execute the query and return the result
	        return query.getResultList();
	    } catch (DateTimeParseException e) {
	        // Handle parsing exception
	        return null;
	    }
	}
	
	
	public List<Card> searchCardsBeforeDeedline(String deedlineStr) {
	    try {
	        // Parse the date string into a LocalDate object
	        LocalDate deedline = LocalDate.parse(deedlineStr);

	        // Construct the JPQL query to search for cards with the given deedline
	        String jpql = "SELECT c FROM Card c WHERE c.deedline <= :deedline";

	        // Create TypedQuery with the JPQL query
	        TypedQuery<Card> query = em.createQuery(jpql, Card.class);

	        // Set the parameter
	        query.setParameter("deedline", deedline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

	        // Execute the query and return the result
	        return query.getResultList();
	    } catch (DateTimeParseException e) {
	        // Handle parsing exception
	        return null;
	    }
	}
	
	public List<Card> searchCardsAfterDeedline(String deedlineStr) {
	    try {
	        // Parse the date string into a LocalDate object
	        LocalDate deedline = LocalDate.parse(deedlineStr);

	        // Construct the JPQL query to search for cards with the given deedline
	        String jpql = "SELECT c FROM Card c WHERE c.deedline >= :deedline";

	        // Create TypedQuery with the JPQL query
	        TypedQuery<Card> query = em.createQuery(jpql, Card.class);

	        // Set the parameter
	        query.setParameter("deedline", deedline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

	        // Execute the query and return the result
	        return query.getResultList();
	    } catch (DateTimeParseException e) {
	        // Handle parsing exception
	        return null;
	    }
	}


}
