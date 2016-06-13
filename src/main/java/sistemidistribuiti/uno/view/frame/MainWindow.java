package sistemidistribuiti.uno.view.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

import sistemidistribuiti.uno.exception.NextPlayerNotFoundException;
import sistemidistribuiti.uno.model.card.CARD_COLOR;
import sistemidistribuiti.uno.model.card.CARD_TYPE_ENUM;
import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.NumberCard;
import sistemidistribuiti.uno.model.card.impl.SpecialCard;
import sistemidistribuiti.uno.model.player.PLAYER_STATE;
import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.view.listener.GameGUIListener;
import sistemidistribuiti.uno.workflow.GameManager;
import sistemidistribuiti.uno.workflow.Starter;
import java.awt.Color;

public class MainWindow extends JFrame implements GameGUIListener{

	private final static Logger logger = Logger.getLogger(MainWindow.class.getName());

	private static final long serialVersionUID = -7507193594059818919L;
	
	public static ImageManager imageLoader = new ImageManager();
	private AffineTransform a = new AffineTransform();
	
	private JPanel gamePanel;
	private GameManager gameManager;
	private Painter painter = new Painter();
	private Timer timer;
	private boolean flag;
	
	private ArrayList<Shape> playerHandShapes;
	private Ellipse2D playShape;
	private Ellipse2D drawShape;
	private Ellipse2D unoShape;
	private int selectedCardIndex = -1;
	private Font font;
	
	public static final String BUTTON_ENABLED = "enabled";
	public static final String BUTTON_DISABLED = "disabled";
	public static final String BUTTON_FOCUS = "focused";
	
	private String statePlayButton;
	private String stateDrawButton;
	private String stateUnoButton;
	
	private JLabel lblThisUser;
	private JLabel lastPlayedCard;
	private JLabel lblMessage;
	private JLabel loadCircle;
	private JLabel[] lblPlayers = new JLabel[3];
	private JLabel lblInfo;

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
					
