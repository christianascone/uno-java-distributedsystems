package sistemidistribuiti.uno.model.card.impl;

import sistemidistribuiti.uno.model.card.CARD_COLOR;
import sistemidistribuiti.uno.model.card.CARD_TYPE_ENUM;
import sistemidistribuiti.uno.model.card.SPECIAL_CARD_TYPE;
import sistemidistribuiti.uno.model.card.UnoCard;

public class SpecialCard extends UnoCard{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6864973083834352541L;
	private String code;
	private SPECIAL_CARD_TYPE specialCardType;

	public SpecialCard(CARD_COLOR color, SPECIAL_CARD_TYPE specialCardType) {
		super(color, CARD_TYPE_ENUM.SPECIAL_CARD);
		this.specialCardType = specialCardType;
		if(CARD_COLOR.getValidColor().contains(color))
			this.code = specialCardType.name()+"_"+color.name();
		else
			this.code = specialCardType.name();
	}

	public String getCode() {
		return code;
	}

	public SPECIAL_CARD_TYPE getSpecialCardType() {
		return specialCardType;
	}
}
