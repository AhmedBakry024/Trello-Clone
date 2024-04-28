package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    private int teamLeader;
    
    
    public Board() {
    }

    public Board(String name, int  teamLeader) {
        this.name = name;
        this.teamLeader = teamLeader;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(int teamLeader) {
        this.teamLeader = teamLeader;
    }

    
    
    
    @Column
    @ElementCollection(targetClass=Integer.class , fetch = FetchType.LAZY)
    private List<Integer> invitedID = new ArrayList<>();


	public List<Integer> getInvitedID() {
		
		
		
		 if (this.invitedID == null) {
	            this.invitedID =  new ArrayList<>();
	        }
	        return invitedID;
		
	
	}

	public void setInvitedID(List<Integer> invitedID) {
		this.invitedID = invitedID;
	}




	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id") 
    @JsonIgnore
    private User user;

   
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
	    @ManyToMany
	    @JoinTable(
	            name = "Board_User",
	            joinColumns = @JoinColumn(name = "board_id"),
	            inverseJoinColumns = @JoinColumn(name = "user_id")
	    )
	    @JsonIgnore
	    private Set<User> invitedUsers = new HashSet<>();
	
	    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
		@JsonIgnore
	    private List <ListOfCards> listOfCards ;

	    public void setList(ListOfCards list) {
	    	if(list != null) {
	    		listOfCards.add(list);
	    		list.setBoard(this);
	    	}
	    }
	 
	 
	
}
