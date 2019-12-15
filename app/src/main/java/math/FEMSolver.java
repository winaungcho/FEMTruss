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
package math;
import java.util.*;
import dobj.*;
import java.io.*;

public class FEMSolver implements java.io.Serializable 
{
	static final long serialVersionUID = 4L;

	public static boolean successfullrun=false;
	private String Kname="K.txt";
	static public String resultFile="RTruss2D.txt";
	static public String resultPath="";

	public ArrayList<GPoint> nodes=new ArrayList<GPoint>();
	public ArrayList<lineConnect> lines = new ArrayList<lineConnect> ();
	public float[] eForce=null;
	public float[] Disp=null;
	public float[] Reaction=null;
	protected ArrayList<FEMNode> objs;
	protected float[][] K=null;
	protected float[] F=null;

	public FEMSolver(ArrayList<FEMNode> o)
	{
		objs=o;
		generateFEMdata();
	}
	public long CheckVer()
    {
        return serialVersionUID;
    }
	int SearchNodes(GPoint p)
	{
		int i, n = nodes.size();
		for (i=0;i<n;i++)
		    if (p.IsSameLoc(nodes.get(i)))
				return i;
		return -1;
	}
	public void generateFEMdata( ){
		int i, j1=0, j2=0, n = objs.size();
		
		lines.clear();
		for (i=0;i<n;i++){
			FEMNode obj=objs.get(i);
			String cname = obj.getClass().getSimpleName();

			if (cname.endsWith("FEMLine")){
				float[] xyz= new float[6];
				((FEMLine)obj).GetPoint(xyz);

				GPoint p1 = new GPoint(0,0,0);
				GPoint p2 = new GPoint(0.0f, 0.0f, 0.0f);
				p1.MoveTo(xyz[0], xyz[1], xyz[2]);
				p2.MoveTo(xyz[3], xyz[4], xyz[5]);

				j1 = SearchNodes(p1);
				if (j1<0){
					nodes.add(p1);
					j1 = SearchNodes(p1);
					//j1=nodes.size()-1;
				}
				j2 = SearchNodes(p2);
				if (j2<0){
					nodes.add(p2);
					j2 = SearchNodes(p2);
					//j2=nodes.size()-1;
				}
				lines.add(new lineConnect(i, j1, j2));
			}
		}
	}
	public void RetrieveDisp(GPoint p, float[] d, int dof){
		int i, np, index;
		np = SearchNodes(p);
		if (np>-1){
			index=np*dof;
			for (i=0;i<dof;i++)
				d[i]=Disp[index+i];
		}
	}

	float[] modifyK(float[][] K, float[] F, int dof)
	{
		int i, j, k, jn, n=objs.size();
		int[] rescode = {1,2,4,8,16,32};
		int kn=nodes.size()*dof;
		FEMNode obj=null;
		NodeProp np;
		float[] D=new float[kn];
		for (i=0;i<kn;i++)
		    D[i]=F[i];
		for (i=0;i<n;i++){
			obj=objs.get(i);
			String cname = obj.getClass().getSimpleName();
			if (cname.endsWith("FEMNode")){
				np = (NodeProp) obj.GetProp();
				if (np!=null){
				    jn = SearchNodes(obj);
				    for (k=0;k<dof;k++){
					    if ((np.res & (rescode[k])) > 0){
						    for (j=0;j<kn;j++){
								if (j==(jn*dof+k))
									D[j]=np.Disp[k];
								else
									D[j]=D[j]-K[j][jn*dof+k]*np.Disp[k];
							    K[j][jn*dof+k]=K[jn*dof+k][j]=0;
						    }
						    K[jn*dof+k][jn*dof+k]=1;
					    }
						if(np.Sk[k] != 0)
							K[jn*dof+k][jn*dof+k]+=np.Sk[k];
				    }
				}
			}
		}
		return D;
	}

	float[] FvecElement(lineConnect lc, int dof){
		return null;
	}
	float[][] KmatElement(lineConnect lc, int dof){
		return null;
	}
	public void SaveHtml(String pathname, String fname){}

