
/*
 A project of the Finite Element analysis on truss system.
 It is free to use this file and associated files
 for the educational use only as long as this block exist
 unchanged. For the commercial use, 
 contact with auther/developer of this project.
 Developer: Win Aung Cho, winaungcho@gmail.com
 StructSoftLab.com
 15-December, 2019
 LICENSE:  https://github.com/winaungcho/FEMTruss/blob/master/LICENSE
 */
package dobj;
import java.io.*;
import math.*;
import com.structsoftlab.edu.femtruss.*;

public class GPoint implements java.io.Serializable {
    static final long serialVersionUID = 4L;
    static final float PI = (float)Math.PI;
    static final float SNAPTOL = 25.5f;
    static float Y0=800;

    protected float x, y, z;
    public GPoint(float a, float b, float c)
	{
		x=a;y=b;z=c;
	}
    public GPoint(GPoint g){x=g.x;y=g.y;z=g.z;}
    public float getX(){return x;}
    public float getY(){return y;}
	public float getZ(){return z;}
    public void setXYZ(GPoint g){x=g.x;y=g.y;z=g.z;}
	public boolean IsSameLoc(GPoint p)
	{
		if ((Math.abs(p.z-z) < Geom.EPSILON) &&
		    (Math.abs(p.y-y) < Geom.EPSILON) &&
			(Math.abs(p.x-x) < Geom.EPSILON))
			return true;
		return false;
	}
    
    public void Modify(float dx, float dy, float dz){
        MoveTo(dx+x, dy+y, dz+z);
    }
    
    public int Move(float dx, float dy, float dz)
    {
        x += dx; y += dy; z += dz;
        return 0;
    }
    public int MoveTo(float a, float b, float c)
    {
        x=a;y=b;z=c;
        return 0;
    }
	public float Dist(float x1, float y1){
        float dx=x1-x, dy=y1-y;
        float l=(float)Math.sqrt(dx*dx+dy*dy);
        return l;
    }
	public float Angle(float dx, float dy, int clockwise){
        //Clockwise

        float sang;
        if (dx != 0){
            sang = (float)Math.atan(-clockwise*dy/dx)*180.0f/PI;
            if (dx < 0)
                sang = 180+sang;
        }
        else
            sang = -clockwise*dy < 0f ? 270.0f : 90f;
        if (sang < 0)
            sang += 360.0;
        return sang;
    }
    public void Rotate(float xc, float yc, float angdeg){
        float dx = x-xc, dy = y-yc;
        float angrad = angdeg*PI/180.0f;
        float c = (float)Math.cos(angrad);
        float s = (float)Math.sin(angrad);
        // counter clockwise
        x = xc+dx*c-dy*s;
        y = yc+dx*s+dy*c;

        // clockwise
        /*
        x = xc+dx* + dy*s;
        y = yc-dx*s+dy*c;
        */
    }
    
    
    public void appendpoint(GPoint gp){}
    public void undopoint(GPoint gp){}
    public long CheckVer()
    {
        return serialVersionUID;
    }
    public void writeObject(ObjectOutputStream out) throws IOException {
        out.writeFloat(x);out.writeFloat(y);out.writeFloat(z);
    }
    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        x=in.readFloat();y=in.readFloat();z=in.readFloat();
    }
}
