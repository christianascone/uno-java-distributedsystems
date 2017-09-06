package distributedsystems.uno.view.frame;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_OFF;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;
import static java.awt.RenderingHints.VALUE_RENDER_QUALITY;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import distributedsystems.uno.exception.NextPlayerNotFoundException;
import distributedsystems.uno.model.card.UnoCard;
import distributedsystems.uno.model.card.impl.NumberCard;
import distributedsystems.uno.model.card.impl.SpecialCard;
import distributedsystems.uno.model.player.PLAYER_STATE;
import distributedsystems.uno.model.player.Player;
import distributedsystems.uno.workflow.GameManager;

public class Painter {
	
	private BufferedImage deckCapture;
	private BufferedImage[] playerCardCapture = new BufferedImage[3];
	private BufferedImage currentPlayerCardCapture;
	private BufferedImage lastCardCapture;
	private BufferedImage buttonPlay;
	private BufferedImage buttonDraw;
	private BufferedImage buttonUno;
	private List<Integer> allUsersInPosition;
	ImageManager images = MainWindow.imageLoader;
	private double angle = 4.2;
	
	public Painter(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		deckCapture = gc.createCompatibleImage(200, 300, BufferedImage.TRANSLUCENT);
	    lastCardCapture = gc.createCompatibleImage(120, 180, BufferedImage.TRANSLUCENT);
	    for (int i=0; i<playerCardCapture.length; i++)
	    	playerCardCapture[i] = gc.createCompatibleImage(420, 230, BufferedImage.TRANSLUCENT);
	    currentPlayerCardCapture = gc.createCompatibleImage(694, 330, BufferedImage.TRANSLUCENT);
	    buttonPlay = gc.createCompatibleImage(105, 43, BufferedImage.TRANSLUCENT);
	    buttonDraw = gc.createCompatibleImage(105, 43, BufferedImage.TRANSLUCENT);
	    buttonUno = gc.createCompatibleImage(166, 68, BufferedImage.TRANSLUCENT);
	    allUsersInPosition = new ArrayList<>();
	}
	