	float[][] AssembleK(int dof){
		float[][] k;
		int i, jn=nodes.size();
		int m,n,ln=lines.size();
		K = new float[jn*dof][jn*dof];
		for(m=0;m<dof*jn;m++){
			for (n=0;n<dof*jn;n++){
				K[m][n]=0;
			}
		}
		for (i=0;i<ln;i++){
			lineConnect lc=lines.get(i);
			k=KmatElement(lc, dof);
			//if (lc.ln == 1 || lc.ln == 5 || lc.ln == 7){
			for(m=0;m<dof;m++){
				for (n=0;n<dof;n++){
					K[lc.j1*dof+m][lc.j1*dof+n]=K[lc.j1*dof+m][lc.j1*dof+n]+k[m][n];
					K[lc.j2*dof+m][lc.j2*dof+n]=K[lc.j2*dof+m][lc.j2*dof+n]+k[m+dof][n+dof];
					K[lc.j2*dof+m][lc.j1*dof+n]=K[lc.j2*dof+m][lc.j1*dof+n]+k[m+dof][n];
					K[lc.j1*dof+m][lc.j2*dof+n]=K[lc.j1*dof+m][lc.j2*dof+n]+k[m][n+dof];
				}
			}
			//}
		}
		return K;
	}
	float[] AssembleF(int dof){
		int i, j, jn, n=objs.size();
		int kn=nodes.size()*dof;
		FEMNode obj=null;
		NodeProp np;
		float[] fe=null;
		F=new float[kn];
		for (i=0;i<kn;i++)
		    F[i]=0;
		for (i=0;i<n;i++){
			obj=objs.get(i);
			String cname = obj.getClass().getSimpleName();
			if (cname.endsWith("FEMNode")){
				np = (NodeProp) obj.GetProp();
				if (np!=null){
				    jn = SearchNodes(obj);
				    for (j=0;j<dof;j++){
				        F[jn*dof+j]+=np.F[j];
				    }
				}
			}
		}
		n=lines.size();
		for (i=0;i<n;i++){
			lineConnect lc=lines.get(i);
		    fe=FvecElement(lc, dof);
			for (j=0;j<dof;j++){
				F[lc.j1*dof+j]+=fe[j];
				F[lc.j2*dof+j]+=fe[j+dof];
			}

		}
		return F;
	}
	float[] retrieveElementDisp(lineConnect lc, int dof){
		float[] elemD=new float[dof*2];
		for (int i=0;i<dof;i++){
			elemD[i]=Disp[lc.j1*dof+i];
			elemD[i+dof]=Disp[lc.j2*dof+i];
		}
		return elemD;
	}
	public int RunStaticLinearSystem(int dof){
		int i, jn, n, solvcode;
		float[] elemF=null, elemD=null;
		float[][] k=null;
		K = AssembleK(dof);
		F = AssembleF(dof);
		jn= F.length;

		FMatrix.SaveKnF(resultPath, Kname, K, F, true);
		Disp = modifyK(K, F, dof);

		// Solve for disp;
		try{
			solvcode = FMatrix.GeneralSolver(K, Disp);
		} catch (RuntimeException e){
			solvcode=-1;
		}
		if (solvcode == 1){
			K=FMatrix.ReadK(resultPath, Kname, " ");
			//SaveKnF("/sdcard/K2.txt", false);
			Reaction = FMatrix.KmulD(K, Disp);
			for(i=0;i<jn;i++){
				Reaction[i] = Reaction[i]-F[i];
			}
			n = lines.size();
		    eForce=new float[n];
			for(i=0;i<n;i++){
				lineConnect lc=lines.get(i);
				float dx, dy, l, c, s;
			
				dx=nodes.get(lc.j2).getX()-nodes.get(lc.j1).getX();
				dy=nodes.get(lc.j2).getY()-nodes.get(lc.j1).getY();
				l=(float)Math.sqrt((double)(dx*dx+dy*dy));
				if (l==0)
					throw new RuntimeException("Element length is zero! ");
				c=dx/l;
				s=dy/l;

				k=KmatElement(lc, dof);
				elemD=retrieveElementDisp(lc, dof);
				elemF=FMatrix.KmulD(k, elemD);
				eForce[i]=elemF[2]*c+elemF[3]*s;
				//eForce[i]=(float)Math.sqrt(elemF[0]*elemF[0]+elemF[1]*elemF[1]);
			}
			SaveHtml(resultPath, resultFile);
			successfullrun=true;
		}
		//SaveHtml("/sdcard/", resultFile);
		K=null;
		return solvcode;
	}
	public void Save(String pathname, String fname, int dof)
	{
		String endl = "\r\n";
		LineProp lp;
		int ndof, i;
		try{
		    //FileOutputStream outputStream = new FileOutputStream (new File(fname));
		    FileWriter out=new FileWriter(new File(pathname, fname));

			int n=nodes.size();
			ndof=dof*n;
			out.write(endl);
			for(i=0;i<n;i++){
				GPoint nod=nodes.get(i);
				out.write(String.format("%3d %+8.2f, %+8.2f, %+6.2f", i, nod.getX(),
										nod.getY(), nod.getZ()));
				out.write(endl);
			}
			out.write(endl);
			n=lines.size();
			for(i=0;i<n;i++){
				lineConnect lc=lines.get(i);
				out.write(i+" "+ lc.ln +", "+lc.j1+", "+lc.j2+" ");
				lp=(LineProp)((FEMLine)objs.get(lc.ln)).GetProp();
				if (lp!=null){
					out.write("E= "+ lp.E+" A= "+lp.A);
				}
				out.write(endl);
			}
			out.write(endl);
			for(i=0;i<ndof;i++){
				out.write(String.format(" %+6.4f > %+6.4f > %+6.4f", F[i], Disp[i], Reaction[i]));
				out.write(endl);
			}
			out.write(endl);
			for(i=0;i<n;i++){
				lineConnect lc=lines.get(i);
				out.write(String.format("%3d %3d %+6.4f", i, lc.ln, eForce[i]));
				out.write(endl);
			}
			out.write(endl);
			out.close();
		} catch (IOException ex){
		}
	}
}
