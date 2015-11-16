package sistemidistribuiti.uno.model.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.player.Player;

public class Game {
	private List<Player> players;
	private List<UnoCard> deck;
	private List<UnoCard> discarded;
	private Player current;
	
	public Game(List<Player> players, List<UnoCard> deck, Player current) {
		this.players = players;
		this.deck = deck;
		
		shuffleDeck();
		this.discarded = new LinkedList<UnoCard>();
		
		// Draw the starting player
		Random rnd = new Random();
		int randomStartingPlayer = rnd.nextInt(players.size());
		this.current = players.get(randomStartingPlayer);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<UnoCard> getDeck() {
		return deck;
	}

	public List<UnoCard> getDiscarded() {
		return discarded;
	}

	public Player getCurrent() {
		return current;
	}
	
	/**
	 * Shuffle the currenct deck
	 */
	public void shuffleDeck(){
		Collections.shuffle(deck);
	}
}
