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
	
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> comments = new ArrayList<>();
	
	private int assignedTo;
	
	@ManyToOne
	@JoinColumn(name ="list_id")
	@JsonIgnore
	private ListOfCards list;
	
	public Card() {};
	
	public Card(String Title,String description) {
		this.title = Title;
		this.description = description;
	}
	
	public void setList(ListOfCards list) {
		this.list = list;
	}
	
	public ListOfCards getList() {
		return list !=null ? list : null;
	}
	
	public int getCardId() {
		return cardId;
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
		if(Comment != null && !Comment.trim().isEmpty()) {
			this.comments.add(Comment);
		}
	}

	public int getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(int assignedTo) {
		this.assignedTo = assignedTo;
	}
	
}