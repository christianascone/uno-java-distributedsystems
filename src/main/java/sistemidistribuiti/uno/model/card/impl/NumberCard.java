package sistemidistribuiti.uno.model.card.impl;

import sistemidistribuiti.uno.model.card.CARD_COLOR;
import sistemidistribuiti.uno.model.card.CARD_TYPE_ENUM;
import sistemidistribuiti.uno.model.card.UnoCard;

/**
 * Classic card, with a number and a background color
 * 
 * @author Christian Ascone
 *
 */
public class NumberCard extends UnoCard{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7210043546952154154L;
	private String code;
	private int number;

	public NumberCard(CARD_COLOR color, int number) {
		super(color, CARD_TYPE_ENUM.NUMBER_CARD);
		this.number = number;
		this.code = Integer.toString(number) + color.name();
	}

	public int getNumber() {
		return number;
	}

	public String getCode() {
		return code;
	}
	
}
