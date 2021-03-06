package distributedsystems.uno.model.card;

import java.io.Serializable;

/**
 * Abstract class for uno card, classic or special
 * 
 * @author Christian Ascone
 *
 */
public abstract class UnoCard implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4560112007584528044L;
	
	private CARD_COLOR color;
	private CARD_TYPE_ENUM cardType;
	
	public UnoCard(CARD_COLOR color, CARD_TYPE_ENUM cardType) {
		super();
		this.color = color;
		this.cardType = cardType;
	}

	public CARD_COLOR getColor() {
		return color;
	}

	public void setColor(CARD_COLOR color) {
		this.color = color;
	}
	
	public CARD_TYPE_ENUM getCardType() {
		return cardType;
	}
}
