package disproj;
import java.rmi.*;
import java.util.ArrayList;
import java.util.Vector;

public interface ShapeList extends Remote {
    Shape newShape(GraphicalObject g) throws RemoteException;  	    
    ArrayList<Shape> allShapes(int version)throws RemoteException;
    int getVersion() throws RemoteException;
    int register(WhiteBoardCallback wbc) throws RemoteException;
    void deregister(int callbackId) throws RemoteException;
}
