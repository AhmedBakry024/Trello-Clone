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
	private String Name;
	private String Email;
	private String Password;
	private boolean IsTeamLeader;

	
	public User() {
    }
	
	public User(int Id, String Name, String Email, String Password, boolean IsTeamLeader) {
		super();
        this.Id = Id;
        this.Name = Name;
        this.Email = Email;
        this.Password = Password;
        this.IsTeamLeader = IsTeamLeader;
    }
	
	// getters and setters
	public int getId() {
		return Id;
	}
	
	public String getName() {
		return Name;
	}
	
	public String getEmail() {
		return Email;
	}
	
	public String getPassword() {
		return Password;
	}
	
	public boolean getIsTeamLeader() {
		return IsTeamLeader;
	}
	
	public void setName(String name) {
		this.Name = name;
	}
	
	public void setEmail(String email) {
		this.Email = email;
	}
	
	public void setPassword(String password) {
		this.Password = password;
	}
	
	public void setIsTeamLeader(boolean IsTeamLeader) {
		this.IsTeamLeader = IsTeamLeader;
	}
	
	
	

}