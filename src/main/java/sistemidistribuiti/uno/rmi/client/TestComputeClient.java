package sistemidistribuiti.uno.rmi.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import sistemidistribuiti.uno.rmi.interfaces.TestRemoteInterface;


public class TestComputeClient {
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
            
            System.out.println("Insert number 1:");
            int number1 = scan.nextInt();
            
            System.out.println("Insert number 2:");
            int number2 = scan.nextInt();
            
            scan.close();
        	
            Registry registry = LocateRegistry.getRegistry(host);
            TestRemoteInterface comp = (TestRemoteInterface) registry.lookup(name);
            
            int result = comp.multiply(number1, number2);
            System.out.println(result);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }    
}
