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
	public Response CreateCard(Card card,@QueryParam("boardId")int boardId) {
		return cardService.createCard(card,boardId);
	}
	
	
	@POST	
	@Path("/addcardtolist")
	public Response addCardToList(@QueryParam("cardId") int cardId,@QueryParam("listId") int listId) {
		return cardService.addCardToList(cardId, listId);
	}
	
	//add description to card by CardId
	@POST
	@Path("/description")
	public Response addDescription(@QueryParam("cardId")int cardId,@QueryParam("userId")int userId,@QueryParam("Description")String description) {
		return cardService.addDescription(cardId, userId, description);
	}
	
	// add comment to card by CardId
	@POST
    @Path("/comment")
	public Response addComment(@QueryParam("cardId")int cardId,@QueryParam("userId")int userId,@QueryParam("Comment")String comment) {
		return cardService.addComment(cardId, userId,comment);
	}
	
	// assigned to anyone by the CardID
	@POST
    @Path("/assignedTo")
	public Response assignedTo(@QueryParam("cardId")int cardId,@QueryParam("assignedTo")int assignedTo) {
		return cardService.assignedTo(cardId, assignedTo);
	}

	@GET
    @Path("/getallcomment")
	public Response getAllComments(@QueryParam("cardId")int cardId,@QueryParam("userId")int userId) {
		return cardService.getAllComments(cardId,userId);
	}
	
	@GET
	@Path("/getcard")
	public Response getCard(@QueryParam("cardId")int cardId,@QueryParam("userId")int userId) {
		return cardService.getCard(cardId,userId); 
	}
	
}