package sistemidistribuiti.uno.model.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sistemidistribuiti.uno.exception.NextPlayerNotFoundException;
import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.Deck;
import sistemidistribuiti.uno.model.player.PLAYER_STATE;
import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.rmi.client.UnoRemoteClient;

/**
 * Main Game class, which manages all components (players, deck)
 * 
 * @author Christian Ascone
 *
 */
public class Game implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2750321770514228846L;
	private final static Logger logger = Logger.getLogger(UnoRemoteClient.class
			.getName());

	private List<Player> players;
	private Deck deck;
	private Deck discarded;
	private Player current;
	private Direction gameDirection;
	private boolean colorChanged;

	private HashMap<Integer, PLAYER_STATE> playersAvailability = new HashMap<Integer, PLAYER_STATE>();

	public Game(List<Player> players, Deck deck) {
		this.players = players;
		this.deck = deck;

		shuffleDeck();
		this.discarded = new Deck();

		// TODO: Draw a random starting player

		this.current = players.get(0);

		this.gameDirection = Direction.getDefault();
		this.colorChanged = false;

		// Set current active state for every player
		for (Player p : players) {
			this.playersAvailability.put(p.getId(), PLAYER_STATE.ACTIVE);
		}
	}

	public void setPlayerState(int id, PLAYER_STATE state) {
		playersAvailability.put(id, state);
	}

	public PLAYER_STATE getPlayerState(int id) {
		return playersAvailability.get(id);
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
	 * Shuffle the current deck
	 */
	public void shuffleDeck() {
		List<UnoCard> cards = deck.getCardList();
		Collections.shuffle(cards);
	}

	/**
	 * @param newCurrent
	 *            New current player
	 */
	public void setCurrent(Player newCurrent) {
		this.current = newCurrent;
	}

	/**
	 * Checks if a player won the game
	 * 
	 * @return If exists a player who won
	 */
	public boolean playerWon() {
		for (Player player : players) {
			if (player.getState() == PLAYER_STATE.WINNER) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Gets next player (after the current one) who must play
	 * 
	 * @return Next player
	 * @throws NextPlayerNotFoundException
	 *             In case next player is not found
	 */
	public Player getNextPlayer() throws NextPlayerNotFoundException {
		return getNextPlayer(getCurrent().getId());
	}

	/**
	 * Gets next player after the one with given id, who must play
	 * 
	 * @param id
	 *            Id of current player
	 * @return Next player
	 * @throws NextPlayerNotFoundException
	 *             In case next player is not found
	 */
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

		// Search current player
		for (int i = 0; i < players.size(); i++) {
			Player iteratePlayer = players.get(i);
			if (iteratePlayer.getId() == id) {
				i = findNextPlayerIndex(players, directionValue, i);
				Player newCurrent = players.get(i);
				return newCurrent;
			}
		}

		logger.log(Level.INFO, "Nodes in game class:" + getPlayers().toString());

		throw new NextPlayerNotFoundException();
	}

	/**
	 * Find index of next player starting from i-th player
	 * 
	 * @param players
	 *            List of players
	 * @param directionValue
	 *            Turn direction
	 * @param i
	 *            Index of current player
	 * @return The next player
	 */
	private int findNextPlayerIndex(List<Player> players, int directionValue,
			int i) {
		// Gets index of next player (based on direction)
		i = (i + directionValue) % players.size();
		// If 'i' is negative get the last player
		if (i < 0) {
			i = players.size() - 1;
		}
		return i;
	}

	/**
	 * Gets next player (after the current one) who must play, skipping the
	 * first one
	 * 
	 * @return Next player
	 * @throws NextPlayerNotFoundException
	 *             In case next player is not found
	 */
	public Player getNextPlayerWithSkip() throws NextPlayerNotFoundException {
		Player nextPlayer = getNextPlayer();
		return nextPlayer;
	}

	public String toString() {
		String strGame = "";
		strGame = "Next Player -> " + current.getId() + "\n";
		strGame = strGame + "Number of players -> " + players.size() + "\n";
		strGame = strGame + "Players IDs -> ";
		String strCom = "";
		for (int i = 0; i < players.size(); i++) {
			strCom = strCom + players.get(i).getId() + " - ";
		}
		strGame = strGame + strCom;
		return strGame;
	}

	public boolean isColorChanged() {
		return colorChanged;
	}

	public void setColorChanged(boolean colorChanged) {
		this.colorChanged = colorChanged;
	}

}
