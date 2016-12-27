/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package disproj;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 *
 * @author Hampus
 */
public class Callback implements WhiteBoardCallback, Serializable{

    @Override
    public void callback() throws RemoteException {
        System.out.println("Hi, from client");
    }
    
}
