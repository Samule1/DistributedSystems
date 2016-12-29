/*
AUTHOR: HAMPUS CARLSSON
WRITTEN USING NETBEANS IDE 8.2 
VM ARGUMENTS: 
-Djava.security.manager 
-Djava.security.policy=={INSERT FULL PATH TO POLICY}\DisProj\src\disproj\my.policy 


*/

package disproj;
import java.rmi.*;
import java.rmi.server.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ShapeListClient implements WhiteBoardCallback{
    private static final ArrayList<GraphicalObject> listOfGObjs = new ArrayList();
    private static int latestVersion = 0; 
    private static ShapeList shapeListApi = null;
    private static int callbackId; 
    
    public static void main(String args[]){
        
        
        
	if(System.getSecurityManager() == null){
	    System.setSecurityManager(new SecurityManager());
        } else
	    System.out.println("Already has a security manager, so cant set RMI SM");
        try{
	    //Let's always get a copy of the state first.. 
            shapeListApi  = (ShapeList) Naming.lookup("//localhost/ShapeList");
            
            //Synch list with server.
            listOfGObjs.addAll(newObjects(shapeListApi.allShapes(latestVersion), shapeListApi.getVersion()));
            
            
            //Time to sign up to the server!
            WhiteBoardCallback w = new ShapeListClient();
            WhiteBoardCallback stub = (WhiteBoardCallback) UnicastRemoteObject.exportObject(w, 0);
            callbackId = shapeListApi.register(stub);
            
            //Start input reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            startMessage();            
            boolean running = true;
            while(running){
                try{
                    
                    String userinput = reader.readLine();  
                    
                    //Read
                    switch (userinput) {
                        case "R":
                            printObjectList(listOfGObjs);
                            startMessage();
                            break;
                        case "W":
                            shapeMessage();
                            String shapeType = reader.readLine();
                            GraphicalObject g = new GraphicalObject(shapeType);
                            if (shapeListApi.newShape(g) == null)
                                System.out.println("Shape already in list, hint: [R]");
                            break;
                        case "E":
                            //DeRegister!
                            shapeListApi.deregister(callbackId);
                            running = false;
                            break;
                        default:
                            System.out.println("Bad input!");
                            startMessage();
                            break;
                    }
                
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
            System.exit(0);
            

	}catch(RemoteException e) {System.out.println("allShapes: " + e.getMessage());
	}catch(Exception e) {System.out.println("Lookup: " + e.getMessage());}
    }
    
    //Print the start message.
    public static void startMessage(){
        System.out.println("[R]ead, [W]rite, [E]xit:");
    }
    
    public static void shapeMessage(){
        System.out.println("Enter the type of geometric object to write: ");
    }
    
    //Copy the new elemtes and change the version number.
    private static ArrayList<GraphicalObject> newObjects(ArrayList<Shape> ls, int version) throws RemoteException, CloneNotSupportedException{
        latestVersion = version;
        return  ls.stream().map(s -> getGraphicalObjectFromShape(s))
                .collect(Collectors.toCollection(ArrayList::new));
       
 
    }
    
    //Print the list of graphicalobjects
    private static void printObjectList(ArrayList<GraphicalObject> ls){
        for(GraphicalObject g : ls){
            g.print();
        }
       
    }
    
    
    //The callback for the server
    @Override
    public void callback(){
        
        try {
            listOfGObjs.addAll(newObjects(shapeListApi.allShapes(latestVersion), shapeListApi.getVersion()));
            System.out.println("The list has been modified:");
            System.out.println("---------------------------");
            printObjectList(listOfGObjs);
            System.out.println("---------------------------");
            startMessage();
        } catch (RemoteException remoteException) {
        } catch (CloneNotSupportedException cloneNotSupportedException) {
        }
        
    }
    
    //Function used for fetching the graphicalobject from a shape
    private static GraphicalObject getGraphicalObjectFromShape(Shape s){
        try {
            return s.getAllState().Clone();
        } catch (RemoteException ex) {
            return null;
        }
        catch(CloneNotSupportedException ex){
            return null;
        }
    };
}


	             
