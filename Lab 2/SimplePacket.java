import java.io.Serializable;
public class SimplePacket implements Serializable {
  public int id;
  public String message;
  public boolean endOfStream = false;
  public SimplePacket(int id, String message){
    this.id = id;
    this.message = message;
  }
}
