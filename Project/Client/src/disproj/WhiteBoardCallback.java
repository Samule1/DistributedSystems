/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package disproj;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Hampus
 */
public interface WhiteBoardCallback extends Remote {
    public void callback() throws RemoteException; 
}
