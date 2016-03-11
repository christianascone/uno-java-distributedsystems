package sistemidistribuiti.uno.view.frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

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

public class MainWindow extends JFrame implements GameGUIListener{

	private final static Logger logger = Logger.getLogger(MainWindow.class.getName());
	
	private static final int DIRECTION_PREVIOUS = -1;
	private static final int DIRECTION_NEXT = 1;
	/**
	 * 
	 */
	private static final long serialVersionUID = -7507193594059818919L;
	private JPanel contentPane;
	
	private JLabel lblThisUser;
	JButton btnPlay;
	
	private GameManager gameManager;
	private int currentCardIndex;
	
	private JPanel northPanel;
	private JPanel southPanel;
	private JPanel westPanel;
	private JPanel eastPanel;
	private JPanel centerPanel;
	private JPanel cardPanel;
	private JPanel panel;
	private JButton previousCardBtn;
	private JLabel numberLabel;
	private JLabel colorLabel;
	private JButton nextCardBtn;
	private JPanel panel_1;
	private JPanel lastPlayedPanel;
	private JLabel lastPlayedNumber;
	private JLabel lastPlayedColor;
	private JPanel panel_2;
	private JLabel labelCardCounter;
	private JButton btnDraw;

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
	public MainWindow() {
		setResizable(false);
		setTitle("Uno");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 593, 571);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		northPanel = new JPanel();
		contentPane.add(northPanel, BorderLayout.NORTH);
		
		westPanel = new JPanel();
		contentPane.add(westPanel, BorderLayout.WEST);
		
		centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		lastPlayedPanel = new JPanel();
		FlowLayout fl_lastPlayedPanel = (FlowLayout) lastPlayedPanel.getLayout();
		centerPanel.add(lastPlayedPanel);
		
		lastPlayedNumber = new JLabel("Number");
		lastPlayedPanel.add(lastPlayedNumber);
		
		lastPlayedColor = new JLabel("Color");
		lastPlayedPanel.add(lastPlayedColor);
		
		eastPanel = new JPanel();
		contentPane.add(eastPanel, BorderLayout.EAST);
		
