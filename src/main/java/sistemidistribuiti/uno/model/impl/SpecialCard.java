package sistemidistribuiti.uno.model.impl;

import sistemidistribuiti.uno.model.CARD_COLOR;
import sistemidistribuiti.uno.model.SPECIAL_CARD_TYPE;
import sistemidistribuiti.uno.model.UnoCard;

public class SpecialCard extends UnoCard{
	private SPECIAL_CARD_TYPE specialCardType;

	public SpecialCard(CARD_COLOR color, SPECIAL_CARD_TYPE specialCardType) {
		super(color);
		this.specialCardType = specialCardType;
	}

	public SPECIAL_CARD_TYPE getSpecialCardType() {
		return specialCardType;
	}
}
