package sistemidistribuiti.uno.view.frame;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sistemidistribuiti.uno.view.listener.GameGUIListener;
import sistemidistribuiti.uno.workflow.GameManager;
import sistemidistribuiti.uno.workflow.Starter;

import javax.swing.JButton;

public class MainWindow extends JFrame implements GameGUIListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7507193594059818919L;
	private JPanel contentPane;
	
	private JLabel lblUser;
	JButton btnPlay;
	
	private GameManager gameManager;

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
		setTitle("Uno");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsernameTitle = new JLabel("Username:");
		lblUsernameTitle.setBounds(6, 22, 66, 16);
		contentPane.add(lblUsernameTitle);
		
		lblUser = new JLabel("user1");
		lblUser.setBounds(84, 22, 360, 16);
		contentPane.add(lblUser);
		
		btnPlay = new JButton("Play");
		btnPlay.setBounds(199, 167, 117, 29);
		btnPlay.setEnabled(false);
		btnPlay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btnPlay.setEnabled(false);
				gameManager.playMyTurn();
			}
		});
		contentPane.add(btnPlay);
	}

	@Override
	public void setLabelText(String text) {
		lblUser.setText(text);
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
