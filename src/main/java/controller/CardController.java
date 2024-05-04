package controller;

import javax.ejb.Stateless;
import javax.inject.Inject;

import javax.ws.rs.Consumes;
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
@Path("/card")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CardController {
	
	@Inject
	private CardServices cardService;
	
	// create card
	@POST
	@Path("/create")
	public Response CreateCard(Card card) {
		return cardService.createCard(card);
	}
	
	
	@POST	
	@Path("/addcardtolist")
	public Response addCardToList(@QueryParam("cardId") int cardId,@QueryParam("listId") int listId) {
		return cardService.addCardToList(cardId, listId);
	}
	
	//add description to card by CardId
	@POST
	@Path("/description")
	public Response addDescription(@QueryParam("cardId")int cardId,String description) {
		return cardService.addDescription(cardId, description);
	}
	
	// add comment to card by CardId
	@POST
    @Path("/comment")
	public Response addComment(@QueryParam("cardId")int cardId,String comment) {
		return cardService.addComment(cardId, comment);
	}
	
	// assigned to anyone by the CardID
	@POST
    @Path("/assignedTo")
	public Response assignedTo(@QueryParam("cardId")int cardId,@QueryParam("assignedTo")int userId) {
		return cardService.assignedTo(cardId, userId);
	}

	@GET
    @Path("/getallcomment")
	public Response getAllComments(@QueryParam("cardId")int cardId) {
		return cardService.getAllComments(cardId);
	}
	
	@GET
	@Path("/getcard")
	public Response getCard(@QueryParam("cardId")int cardId) {
		return cardService.getCard(cardId);
	}
	
}
