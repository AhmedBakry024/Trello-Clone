package service;

import java.util.List;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.User;


@Stateless
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserServices {
	
	@PersistenceContext(unitName = "database")
	private EntityManager em;

	@POST
	@Path("/create")
	public Response createUser(User user) {
		User userFromDb;
		
		try {
			userFromDb = (User) em.createQuery("SELECT u FROM User u WHERE u.email = :email")
					.setParameter("email", user.getEmail()).getSingleResult();
		} catch (Exception e) {
		    userFromDb = null;
		}
        // check if email is valid
		if (!VALID_EMAIL_ADDRESS_REGEX.matcher(user.getEmail()).matches()) {
			return Response.status(400).entity("Invalid email").build();
		}
		
		// check if the user exists in the database (use contains instead of find to check if the user's email already exists in the database )
		// use create query not find because find is used for primary key
		if (userFromDb != null) {
			return Response.status(400).entity("User already exists").build();
		}
		
		// add the user to the database
		em.persist(user);
		return Response.status(201).entity("User created successfully").build();
	}

	@PUT
	@Path("/login")
	public Response loginUser(User user) {
		
        User userFromDb;
        try {
            userFromDb = (User) em.createQuery("SELECT u FROM User u WHERE u.email = :email")
                    .setParameter("email", user.getEmail()).getSingleResult();
        } catch (Exception e) {
            userFromDb = null;
        }
		// check if email is valid
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(user.getEmail()).matches()) {
            return Response.status(400).entity("Invalid email").build();
        }
		
		// use create query not find because find is used for primary key
		if (userFromDb == null)
		{
			return Response.status(400).entity("User doesn't exist").build();
		}
		
		// check if the password is correct
		if (!userFromDb.getPassword().equals(user.getPassword())) {
			return Response.status(400).entity("Incorrect password").build();
		}
		return Response.status(200).entity(userFromDb).build();
	}
	
	
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(User user) {
//		userServices.updateUser(user);
		return Response.status(200).entity("User updated successfully").build();
	}
	
	@GET
	@Path("/test")
	public Response test() {
		return Response.status(200).entity("Test successful").build();
	}
	
	 // updated 
    // error when creating a new board to a user 
    @GET
    @Path("/getall")
    public Response getAllUsers() {
    	 try {
    	        List<User> users = em.createQuery("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userBoards", User.class)
    	                              .getResultList();
    	        return Response.status(200).entity(users).build();
    	    } catch (Exception e) {
    	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    	                       .entity("Error occurred while fetching users: " + e.getMessage())
    	                       .build();
    	    }
    }
	
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}