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
public class ListServices {


    @PersistenceContext(unitName = "database")
    private EntityManager em;


    public Response createList(ListOfCards listOfCards, int userId) {
        User user;
        Board board;
//        check if there is a list with the same name and same board id
        int existingList =  em.createQuery("SELECT l FROM ListOfCards l WHERE l.listName = :listName AND l.boardId = :boardId")
        		.setParameter("listName", listOfCards.getListName()).setParameter("boardId", listOfCards.getBoardId())
        		.getResultList().size();
        if(existingList > 0) {
        	return Response.status(Response.Status.CONFLICT).entity("List with the same name already exists").build();
        }
        try{
        	user = em.find(User.class, userId);
        	if(user == null)
        		return Response.status(Response.Status.NOT_FOUND).entity("user not found").build();
        	try{
        		board = em.find(Board.class, listOfCards.getBoardId());
        		if(board == null)
            		return Response.status(Response.Status.NOT_FOUND).entity("Board not found").build();
	        	if (!user.getIsTeamLeader()) {
	                return Response.status(403).entity("User is Not TeamLeader").build();
	            } else if (board.getTeamLeader() != user.getId()) {
	                return Response.status(403).entity("User is Not The Team Leader Of This Board").build();
	            } else {
	            	listOfCards.setBoard(board);
	                em.persist(listOfCards);
	                return Response.status(Response.Status.CREATED).entity(listOfCards).build();        
	            }
	    	}catch(Exception e) {
	    		return Response.status(Response.Status.NOT_FOUND).entity("board is not found").build();
	    	}
        }catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("user not found").build();
        }
    }


    public Response deletelist(int listId, int userId) {
        User user;
        ListOfCards listOfCards;
        try {
            user = em.find(User.class, userId);
            if (!user.getIsTeamLeader())
                return Response.status(403).entity("User Are Not TeamLeader").build();
            }catch(Exception e) {
            	return Response.status(Response.Status.NOT_FOUND).entity("User Are Not Found").build();
            }
        
            try {
                listOfCards = em.find(ListOfCards.class, listId);
                if(listOfCards == null)
                	return Response.status(Response.Status.NOT_FOUND).entity("List not found").build(); 
            }catch(Exception e) {
            	return Response.status(Response.Status.NOT_FOUND).entity("List not found").build(); 
            }
            try {
                if (listOfCards.getBoard().getTeamLeader() != userId) {
                    return Response.status(403).entity("User Are Not The Team Leader Of This Board").build();
                } else {
                    try {
                        for (Card card : listOfCards.getCards()) {
                            if (!em.contains(card)) {
                                card = em.merge(card);
                            }
                            listOfCards.removeCard(card);
                            em.remove(card);
                        }
                        em.remove(listOfCards);
                        em.flush();
                        return Response.status(Response.Status.OK).entity("List deleted successfully").build();
                    } catch (Exception e) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting list").build();
                    }
                }
            }catch(Exception e) {
            	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting list").build();
            }
    }


    public Response getAllCardsInList(int listId) {
        ListOfCards list;
        try {
            list = em.find(ListOfCards.class, listId);
            return Response.ok(list.getCards()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // get all lists created 
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