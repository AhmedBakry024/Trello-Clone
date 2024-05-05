package controller;


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
	@Path("/status/{keywords}")
    public Response searchByStatus(@PathParam("keywords") String keywords) {
		
        List<Card> filteredCards = searchService.searchCardsByStatus(keywords);
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
	public Response searchByCreationDate(@PathParam("creationDate") Date creationDate) {
	    List<Card> filteredCards = searchService.searchCardsByCreationDate(creationDate);
	    return Response.ok(filteredCards).build();
	}
	
}