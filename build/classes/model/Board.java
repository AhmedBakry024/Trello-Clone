package service;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "Board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    private String  teamLeader;
    
    public Board() {
    }

    public Board(String name, String  teamLeader) {
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

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id") 
    private User user;

   
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
}
