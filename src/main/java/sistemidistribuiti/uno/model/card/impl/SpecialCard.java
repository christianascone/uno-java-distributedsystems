package sistemidistribuiti.uno.model.card.impl;

import sistemidistribuiti.uno.model.card.CARD_COLOR;
import sistemidistribuiti.uno.model.card.SPECIAL_CARD_TYPE;
import sistemidistribuiti.uno.model.card.UnoCard;

public class SpecialCard extends UnoCard{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6864973083834352541L;
	
	private SPECIAL_CARD_TYPE specialCardType;

	public SpecialCard(CARD_COLOR color, byte[] image,
			SPECIAL_CARD_TYPE specialCardType) {
		super(color, image);
		this.specialCardType = specialCardType;
	}

	public SPECIAL_CARD_TYPE getSpecialCardType() {
		return specialCardType;
	}
}
