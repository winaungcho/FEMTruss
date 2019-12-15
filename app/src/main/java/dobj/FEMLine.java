
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

public class FEMLine extends FEMNode
{
	LineProp _lp=null;
    GPoint n2;
	public FEMLine(){
        super(0f,0f,0f);
        n2 = new GPoint(0f,0f,0f);
    }
	public FEMLine(float a, float b, float c){
        super(a,b, c);
    }
	public void setOrdinate(float x1, float y1, float z1, float x2, float y2, float z2){
		super.MoveTo(x1,y1,z1);
		n2.MoveTo(x2,y2,z2);
	}
    public int Move(float dx, float dy, float dz)
    {
        super.Move(dx, dy, dz);
        n2.Move(dx, dy,dz);
        return 0;
    }
    public void Modify(float dx, float dy, float dz){
        n2.MoveTo(dx+x, dy+y, dz+z);
    }
    public int GetPoint(float[] xy)
    {
        xy[0] = x; xy[1]=y;  xy[2]=z;
		xy[3]=n2.x;xy[4]=n2.y; xy[5]=n2.z;
        return 6;
    }
	public ObjProp GetProp(){return _lp;}
	public void SetLineProp(float[] p, int op)
	{
		if (op==0){
			setOrdinate(p[0],p[1],p[2],p[3],p[4],p[5]);
			return;
		}
		if (_lp == null)
			_lp =new LineProp();
		_lp.SetProp(p, op);
	}
}
