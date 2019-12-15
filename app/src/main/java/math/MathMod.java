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
import com.structsoftlab.edu.femtruss.*;
import java.io.*;
import android.text.format.*;
import dobj.*;
import android.graphics.*;
class lineConnect implements java.io.Serializable {
	static final long serialVersionUID = 4L;
	public int ln, j1, j2;
	lineConnect(){
		ln=j1=j2=0;
	}
	lineConnect(int l, int a, int b){
		ln=l;j1=a;j2=b;
	}
}
public class MathMod extends FEMSolver
{
	public MathMod(ArrayList<FEMNode> o)
	{
		super(o);
		resultPath=MainActivity.getAppContext().getFilesDir().getAbsolutePath();
	}

	@Override
	float[] FvecElement(lineConnect lc, int dof)
	{
		// global
		float dx, dy, l;
		float[] fe=new float[4];
		LineProp lp=(LineProp)((FEMLine)objs.get(lc.ln)).GetProp();
		if (lp!=null){
			dx=nodes.get(lc.j2).getX()-nodes.get(lc.j1).getX();
			dy=nodes.get(lc.j2).getY()-nodes.get(lc.j1).getY();
			l=(float)Math.sqrt((double)(dx*dx+dy*dy));
			fe[0]=fe[2]=0;
			fe[1]=fe[3]=-lp.dens*lp.A*l/2;
		}
		return fe;
	}
	@Override
	float[][] KmatElement(lineConnect lc, int dof)
	{
		// global
		float[][] k=new float[4][4];
		//{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
		float dx, dy, l, c, s, E,A, eabyl;
		LineProp lp=(LineProp)((FEMLine)objs.get(lc.ln)).GetProp();
		if (lp!=null){
			E=lp.E; A=lp.A;
			dx=nodes.get(lc.j2).getX()-nodes.get(lc.j1).getX();
			dy=nodes.get(lc.j2).getY()-nodes.get(lc.j1).getY();
			l=(float)Math.sqrt((double)(dx*dx+dy*dy));
			c=dx/l;
			s=dy/l;
			eabyl=E*A/l;
			k[0][0]=k[2][2]=c*c*eabyl;
			k[1][1]=k[3][3]=s*s*eabyl;
			k[0][2]=k[2][0]=-c*c*eabyl;
			k[1][3]=k[3][1]=-s*s*eabyl;
			k[2][3]=k[3][2]=k[0][1]=k[1][0]=c*s*eabyl;
			k[0][3]=k[3][0]=k[1][2]=k[2][1]=-c*s*eabyl;
		}
		return k;
	}
	
	@Override
	public void SaveHtml(String pathname, String fname)
	{
		String endl = "\r\n";
		LineProp lp;
		int ndof, dof=2, i;
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		String nowStr=today.format("%Y-%m-%d-%k:%M:%S");
		try{
		    //FileOutputStream outputStream = new FileOutputStream (new File(fname));
		    FileWriter out=new FileWriter(new File(pathname, fname));

			int n=nodes.size();
			ndof=dof*n;
			out.write(endl);
			out.write(
				"<!DOCTYPE html>"+
				"<html>"+
				"<head>"+endl+
				"<style>"+
				"table {"+
				"border-collapse: collapse;"+
				"width:100%"+
				"}"+endl+

			"table, td, th {"+
				"border: 1px solid black;"+
				"}"+endl+
			"</style>"+
				"</head>"+endl+
			"<body>");
			out.write("<p>Path:"+pathname+"<br/>");
			out.write("File:"+fname+"</br></p>");
		    out.write("<p>Time of creation:"+nowStr+endl+"</p>");
			out.write(
				"<div class=\"tab\">"+
				"<button class=\"tablinks\" onclick=\"openTab(event, 'Node')\">Node</button>"+
				"<button class=\"tablinks\" onclick=\"openTab(event, 'Connectivity')\">Connectivity</button>"+
				"<button class=\"tablinks\" onclick=\"openTab(event, 'Result')\">Result</button>"+
				"<button class=\"tablinks\" onclick=\"openTab(event, 'ElementForces')\">Element Forces</button>"+
				"</div>");
			out.write(
				"<div id=\"Node\" class=\"tabcontent\">"+
				"<table class=\"zebra\">"+
				"<caption>Node Data</caption>"+
				"<thead><tr>"+ 
				"<th>Node No.</th>"+ 
				"<th>X</th><th>Y</th><th>Z</th>"+ 
				"</tr></thead><tbody>");
			for(i=0;i<n;i++){
				GPoint nod=nodes.get(i);
				out.write(String.format("<tr><td>%3d</td><td align=\"right\">%+8.2f</td><td align=\"right\">%+8.2f</td><td align=\"right\">%+8.2f</td></tr>", i, nod.getX(),nod.getY(), nod.getZ()));
				out.write(endl);
			}
			out.write("</tbody></table></div>");
			out.write(endl);
			out.write(
				"<div id=\"Connectivity\" class=\"tabcontent\">"+
				"<table class=\"zebra\">"+
				"<caption>Connectivity Data</caption>"+
				"<thead><tr>"+ 
				"<th>No.</th> "+
				"<th>ObjID</th><th>Start</th><th>End</th>"+
				"<th>E</th><th>A</th>"+
				"</tr></thead><tbody>");
			n=lines.size();
			for(i=0;i<n;i++){
				lineConnect lc=lines.get(i);
				out.write("<tr><td>"+i+"</td><td>"+ lc.ln +"</td><td>"+lc.j1+"</td><td>"+lc.j2+"</td>");
				lp=(LineProp)((FEMLine)objs.get(lc.ln)).GetProp();
				if (lp!=null){
					out.write("<td align=\"right\">"+ lp.E+"</td><td align=\"right\">"+lp.A+"</td>");
				}
				out.write("</tr>");
				out.write(endl);
			}
			out.write("</tbody></table></div>");
			out.write(endl);
			out.write(
				"<div id=\"Result\" class=\"tabcontent\">"+
				"<table class=\"zebra\">"+
				"<caption>Primary Data</caption>"+
				"<thead><tr>"+
				"<th>Node No.</th>"+
				"<th>DOF No.</th>"+
				"<th>F</th><th>Displacement</th><th>Reaction</th>"+ 
				"</tr></thead><tbody>");

			for(i=0;i<ndof;i++){
				out.write(String.format("<tr><td>%d</td><td>%d</td><td align=\"right\"> %+12.4f</td><td align=\"right\"> %+12.4f</td><td align=\"right\"> %+12.4f</td></tr>", i/dof, i, F[i], Disp[i], Reaction[i]));
				out.write(endl);
			}
			out.write("</tbody></table></div>");
			out.write(endl);
			out.write(
				"<div id=\"ElementForces\" class=\"tabcontent\">"+
				"<table class=\"zebra\">"+
				"<caption>Element Forces</caption>"+
				"<thead><tr>"+ 
				"<th>No.</th>"+
				"<th>ObjID</th><th>Force</th>"+ 
				"</tr></thead><tbody>");
			for(i=0;i<n;i++){
				lineConnect lc=lines.get(i);
				out.write(String.format("<tr><td>%3d</td><td>%3d</td><td align=\"right\">%+12.4f</td></tr>", i, lc.ln, eForce[i]));
				out.write(endl);
			}
			out.write("</tbody></table></div>");
			out.write("</body></html>");
			out.write(endl);
			out.close();
		} catch (IOException ex){
		}
	}
	
}

