package service;

import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import javax.ws.rs.core.Response;

import model.Board;
import model.Card;
import model.ListOfCards;
import model.User;

@Stateless
public class CardServices {
	
	@PersistenceContext(unitName = "database")
	private EntityManager em;
	
	
	// create card
	public Response createCard(Card card,int boardId) {
		User user = em.find(User.class, card.getAssignedTo());
		Board board = em.find(Board.class, boardId);

		if(user == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("User not Found").build();
		}
		if(board == null)
			return Response.status(Response.Status.NOT_FOUND).entity("Board not Found").build();
		for(ListOfCards list : board.getListOfCards()) {
			if(list.getListName() == "Todo")
				card.setList(list);
		}
		em.persist(card);
		return Response.status(Response.Status.CREATED).entity(card).build();
	}
	
	
	public Response addCardToList(int cardId,int listId) {
		
		Card card = em.find(Card.class, cardId);
		if(card == null)
			return Response.status(Response.Status.NOT_FOUND).build();
		else {
			ListOfCards list = em.find(ListOfCards.class, listId);
			if(list == null)
				return Response.status(Response.Status.NOT_FOUND).build();
			list.addCard(card);
			return Response.status(Response.Status.OK).entity(card).build();
		}
	}
	

	public Response addDescription(int cardId,String description) {
		Card card = em.find(Card.class, cardId);
		if(card == null)
			return Response.status(Response.Status.NOT_FOUND).build();
		else {
			card.setDescription(description);
			em.merge(card);
			return Response.status(Response.Status.OK).entity("Description added successfully. ").build();
		}
	}
	

	public Response addComment(int cardId,String comment){
		Card card = em.find(Card.class, cardId);
		if(card == null)
			return Response.status(Response.Status.NOT_FOUND).build();
		else {
			card.addComment(comment);
			em.merge(card);
			return Response.status(Response.Status.OK).entity("Comment added successfully. ").build();
		}
	}
	

	public Response assignedTo(int cardId, int Id){
		Card card = em.find(Card.class, cardId);
		if(card == null)
			return Response.status(Response.Status.NOT_FOUND).build();
		else {
			if(card.getList() == null || card.getList().getBoard() == null)
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("card are not in list or the list are not in board please check again").build();
			if(!card.getList().getBoard().getInvitedID().contains(Id))
				return Response.status(Response.Status.NOT_FOUND).entity("User are not in the board").build();
			User user = em.find(User.class, Id);
			if(user == null)
				return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
			card.setAssignedTo(Id);
			em.merge(card);
			return Response.status(Response.Status.OK).build();
		}
	}	
	
    public Response getAllComments(int cardId) {
        Card list = em.find(Card.class, cardId);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
        	List<String> comments = list.getComments();
            return Response.ok(comments).build();
        }
    }
	

	public Response getCard(int cardId) {
		Card card = em.find(Card.class, cardId);
        if (card == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(card).build();
        }
	}
	
}