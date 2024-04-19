package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;
	private String name;
	private String email;
	private String password;
	private boolean isTeamLeader;

	
	public User() {
    }
	
	public User(int Id, String name, String email, String password, boolean isTeamLeader) {
		super();
        this.Id = Id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isTeamLeader = isTeamLeader;
    }
	
	// getters and setters
	public int getId() {
		return Id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean getIsTeamLeader() {
		return isTeamLeader;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setIsTeamLeader(boolean isTeamLeader) {
		this.isTeamLeader = isTeamLeader;
	}
	
	
	

}