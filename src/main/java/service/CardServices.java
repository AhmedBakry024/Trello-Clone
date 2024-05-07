package service;

import java.util.List;


import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import javax.ws.rs.core.Response;

import messaging.JMSClient;
import model.Board;
import model.Card;
import model.ListOfCards;
import model.User;

@Stateless
public class CardServices {

    @PersistenceContext(unitName = "database")
    private EntityManager em;
    
    @Inject 
    private JMSClient jmsClient;


    // create card
    public Response createCard(Card card, int boardId) {
        User user, reporter;
        Board board;
        try {
            user = em.find(User.class, card.getreporterId());
            if(user == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("User are not found").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("reporter_id not correct").build();
        }
        try {
            reporter = em.find(User.class, card.getAssignedTo());
            if(reporter == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("User are not found").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("assignedTo_id not correct").build();
        }
        try {
            board = em.find(Board.class, boardId);
            if(board == null)
            	return Response.status(Response.Status.NOT_FOUND).entity("Board not Found").build();
            if(!board.getInvitedID().contains(reporter.getId()))
            	return Response.status(Response.Status.NOT_FOUND).entity("reporter are not in the board").build();
            if(!board.getInvitedID().contains(user.getId()))
            	return Response.status(Response.Status.NOT_FOUND).entity("assign are not in the board").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Board not Found").build();
        }
        for (ListOfCards list : board.getListOfCards()) {
            if (list.getListName() == "Todo") card.setList(list);
        }
        em.persist(card);
        return Response.status(Response.Status.CREATED).entity(card).build();

    }

    public Response addCardToList(int cardId, int listId,int userId) {
        Card card;
        User user;
        try{
        	user = em.find(User.class, userId);
        	if(user == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("User are not found").build();
        }catch(Exception e) {
        	return Response.status(Response.Status.NOT_FOUND).entity("User are not found").build();
        }
        try {
            card = em.find(Card.class, cardId);
            if(card == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("card are not found").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card are not found").build();
        }
        try {
            ListOfCards list = em.find(ListOfCards.class, listId);
            if(list == null)
            	return Response.status(Response.Status.NOT_FOUND).entity("list are not found").build();
            if(!check(card,user)) {
            	return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("you have not allowed move this card").build();
            }
            list.addCard(card);
            jmsClient.sendMessage("Card with ID " + cardId + " moved to list with ID " + listId + "\n\n\n\n\n\n\n");
            return Response.status(Response.Status.OK).entity(card).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("list are not found").build();
        }
    }

    public Response addDescription(int cardId, int userId ,String description) {
        Card card;
        User user;
        try {
        	user = em.find(User.class,userId);
        	if(user == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("User are not found").build();
        }catch(Exception e) {
        	return Response.status(Response.Status.NOT_FOUND).entity("user are not found").build();
        }
        try {
            card = em.find(Card.class, cardId);
            if(card == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("card are not found").build();
            if(!check(card,user))
            	return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("you have not allowed to add description on this card").build();
            card.setDescription(description);
            em.merge(card);
            jmsClient.sendMessage("Description added to card with ID " + cardId);
            return Response.status(Response.Status.OK).entity("Description added successfully. ").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("card are not found").build();
        }
    }

    public Response addComment(int cardId, int userId ,String comment) {
        Card card;
        User user;
        try {
        	user = em.find(User.class,userId);
        	if(user == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("User are not found").build();
        }catch(Exception e) {
        	return Response.status(Response.Status.NOT_FOUND).entity("user are not found").build();
        }
        try {
            card = em.find(Card.class, cardId);
            if(card == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("card are not found").build();
            if(!check(card,user))
            	return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("you have not allowed to add comment on this card").build();
            card.addComment(comment);
            em.merge(card);
            jmsClient.sendMessage("User with ID " + userId + " added comment to card with ID " + cardId);
            return Response.status(Response.Status.OK).entity("Comment added successfully. ").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("card are not found").build();
        }
    }

    public Response assignedTo(int cardId, int assignedTo) {
    	        Card card;
    	try {
        card = em.find(Card.class, cardId);
    	} catch(Exception e) {
    		return Response.status(Response.Status.NOT_FOUND).entity("card not found").build();
    	}
        
            if (card.getList() == null || card.getList().getBoard() == null)
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                		.entity("card is not in list or the list is not in board please check again").build();
            
            List<Integer> users = card.getList().getBoard().getInvitedID();
            if (!users.contains(assignedTo)){
                return Response.status(Response.Status.NOT_FOUND).entity("User is not in the board").build();
            }
            User user;
            try {
            user = em.find(User.class, assignedTo);
            } catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
            if(!check(card,user))
            	return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("you are not allowed to assign this user to this card").build();
            
            if (user == null) {
            	return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
            }
            
            card.setAssignedTo(assignedTo);
            em.merge(card);
            jmsClient.sendMessage("User with ID " + assignedTo + " assigned to card with ID " + cardId);
            return Response.status(Response.Status.OK).entity("User assigned successfully to card").build();
        }
    

    public Response getAllComments(int cardId,int userId) {
        Card list;
        User user;
        try {
        	user = em.find(User.class,userId);
        	if(user == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("User is not found").build();
        }catch(Exception e) {
        	return Response.status(Response.Status.NOT_FOUND).entity("user is not found").build();
        }
        try {
            list = em.find(Card.class, cardId);
            if(list == null)
            	return Response.status(Response.Status.NOT_FOUND).entity("card not Found").build();
            if(!check(list,user))
            	return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("you are not allowed to get all comments on this card").build();
            List<String> comments = list.getComments();
            return Response.ok(comments).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("list are not found").build();
        }
    }

    public Response getCard(int cardId,int userId) {
        Card card;
        User user;
        try {
        	user = em.find(User.class, userId);
        	if(user == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("User are not found").build();
        }catch(Exception e) {
        	return Response.status(Response.Status.NOT_FOUND).entity("card are not found").build();
        }
        try{
        	card= em.find(Card.class, cardId);
        	if(card == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("card are not found").build();
        	if(!check(card, user))
        		return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("you have not allowed to get this card").build();
        	return Response.ok(card).build();
        }catch(Exception e) {
        	return Response.status(Response.Status.NOT_FOUND).entity("card are not found").build();
        }
    }
    
	public boolean check(Card card , User user) {
		if(card == null || user == null)
			return false;
		for(int i : card.getList().getBoard().getInvitedID()) {
			if(i == user.getId())
				return true;
		}
		return false;
	}
}