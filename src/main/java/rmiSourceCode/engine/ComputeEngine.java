package rmiSourceCode.engine;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rmiSourceCode.compute.Compute;
import rmiSourceCode.compute.Task;

public class ComputeEngine implements Compute {

    public ComputeEngine() {
        super();	
    }

    public <T> T executeTask(Task<T> t) {
        return t.execute();
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        System.setProperty("java.security.policy","/Library/Java/JavaVirtualMachines/jdk1.8.0_20.jdk/Contents/Home/jre/lib/security/java.policy");

        try {
            String name = "Compute";
            Compute engine = new ComputeEngine();
            LocateRegistry.createRegistry(1099);
            Compute stub = (Compute) UnicastRemoteObject.exportObject(engine, 1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }
}