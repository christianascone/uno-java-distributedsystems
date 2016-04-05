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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import sistemidistribuiti.uno.exception.NextPlayerNotFoundException;
import sistemidistribuiti.uno.model.card.CARD_COLOR;
import sistemidistribuiti.uno.model.card.CARD_TYPE_ENUM;
import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.NumberCard;
import sistemidistribuiti.uno.model.card.impl.SpecialCard;
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
	
	private ArrayList<Shape> playerHandShapes = new ArrayList<Shape>();
	private RoundRectangle2D emptyPlayerCardShape;
	private Ellipse2D playShape;
	private Ellipse2D drawShape;
	private int selectedCardIndex = -1;
	
	public static final String BUTTON_ENABLED = "enabled";
	public static final String BUTTON_DISABLED = "disabled";
	public static final String BUTTON_FOCUS = "focused";
	
	private String statePlayButton;
	private String stateDrawButton;
	
	private JLabel lblThisUser;
	private JLabel lastPlayedCard;
	private JLabel lblWaiting;
	private JLabel loadCircle;
	private JLabel[] lblPlayers = new JLabel[3];

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
                    painter.paintOPCapture(g, a);
                    painter.paintPlayerCapture(g);
                    painter.paintLastCard(g);
                    painter.paintButtonPlay(g);
                    painter.paintButtonDraw(g);
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
		
		lblThisUser = new JLabel("user");
		lblThisUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblThisUser.setForeground(Color.WHITE);
		lblThisUser.setFont(new Font("Arista", Font.PLAIN, 30));
		lblThisUser.setBounds(1090, 80, 132, 37);
		gamePanel.add(lblThisUser);
		
		lblPlayers[0] = new JLabel("player2");
		lblPlayers[0].setForeground(Color.WHITE);
		lblPlayers[0].setFont(new Font("Arista", Font.PLAIN, 28));
		lblPlayers[0].setBounds(60, 224, 120, 37);
		lblPlayers[0].setVisible(false);
		gamePanel.add(lblPlayers[0]);
		
		lblPlayers[1] = new JLabel("player3");
		lblPlayers[1].setForeground(Color.WHITE);
		lblPlayers[1].setFont(new Font("Arista", Font.PLAIN, 28));
		lblPlayers[1].setBounds(515, 8, 120, 37);
		lblPlayers[1].setVisible(false);
		gamePanel.add(lblPlayers[1]);
		
		lblPlayers[2] = new JLabel("player4");
		lblPlayers[2].setForeground(Color.WHITE);
		lblPlayers[2].setFont(new Font("Arista", Font.PLAIN, 28));
		lblPlayers[2].setBounds(951, 224, 120, 37);
		lblPlayers[2].setVisible(false);
		gamePanel.add(lblPlayers[2]);
		
		lastPlayedCard = new JLabel("");
		lastPlayedCard.setBounds(510, 280, 120, 180);
		lastPlayedCard.setIcon(new ImageIcon(imageLoader.getComp("shadow.png")
				.getScaledInstance(120, 180,Image.SCALE_SMOOTH)));
		gamePanel.add(lastPlayedCard);
		
		this.stateDrawButton = BUTTON_DISABLED;
		this.statePlayButton = BUTTON_DISABLED;
		
	    emptyPlayerCardShape = new RoundRectangle2D.Float(380, 475, 120, 180, 7, 7);
	    playShape = new Ellipse2D.Float(530, 689, 105, 43);
	    drawShape = new Ellipse2D.Float(675, 689, 105, 43);
		painter.captureDeck(108);
		painter.capturePlayButton(BUTTON_DISABLED);
		painter.captureDrawButton(BUTTON_DISABLED);
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
			painter.captureDrawButton(BUTTON_ENABLED);
			setStateDrawButton(BUTTON_ENABLED);
		}
		logger.log(Level.INFO, "Finished in playMyTurn GUI");
		repaint();
	}

	@Override
	public void updateGameField() {		
		this.loadCircle.setVisible(false);
		setPlayerTurn();
		
		try {
			setOtherPlayersLabel();
		} catch (NextPlayerNotFoundException e) {
			e.printStackTrace();
		}
		
		painter.clearImages();
	    painter.captureDeck(gameManager.getGame().getDeck().getCardList().size());
		try {
			painter.captureOtherPlayerHand(a, gameManager);
		} catch (NextPlayerNotFoundException e) {
			e.printStackTrace();
		}
	    
		setupLastPlayedCardView();
		setupCardView();
	}
	
	/**
	 * Setup how the card is showed
	 * 
	 * @param showed
	 */
	private void setupCardView() {
		painter.capturePlayerHand(a, gameManager, selectedCardIndex);
		setPlayerShapes();
		repaint();
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
		
		List<UnoCard> myCards = gameManager.getMyCards();
		UnoCard selected = myCards.get(selectedCardIndex);
		myCards.remove(selected);
		gameManager.discardCard(selected);
		
		if(myCards.isEmpty()){
			gameManager.winGame();
		}
		
		manageCard(selected);
		
		passTurn(myCards);
	}

	/**
	 * Manage the played card
	 * 
	 * @param showed
	 */
	private void manageCard(UnoCard showed) {
		if(showed.getCardType() == CARD_TYPE_ENUM.SPECIAL_CARD && showed.getColor() == CARD_COLOR.RAINBOW){
			CARD_COLOR[] colors = {CARD_COLOR.RED, CARD_COLOR.BLUE, CARD_COLOR.YELLOW, CARD_COLOR.GREEN};
			CARD_COLOR input = (CARD_COLOR) JOptionPane.showInputDialog(null, "Choose color",
			        "Choose the color for this card.", JOptionPane.QUESTION_MESSAGE, null, // Use
			                                                                        // default
			                                                                        // icon
			        colors, // Array of choices
			        colors[0]); // Initial choice
			showed.setColor(input);
		}

		if(showed.getCardType() == CARD_TYPE_ENUM.SPECIAL_CARD){
			SpecialCard specialCard = (SpecialCard) showed;
			try {
				gameManager.manageSpecialCard(specialCard);
			} catch (NextPlayerNotFoundException e) {
				// TODO Auto-generated catch block
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
	}

	/**
	 * Pass the turn to the next player
	 * @param cards
	 */
	private void passTurn(List<UnoCard> cards) {
		painter.capturePlayButton(BUTTON_DISABLED);
		painter.captureDrawButton(BUTTON_DISABLED);
		setStatePlayButton(BUTTON_DISABLED);
		setStateDrawButton(BUTTON_DISABLED);
		
		selectedCardIndex = -1;
		painter.capturePlayerHand(a, gameManager, selectedCardIndex);
		
		setupLastPlayedCardView();
		
		if(!cards.isEmpty()){
			setupCardView();
		}
		gameManager.playMyTurn();
	}
	
	/**
	 * Checks whether there is at least a playable card in the player hand
	 * 
	 * @return true or false
	 */
	private boolean atLeastOneCardPlayable() {
		logger.log(Level.INFO, "5.0.1 playable ? ");
		for(int i = 0; i < gameManager.getMyCards().size(); i++){
			logger.log(Level.INFO, "5.0.3 playable ? " + i);
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
		logger.log(Level.INFO, "6.0.2 playable ? ");
		UnoCard lastPlayed = gameManager.getLastPlayedCard();
		logger.log(Level.INFO, "6.0.3 playable ? ");
		List<UnoCard> cards = gameManager.getMyCards();
		logger.log(Level.INFO, "6.0.4 playable ? ");
		UnoCard toPlay = cards.get(index);
		logger.log(Level.INFO, "6.0.5 playable ? ");
		if(toPlay.getColor() == lastPlayed.getColor()){
			return true;
		}
		logger.log(Level.INFO, "6.0.6 playable ? ");
		
		switch(toPlay.getCardType()){
		case NUMBER_CARD:
			logger.log(Level.INFO, "6.0.7 playable ? ");
			if(lastPlayed.getCardType() == CARD_TYPE_ENUM.NUMBER_CARD){
				logger.log(Level.INFO, "6.0.7.1 playable ? ");

				NumberCard lastPlayedNumberCard = (NumberCard) lastPlayed;
				logger.log(Level.INFO, "6.0.7.2 playable ? ");

				NumberCard toPlayNumberCard = (NumberCard) toPlay;
				logger.log(Level.INFO, "6.0.7.3 playable ? ");

				if(lastPlayedNumberCard.getNumber() == toPlayNumberCard.getNumber()){
					logger.log(Level.INFO, "6.0.7.4 playable ? ");

					return true;
				}
				logger.log(Level.INFO, "6.0.7.5 playable ? ");
			}
			logger.log(Level.INFO, "6.0.7.6 playable ? ");

			break;
		case SPECIAL_CARD:
			logger.log(Level.INFO, "6.0.8 playable ? ");
			logger.log(Level.INFO, toPlay.getColor().name());
			if(toPlay.getColor() == CARD_COLOR.RAINBOW){
				logger.log(Level.INFO, "6.0.8.1 playable ? ");
				return true;
			}
			logger.log(Level.INFO, "6.0.8.2 playable ? ");
			SpecialCard toPlaySpecialCard = (SpecialCard) toPlay;
			logger.log(Level.INFO, "6.0.8.3 playable ? ");
			
			SpecialCard lastPlayedSpecialCard = null;
			try{
				lastPlayedSpecialCard = (SpecialCard) lastPlayed;
			}catch(Exception e){
				return false;
			}
			logger.log(Level.INFO, "6.0.8.4 playable ? ");
			if(toPlaySpecialCard.getSpecialCardType() == lastPlayedSpecialCard.getSpecialCardType()){
				logger.log(Level.INFO, "6.0.8.5 playable ? ");
				return true;
			}
			logger.log(Level.INFO, "6.0.8.6 playable ? ");
			break;
		default:
			logger.log(Level.INFO, "6.0.9 playable ? ");
			break;
		}
		logger.log(Level.INFO, "6.0.10 playable ? ");		
		return false;
	}

	@Override
	public void showWinnerAlert(Player player) {
		JOptionPane.showMessageDialog(null, String.format("Player %s won!", player.getNickname()));
	}
	
	public void setupWaiting() {
		ImageIcon loading = new ImageIcon(getClass().getResource("/images/ajax-loader.gif"));
		lblWaiting = new JLabel("waiting for other players...",JLabel.CENTER);
		lblWaiting.setForeground(Color.WHITE);
		lblWaiting.setFont(new Font("Arista", Font.PLAIN, 32));
		lblWaiting.setHorizontalAlignment(SwingConstants.LEFT);
		lblWaiting.setBounds(80, 23, 388, 48);
		gamePanel.add(lblWaiting);		
		loadCircle = new JLabel();
		loadCircle.setBounds(20, 10, 55, 55);
		loadCircle.setIcon(loading);
		loadCircle.setVisible(true);
		gamePanel.add(loadCircle);
	}
	
	public void setOtherPlayersLabel() throws NextPlayerNotFoundException{
		List<Player> players = gameManager.getGame().getPlayers();
		Player current = null;
		for(Player player : players){
			if(player.getId() == gameManager.getId()){
				current = player;
			}
		}
		for (int i=0; i<lblPlayers.length; i++){
			Player p = gameManager.getGame().getNextPlayer(current.getId());
			lblPlayers[i].setText(p.getNickname());
			lblPlayers[i].setVisible(true);
			current =p;
		}
	}
	
	public void setPlayerTurn(){
		Player current = gameManager.getGame().getCurrent();
		String turn = null;
		if(current.getId()==gameManager.getId())
			turn="your";
		else
			turn=current.getNickname();
		lblWaiting.setText("It's "+turn+" turn!");
	}
	
	public void setPlayerShapes(){
		List<UnoCard> cards = gameManager.getMyCards();
		int handsize = cards.size();
		int x = 15 - (handsize/7)*5;
		int space = 140 - handsize*10;
	    playerHandShapes.clear();
	    for (int i = 0; i < handsize; i++) {
	    	if(i==0) a.translate(x, 0);
	        playerHandShapes.add(a.createTransformedShape(emptyPlayerCardShape));
	        a.translate(space, 0);
	    }
	    a.setToIdentity();
	}
	
	public String getStatePlayButton() {
		return statePlayButton;
	}

	public void setStatePlayButton(String statePlayButton) {
		this.statePlayButton = statePlayButton;
	}

	public String getStateDrawButton() {
		return stateDrawButton;
	}

	public void setStateDrawButton(String stateDrawButton) {
		this.stateDrawButton = stateDrawButton;
	}
	
	private class MouseManager extends MouseAdapter{
		
		private int oldIndex;
		
		@Override
		public void mouseMoved(MouseEvent e){
			Point p = e.getPoint();
			if (playShape.contains(p) && getStatePlayButton().equals(BUTTON_ENABLED)){
				painter.capturePlayButton(BUTTON_FOCUS);
				setStatePlayButton(BUTTON_FOCUS);
				repaint();
				return;
			} else if (drawShape.contains(p) && getStateDrawButton().equals(BUTTON_ENABLED)){
				painter.captureDrawButton(BUTTON_FOCUS);
				setStateDrawButton(BUTTON_FOCUS);
				repaint();
				return;
			} else if (!playShape.contains(p) && getStatePlayButton().equals(BUTTON_FOCUS)){
				painter.capturePlayButton(BUTTON_ENABLED);
				setStatePlayButton(BUTTON_ENABLED);
				repaint();
				return;
			} else if (!drawShape.contains(p) && getStateDrawButton().equals(BUTTON_FOCUS)){
				painter.captureDrawButton(BUTTON_ENABLED);
				setStateDrawButton(BUTTON_ENABLED);
				repaint();
				return;
			}
		}

		@Override
		public void mousePressed(MouseEvent e){
			Point p = e.getPoint();
			if (playShape.contains(p) && !getStatePlayButton().equals(BUTTON_DISABLED)
					&& selectedCardIndex != -1) {
				playCard();
				return;
			} else if (drawShape.contains(p) && !getStateDrawButton().equals(BUTTON_DISABLED)){
				drawCard();
				return;
			} else if (playShape.contains(p) && !getStatePlayButton().equals(BUTTON_DISABLED)
					&& selectedCardIndex == -1){
				JOptionPane.showMessageDialog(null, "Select a card!");
				return;
			}
	         
			for (int s = playerHandShapes.size() - 1; s >= 0; s--) {
				if (playerHandShapes.get(s).contains(e.getPoint())) {
					selectedCardIndex = s;
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
