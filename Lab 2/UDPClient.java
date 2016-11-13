import java.net.*;
import java.io.*;

public class UDPClient{
    private static packetGenerator pg = new packetGenerator();
    private  static int serverPort = 6789;
    public static void main(String args[]){
    	// args give message contents and destination hostname
    	DatagramSocket aSocket = null;


    	try {
    	    aSocket = new DatagramSocket();
          byte[] m = null;
          InetAddress aHost = InetAddress.getByName(args[1]);
    	    //byte [] m = args[0].getBytes();
          //byte[] m = bos.toByteArray();

          int nPackets = Integer.parseInt(args[0]);
          System.out.println("Sending: "+nPackets);
          for(int i = 0; i<nPackets; i++){
            m = nextPacketAsByteArray((i == nPackets-1));
      	    DatagramPacket request = new DatagramPacket(m,  m.length, aHost, serverPort);
      	    aSocket.send(request);
          }


    	}
    	catch (SocketException e){
    	    System.out.println("Socket: " + e.getMessage());
    	}
    	catch (IOException e){
    	    System.out.println("IO: " + e.getMessage());
    	}
    	finally {
    	    if(aSocket != null) aSocket.close();
    	}
  }
  public static byte[] nextPacketAsByteArray(boolean endOfStream){
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = null;
    try{
      out = new ObjectOutputStream(bos);
      SimplePacket packet = pg.getNextPacket();
      
      packet.endOfStream = endOfStream;
      out.writeObject(packet);
      out.flush();

    }catch(Exception e){

    }finally {
      try {
        bos.close();
      } catch (IOException ex) {
        // ignore close exception
      }
      return bos.toByteArray();
    }
  }
}
