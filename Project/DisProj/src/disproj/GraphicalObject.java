package disproj;

import java.awt.Rectangle;
import java.awt.Color;
import java.io.Serializable;

public class GraphicalObject implements Serializable, Cloneable{
    public String type;

    
    //	constructors
    public GraphicalObject() { }
    
    public GraphicalObject(String aType) {
		type = aType;

    }
    
    public void print(){
		System.out.println(type);
		
    }
    
    public GraphicalObject Clone() throws CloneNotSupportedException{
        return (GraphicalObject) super.clone(); 
    }
}