		southPanel = new JPanel();
		contentPane.add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new GridLayout(0, 1, 2, 0));
		cardPanel = new JPanel();
		
		//imagePanel.setVerticalAlignment(SwingConstants.TOP);
		southPanel.add(cardPanel);
		
		previousCardBtn = new JButton("Previous");
		previousCardBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				previousCard();
			}
		});
		cardPanel.add(previousCardBtn);
		
		numberLabel = new JLabel("Number");
		cardPanel.add(numberLabel);
		
		colorLabel = new JLabel("Color");
		cardPanel.add(colorLabel);
		
		nextCardBtn = new JButton("Next");
		nextCardBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				nextCard();
			}
		});
		cardPanel.add(nextCardBtn);
		
		panel_2 = new JPanel();
		southPanel.add(panel_2);
		
		labelCardCounter = new JLabel("curr/total");
		labelCardCounter.setHorizontalAlignment(SwingConstants.CENTER);
		labelCardCounter.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		panel_2.add(labelCardCounter);
		
		panel_1 = new JPanel();
		southPanel.add(panel_1);
		
		btnPlay = new JButton("Play");
		panel_1.add(btnPlay);
		btnPlay.setEnabled(false);
		
		btnDraw = new JButton("Draw");
		btnDraw.setEnabled(false);
		panel_1.add(btnDraw);
		btnPlay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				playCard();
			}

		});
		
		btnDraw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				drawCard();
			}
		});
		
		panel = new JPanel();
		southPanel.add(panel);
		
		JLabel lblUsernameTitle = new JLabel("Username:");
		panel.add(lblUsernameTitle);
		
		lblThisUser = new JLabel("user1");
		panel.add(lblThisUser);
	}

	@Override
	public void setLabelText(String text) {
		lblThisUser.setText(text);
	}

	@Override
	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	@Override
	public void playMyTurn() {
		List<UnoCard> cards = gameManager.getMyCards();
		UnoCard showed = cards.get(currentCardIndex);
		setupCardView(showed);


		if(atLeastOneCardPlayable()){
			logger.log(Level.INFO, "5 - at least 1");
			this.btnPlay.setEnabled(true);
		}else{
			logger.log(Level.INFO, "5.1 - at least 1 NOT ");
			this.btnDraw.setEnabled(true);
		}
		logger.log(Level.INFO, "Finished in playMyTurn GUI");
	}

	@Override
	public void updateGameField() {
		setupLastPlayedCardView();	
	}
	
	private void nextCard(){
		changeCard(DIRECTION_NEXT);
	}
	
	private void previousCard(){
		changeCard(DIRECTION_PREVIOUS);
	}
	
	/**
	 * Change the current card with given parameter for direction
	 * @param direction
	 */
	private void changeCard(int direction){
		List<UnoCard> cards = gameManager.getMyCards();
		currentCardIndex = (currentCardIndex + direction) % cards.size();
		if(currentCardIndex < 0){
			currentCardIndex = cards.size() - 1;
		}
		
		setupCardView(cards.get(currentCardIndex));
	}
	
	/**
	 * Setup how the card is showed
	 * 
	 * @param showed
	 */
	private void setupCardView(UnoCard showed) {
		this.labelCardCounter.setText(String.format("%d/%d", currentCardIndex+1, gameManager.getMyCards().size()));
		
		switch (showed.getCardType()) {
		case NUMBER_CARD:
			NumberCard numberCard = (NumberCard) showed;
			this.numberLabel.setText(numberCard.getNumber()+"");
			this.colorLabel.setText(numberCard.getColor().toString());
			break;
		case SPECIAL_CARD:
			SpecialCard specialCard = (SpecialCard) showed;
			this.numberLabel.setText(specialCard.getSpecialCardType().toString());
			this.colorLabel.setText(specialCard.getColor().toString());
			break;
		}
	}
	
	/**
	 * Setup the view for the last played card
	 */
	private void setupLastPlayedCardView() {
		UnoCard showed = gameManager.getLastPlayedCard();
		switch (showed.getCardType()) {
		case NUMBER_CARD:
			NumberCard numberCard = (NumberCard) showed;
			this.lastPlayedNumber.setText(numberCard.getNumber()+"");
			this.lastPlayedColor.setText(numberCard.getColor().toString());
			break;
		case SPECIAL_CARD:
			SpecialCard specialCard = (SpecialCard) showed;
			this.lastPlayedNumber.setText(specialCard.getSpecialCardType().toString());
			this.lastPlayedColor.setText(specialCard.getColor().toString());
			break;
		}
	}
	
	/**
	 * Play the card
	 */
	private void playCard() {
		if(!currentCardIsPlayable(currentCardIndex)){
			JOptionPane.showMessageDialog(null, "You cannot play this card.");
			return;
		}
		
		List<UnoCard> myCards = gameManager.getMyCards();
		UnoCard showed = myCards.get(currentCardIndex);
		myCards.remove(showed);
		gameManager.discardCard(showed);
		
		if(myCards.isEmpty()){
			gameManager.winGame();
		}
		
		manageCard(showed);
		
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
		
		setupLastPlayedCardView();
		
		currentCardIndex = 0;
		
		if(!cards.isEmpty()){
			setupCardView(cards.get(0));
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
			if(toPlay.getColor() == CARD_COLOR.RAINBOW){
				logger.log(Level.INFO, "6.0.8.1 playable ? ");
				return true;
			}
			logger.log(Level.INFO, "6.0.8.2 playable ? ");
			SpecialCard toPlaySpecialCard = (SpecialCard) toPlay;
			logger.log(Level.INFO, "6.0.8.3 playable ? ");
			SpecialCard lastPlayedSpecialCard = (SpecialCard) lastPlayed;
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


}
