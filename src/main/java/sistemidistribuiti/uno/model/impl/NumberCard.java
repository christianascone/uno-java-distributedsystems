package sistemidistribuiti.uno.model.impl;

import sistemidistribuiti.uno.model.CARD_COLOR;
import sistemidistribuiti.uno.model.UnoCard;

/**
 * Classic card, with a number and a background color
 * 
 * @author christian
 *
 */
public class NumberCard extends UnoCard{
	private int number;

	public NumberCard(CARD_COLOR color, byte[] image, int number) {
		super(color, image);
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
	
}
