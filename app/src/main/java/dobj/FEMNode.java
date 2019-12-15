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

public class FEMNode extends GPoint{
	NodeProp _np=null;
	public FEMNode(){
		super(0,0,0);
	}
    public FEMNode(float a, float b, float c){
        super(a,b, c);
    }
	public ObjProp GetProp(){return _np;}
    public void setOrdinate(float x1, float y1, float x2, float y2){
		
	}
    public void setOrdinate(float x1, float y1, float z1, float x2, float y2, float z2){
		
	}
	public void setOrdinate(float[] xy){
		x=xy[0];
		y=xy[1];
		z=xy[2];
	}
	public void SetNodeProp(float[] p, int op)
	{
		if(op==0){
			setOrdinate(p);
			return;
		}
		if (_np == null)
			_np =new NodeProp();
		_np.SetProp(p, op);
	}
}
