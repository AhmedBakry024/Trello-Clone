package model;

import javax.persistence.ManyToMany;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    
    @ManyToMany(mappedBy = "invitedUsers") 
    @JsonIgnore
    private Set<Board> boards = new HashSet<>();
    
    @OneToMany(mappedBy="user") 
    @JsonIgnore
    private Set<Board> userBoards;


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
    
    @Column
    @ElementCollection(targetClass=Integer.class , fetch = FetchType.LAZY)
    private List<Integer> boardID = new ArrayList<>();


    public List<Integer> getBoardID() {
    	 if (this.boardID == null) {
	            this.boardID =  new ArrayList<>();
	        }
	        return boardID;
	}
	public void setBoardID(List<Integer> boardID) {
		this.boardID = boardID;
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

	public void setUserBoards(Set<Board> userBoards) {
		this.userBoards = userBoards;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTeamLeader(boolean isTeamLeader) {
		this.isTeamLeader = isTeamLeader;
	}
}