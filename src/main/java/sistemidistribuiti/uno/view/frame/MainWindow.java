package sistemidistribuiti.uno.view.frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.NumberCard;
import sistemidistribuiti.uno.model.card.impl.SpecialCard;
import sistemidistribuiti.uno.view.listener.GameGUIListener;
import sistemidistribuiti.uno.workflow.GameManager;
import sistemidistribuiti.uno.workflow.Starter;

public class MainWindow extends JFrame implements GameGUIListener{

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {		
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
		centerPanel.setLayout(null);
		
		eastPanel = new JPanel();
		contentPane.add(eastPanel, BorderLayout.EAST);
		
		southPanel = new JPanel();
		contentPane.add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new GridLayout(0, 1, 2, 0));
		cardPanel = new JPanel();
		
		//imagePanel.setVerticalAlignment(SwingConstants.TOP);
		southPanel.add(cardPanel);
		
		previousCardBtn = new JButton("Previous");
		cardPanel.add(previousCardBtn);
		
		numberLabel = new JLabel("Number");
		cardPanel.add(numberLabel);
		
		colorLabel = new JLabel("Color");
		cardPanel.add(colorLabel);
		
		nextCardBtn = new JButton("Next");
		cardPanel.add(nextCardBtn);
		
		panel_1 = new JPanel();
		southPanel.add(panel_1);
		
		btnPlay = new JButton("Play");
		panel_1.add(btnPlay);
		btnPlay.setEnabled(false);
		btnPlay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				playCard();
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
		this.btnPlay.setEnabled(true);
		List<UnoCard> cards = gameManager.getCurrentPlayerCards();
		
		UnoCard showed = cards.get(currentCardIndex);
		setupCardView(showed);
	}

	/**
	 * Setup how the card is showed
	 * 
	 * @param showed
	 */
	private void setupCardView(UnoCard showed) {
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
	 * Play the card
	 */
	private void playCard() {
		btnPlay.setEnabled(false);
		
		List<UnoCard> cards = gameManager.getCurrentPlayerCards();
		UnoCard showed = cards.get(currentCardIndex);
		cards.remove(showed);
		
		gameManager.discardCard(showed);
		
		setupCardView(cards.get(0));
		gameManager.playMyTurn();
	}
}