					Starter.startGame(args, frame);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}
	
	/**
	 * Create the frame.
	 */
	public MainWindow() throws Exception{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setResizable(false);
		setTitle("Uno Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
		getContentPane().setMaximumSize(new Dimension(1280, 750));
	    getContentPane().setMinimumSize(new Dimension(1280, 750));
	    getContentPane().setPreferredSize(new Dimension(1280, 750));
	    setIconImage(imageLoader.getResourceImage("ico", ".png"));
	    gamePanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics graphics) {
            	super.paintComponent(graphics);
                Graphics2D g = (Graphics2D)graphics;
                painter.setRenderingHints(g);
                painter.paintBackground(g);
                painter.paintDeckCapture(g);
                if(gameManager.getGame()!=null){
                    painter.paintPlayerCapture(g);
                    painter.paintLastCard(g);
                    painter.paintOPCapture(g, a);
                    painter.paintButtonPlay(g);
                    painter.paintButtonDraw(g);
                    painter.paintButtonUno(g);
                }
                a.setToIdentity();
                g.setTransform(a);
            }
        };
		getContentPane().add(gamePanel, BorderLayout.SOUTH);
	    pack();
	    setLocationRelativeTo(null);
	    setVisible(true);
	    
	    gamePanel.setFocusable(true);
	    gamePanel.setFocusTraversalKeysEnabled(false);
	    gamePanel.setOpaque(true);
	    gamePanel.setLayout(null);
	    gamePanel.addMouseListener(new MouseManager());
	    gamePanel.addMouseMotionListener(new MouseManager());
	    gamePanel.setMinimumSize(new Dimension(1280, 750));
	    gamePanel.setMaximumSize(new Dimension(1280, 750));
	    gamePanel.setPreferredSize(new Dimension(1280, 750));
	    gamePanel.setSize(new Dimension(1280, 750));
	    gamePanel.setBounds(0, 0, 1280, 750);
	    
		font =ImageManager.font;
		
		lblThisUser = new JLabel("user");
		lblThisUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblThisUser.setForeground(Color.WHITE);
		lblThisUser.setFont(font.deriveFont(Font.PLAIN, 30));
		lblThisUser.setBounds(1080, 105, 132, 37);
		gamePanel.add(lblThisUser);
		
		lblPlayers[0] = new JLabel("player2");
		lblPlayers[0].setForeground(Color.WHITE);
		lblPlayers[0].setFont(font.deriveFont(Font.PLAIN, 28));
		lblPlayers[0].setBounds(63, 244, 120, 37);
		lblPlayers[0].setVisible(false);
		gamePanel.add(lblPlayers[0]);
		
		lblPlayers[1] = new JLabel("player3");
		lblPlayers[1].setForeground(Color.WHITE);
		lblPlayers[1].setFont(font.deriveFont(Font.PLAIN, 28));
		lblPlayers[1].setBounds(515, 24, 120, 37);
		lblPlayers[1].setVisible(false);
		gamePanel.add(lblPlayers[1]);
		
		lblPlayers[2] = new JLabel("player4");
		lblPlayers[2].setForeground(Color.WHITE);
		lblPlayers[2].setFont(font.deriveFont(Font.PLAIN, 28));
		lblPlayers[2].setBounds(931, 244, 120, 37);
		lblPlayers[2].setVisible(false);
		gamePanel.add(lblPlayers[2]);
		
		lastPlayedCard = new JLabel("");
		lastPlayedCard.setBounds(510, 260, 120, 180);
		lastPlayedCard.setIcon(new ImageIcon(imageLoader.getComp("shadow.png")
				.getScaledInstance(120, 180,Image.SCALE_SMOOTH)));
		gamePanel.add(lastPlayedCard);
		
		lblInfo = new JLabel("");
		lblInfo.setForeground(Color.WHITE);
		lblInfo.setFont(font.deriveFont(Font.PLAIN, 32));
		lblInfo.setBounds(60, 81, 367, 37);
		gamePanel.add(lblInfo);
		
		this.stateDrawButton = BUTTON_DISABLED;
		this.statePlayButton = BUTTON_DISABLED;
		this.stateUnoButton = BUTTON_DISABLED;
		
		playerHandShapes = new ArrayList<Shape>();
	    playShape = new Ellipse2D.Float(535, 674, 105, 43);
	    drawShape = new Ellipse2D.Float(680, 674, 105, 43);
	    unoShape = new Ellipse2D.Float(730, 544, 166, 68);
		painter.captureDeck(108);
		painter.capturePlayButton(BUTTON_DISABLED);
		painter.captureDrawButton(BUTTON_DISABLED);
		painter.captureUnoButton(BUTTON_DISABLED);
		setupWaiting();
	    repaint();
	    
	}

	@Override
	public void setup(String text) {
		lblThisUser.setText(text);
	}

	@Override
	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	@Override
	public void playMyTurn() {
		setupCardView();
		
		if(atLeastOneCardPlayable()){
			logger.log(Level.INFO, "5 - at least 1");
			setStatePlayButton(BUTTON_ENABLED);
			painter.capturePlayButton(BUTTON_ENABLED);
		}else{
			logger.log(Level.INFO, "5.1 - at least 1 NOT ");
			setStatePlayButton(BUTTON_DISABLED);
			painter.capturePlayButton(BUTTON_DISABLED);
			painter.captureDrawButton(BUTTON_ENABLED);
			setStateDrawButton(BUTTON_ENABLED);
		}
		repaint();
		logger.log(Level.INFO, "Finished in playMyTurn GUI");
	}

	@Override
	public void updateGameField() {		
		this.loadCircle.setVisible(false);
		setPlayerTurn();
		
		painter.clearImages();
	    painter.captureDeck(gameManager.getGame().getDeck().getCardList().size());
		try {
			if(painter.getUserPositionList().isEmpty())
				painter.setPlayerUIPosistion(gameManager);
			painter.captureOtherPlayerHand(a, gameManager);
		} catch (NextPlayerNotFoundException e) {
			e.printStackTrace();
		}
		repaint();

		try {
			setOtherPlayersLabel();
		} catch (NextPlayerNotFoundException e) {
			e.printStackTrace();
		}
	    
		setupLastPlayedCardView();
		setupCardView();
		
		setInfoColor();
	}
	
	@Override
	public void refreshUILazyUser(){
		setStatePlayButton(BUTTON_DISABLED);
		setStateDrawButton(BUTTON_DISABLED);
		List<UnoCard> myCards = gameManager.getMyCards();
		UnoCard drawedCard = gameManager.drawCard();
		myCards.add(drawedCard);
		setupCardView();
	}
	
	private void setInfoColor() {
		if (gameManager.getGame().isColorChanged()){
			this.lblInfo.setText("The color is "+ gameManager.getLastPlayedCard().getColor() + " now!");
			this.lblInfo.setVisible(true);
		} else {
			this.lblInfo.setVisible(false);
		}	
	}

	/**
	 * Setup how the card is showed
	 * 
	 * @param showed
	 */
	private void setupCardView() {
		painter.capturePlayerHand(a, gameManager, selectedCardIndex);
		repaint();
		setPlayerShapes();
	}
	
	/**
	 * Setup the view for the last played card
	 */
	private void setupLastPlayedCardView() {
		this.lastPlayedCard.setVisible(false);
		painter.captureLastCard(gameManager);
		repaint();
	}
	
	/**
	 * Play the card
	 */
	private void playCard() {
		if(!currentCardIsPlayable(selectedCardIndex)){
			JOptionPane.showMessageDialog(null, "You cannot play this card.");
			return;
		}
		
		gameManager.getGame().setColorChanged(false);
		
		List<UnoCard> myCards = gameManager.getMyCards();
		UnoCard selected = myCards.get(selectedCardIndex);
		myCards.remove(selected);
		gameManager.discardCard(selected);
		
		if(myCards.isEmpty()){
			gameManager.winGame();
		}
		
		manageCard(selected);
		setInfoColor();		
		passTurn(myCards);
		
		try {
			painter.captureOtherPlayerHand(a, gameManager);
		} catch (NextPlayerNotFoundException e1) {
			e1.printStackTrace();
		}
		repaint();
		try {
			setOtherPlayersLabel();
		} catch (NextPlayerNotFoundException e) {
			e.printStackTrace();
		}
		setPlayerTurn();
	}

	/**
	 * Manage the played card
	 * 
	 * @param showed
	 */
	private void manageCard(UnoCard showed) {
		if(showed.getCardType() == CARD_TYPE_ENUM.SPECIAL_CARD && showed.getColor() == CARD_COLOR.RAINBOW){
			CARD_COLOR[] colors = {CARD_COLOR.RED, CARD_COLOR.BLUE, CARD_COLOR.YELLOW, CARD_COLOR.GREEN};
			int response = JOptionPane.showOptionDialog(null, "Choose color: ", "Choose color",
			        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
			        new ImageIcon(imageLoader.getComp("WILDico.png")), colors, colors[0]);
			CARD_COLOR input = null;
			switch (response){
			case 0:
				input=CARD_COLOR.RED;
				break;
			case 1:
				input=CARD_COLOR.BLUE;
				break;
			case 2:
				input=CARD_COLOR.YELLOW;
				break;
			case 3: 
				input=CARD_COLOR.GREEN;
				break;
			}
			showed.setColor(input);
			gameManager.getGame().setColorChanged(true);
		}

		if(showed.getCardType() == CARD_TYPE_ENUM.SPECIAL_CARD){
			SpecialCard specialCard = (SpecialCard) showed;
			try {
				gameManager.manageSpecialCard(specialCard);
			} catch (NextPlayerNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Draw a card
	 */
	private void drawCard(){
		List<UnoCard> myCards = gameManager.getMyCards();
		UnoCard drawedCard = gameManager.drawCard();
		myCards.add(drawedCard);
		
		passTurn(myCards);
		try {
			painter.captureOtherPlayerHand(a, gameManager);
		} catch (NextPlayerNotFoundException e1) {
			e1.printStackTrace();
		}
		repaint();
		setPlayerTurn();
	}
	
	private void drawTwoCardsUno(){
		List<UnoCard> myCards = gameManager.getMyCards();
		for(int i=0; i<2; i++){
			UnoCard drawedCard = gameManager.drawCard();
			myCards.add(drawedCard);
		}
	}
	
	

	/**
	 * Pass the turn to the next player
	 * @param cards
	 */
	private void passTurn(List<UnoCard> cards) {
		setStatePlayButton(BUTTON_DISABLED);
		setStateDrawButton(BUTTON_DISABLED);
		flag = true;
		selectedCardIndex = -1;
		
		painter.capturePlayerHand(a, gameManager, selectedCardIndex);
		setupLastPlayedCardView();
		
		if(!cards.isEmpty()){
			setupCardView();
		}
		
		if(cards.size()==1){
			flag=false;
			setStateUnoButton(BUTTON_ENABLED);
			timer = new Timer(6000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					//draw 2 cards
					drawTwoCardsUno();
					setupCardView();
					setStateUnoButton(BUTTON_DISABLED);
					gameManager.playMyTurn();
				}
			});
			timer.setRepeats(false);
			timer.start();
		}
		if (flag)
			gameManager.playMyTurn();
	}
	
	/**
	 * Checks whether there is at least a playable card in the player hand
	 * 
	 * @return true or false
	 */
	private boolean atLeastOneCardPlayable() {
		for(int i = 0; i < gameManager.getMyCards().size(); i++){
			boolean playable = currentCardIsPlayable(i);
			if(playable){
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the current card is playable
	 * @return
	 */
	private boolean currentCardIsPlayable(int index) {
		UnoCard lastPlayed = gameManager.getLastPlayedCard();
		List<UnoCard> cards = gameManager.getMyCards();
		UnoCard toPlay = cards.get(index);
		if(toPlay.getColor() == lastPlayed.getColor()){
			return true;
		}
		
		switch(toPlay.getCardType()){
		case NUMBER_CARD:
			if(lastPlayed.getCardType() == CARD_TYPE_ENUM.NUMBER_CARD){
				NumberCard lastPlayedNumberCard = (NumberCard) lastPlayed;
				NumberCard toPlayNumberCard = (NumberCard) toPlay;
				if(lastPlayedNumberCard.getNumber() == toPlayNumberCard.getNumber()){
					return true;
				}
			}
			break;
		case SPECIAL_CARD:
			logger.log(Level.INFO, toPlay.getColor().name());
			if(toPlay.getColor() == CARD_COLOR.RAINBOW){
				return true;
			}
			SpecialCard toPlaySpecialCard = (SpecialCard) toPlay;
			SpecialCard lastPlayedSpecialCard = null;
			try{
				lastPlayedSpecialCard = (SpecialCard) lastPlayed;
			}catch(Exception e){
				return false;
			}
			if(toPlaySpecialCard.getSpecialCardType() == lastPlayedSpecialCard.getSpecialCardType()){
				return true;
			}
			break;
		default:
			break;
		}	
		return false;
	}

	@Override
	public void showWinnerAlert(Player player) {
		JOptionPane.showMessageDialog(null, String.format("Player %s won!", player.getNickname()));
	}
	
	private void setupWaiting() {
		ImageIcon loading = new ImageIcon(getClass().getResource("/images/ajax-loader.gif"));
		lblMessage = new JLabel("waiting for other players...",JLabel.CENTER);
		lblMessage.setForeground(Color.WHITE);
		lblMessage.setFont(font.deriveFont(Font.PLAIN, 32));
		lblMessage.setHorizontalAlignment(SwingConstants.LEFT);
		lblMessage.setBounds(80, 28, 388, 48);
		gamePanel.add(lblMessage);		
		loadCircle = new JLabel();
		loadCircle.setBounds(20, 13, 55, 55);
		loadCircle.setIcon(loading);
		loadCircle.setVisible(true);
		gamePanel.add(loadCircle);
	}
	
	private void setOtherPlayersLabel() throws NextPlayerNotFoundException{
		List<Player> players = gameManager.getGame().getPlayers();
		for (int i=0; i<lblPlayers.length; i++){
			if(gameManager.getGame().getPlayerState(painter.getUserPositionList().get(i))==PLAYER_STATE.ACTIVE){
				for(Player p : players){
					if(p.getId()==painter.getUserPositionList().get(i)){
						lblPlayers[i].setVisible(true);
						lblPlayers[i].setText(p.getNickname());
						break;
					}
				}
			}	else {
				lblPlayers[i].setText("");
			}
		}
	}
	
	private void setPlayerTurn(){
		Player current = gameManager.getGame().getCurrent();
		String turn = null;
		if(current.getId()==gameManager.getId())
			turn="your";
		else
			turn=current.getNickname();
		lblMessage.setBounds(60, 45, 388, 48);
		lblMessage.setText("It's "+turn+" turn!");
	}
	
	private void setPlayerShapes(){
		List<UnoCard> cards = gameManager.getMyCards();
		int handsize = cards.size();
		int anchorX = 330;
		if(handsize != 0){
			int x = 60 - (handsize/7)*5;
			if (handsize < 4) x = 130;
			else if( handsize > 10) x = 50;
			int space = 590/handsize -15;
			if (space > 140) space = 140;
			if (space < 38) space = 38;
		    this.playerHandShapes.clear();
		    for (int i = 0; i < handsize; i++) {
		    	if(i==0) {
		    		anchorX = anchorX+x;
		    	} else {
		    		anchorX = anchorX + space;
		    	}
		        playerHandShapes.add(new RoundRectangle2D.Float(anchorX, 467, 120, 180, 7, 7));
		    }
		}
	}
	
	public String getStatePlayButton() {
		return statePlayButton;
	}

	public void setStatePlayButton(String statePlayButton) {
		this.statePlayButton = statePlayButton;
		painter.capturePlayButton(statePlayButton);
		repaint();
	}

	public String getStateDrawButton() {
		return stateDrawButton;
	}

	public void setStateDrawButton(String stateDrawButton) {
		this.stateDrawButton = stateDrawButton;
		painter.captureDrawButton(stateDrawButton);
		repaint();
	}
	
	public String getStateUnoButton(){
		return stateUnoButton;
	}
	
	public void setStateUnoButton(String stateUnoButton){
		this.stateUnoButton = stateUnoButton;
		painter.captureUnoButton(stateUnoButton);
		repaint();
	}

	private class MouseManager extends MouseAdapter{
		
		private int oldIndex = -1;
		
		@Override
		public void mouseMoved(MouseEvent e){
			Point p = e.getPoint();
			if (playShape.contains(p) && getStatePlayButton().equals(BUTTON_ENABLED)){
				setStatePlayButton(BUTTON_FOCUS);
				return;
			} else if (drawShape.contains(p) && getStateDrawButton().equals(BUTTON_ENABLED)){
				setStateDrawButton(BUTTON_FOCUS);
				return;
			} else if (!playShape.contains(p) && getStatePlayButton().equals(BUTTON_FOCUS)){
				setStatePlayButton(BUTTON_ENABLED);
				return;
			} else if (!drawShape.contains(p) && getStateDrawButton().equals(BUTTON_FOCUS)){
				setStateDrawButton(BUTTON_ENABLED);
				return;
			} else if (unoShape.contains(p) && getStateUnoButton().equals(BUTTON_ENABLED)){
				setStateUnoButton(BUTTON_FOCUS);
				return;
			} else if (!unoShape.contains(p) && getStateUnoButton().equals(BUTTON_FOCUS)){
				setStateUnoButton(BUTTON_ENABLED);
				return;
			}
		}

		@Override
		public void mousePressed(MouseEvent e){
			Point p = e.getPoint();
			if (playShape.contains(p) && !getStatePlayButton().equals(BUTTON_DISABLED)
					&& selectedCardIndex != -1) {
				playCard();
				selectedCardIndex = -1;
				this.oldIndex = -1;
				return;
			} else if (drawShape.contains(p) && !getStateDrawButton().equals(BUTTON_DISABLED)){
				drawCard();
				return;
			} else if (playShape.contains(p) && !getStatePlayButton().equals(BUTTON_DISABLED)
					&& selectedCardIndex == -1){
				JOptionPane.showMessageDialog(null, "Select a card!");
				return;
			} else if (unoShape.contains(p) && !getStateUnoButton().equals(BUTTON_DISABLED)){
				timer.stop();
				gameManager.playMyTurn();
				setStateUnoButton(BUTTON_DISABLED);
				return;
			}
	         
			for (int s = playerHandShapes.size() - 1; s >= 0; s--) {
				if (playerHandShapes.get(s).contains(e.getPoint())) {
					selectedCardIndex = s;
					logger.log(Level.INFO, "Selected card "+ selectedCardIndex);
					if(this.oldIndex == selectedCardIndex)
						selectedCardIndex = -1;
					painter.capturePlayerHand(a, gameManager, selectedCardIndex);
	                repaint();
	                this.oldIndex = selectedCardIndex;
	                return;
	             }
	          }
		}
	}
}
