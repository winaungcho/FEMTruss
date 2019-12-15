
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

public class LineProp extends ObjProp
{
	public float w[]=new float[3];
	public float P[][]=new float[2][3];
	public float A,Ixx, Iyy, Ip, E, nu, dens;
	public LineProp(){
		int i;
		for(i=0;i<3;i++){
			w[i]=0;
			P[0][i]=0;
			P[1][i]=0;
		}
		A=0;Ixx=0; Iyy=0; Ip=0; 
		E=0; nu=0; dens=0;
	}
	public LineProp(LineProp cp){
		int i;
		for(i=0;i<3;i++){
			w[i]=cp.w[i];
			P[0][i]=cp.P[0][i];
			P[1][i]=cp.P[1][i];
		}
		A=cp.A;Ixx=cp.Ixx; Iyy=cp.Iyy; Ip=cp.Ip; 
		E=cp.E; nu=cp.nu; dens=cp.dens;
	}
	public void SetMatProp(float e, float n)
	{
		E=e;
		nu = n;
	}
	public void SetGeomProp(float a, float ixx, float iyy, float ip)
	{
		A=a;Ixx=ixx;Iyy=iyy;Ip=ip;
	}
	public void SetProp(float[] p, int op)
	{
		int i;
		switch(op){
			case 1:
				E=p[0];
				nu=p[1];
				A=p[2];
			break;
			case 2:
				E=p[0];
				nu=p[1];
				A=p[2];
				dens=p[3];
			break;
			case 3:
				w[0]=p[0];
				w[1]=p[1];
				w[2]=p[2];
			break;
			case 4:
				for(i=0;i<3;i++)
				{
					P[0][i]=p[i*2];
					P[1][i]=p[i*2+1];
				}
			break;
			case 5:
				A=p[0];
				Ixx=p[1];
				Iyy=p[2];
				Ip=p[3];
				E=p[4];
				nu=p[5];
				dens=p[6];
			break;
		}
	}
}
