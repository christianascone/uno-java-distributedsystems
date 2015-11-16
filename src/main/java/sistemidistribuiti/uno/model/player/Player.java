package sistemidistribuiti.uno.model.player;

import java.util.List;

import sistemidistribuiti.uno.model.card.UnoCard;

public class Player {
	private String nickname;
	private String url;
	private List<UnoCard> cards;
	private PLAYER_STATE state;
	
	public Player(String nickname, String url, List<UnoCard> cards) {
		this.nickname = nickname;
		this.url = url;
		this.cards = cards;
		this.state = PLAYER_STATE.ACTIVE;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
}
