package distributedsystems.uno.view.listener;

import distributedsystems.uno.model.player.Player;
import distributedsystems.uno.workflow.GameManager;

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
	
	void refreshUILazyUser();

}
