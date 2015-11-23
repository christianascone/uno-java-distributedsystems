package sistemidistribuiti.uno.workflow;

import sistemidistribuiti.uno.listener.DataReceiverListener;
import sistemidistribuiti.uno.model.card.impl.Deck;

/**
 * Data receiver class which manage the callback when RMI methods are sent to 
 * a class
 * @author christian
 *
 */
public class DataReceiver implements DataReceiverListener{
	private Deck deck;
	
	public Deck getDeck() {
		return this.deck;
	}
	
	@Override
	public void setDeck(Deck deck) {
		this.deck = deck;
	}

}
