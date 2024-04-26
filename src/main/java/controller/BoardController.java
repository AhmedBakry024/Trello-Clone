package controller;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
@Path("/board")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BoardController {

	
	@Inject
	private BoardService boardService;

	 
	    @GET
	    @Path("/test")
	    public String test() {
	        return boardService.test();
	    }

	    @POST
	    @Path("/createBoard")
	    public Response createBoard(@QueryParam("userId") int userId, Board board) {
	        return boardService.createBoard(userId, board);
	    }

	    @GET
	    @Path("/getBoardsByUserId")
	    public List<Board> getBoardsByUserId(@QueryParam("userId") int userId) {
	        return boardService.getBoardsByUserId(userId);
	    }

	    @POST
	    @Path("/addUserToInvitedList")
	    public Response addUserToInvitedList(
	            @QueryParam("userId") int userId,
	            @QueryParam("boardId") int boardId) {
	        return boardService.addUserToInvitedList(userId, boardId);
	    }
	
	    @GET
	    @Path("/invitedUsers")
	    public Response getInvitedUsersForBoard(@QueryParam("boardId") int boardId) {
	        return boardService.getInvitedUsersForBoard(boardId);
	    }
	 
	    @DELETE
	    @Path("/deleteBoard")
	    @Produces(MediaType.TEXT_PLAIN)
	    public String deleteBoard(@QueryParam("name") String name, @QueryParam("teamLeader") int teamLeader) {
	        return boardService.deleteBoard(name, teamLeader);
	    }
}
