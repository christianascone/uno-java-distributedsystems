package rmiSourceCode.engine;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import rmiSourceCode.compute.Compute;
import rmiSourceCode.compute.Task;

public class ComputeEngine implements Compute {

    public ComputeEngine() {
        super();	
    }

    public <T> T executeTask(Task<T> t) {
    	System.out.println("Called");
        return t.execute();
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        URL url = ComputeEngine.class.getClassLoader().getResource("java.policy");
        
        System.setProperty("java.security.policy",url.getPath());

        try {
            Compute engine = new ComputeEngine();
            
            Scanner scan = new Scanner(System.in);
            
            System.out.println("Insert server name:");
            String name = scan.nextLine();
            
            System.out.println("Insert server port:");
            int port = scan.nextInt();
            scan.close();
            
            LocateRegistry.createRegistry(port);
            Compute stub = (Compute) UnicastRemoteObject.exportObject(engine, port);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }
}