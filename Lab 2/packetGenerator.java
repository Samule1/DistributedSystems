public class packetGenerator{
  private int nextPackId = 1;
  public SimplePacket getNextPacket(){
    return new SimplePacket(nextPackId++, "");
  }
}
