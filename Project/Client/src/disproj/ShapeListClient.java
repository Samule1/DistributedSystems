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
import java.io.InputStreamReader;
import java.util.ArrayList;


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

            copyGraphicalObjects(shapeListApi.allShapes(), shapeListApi.getVersion());
            
            //Time to sign up to the server!
            WhiteBoardCallback w = new ShapeListClient();
            WhiteBoardCallback stub = (WhiteBoardCallback) UnicastRemoteObject.exportObject(w, 0);
            callbackId = shapeListApi.register(stub);
            
            //Start input reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        
            boolean running = true;
            while(running){
                try{
                    startMessage();
                    String userinput = reader.readLine();  
                    
                    //Read
                    if(userinput.equals("R")){
                      
                      printObjectList(listOfGObjs);
                    }
                    
                    //Write
                    else if(userinput.equals("W")){
                      String shapeType = reader.readLine();
                      GraphicalObject g = new GraphicalObject(shapeType);
                      shapeListApi.newShape(g);
                    }
                    
                    //Exit
                    else if(userinput.equals("E")){
                      //DeRegister!
                      shapeListApi.deregister(callbackId);
                      running = false; 
                    }
                    
                    //Handle outliers
                    else{
                      System.out.println("Bad input!");
                    }
                
                }catch(Exception e){

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
    
    //Copy the new elemtes and change the version number.
    private static void copyGraphicalObjects(ArrayList<Shape> ls, int version) throws RemoteException, CloneNotSupportedException{
        
        if (latestVersion != version) {
            for (int i = listOfGObjs.size(); i < ls.size(); i++) {
                listOfGObjs.add(i, ls.get(i).getAllState().Clone());
            }
            
            latestVersion = version;
        }
        
 
    }
    private static void printObjectList(ArrayList<GraphicalObject> ls){
        for(GraphicalObject g : ls){
            g.print();
        }
    }

    @Override
    public void callback(){
        
        try {
            copyGraphicalObjects(shapeListApi.allShapes(), shapeListApi.getVersion());
            System.out.println("The list has been modified:");
            System.out.println("---------------------------");
            printObjectList(listOfGObjs);
            System.out.println("---------------------------");
        } catch (RemoteException remoteException) {
        } catch (CloneNotSupportedException cloneNotSupportedException) {
        }
        
    }
}


	             
