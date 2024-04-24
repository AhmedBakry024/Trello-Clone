package service;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Card;
import model.ListOfCards;

@Stateless
@Path("/list")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ListServices {
	@PersistenceContext(unitName = "database")
	private EntityManager em;
	
	
	//create list
	
	@POST
	@Path("/create")
	public Response createList(ListOfCards listOfCards) {
		em.persist(listOfCards);
		return Response.status(Response.Status.CREATED).entity(listOfCards).build();
	}
	
	//delete a list by ListId
	
	@DELETE
	@Path("/deletelist/{listId}")
	public Response deletelist(@PathParam("listId") int listId) {
		ListOfCards listOfCards = em.find(ListOfCards.class, listId);
		if(listOfCards == null)
			return Response.status(Response.Status.NOT_FOUND).build();
		else {
			em.remove(listOfCards);
			return  Response.status(Response.Status.OK).build();
		}
	}
	
	@GET
    @Path("/getallcardslist/{listId}")
    public Response getAllCardsInList(@PathParam("listId") int listId) {
        ListOfCards list = em.find(ListOfCards.class, listId);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(list.getCardIds()).build();
        }
    }
	

}
