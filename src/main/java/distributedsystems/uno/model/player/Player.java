package distributedsystems.uno.model.player;

import java.io.Serializable;
import java.util.List;

import distributedsystems.uno.model.card.UnoCard;

public class Player implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6265969723023495838L;
	
	private int id;
	private String nickname;
	private String host;
	private List<UnoCard> cards;
	private PLAYER_STATE state;
	
	public Player(int id, String nickname, String url, List<UnoCard> cards) {
		this.id = id;
		this.nickname = nickname;
		this.host = url;
		this.cards = cards;
		this.state = PLAYER_STATE.ACTIVE;
	}

	public int getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String url) {
		this.host = url;
	}

	public List<UnoCard> getCards() {
		return cards;
	}

	public void setCards(List<UnoCard> cards) {
		this.cards = cards;
	}

	public PLAYER_STATE getState() {
		return state;
	}

	public void setState(PLAYER_STATE state) {
		this.state = state;
	}

	/**
	 * Adds a card to the owned cards
	 * @param draw
	 */
	public void addCard(UnoCard draw) {
		this.cards.add(draw);
	}
}
