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

    @Column
    @ElementCollection(targetClass=Integer.class , fetch = FetchType.LAZY)
    private List<Integer> invitedID = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "Board_User",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> invitedUsers ;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
    private List <ListOfCards> listOfCards ;
    
    @Column
    @ElementCollection(targetClass=Integer.class , fetch = FetchType.LAZY)
    private List<Integer> listOfCardsId ;
    
    @Column(unique = true)
    private String name;
    
    private int teamLeader;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id") 
    @JsonIgnore
    private User user;
    
    
    public Board() {
    }

    public Board(String name, int  teamLeader) {
        this.name = name;
        this.teamLeader = teamLeader;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getInvitedID() {

		 if (this.invitedID == null) {
	            this.invitedID =  new ArrayList<>();
	        }
	        return invitedID;
	}

    public List<ListOfCards> getListOfCards(){
		return listOfCards;
	}

    public List<Integer> getListOfCardsId() {
		 if (this.listOfCardsId == null) {
	            this.listOfCardsId =  new ArrayList<>();
	        }
	        return listOfCardsId;
	}

    public String getName() {
        return name;
    }

    public int getTeamLeader() {
        return teamLeader;
    }

	public User getUser() {
		return user;
	}

	public void setId(int id) {
        this.id = id;
    }
   
	public void setInvitedID(List<Integer> invitedID) {
		this.invitedID = invitedID;
	}

	public void setList(ListOfCards list) {
		if(list != null) {
	   		listOfCards.add(list);
	   		list.setBoard(this);
  		}
	}

	public void setListOfCardsId(List<Integer> listOfCardsId) {
		this.listOfCardsId = listOfCardsId;
	}
	 
	    public void setName(String name) {
		    this.name = name;
		}
	    
	    public void setTeamLeader(int teamLeader) {
		    this.teamLeader = teamLeader;
		}

		public void setUser(User user) {
			this.user = user;
		}	
}
