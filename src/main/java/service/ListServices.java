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
        User user = em.find(User.class, userId);
        Board board = em.find(Board.class, listOfCards.getBoardId());
        if (board == null)
            return Response.status(Response.Status.NOT_FOUND).entity("User Are Not have a board").build();
        listOfCards.setBoard(board);
        try {
            if (user != null) {
                if (!user.getIsTeamLeader()) {
                    return Response.status(403).entity("User Are Not TeamLeader").build();
                } else if (board.getTeamLeader() != user.getId()) {
                    return Response.status(403).entity("User Are Not The Team Leader Of This Board").build();
                } else {
                    em.persist(listOfCards);
                    return Response.status(Response.Status.CREATED).entity(listOfCards).build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).entity("User Are Not Found").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error").build();
        }
    }


    public Response deletelist(int listId, int userId) {
        User user = em.find(User.class, userId);

        if (user != null) {
            if (!user.getIsTeamLeader()) {
                return Response.status(403).entity("User Are Not TeamLeader").build();
            }
            ListOfCards listOfCards = em.find(ListOfCards.class, listId);
            if (listOfCards == null)
                return Response.status(Response.Status.NOT_FOUND).build();
            else if (listOfCards.getBoard().getTeamLeader() != userId) {
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
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User Are Not Found").build();
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