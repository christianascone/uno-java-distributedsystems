package sistemidistribuiti.uno.model.card;

import java.io.Serializable;

/**
 * Abstract class for uno card, classic or special
 * 
 * @author christian
 *
 */
public abstract class UnoCard implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4560112007584528044L;
	
	private CARD_COLOR color;
	private byte[] image;
	
	private CARD_TYPE_ENUM cardType;
	
	public UnoCard(CARD_COLOR color, byte[] image, CARD_TYPE_ENUM cardType) {
		super();
		this.color = color;
		this.image = image;
		this.cardType = cardType;
	}

	public CARD_COLOR getColor() {
		return color;
	}

	public void setColor(CARD_COLOR color) {
		this.color = color;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public CARD_TYPE_ENUM getCardType() {
		return cardType;
	}
}
