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
	private JButton btnDraw;
	private JButton btnPlay;
	private JLabel lblWaiting;
	private JLabel loadCircle;

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
		getContentPane().setMaximumSize(new Dimension(1280, 720));
	    getContentPane().setMinimumSize(new Dimension(1280, 720));
	    getContentPane().setPreferredSize(new Dimension(1280, 720));
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
                    //painter.paintOPCapture(g, a);
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
	    gamePanel.setLayout(null);
	    gamePanel.addMouseListener(new MouseManager());
	    gamePanel.addMouseMotionListener(new MouseManager());
	    gamePanel.setMinimumSize(new Dimension(1280, 720));
	    gamePanel.setMaximumSize(new Dimension(1280, 720));
	    gamePanel.setPreferredSize(new Dimension(1280, 720));
	    gamePanel.setSize(new Dimension(1280, 720));
	    gamePanel.setBounds(0, 0, 1280, 720);
		
		lblThisUser = new JLabel("user");
		lblThisUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblThisUser.setForeground(Color.WHITE);
		lblThisUser.setFont(new Font("Arista", Font.PLAIN, 29));
		lblThisUser.setBounds(1090, 77, 132, 37);
		gamePanel.add(lblThisUser);
		
		lastPlayedCard = new JLabel("");
		lastPlayedCard.setBounds(510, 240, 120, 180);
		lastPlayedCard.setIcon(new ImageIcon(imageLoader.getComp("shadow.png")
				.getScaledInstance(120, 180,Image.SCALE_SMOOTH)));
		gamePanel.add(lastPlayedCard);
		
		btnPlay = new JButton("Play");
		btnPlay.setBounds(580, 664, 61, 29);
		gamePanel.add(btnPlay);
		btnPlay.setEnabled(false);
		
		btnDraw = new JButton("Draw");
		btnDraw.setBounds(647, 664, 69, 29);
		gamePanel.add(btnDraw);
		btnDraw.setEnabled(false);		
		
		btnDraw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				drawCard();
			}
		});
		btnPlay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				playCard();
			}

		});
		
		this.stateDrawButton = BUTTON_DISABLED;
		this.statePlayButton = BUTTON_DISABLED;
		
	    emptyPlayerCardShape = new RoundRectangle2D.Float(401, 450, 120, 180, 7, 7);
	    playShape = new Ellipse2D.Float(530, 659, 105, 43);
	    drawShape = new Ellipse2D.Float(675, 659, 105, 43);
		painter.captureDeck(108);
		painter.capturePlayButton(BUTTON_DISABLED);
		painter.captureDrawButton(BUTTON_DISABLED);
		setupWaiting();
	    repaint();
	    
	}

	@Override
	public void setup(String text) {
		lblThisUser.setText(text);
		btnPlay.setVisible(false);
		btnDraw.setVisible(false);
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
			this.btnPlay.setEnabled(true);
			setStatePlayButton(BUTTON_ENABLED);
			painter.capturePlayButton(BUTTON_ENABLED);
		}else{
			logger.log(Level.INFO, "5.1 - at least 1 NOT ");
			this.btnDraw.setEnabled(true);
			painter.captureDrawButton(BUTTON_ENABLED);
			setStateDrawButton(BUTTON_ENABLED);
		}
		logger.log(Level.INFO, "Finished in playMyTurn GUI");
		repaint();
	}

	@Override
	public void updateGameField() {		
		this.lblWaiting.setVisible(false);
		this.loadCircle.setVisible(false);
		
		painter.clearImages();
	    painter.captureDeck(gameManager.getGame().getDeck().getCardList().size());
		//painter.captureOtherPlayerCard(a);
	    
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
		btnPlay.setEnabled(false);
		btnDraw.setEnabled(false);
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
		lblWaiting.setFont(new Font("Arista", Font.PLAIN, 28));
		lblWaiting.setHorizontalAlignment(SwingConstants.LEFT);
		lblWaiting.setBounds(80, 28, 479, 37);
		gamePanel.add(lblWaiting);		
		loadCircle = new JLabel();
		loadCircle.setBounds(20, 10, 55, 55);
		loadCircle.setIcon(loading);
		loadCircle.setVisible(true);
		gamePanel.add(loadCircle);
	}
	
	public void setPlayerShapes(){
		List<UnoCard> cards = gameManager.getMyCards();
		int handsize = cards.size();
	    playerHandShapes.clear();
	    for (int i = 0; i < handsize; i++) {
	        playerHandShapes.add(a.createTransformedShape(emptyPlayerCardShape));
	        a.translate(62, 0);
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
		public void mouseEntered(MouseEvent e){
			
		}

		@Override
		public void mousePressed(MouseEvent e){
			Point p = e.getPoint();
			if (playShape.contains(p) && getStatePlayButton().equals(BUTTON_ENABLED)
					&& selectedCardIndex != -1) {
				playCard();
				return;
			} else if (drawShape.contains(p) && getStateDrawButton().equals(BUTTON_ENABLED)){
				drawCard();
				return;
			} else if (playShape.contains(p) && getStatePlayButton().equals(BUTTON_ENABLED)
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
