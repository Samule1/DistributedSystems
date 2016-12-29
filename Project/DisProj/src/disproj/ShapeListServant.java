package disproj;

import java.rmi.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ShapeListServant implements ShapeList{
    private ArrayList<Shape> theList;
    private int version;
    private ArrayList<WhiteBoardCallback> connections; 
    private HashMap<Integer, WhiteBoardCallback> serverConnections; 
    private HashMap<String, Boolean> shapeLookUp;
    private int nextCallbackId = 1; 
    
    public ShapeListServant()throws RemoteException{
        theList = new ArrayList();
        version = 0;
        connections = new ArrayList<>(); 
        serverConnections = new HashMap();
        shapeLookUp = new HashMap(); 
        
    }
    //Adds new shape to the list, if it does not already exist.
    public Shape newShape(GraphicalObject g) throws RemoteException{
         
        if(!shapeLookUp.containsKey(g.type)){
            
            version++;

            //Add new shape 
            Shape s = new ShapeServant(g, version);
            theList.add(s);

            //Add to look up;
            shapeLookUp.put(g.type, Boolean.TRUE);
            
            //For deleting after iteration. 
            ArrayList<Integer> clientIds = new ArrayList();
            //Notify all clients
            serverConnections.forEach((id, wbc) -> {
                try {
                    wbc.callback();
                } catch (RemoteException ex) {
                    //serverConnections.remove(id);
                    clientIds.add(id);
                    System.out.println("Dropped inactive client from server");
                }
            });
            
            //Delete the inactive callbacks. 
            clientIds.forEach(id -> serverConnections.remove(id));
            
            //If we deleted, display the remaining connectios.
            if(clientIds.size() > 0)
                System.out.println("Number of connections: "+serverConnections.size());
            
            return s;
            
        }
        else{
            System.out.println("Shape Exists!");
            return null;
        }     
        
    }
    //Returns all the NEW entries to the shapelist using the version of the client's list.
    public  ArrayList<Shape> allShapes(int version)throws RemoteException{
        return theList.stream().filter(s -> filterOnVersion(s, version))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    //Returns the version of the list. 
    public int getVersion() throws RemoteException{
        return version;
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
    
    private boolean filterOnVersion(Shape s, int v){
        try {
            return s.getVersion() > v;
        } catch (RemoteException ex) {
            return false; 
        }
    }
}
