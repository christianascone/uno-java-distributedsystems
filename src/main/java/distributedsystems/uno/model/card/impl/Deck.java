package distributedsystems.uno.model.card.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import distributedsystems.uno.model.card.UnoCard;

public class Deck implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3382222528225025132L;
	
	private List<UnoCard> cardList;
	
	public Deck(){
		this.cardList = new LinkedList<UnoCard>();
	}

	public Deck(List<UnoCard> cards) {
		this.cardList = cards;
	}

	public List<UnoCard> getCardList() {
		return cardList;
	}

	public void setCardList(List<UnoCard> cardList) {
		this.cardList = cardList;
	}
}
