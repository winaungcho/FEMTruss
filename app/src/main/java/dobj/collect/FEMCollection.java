
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
package dobj.collect;
import java.util.*;
import dobj.*;
import math.*;

public class FEMCollection implements java.io.Serializable
{
	private static final long serialVersionUID = 4L;
	public ArrayList<FEMNode> objs= new ArrayList<FEMNode>();
	
	public long CheckVer()
    {
        return serialVersionUID;
    }
	public ArrayList<FEMNode> GetObjs(){
		return objs;
	}
	public void Add(FEMNode nod)
    {
        if (objs.contains(nod))
			return;
        objs.add(nod);
		MathMod.successfullrun=false;
    }
	public FEMLine AddLine(float x1, float y1, float z1, float x2, float y2, float z2){
		FEMLine obj = new FEMLine();
		obj.MoveTo(x1, y1, z1);
		obj.Modify(x2-x1, y2-y1, z2-z1);
		Add(obj);
		return obj;
	}
	public void AddNodeFromLines()
	{
		int i, n = objs.size();
		float[] xyz={0,0,0,0,0,0};
		for (i=0;i<n;i++){
			FEMNode obj=objs.get(i);
			String cname = obj.getClass().getSimpleName();
			if (cname.endsWith("FEMLine")){
				((FEMLine)obj).GetPoint(xyz);
				AddUniqueNode(new FEMNode(xyz[0], xyz[1], xyz[2]));
				AddUniqueNode(new FEMNode(xyz[3], xyz[4], xyz[5]));
			}
		}
	}
	public int AddUniqueNode(FEMNode nod)
    {
		int i;
		i=SearchNodes(nod);
        if (i>=0)
			return i;
        objs.add(nod);
		return objs.size()-1;
    }
	int SearchNodes(GPoint p)
	{
		int i, n = objs.size();
		for (i=0;i<n;i++){
			FEMNode obj=objs.get(i);
			String cname = obj.getClass().getSimpleName();
			if (cname.endsWith("FEMNode")) 
		        if (p.IsSameLoc(obj))
				    return i;
		}
		return -1;
	}
}
