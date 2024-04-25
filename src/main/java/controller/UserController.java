package controller;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.*;
import service.*;

@Stateless
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
	
	@Inject
	private UserServices userService;

	@GET
	@Path("/all")
	public Response getAllUsers() {
		return userService.getAllUsers();
	}

	
	@POST
	@Path("/create")
	public Response signupUser(User user) {
		return userService.createUser(user);
	}

	
	@PUT
	@Path("/login")
	public Response loginUser(User user) {
		return userService.loginUser(user);
	}

	  @PUT
	  @Path("/editName/{id}")
	  public Response editName(int id, String name) {
            return userService.editName(id, name);
        }
	  
	  @PUT
	  @Path("/editEmail/{id}")
	  public Response editEmail(int id, String email) {
			return userService.editEmail(id, email);
		}
	  
	  @PUT
	  @Path("/editPassword/{id}")
	  public Response editPassword(int id, String password) {
		              return userService.editPassword(id, password);
		              
	  }
	  
	  @GET
		@Path("/test")
		public Response test() {
			return Response.status(200).entity("Test successful").build();
		}
}