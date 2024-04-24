package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "list")
public class ListOfCards {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int listId;
	private String listName;
	
	@OneToMany(mappedBy = "listId")
	private List<Card> cards = new ArrayList<>();
	
	public List<Integer> getCardIds(){
		List<Integer> cardIds = new ArrayList<>();
		for(Card card : cards) {
			cardIds.add(card.getCardId());
		}
		return cardIds;
	}
	
	public ListOfCards() {};
	
	public ListOfCards(String listName) {
		this.listName = listName;
	}
	
	public void addCard(Card card) {
		cards.add(card);
		card.setListId(this);
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
		return listId;
	}

	public void setListId(int listId) {
		this.listId = listId;
	}
	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> Cards) {
		this.cards = Cards;
	}
	
}