	public void setRenderingHints(Graphics2D g){
		g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
		g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_OFF);
	}
	
	public void clearImages(){
		for (int i=0; i<playerCardCapture.length; i++)
			images.clearImage(playerCardCapture[i]);
		images.clearImage(currentPlayerCardCapture);
		images.clearImage(lastCardCapture);
		images.clearImage(deckCapture);
	}
	
	public List<Integer> getUserPositionList(){
		return allUsersInPosition;
	}
	
	public void setPlayerUIPosistion(GameManager gm) throws NextPlayerNotFoundException{
		List<Player> players = gm.getGame().getPlayers();
		int idCurrent = gm.getId();
		for (int i=0; i<players.size()-1; i++){
			Player p = gm.getGame().getNextPlayer(idCurrent);
			int id = p.getId();
			allUsersInPosition.add(id);
			idCurrent = id;
		}
	}
	
	public void paintCard(Graphics2D g, String c, int x, int y){
		g.drawImage(images.getCard(c), x, y, 120, 180, null);
	}

	public void paintCard(Graphics2D g, String c, int x, int y, int width,
			int height){
		g.drawImage(images.getCard(c), x, y, width, height, null);
	}
	
	public void capturePlayButton(String state){
		images.clearImage(buttonPlay);
		Graphics2D g = buttonPlay.createGraphics();
		setRenderingHints(g);
		switch (state){
		case MainWindow.BUTTON_ENABLED:
			g.drawImage(images.getComp("play.png"),0, 0, 105, 43, null);
			break;
		case MainWindow.BUTTON_DISABLED:
			g.drawImage(images.getComp("playDisabled.png"), 0, 0, 105, 43, null);
			break;
		case MainWindow.BUTTON_FOCUS:
			g.drawImage(images.getComp("playFocus.png"), 0, 0, 105, 43, null);
			break;
		}
	}
	
	public void captureDrawButton(String state){
		images.clearImage(buttonDraw);
		Graphics2D g = buttonDraw.createGraphics();
		setRenderingHints(g);
		switch (state){
		case MainWindow.BUTTON_ENABLED:
			g.drawImage(images.getComp("draw.png"),0, 0, 105, 43, null);
			break;
		case MainWindow.BUTTON_DISABLED:
			g.drawImage(images.getComp("drawDisabled.png"), 0, 0, 105, 43, null);
			break;
		case MainWindow.BUTTON_FOCUS:
			g.drawImage(images.getComp("drawFocus.png"), 0, 0, 105, 43, null);
			break;
		}
	}
	
	public void captureUnoButton(String state){
		images.clearImage(buttonUno);
		Graphics2D g = buttonUno.createGraphics();
		setRenderingHints(g);
		switch (state){
		case MainWindow.BUTTON_ENABLED:
			g.drawImage(images.getComp("uno.png"),0, 0, 166, 68, null);
			break;
		case MainWindow.BUTTON_DISABLED:
			break;
		case MainWindow.BUTTON_FOCUS:
			g.drawImage(images.getComp("unoFocus.png"), 0, 0, 166, 68, null);
			break;
		}
	}
	
	public void captureLastCard(GameManager gameManager){
		images.clearImage(lastCardCapture);
		Graphics2D g = lastCardCapture.createGraphics();
		setRenderingHints(g);
		UnoCard showed = gameManager.getLastPlayedCard();
		String code = null;
		switch (showed.getCardType()) {
		case NUMBER_CARD:
			NumberCard numberCard = (NumberCard) showed;
			code = numberCard.getCode();
			break;
		case SPECIAL_CARD:
			SpecialCard specialCard = (SpecialCard) showed;
			code = specialCard.getCode();
			break;
		}
		paintCard(g, code, 0, 0);
	}
	
	public void capturePlayerHand(AffineTransform a, GameManager gameManager, int overIndex){
		images.clearImage(currentPlayerCardCapture);
		Graphics2D g = currentPlayerCardCapture.createGraphics();
		setRenderingHints(g);
		List<UnoCard> cards = gameManager.getMyCards();
		int handsize = cards.size();
		if(handsize != 0){
			int x = 60 - (handsize/7)*5;
			if (handsize < 4) x = 130;
			else if( handsize > 10) x = 50;
			int y = 15;
			int space = 590/handsize -15;
			if (space > 140) space = 140;
			else if (space < 38) space = 38;
			int showX = 0, showY = 0;
			String showCode = null;
			for (int i = 0; i < handsize; i++) {
				String code = null;
				UnoCard card = cards.get(i);
				switch (card.getCardType()) {
				case NUMBER_CARD:
					NumberCard numberCard = (NumberCard) card;
					code = numberCard.getCode();
					break;
				case SPECIAL_CARD:
					SpecialCard specialCard = (SpecialCard) card;
					code = specialCard.getCode();
					break;
				}
				if (i == overIndex) {
					showX = x;
					showY = y -15;
					showCode = code;
					x = x+space;
					continue;
				}
				paintCard(g, code, x, y);
				x = x+space;
			}
			if (overIndex != -1)
				paintCard(g, showCode, showX, showY);
		}
	}
	
	public void captureOtherPlayerHand(AffineTransform a, GameManager gm) throws NextPlayerNotFoundException{
		a.setToIdentity();
		List<Player> players = gm.getGame().getPlayers();
		for (int i=0; i<playerCardCapture.length; i++){
			images.clearImage(playerCardCapture[i]);
			Graphics2D g = playerCardCapture[i].createGraphics();
			setRenderingHints(g);
			Player toDrawn = null;
			if(gm.getGame().getPlayerState(allUsersInPosition.get(i))==PLAYER_STATE.ACTIVE){
				for(Player p : players){
					if(p.getId()==allUsersInPosition.get(i)){
						toDrawn = p;
						break;
					}
				}
			}	else {
				continue;
			}
			if(toDrawn != null){
				int size = toDrawn.getCards().size();
				if(size != 0){
					double initialAngle = Math.toRadians(-angle * (size + 1) / 2);
					a.rotate(initialAngle, 150, 342 - (5 * size / 4));
					g.setTransform(a);
					int x = 0;
					if(size > 16){
						size = 16;
						x = 90;
					}
					else if(size > 10){
						x = 100;
					}
					else {x = 70;} 
					int y = 25 - (5 * size / 4); 
					for (int j = size; j >= 0; j--) {
						a.rotate(Math.toRadians(angle), 150, 342 - (5 * size / 4));
						g.setTransform(a);
						if (j != size && size <= 10) {
							paintCard(g, "back", x, y, 100, 140);
						} else if (j != size && size > 10){
							paintCard(g, "back", x, y, 80, 120);
						}
					}
				}
				a.setToIdentity();
			}
		}
	}
	
	public void captureDeck(int deckSize){
		images.clearImage(deckCapture);
		if (deckSize <= 0) return;
		Graphics2D g = deckCapture.createGraphics();
		setRenderingHints(g);
		int size = deckSize / 4 + 1;
		for (int i = 0; i < size; i++) {
			g.drawImage(images.getCard("back"), 23 - (deckSize) / 4 + 1 - i + size - 2, 21 - (deckSize) / 4 + 1 - i + size, 120, 180, null);
		}
	}
	
	public void paintOPCapture(Graphics2D g, AffineTransform a){
		a.setToIdentity();
		g.setTransform(a);
		g.drawImage(playerCardCapture[0], 65, 266, 420, 230, null);
		g.drawImage(playerCardCapture[1], 513, 46, 420, 230, null);
		g.drawImage(playerCardCapture[2], 923, 266, 420, 230, null);
	}
	
	public void paintPlayerCapture(Graphics2D g){
		g.drawImage(currentPlayerCardCapture, 330, 467, 694, 330, null);
	}
	
	public void paintDeckCapture(Graphics2D g){
		g.drawImage(deckCapture, 650, 236, 200, 300, null);
	}
	
	public void paintLastCard(Graphics2D g){
		g.drawImage(lastCardCapture, 510, 260, 120, 180, null);
	}
	   
	public void paintBackground(Graphics2D g){
		g.drawImage(images.getComp("wallpaper.png"), 0, 0, 1280, 750, null);
	}
	
	public void paintButtonPlay(Graphics2D g){
		g.drawImage(buttonPlay, 535, 674, 105, 43, null);
	}

	public void paintButtonDraw(Graphics2D g) {
		g.drawImage(buttonDraw, 680, 674, 105, 43, null);
	}
	
	public void paintButtonUno(Graphics2D g){
		g.drawImage(buttonUno, 730, 544, 166, 68, null);
	}
}
