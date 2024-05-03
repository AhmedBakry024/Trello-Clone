package model;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.OneToMany;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private boolean isTeamLeader;

    public User() {
    }
    public User(int Id, String name, String email, String password, boolean isTeamLeader) {
        this.id = Id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isTeamLeader = isTeamLeader;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean getIsTeamLeader() {
        return this.isTeamLeader;
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
    
    @OneToMany(mappedBy="user")	
    @JsonIgnore
    private Set<Board> userBoards;



	public void setUserBoards(Set<Board> userBoards) {
		this.userBoards = userBoards;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTeamLeader(boolean isTeamLeader) {
		this.isTeamLeader = isTeamLeader;
	}
	 public Set<Board> getUserBoards() {
	        if (this.userBoards == null) {
	            this.userBoards = new HashSet<Board>(); 
	        }
	        return userBoards;
	    }
	 
	 
	 @ManyToMany(mappedBy = "invitedUsers")
	    @JsonIgnore
	    private Set<Board> boards = new HashSet<>();

 
	 
	 
}