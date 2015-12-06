package sistemidistribuiti.uno.model.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.Deck;
import sistemidistribuiti.uno.model.player.PLAYER_STATE;
import sistemidistribuiti.uno.model.player.Player;

public class Game implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2750321770514228846L;
	
	private List<Player> players;
	private Deck deck;
	private Deck discarded;
	private Player current;
	private Direction gameDirection;
	
	public Game(List<Player> players, Deck deck) {
		this.players = players;
		this.deck = deck;
		
		shuffleDeck();
		this.discarded = new Deck();
		
		// Draw the starting player
		Random rnd = new Random();
		int randomStartingPlayer = rnd.nextInt(players.size());
		this.current = players.get(randomStartingPlayer);
		
		this.gameDirection = Direction.getDefault();
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Deck getDeck() {
		return deck;
	}

	public Deck getDiscarded() {
		return discarded;
	}

	public Player getCurrent() {
		return current;
	}
	
	public Direction getGameDirection() {
		return gameDirection;
	}
	
	/**
	 * Reverse game direction
	 */
	public void reverseDirection(){
		gameDirection = Direction.reverseDirection(gameDirection);
	}

	/**
	 * Shuffle the currenct deck
	 */
	public void shuffleDeck(){
		List<UnoCard> cards = deck.getCardList();
		Collections.shuffle(cards);
	}

	public void setCurrent(Player newCurrent) {
		this.current = newCurrent;
	}

	public boolean playerWon() {
		for(Player player : players){
			if(player.getState() == PLAYER_STATE.WINNER){
				return true;
			}
		}
		
		return false;
	}
}
