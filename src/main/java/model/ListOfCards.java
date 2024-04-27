package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "list")
public class ListOfCards {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String listName;
	
	@OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Card> cards = new ArrayList<>();

	public ListOfCards() {};
	
	public ListOfCards(int id,String listName,List<Card>cards) {
		this.id = id;
		this.listName = listName;
		this.cards = cards;
	}
	
	public void addCard(Card card) {
		if(card !=null) {
			cards.add(card);
			card.setList(this);
		}
	}
	
	public void removeCard(Card card) {
		cards.remove(card);
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public int getListId() {
		return id;
	}

	public List<Card> getCards() {
		return cards;
	}

}