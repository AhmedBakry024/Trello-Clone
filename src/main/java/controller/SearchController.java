package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Card;

import service.*;

@Stateless
@Path("/search")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SearchController{
	
	@Inject
	private SearchService searchService;

	@GET
    @Path("/get")
    public Response getExample() {
        int id = 123;
        return Response.ok(id).build();
    }
    
	
    //---------------------------------------------------------------
	// Search by a card title in The DB
	//---------------------------------------------------------------
	@GET
	@Path("/title/{keywords}")
    public Response searchByTitle(@PathParam("keywords") String keywords) {
		
		// Process search criteria and perform database query
        List<Card> filteredCards = searchService.searchCardsByTitle(keywords);
        return Response.ok().entity(filteredCards).build();
    }
	
	//---------------------------------------------------------------
	// Search by a card description in The DB
	//---------------------------------------------------------------
	@GET
	@Path("/description/{keywords}")
    public Response searchByDescription(@PathParam("keywords") String keywords) {
		
        List<Card> filteredCards = searchService.searchCardsByDescription(keywords);
        return Response.ok().entity(filteredCards).build();
    }
	
	
	//---------------------------------------------------------------
	// Search by a card status in The DB
	//---------------------------------------------------------------
	@GET
	@Path("/status/{keyword}")
    public Response searchByStatus(@PathParam("keyword") String keyword) {
		
        List<Card> filteredCards = searchService.searchCardsByStatus(keyword);
        return Response.ok().entity(filteredCards).build();
    }
	
	
	//---------------------------------------------------------------
	// Search by a card assignee in The DB
	//---------------------------------------------------------------
	@GET
	@Path("/assignee/{assignedToId}")
    public Response searchByAssignee(@PathParam("assignedToId") int assignedToId) {
		
        List<Card> filteredCards = searchService.searchCardsByAssignedTo(assignedToId);
        return Response.ok().entity(filteredCards).build();
    }
	
	//---------------------------------------------------------------
	// Search by a card reporter in The DB
	//---------------------------------------------------------------
	@GET
	@Path("/reporter/{reporterId}")
    public Response searchByReporter(@PathParam("reporterId") int reporterId) {
		
        List<Card> filteredCards = searchService.searchCardsByReporterId(reporterId);
        return Response.ok().entity(filteredCards).build();
    }
	
	//---------------------------------------------------------------
	// Search by a card creation date in The DB
	//---------------------------------------------------------------
	@GET
	@Path("/creationDate/{creationDate}")
	public Response searchByCreationDate(@PathParam("creationDate") String creationDateStr) {
	    try {
	        // Parse the date string into a Date object with the format yyyy-MM-dd
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date creationDate = dateFormat.parse(creationDateStr);

	        // Search cards by the parsed creation date
	        List<Card> filteredCards = searchService.searchCardsByCreationDate(creationDateStr);
	        return Response.ok(filteredCards).build();
	    } catch (ParseException e) {
	        // Handle parsing exception
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity("Invalid date format. Please use yyyy-MM-dd format.")
	                .build();
	    }
	}
	
	
	
	//---------------------------------------------------------------
	// Search by a specific card Deedline  in The DB
	//---------------------------------------------------------------
	@GET
	@Path("/deedline/{deedline}")
	public Response searchByDeedline(@PathParam("deedline") String deedlineStr) {
	    try {
	        // Parse the date string into a Date object with the format yyyy-MM-dd
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date deedline = dateFormat.parse(deedlineStr);

	        // Search cards by the parsed deedline
	        List<Card> filteredCards = searchService.searchCardsBySpecificDeedline(deedlineStr);
	        return Response.ok(filteredCards).build();
	    } catch (ParseException e) {
	        // Handle parsing exception
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity("Invalid date format. Please use yyyy-MM-dd format.")
	                .build();
	    }
	}
	
	
	//---------------------------------------------------------------
	// Search by cards Before Deedline  in The DB
	//---------------------------------------------------------------
	@GET
	@Path("/before-Deedline/{deedline}")
	public Response searchBeforeDeedline(@PathParam("deedline") String deedlineStr) {
	    try {
	        // Parse the date string into a Date object with the format yyyy-MM-dd
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date deedline = dateFormat.parse(deedlineStr);

	        // Search cards by the parsed deedline
	        List<Card> filteredCards = searchService.searchCardsBeforeDeedline(deedlineStr);
	        return Response.ok(filteredCards).build();
	    } catch (ParseException e) {
	        // Handle parsing exception
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity("Invalid date format. Please use yyyy-MM-dd format.")
	                .build();
	    }
	}
	
	
	//---------------------------------------------------------------
		// Search by cards After Deedline  in The DB
		//---------------------------------------------------------------
		@GET
		@Path("/after-Deedline/{deedline}")
		public Response searchAfterDeedline(@PathParam("deedline") String deedlineStr) {
		    try {
		        // Parse the date string into a Date object with the format yyyy-MM-dd
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		        Date deedline = dateFormat.parse(deedlineStr);

		        // Search cards by the parsed deedline
		        List<Card> filteredCards = searchService.searchCardsAfterDeedline(deedlineStr);
		        return Response.ok(filteredCards).build();
		    } catch (ParseException e) {
		        // Handle parsing exception
		        return Response.status(Response.Status.BAD_REQUEST)
		                .entity("Invalid date format. Please use yyyy-MM-dd format.")
		                .build();
		    }
		}
	
	
}