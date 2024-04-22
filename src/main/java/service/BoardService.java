package service;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejbs.calculation;

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


    // done 
    //delete a board 
    @DELETE
    @Path("deleteBoard")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteBoard(@QueryParam("name") String name, @QueryParam("teamLeader") String teamLeader) {
        try {
           
	
            Board board = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :name", Board.class)
                                       .setParameter("name", name)
                                       .getSingleResult();
            
            if (board == null) {
                return "Board with name " + name + " not found";
            }

            
            if (!board.getTeamLeader().equals(teamLeader)) {
                return "Only the team leader " + board.getTeamLeader() + " can delete this board";
            }

            entityManager.remove(board);
            return board.getTeamLeader() +" Board " + name + " successfully deleted";
            
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to delete board. Status: 403";
        }
    }
   
    
//    // to do 
//    @GET
//    @Path("getallboard")
//    public Response getAllBoards() {
//        try {
//            List<Board> boards = entityManager.createQuery(
//                    "SELECT b FROM Board b JOIN FETCH b.user",
//                    Board.class
//                ).getResultList();
//            
//            return Response.status(200).entity(boards).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                           .entity("Error occurred while fetching boards: " + e.getMessage())
//                           .build();
//        }
//    }
    

    // Users can view all boards they have access to.
    @GET
    @Path("getBoardsByUser/{userId}")
    public Response getBoardsByUser(@PathParam("userId") int userId) {
        try {
            // Retrieve user from the database based on the provided userId
            User user = entityManager.find(User.class, userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User with ID " + userId + " not found")
                        .build();
            }

            // Query to fetch all boards created by the specified user
            TypedQuery<Board> query = entityManager.createQuery(
                    "SELECT b FROM Board b WHERE b.user = :user",
                    Board.class
            );
            query.setParameter("user", user);
            List<Board> boards = query.getResultList();

            // Convert Board entities to BoardDTOs
            List<BoardDTO> boardDTOs = boards.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return Response.status(Response.Status.OK)
                    .entity(boardDTOs)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error occurred while fetching boards: " + e.getMessage())
                    .build();
        }
    }

    // Helper method to convert Board entity to BoardDTO
    private BoardDTO convertToDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setName(board.getName());
        boardDTO.setTeamLeader(board.getTeamLeader());
        // You can add more properties to map from Board to BoardDTO as needed
        return boardDTO;
    }
    
    

   
    
    // finished my part
    // takes userID ,checks if exists 
    // checks is teamLeader 
    // check unique name 
    // create a new board
    @POST
    @Path("createBoard/{userId}")
    
    public Response createBoard(@PathParam("userId") int userId, Board board) {
        try {
            // Retrieve user from the database based on the provided userId
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
            // Set the team leader of the board to the user's name
            board.setTeamLeader(user.getName());

            // Associate the board with the user
            board.setUser(user);

            // Persist the board in the database
            entityManager.persist(board);

            // Return success response
            return Response.status(Response.Status.CREATED)
                    .entity("Board created successfully")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error occurred while creating board: " + e.getMessage())
                    .build();
        }
    }
      
}
