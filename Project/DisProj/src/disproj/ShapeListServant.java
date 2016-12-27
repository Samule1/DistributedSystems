package disproj;

import java.rmi.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ShapeListServant implements ShapeList{
    private ArrayList<Shape> theList;
    private int version;
    private ArrayList<WhiteBoardCallback> connections; 
    private HashMap<Integer, WhiteBoardCallback> serverConnections; 
    private int nextCallbackId = 1; 
    
    public ShapeListServant()throws RemoteException{
        theList = new ArrayList();
        version = 0;
        connections = new ArrayList<>(); 
        serverConnections = new HashMap();
        
    }

    public Shape newShape(GraphicalObject g) throws RemoteException{
	version++;
       	Shape s = new ShapeServant( g, version);
        theList.add(s);
        serverConnections.forEach((id, wbc) -> {
            try {
                wbc.callback();
            } catch (RemoteException ex) {
                serverConnections.remove(id);
                System.out.println("Removed with grace");
            }
        });
        return s;
    }

    public  ArrayList<Shape> allShapes()throws RemoteException{
        return theList;
    }

    public int getVersion() throws RemoteException{
        return version;
    } 

    @Override
    public void sayHi() throws RemoteException {
        System.out.println("Hi");
    }

    @Override
    public int register(WhiteBoardCallback wbc) {
        int callbackId = this.nextCallbackId++; 
        serverConnections.put(callbackId, wbc);
        System.out.println("Number of connections: " + serverConnections.size());
        
        return callbackId;
    }

    @Override
    public void deregister(int callbackId) throws RemoteException {
        serverConnections.remove(callbackId);
        System.out.println("Number of connections: " + serverConnections.size());
    }
}
