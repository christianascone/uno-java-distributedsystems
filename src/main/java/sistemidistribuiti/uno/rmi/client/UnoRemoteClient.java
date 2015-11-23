package sistemidistribuiti.uno.rmi.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.Deck;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;
import sistemidistribuiti.uno.utils.DeckHelper;

/**
 * Remote client.
 * Currently it is just an example caller
 * 
 * @author christian
 *
 */
public class UnoRemoteClient {
	public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
        	Scanner scan = new Scanner(System.in);
            
            System.out.println("Insert server name:");
            String name = scan.nextLine();
            
            System.out.println("Insert route host:");
            String host = scan.nextLine();
            
            scan.close();
        	
            Registry registry = LocateRegistry.getRegistry(host);
            UnoRemoteGameInterface comp = (UnoRemoteGameInterface) registry.lookup(name);
            
            Deck deck = new Deck();
            List<UnoCard> cards = DeckHelper.getDeck();
            deck.setCardList(cards);
            
            comp.sendDeck(deck);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }    
}
