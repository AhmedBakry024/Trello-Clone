package service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Board;
import model.Card;
import model.ListOfCards;
import model.User;

@Stateless
@Path("/list")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ListServices {
	
	@PersistenceContext(unitName = "database")
	private EntityManager em;

	//create list
	@POST
	@Path("/create/{userId}")
	public Response createList(ListOfCards listOfCards,@PathParam("userId") int userId) {
		User user = em.find(User.class, userId);
		Board board = em.find(Board.class, listOfCards.getBoardId());
		if(board == null) 
			return Response.status(Response.Status.NOT_FOUND).entity("User Are Not have a board").build();
		listOfCards.setBoard(board);
		try {
				if(user != null) {					
					if(!user.getIsTeamLeader()) {
						return Response.status(403).entity("User Are Not TeamLeader").build();
					}else if(board.getTeamLeader() != user.getId()) {
						return Response.status(403).entity("User Are Not The Team Leader Of This Board").build();
					}else {
						em.persist(listOfCards);
						return Response.status(Response.Status.CREATED).entity(listOfCards).build();
					}
				}
				return Response.status(Response.Status.NOT_FOUND).entity("User Are Not Found").build();
			}catch (Exception e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error").build();
		}
	}
	
	//delete a list by ListId	
	@DELETE
	@Path("/deletelist/{listId}/{userId}")
	public Response deletelist(@PathParam("listId") int listId,@PathParam("userId")int userId) {
		User user = em.find(User.class, userId);
		
		if(user != null) {
			if(!user.getIsTeamLeader()) {
				return Response.status(403).entity("User Are Not TeamLeader").build();
			}
			ListOfCards listOfCards = em.find(ListOfCards.class, listId);
			if(listOfCards == null)
				return Response.status(Response.Status.NOT_FOUND).build();
			else if(listOfCards.getBoard().getTeamLeader() !=  userId) {
				return Response.status(403).entity("User Are Not The Team Leader Of This Board").build();
			}
			else {
				try {
					for (Card card : listOfCards.getCards()) {
						if(!em.contains(card)) {
							card = em.merge(card);
						}
						listOfCards.removeCard(card);
			            em.remove(card);
			        }
			        em.remove(listOfCards);
		            em.flush();
			        return Response.status(Response.Status.OK).entity("List deleted successfully").build();
				}catch(Exception e) {
		        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting list").build();
		        }
			}
		}
		return Response.status(Response.Status.NOT_FOUND).entity("User Are Not Found").build();
	}
	
	//Not Working
	@GET
    @Path("/getallcardslist/{listId}")
    public Response getAllCardsInList(@PathParam("listId") int listId) {
        ListOfCards list = em.find(ListOfCards.class, listId);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(list.getCards()).build();
        }
    }
	
	
	// get all lists created 
	@GET
    @Path("/getAll")
    public Response getAllLists() {
        try {
            List<ListOfCards> lists = em.createQuery("SELECT l FROM ListOfCards l", ListOfCards.class)
                                        .getResultList();
            return Response.ok(lists).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error fetching lists: " + e.getMessage())
                           .build();
        }
    }
	
		
}