package sistemidistribuiti.uno.view.frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
	private JPanel northPanel;
	private JPanel southPanel;
	private JPanel westPanel;
	private JPanel eastPanel;
	private JPanel centerPanel;

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
		setBounds(100, 100, 535, 474);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		northPanel = new JPanel();
		contentPane.add(northPanel, BorderLayout.NORTH);
		
		southPanel = new JPanel();
		contentPane.add(southPanel, BorderLayout.SOUTH);
		
		JLabel lblUsernameTitle = new JLabel("Username:");
		southPanel.add(lblUsernameTitle);
		
		lblThisUser = new JLabel("user1");
		southPanel.add(lblThisUser);
		
		westPanel = new JPanel();
		contentPane.add(westPanel, BorderLayout.WEST);
		
		eastPanel = new JPanel();
		contentPane.add(eastPanel, BorderLayout.EAST);
		
		centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(null);
		
		btnPlay = new JButton("Play");
		btnPlay.setBounds(215, 164, 75, 29);
		centerPanel.add(btnPlay);
		btnPlay.setEnabled(false);
		btnPlay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btnPlay.setEnabled(false);
				gameManager.playMyTurn();
			}
		});
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
	}
}
