package sistemidistribuiti.uno.view.listener;

import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.workflow.GameManager;

public interface GameGUIListener {
	/**
	 * Set the label text
	 * @param text
	 */
	void setup(String text);
	
	void setGameManager(GameManager gameManager);

	void playMyTurn();

	void updateGameField();
	
	void showWinnerAlert(Player player);
}
