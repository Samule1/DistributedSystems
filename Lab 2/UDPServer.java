import java.net.*;
import java.io.*;

public class UDPServer{
  private static int previousId = 0;
  private static int lostpackets = 0;
  public static void main(String args[]){
    	DatagramSocket aSocket = null;

	try{
	    aSocket = new DatagramSocket(6789);
	    // create socket at agreed port

	    byte[] buffer = new byte[1000];

	    while(true){
    		DatagramPacket request = new DatagramPacket(buffer, buffer.length);
    		aSocket.receive(request);

    		/*DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(),
    							  request.getAddress(), request.getPort()); */
        //Parsing into object
        ByteArrayInputStream bis = new ByteArrayInputStream(request.getData());
        ObjectInput in = null;
        SimplePacket o = null;

        try {
          in = new ObjectInputStream(bis);
          o = (SimplePacket)in.readObject();

        }catch(Exception e){
          System.out.println("fucked");
        }finally {
          try {
            if (in != null) {
              in.close();
            }
          }catch (IOException ex) {
            // ignore close exception
          }
        }
        
        if((o.id - 1) != previousId)
            lostpackets++;
        previousId = o.id;
        if(o.endOfStream){
          System.out.println("END OF STREAM. lostpackets: "+ lostpackets);
          lostpackets = 0;
          previousId = 0;
        }

    		//aSocket.send(reply);
	    }
	}
	catch (SocketException e){
	    System.out.println("Socket: " + e.getMessage());
	}
	catch (IOException e) {
	    System.out.println("IO: " + e.getMessage());
	}
	finally {
	    if(aSocket != null) aSocket.close();
	}
    }
}
