package controller;

import javax.ejb.Stateless;
import javax.inject.Inject;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.*;
import service.*;

@Stateless
@Path("/list")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ListController {
	@Inject
	ListServices listService;
	
	//create list
	@POST
	@Path("/create")
	public Response createList(ListOfCards list,@QueryParam("userId")int userId){
		return listService.createList(list, userId);
	}
	
	//delete a list by ListId	
	@DELETE
	@Path("/deletelist")
	public Response deleteList(@QueryParam("listId") int listId,@QueryParam("userId")int userId) {
		return listService.deletelist(listId, userId);
	}
	
	//Not Working
	@GET
    @Path("/getallcardslist")
	 public Response getAllCardsInList(@QueryParam("listId") int listId) {
		return listService.getAllCardsInList(listId);
	}	
	
	// get all lists created 
	@GET
    @Path("/getAll")
    public Response getAllLists(){
		return listService.getAllLists();
	}
	
}
