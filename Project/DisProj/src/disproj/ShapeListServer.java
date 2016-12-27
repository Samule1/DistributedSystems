/*
AUTHOR: HAMPUS CARLSSON
WRITTEN USING NETBEANS IDE 8.2 
VM ARGUMENTS: 
-Djava.security.manager 
-Djava.security.policy=={INSERT FULL PATH TO POLICY}\DisProj\src\disproj\my.policy 


*/

package disproj;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

public class ShapeListServer {
    public static final int DEAFAULT_PORT = 1099;
    public static void main(String args[]){
	    
	System.setSecurityManager(new SecurityManager());
       
        
	 
        try{
            ShapeList aShapeList = new ShapeListServant();
            
            
            /*if(LocateRegistry.getRegistry() == null){
                LocateRegistry.createRegistry(1099);
            }*/
            LocateRegistry.createRegistry(DEAFAULT_PORT);
            ShapeList stub = (ShapeList) UnicastRemoteObject.exportObject(aShapeList,0);
            
            
	    Naming.rebind("ShapeList", aShapeList);
            
			
            System.out.println("ShapeList server ready");
        }catch(Exception e) {
            System.out.println("ShapeList server main " + e.getMessage());
        }
    }
    
}
