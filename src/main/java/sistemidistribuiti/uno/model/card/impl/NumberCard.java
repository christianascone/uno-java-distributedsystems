package sistemidistribuiti.uno.model.card.impl;

import sistemidistribuiti.uno.model.card.CARD_COLOR;
import sistemidistribuiti.uno.model.card.CARD_TYPE_ENUM;
import sistemidistribuiti.uno.model.card.UnoCard;

/**
 * Classic card, with a number and a background color
 * 
 * @author christian
 *
 */
public class NumberCard extends UnoCard{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7210043546952154154L;
	
	private int number;

	public NumberCard(CARD_COLOR color, byte[] image, int number) {
		super(color, image, CARD_TYPE_ENUM.NUMBER_CARD);
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
	
}
