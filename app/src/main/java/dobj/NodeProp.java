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
import android.graphics.*;
import java.io.*;
import math.*;

public class NodeProp extends ObjProp
{
	
	public int res; // 1 2 4 8 16 32 dx dy dz rx ry rz
	public float F[]=new float[6];
	public float Disp[]=new float[6];
	public float Sk[]=new float[6];
	public NodeProp(){
		int i;
		for(i=0;i<6;i++){
			F[i]=0;
			Disp[i]=0;
			Sk[i]=0;
		}
	}
	public NodeProp(float a, float b, float c){
        int i;
		for(i=0;i<6;i++){
			F[i]=0;
			Disp[i]=0;
			Sk[i]=0;
		}
		F[0]=a;F[1]=b;F[2]=c;
    }
	public void SetProp(float[] p, int op)
	{
		int i;
		switch(op){
			case 1:
				for(i=0;i<3;i++){
					F[i]=p[i];
				}
				break;
			case 2:
				for(i=0;i<6;i++){
					F[i]=p[i];
				}
				break;
			case 3:
				for(i=0;i<3;i++){
					Disp[i]=p[i];
				}
				break;
			case 4:
				for(i=0;i<6;i++){
					Disp[i]=p[i];
				}
				break;
			case 5:
				res = (int)p[0];
				break;
			case 6:
				for(i=0;i<3;i++){
					Sk[i]=p[i];
				}
				break;
			case 7:
				for(i=0;i<6;i++){
					Sk[i]=p[i];
				}
				break;
		}
	}

	public void writeObject(ObjectOutputStream out) throws IOException {
        int i;
		out.writeInt(res);
		for(i=0;i<6;i++){
			out.writeFloat(F[i]);
		}
		for(i=0;i<6;i++){
			out.writeFloat(Disp[i]);
		}
    }
    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        int i;
		res=in.readInt();
		for(i=0;i<6;i++){
			F[i]=in.readFloat();
		}
		for(i=0;i<6;i++){
			Disp[i]=in.readFloat();
		}
    }
}
