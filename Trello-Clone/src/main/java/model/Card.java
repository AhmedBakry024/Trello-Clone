package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "card")
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cardId;
	private String title;
	private String description;
	private int listID;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> comments = new ArrayList<>();
	
	private int assignedTo;
	
	@ManyToOne
	@JoinColumn(name ="list_id")
	@JsonIgnore
	private ListOfCards listId;
	
	public Card() {};
	
	public Card(int CardId,String Title,String description,int assignedTo) {
		this.cardId = CardId;
		this.title = Title;
		this.description = description;
		listId.addCard(this);
		listID = listId.getListId();
	}
	
	public void setListId(ListOfCards listId) {
		listId.setListId(listID);
		this.listId = listId;
	}
	
	public ListOfCards getListId() {
		return listId !=null ? listId : null;
	}
	
	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getComments() {
		return comments;
	}

	public void addComment(String Comment) {
		comments.add(Comment);
	}

	public int getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(int assignedTo) {
		this.assignedTo = assignedTo;
	}
	
}
