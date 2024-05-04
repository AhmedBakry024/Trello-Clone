package model;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
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
@Table(name = "sprint")
public class sprint {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	 
		
		@JsonIgnore
		private int sprintId ; 
		
		   public int getSprintId() {
			return sprintId;
		}
		public void setSprintId(int sprintId) {
			this.sprintId = sprintId;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public sprint() {
		    }
		   public sprint(int id) {
			   sprintId = id ; 
		   }
	   
		    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
		    @JsonIgnore
		    private Set<Card> cards = new HashSet<>();;
	   
		   
		   public Set<Card> getLists() {
		        return cards;
		    }

		    public void setLists(Set<Card> cards) {
		        this.cards = cards;
		    }
		      
		
		    @Column
		    @ElementCollection(targetClass=Integer.class , fetch = FetchType.LAZY)
		    private List<Integer> cardsID  = new ArrayList<>();;

			public List<Integer> getCardId() {
				
				 if (this.cardsID == null) {
			            this.cardsID =  new ArrayList<>();
			        }
			        return cardsID;
			}

			public void setCardId(List<Integer> invitedID) {
				this.cardsID = invitedID;
			}
		
			 public void addToCardid(int cardId) {
			        this.cardsID.add(cardId);
			    }
			
		    
}
