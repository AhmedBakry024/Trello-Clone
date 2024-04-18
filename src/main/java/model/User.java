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
	private int id;
	private String name;
	private String email;
	private String password;
	private boolean IsTeamLeader;

	
	public User() {
    }
	
	public User(int id, String name, String email, String password, boolean IsTeamLeader) {
		super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.IsTeamLeader = IsTeamLeader;
    }
	
	// getters and setters
	public int getId() {
		return id;
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
		return IsTeamLeader;
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
	
	public void setIsTeamLeader(boolean IsTeamLeader) {
		this.IsTeamLeader = IsTeamLeader;
	}
	
	
	

}