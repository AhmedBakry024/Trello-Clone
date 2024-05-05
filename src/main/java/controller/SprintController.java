package controller;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import service.SprintService;

@Stateless
@Path("/sprint")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SprintController {

	@EJB
    private SprintService sprintService;

	// not working 
    @GET
    @Path("/getall")
    public Response getListsBySprintId() {
        return sprintService.getAllSprintCardIds();
    }

    // working 
    @POST
    @Path("/create")
    public Response createSprint() {
        return sprintService.createSprint();
    }
	
    // working 
	 @GET
	 @Path("/test")
	  public String test() {
	    return "test sprint";
	 }

	    // working 
	    @GET
	    @Path("/getSprint")
	    public Response getSprintById(@QueryParam("id") int sprintId) {
		 return sprintService.getSprintById(sprintId);
 
	 }
	    
	    // working
	    @GET
	    @Path("/cardCount")
	    public Response getCardCountInSprintLists(@QueryParam("id") int sprintId) {
	    	 return sprintService.getCardCountInSprintLists(sprintId);
	    }
	    
}
