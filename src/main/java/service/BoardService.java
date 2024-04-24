package service;

import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Board;
import model.User;

import javax.ws.rs.*;

@Stateless
@Path("/board")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoardService {

    @PersistenceContext(unitName = "database")
    private EntityManager entityManager;

    @GET
    @Path("/test")
    public String test() {
        return "test board from board service";
    }

    
    // - --
    //----------------------------------------------------------------
    // 2.a. - Users can create a new board with a unique name.
    // done  
    // create a new board
    //----------------------------------------------------------------

    @POST
    @Path("createBoard/{userId}")
    
    public Response createBoard(@PathParam("userId") int userId, Board board) {
        try {
        	
            User user = entityManager.find(User.class, userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User with ID " + userId + " not found")
                        .build();
            }

            if (!user.getIsTeamLeader()) {
            	 return Response.status(Response.Status.NOT_FOUND)
                         .entity("User with ID " + userId + " is not a Team Leader")
                         .build();
            }
            // Set the team leader of the board to the user's ID 
            board.setTeamLeader(user.getId());

            board.setUser(user);
            
            entityManager.persist(board);
        
            return Response.status(Response.Status.CREATED)
                    .entity("Board created successfully")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error occurred while creating board: " + e.getMessage())
                    .build();
        }
    }
    
    
    
    
    //---------------------------------------------------------------- 
    // done 
    // 2.b. - Users can view all boards they have access to.
    // get all boards that a user has access to it by passing the user ID 
    //---------------------------------------------------------------- 

    @GET
    @Path("/getBoardsByUserId/{userId}")
    public List<Board> getBoardsByUserId(@PathParam("userId") int userId) {
        try {
            // Find the user by ID
            User user = entityManager.find(User.class, userId);

            if (user == null) {
                throw new NotFoundException("User with ID " + userId + " not found");
            }

            // Get all boards associated with this user
            List<Board> boards = entityManager.createQuery(
                    "SELECT DISTINCT b FROM Board b LEFT JOIN FETCH b.invitedID WHERE b.user.id = :userId", Board.class)
                    .setParameter("userId", userId)
                    .getResultList();

            if (boards.isEmpty()) {
                throw new NotFoundException("No boards found for User ID: " + userId);
            }

            return boards;
        } catch (Exception e) {
            throw new InternalServerErrorException("Error fetching boards for User ID: " + userId);
        }
    }
    
    
    
    //---------------------------------------------------------------- 
    // 2.c. - Users can invite other users to collaborate on a board.
    // 
    //  
    //---------------------------------------------------------------- 
    
    
    
    
    @POST
    @Path("/addUserToInvitedList")
    public Response addUserToInvitedList(
            @QueryParam("userId") int userId,
            @QueryParam("boardId") int boardId) {
        try {
            // Find the Board entity by ID
            Board board = entityManager.find(Board.class, boardId);
            if (board == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Board with ID " + boardId + " not found.")
                        .build();
            }

            // Add userId to the invitedID list of the board
            List<Integer> invitedIDList = board.getInvitedID();
            if (!invitedIDList.contains(userId)) {
                invitedIDList.add(userId);
                board.setInvitedID(invitedIDList);
                entityManager.merge(board);
                return Response.status(Response.Status.OK)
                        .entity("User with ID " + userId + " added to invited list of Board with ID " + boardId)
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("User with ID " + userId + " is already in the invited list of Board with ID " + boardId)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error adding user to invited list: " + e.getMessage())
                    .build();
        }
    }

    
    // return the list of IDs of the invited users 
    @GET
    @Path("/invitedUsers")
    public Response getInvitedUsersForBoard(@QueryParam("boardId") int boardId) {
        try {
            // Find the Board entity by ID
            Board board = entityManager.find(Board.class, boardId);
            if (board == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Board with ID " + boardId + " not found.")
                        .build();
            }

            // Initialize the invitedID collection to avoid lazy loading outside of session
            board.getInvitedID().size(); // This triggers initialization

            // Get the list of invited user IDs from the board entity
            List<Integer> invitedUsers = board.getInvitedID();

            return Response.status(Response.Status.OK)
                    .entity(invitedUsers)
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching invited users for board: " + e.getMessage())
                    .build();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    @POST
//    @Path("addUserToBoard")
//    public Response addUserToBoard(
//            @QueryParam("userId") int userId,
//            @QueryParam("boardId") int boardId) {
//
//        try {
//            // Find the User entity by ID
//            User user = entityManager.find(User.class, userId);
//            if (user == null) {
//                return Response.status(Response.Status.NOT_FOUND)
//                        .entity("User with ID " + userId + " not found.")
//                        .build();
//            }
//
//            // Find the Board entity by ID
//            Board board = entityManager.find(Board.class, boardId);
//            if (board == null) {
//                return Response.status(Response.Status.NOT_FOUND)
//                        .entity("Board with ID " + boardId + " not found.")
//                        .build();
//            }
//
//            // Add board to user's list of boards
//            user.getUserBoards().add(board);
//
//            // Add user to board's list of invited users
//          //  board.getInvitedBoards().add(user);
//            board.getInvitedUsersId().add(user.getId());
//            
//            entityManager.merge(user);
//            entityManager.merge(board);
//
//            return Response.status(Response.Status.OK)
//                    .entity("User with ID " + userId + " added to Board with ID " + boardId)
//                    .build();
//
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Error adding user to board: " + e.getMessage())
//                    .build();
//        }
//    }
////    
////    
////  @GET
//    @Path("/invitedUsers")
//    public Response getInvitedUsersForBoard(@QueryParam("boardId") int boardId) {
//        try {
//            // Find the Board entity by ID
//            Board board = entityManager.find(Board.class, boardId);
//            if (board == null) {
//                return Response.status(Response.Status.NOT_FOUND)
//                        .entity("Board with ID " + boardId + " not found.")
//                        .build();
//            }
//
//            // Get the set of invited users from the board entity
//            List invitedUsers = board.getInvitedUsersId();
//
//            return Response.status(Response.Status.OK)
//                    .entity(invitedUsers)
//                    .build();
//
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Error fetching invited users for board: " + e.getMessage())
//                    .build();
//        }
//    }
    
    
    
    
    
    
    
    //---------------------------------------------------------------- 
    // 2.d. - Users can delete a board they created
    // done 
    //delete a board 
    //---------------------------------------------------------------- 

    @DELETE
    @Path("deleteBoard")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteBoard(@QueryParam("name") String name, @QueryParam("teamLeader") int teamLeader) {
        try {
           
	
            Board board = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :name", Board.class)
                                       .setParameter("name", name)
                                       .getSingleResult();
            
            if (board == null) {
                return "Board with name " + name + " not found";
            }

            
            if (board.getTeamLeader() != teamLeader ) {
                return "Only the team leader " + board.getTeamLeader() + " can delete this board";
            }

            entityManager.remove(board);
            return board.getTeamLeader() +" Board " + name + " successfully deleted";
            
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to delete board. Status: 403";
        }
    }
      
}
