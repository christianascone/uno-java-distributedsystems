package sistemidistribuiti.uno.view.listener;

import sistemidistribuiti.uno.workflow.GameManager;

public interface GameGUIListener {
	/**
	 * Set the label text
	 * @param text
	 */
	void setLabelText(String text);
	
	void setGameManager(GameManager gameManager);

	void playMyTurn();

	void updateGameField();
}