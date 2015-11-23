package sistemidistribuiti.uno.listener;

import sistemidistribuiti.uno.model.card.impl.Deck;

/**
 * Listener used for the RMI callback
 * 
 * @author christian
 *
 */
public interface DataReceiverListener {
	/**
	 * Set the deck received from client
	 * @param deck
	 */
	void setDeck(Deck deck);
}
