package model;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "list")
public class ListOfCards {
	@ManyToOne
	@JoinColumn(name ="board_id")
	@JsonIgnore
	private Board board;
	private int boardId;
	
	@OneToMany(mappedBy = "list",fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Set<Card> cards ;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String listName;
	
	public ListOfCards() {};
	public ListOfCards(int id,String listName,Set<Card>cards,int boardId) {
		this.id = id;
		this.listName = listName;
		this.cards = cards;
		this.boardId= boardId;
	}
	public ListOfCards(String listName, int boardId) {
	    this.listName = listName;
	    this.boardId = boardId;
	}
	
	public void addCard(Card card) {
		if(card !=null) {
			cards.add(card);
			card.setList(this);
		}
	}

	public Board getBoard() {
		return board;
	}

	public int getBoardId() {
		return boardId;
	}

	public Set<Card> getCards() {
		return cards;
	}

	public int getListId() {
		return id;
	}

	public String getListName() {
		return listName;
	}
	
	public void removeCard(Card card) {
		cards.remove(card);
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}
	
	public void setListName(String listName) {
		this.listName = listName;
	}
	
	
	
}