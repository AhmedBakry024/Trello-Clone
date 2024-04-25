package service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import model.Card;
import model.User;

@Stateless
@Path("/card")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CardServices {
	@PersistenceContext(unitName = "database")
	private EntityManager em;
	
	
	// create card
	@POST
	@Path("/create") 
	public Response createCard(Card card) {
		em.persist(card);
		return Response.status(Response.Status.CREATED).entity(card).build();
	}
	
	//add description to card by CardId
	
	@POST
	@Path("/{cardId}/description")
	public Response addDescription(@PathParam("cardId")int cardId,String description) {
		Card card = em.find(Card.class, cardId);
		if(card == null)
			return Response.status(Response.Status.NOT_FOUND).build();
		else {
			card.setDescription(description);
			em.merge(card);
			return Response.status(Response.Status.OK).entity("Description added successfully. ").build();
		}
	}
	
	// add comment to card by CardId
	@POST
    @Path("/{cardId}/comment")
	public Response addComment(@PathParam("cardId")int cardId,String comment){
		Card card = em.find(Card.class, cardId);
		if(card == null)
			return Response.status(Response.Status.NOT_FOUND).build();
		else {
			card.addComment(comment);
			em.merge(card);
			return Response.status(Response.Status.OK).entity("Comment added successfully. ").build();
		}
	}
	
	// assigned to anyone by the CardID
	@POST
    @Path("/{cardId}/assignedTo/{assignedTo}")
	public Response assignedTo(@PathParam("cardId")int cardId,@PathParam("assignedTo") int Id){
		Card card = em.find(Card.class, cardId);
		if(card == null)
			return Response.status(Response.Status.NOT_FOUND).build();
		else {
			User user = em.find(User.class, Id);
			if(user == null)
				return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
			card.setAssignedTo(Id);
			em.merge(card);
			return Response.status(Response.Status.OK).build();
		}
	}	
	
	@GET
    @Path("/getallcomment/{cardId}")
    public Response getAllCardsInList(@PathParam("cardId") int cardId) {
        Card list = em.find(Card.class, cardId);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
        	List<String> comments = list.getComments();
            return Response.ok(comments).build();
        }
    }
	
	@GET
	@Path("/getcard/{cardId}")
	public Response getCard(@PathParam("cardId")int cardId) {
		Card card = em.find(Card.class, cardId);
        if (card == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(card).build();
        }
	}
	
}