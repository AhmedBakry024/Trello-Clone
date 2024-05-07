package service;

import java.util.List;
import java.util.regex.Pattern;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

import model.User;

@Stateless
public class UserServices {
	
	@PersistenceContext(unitName = "database")
	private EntityManager em;
	
//	@Inject
//	private JMSClient messagingClient;

	public Response createUser(User user) {
		User userFromDb;
//		messagingClient.sendMessage("User created: " + user.getEmail());
		
		try {
			userFromDb = (User) em.createQuery("SELECT u FROM User u WHERE u.email = :email")
					.setParameter("email", user.getEmail()).getSingleResult();
		} catch (Exception e) {
		    userFromDb = null;
		}
        // check if email is valid
		if (!isEmailValid(user.getEmail())) {
			return Response.status(400).entity("Invalid email").build();
		}
		
		// check if the user exists in the database (use contains instead of find to check if the user's email already exists in the database )
		// use create query not find because find is used for primary key
		if (userFromDb != null) {
			return Response.status(400).entity("User already exists").build();
		}
		
		// add the user to the database
		em.persist(user);
		return Response.status(200).entity("User created successfully").build();
	}

	
	public Response loginUser(User user) {
		
        User userFromDb;
        
		// check if email is valid
        if (!isEmailValid(user.getEmail())) {
            return Response.status(400).entity("Invalid email").build();
        }
        try {
            userFromDb = (User) em.createQuery("SELECT u FROM User u WHERE u.email = :email")
                    .setParameter("email", user.getEmail()).getSingleResult();
        } catch (Exception e) {
            userFromDb = null;
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
	
    
	public Response editName(int id, String name) {
		User user = em.find(User.class, id);
		if (user == null) {
			return Response.status(400).entity("User doesn't exist").build();
		}
		user.setName(name);
		em.merge(user);
		return Response.status(200).entity("Name updated successfully").build();
	}
	
	
	public Response editEmail(int id, String email) {
		User user = em.find(User.class, id);
		if (user == null) {
			return Response.status(400).entity("User doesn't exist").build();
		}
		user.setEmail(email);
		em.merge(user);
		return Response.status(200).entity("Email updated successfully").build();
	}
	
	
	public Response editPassword(int id, String password) {
		User user = em.find(User.class, id);
		if (user == null) {
			return Response.status(400).entity("User doesn't exist").build();
		}
		user.setPassword(password);
		em.merge(user);
		return Response.status(200).entity("Password updated successfully").build();
	}
	
	public boolean isEmailValid(String email) {
		final Pattern VALID_EMAIL_ADDRESS_REGEX =
			    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		return VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches();
	}
	
	
}