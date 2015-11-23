package rmiSourceCode.client;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import rmiSourceCode.compute.Compute;

public class ComputePi {
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
            
            System.out.println("Insert number for PI:");
            int number = scan.nextInt();
            
            scan.close();
        	
            Registry registry = LocateRegistry.getRegistry(host);
            Compute comp = (Compute) registry.lookup(name);
            Pi task = new Pi(number);
            BigDecimal pi = comp.executeTask(task);
            System.out.println(pi);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }    
}