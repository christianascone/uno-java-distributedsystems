package sistemidistribuiti.uno.view.frame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sistemidistribuiti.uno.view.listener.GameGUIListener;
import sistemidistribuiti.uno.workflow.GameManager;
import sistemidistribuiti.uno.workflow.Starter;

public class MainWindows extends JFrame implements GameGUIListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7507193594059818919L;
	private JPanel contentPane;
	
	private JLabel lblUser;
	
	private GameManager gameManager;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindows frame = new MainWindows();
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
	public MainWindows() {
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
	}

	@Override
	public void setLabelText(String text) {
		lblUser.setText(text);
	}

	@Override
	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
