package sistemidistribuiti.uno.model.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import sistemidistribuiti.uno.exception.NextPlayerNotFoundException;
import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.Deck;
import sistemidistribuiti.uno.model.player.CurrentNode;
import sistemidistribuiti.uno.model.player.PLAYER_STATE;
import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.rmi.client.UnoRemoteClient;

public class Game implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2750321770514228846L;
	private final static Logger logger = Logger.getLogger(UnoRemoteClient.class.getName());

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
		this.current = players.get(0);

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
	public void reverseDirection() {
		gameDirection = Direction.reverseDirection(gameDirection);
	}

	/**
	 * Shuffle the currenct deck
	 */
	public void shuffleDeck() {
		List<UnoCard> cards = deck.getCardList();
		Collections.shuffle(cards);
	}

	public void setCurrent(Player newCurrent) {
		this.current = newCurrent;
	}

	public boolean playerWon() {
		for (Player player : players) {
			if (player.getState() == PLAYER_STATE.WINNER) {
				return true;
			}
		}

		return false;
	}

	public Player getNextPlayer() throws NextPlayerNotFoundException {
		return getNextPlayer(getCurrent().getId());
	}

	public Player getNextPlayer(int id) throws NextPlayerNotFoundException {

		List<Player> players = getPlayers();
		
		Direction direction = getGameDirection();

		int directionValue = 0;

		switch (direction) {
		case BACKWARD:
			directionValue = -1;
			break;
		case FORWARD:
			directionValue = 1;
			break;
		}

		for (int i = 0; i < players.size(); i++) {
			Player iteratePlayer = players.get(i);
			if (iteratePlayer.getId() == id) {
				i = (i + directionValue) % players.size();
				if (i < 0) {
					i = players.size() - 1;
				}				
				Player newCurrent = players.get(i);
				return newCurrent;
			}
		}

		logger.log(Level.INFO, "Nodes in game class:" + getPlayers().toString());
		
		throw new NextPlayerNotFoundException();
	}

	public Player getNextPlayerWithSkip() throws NextPlayerNotFoundException {
		Player nextPlayer = getNextPlayer();
		//return getNextPlayer(nextPlayer.getId());
		return nextPlayer;
	}
	
	public String toString(){
		String strGame = "";
		strGame = "Next Player -> " + current.getId() + "\n";
		strGame = strGame + "Number of players -> " + players.size() + "\n";
		strGame = strGame + "Players IDs -> ";
		String strCom = "";
		for (int i = 0; i< players.size(); i++){
			strCom = strCom + players.get(i).getId() + " - ";			
		}
		strGame = strGame + strCom;
		return strGame;
	}
	
}
