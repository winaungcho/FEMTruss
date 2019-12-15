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

package com.structsoftlab.edu.femtruss;

import android.app.*;
import android.os.*;
import android.content.*;
import dobj.collect.*;
import dobj.*;
import math.*;
import android.webkit.*;

public class MainActivity extends Activity 
{
	public static Activity appContext;
	FEMCollection femCollection;
	MathMod mathMod;
	WebView webview;
	String path="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		appContext = this;
        setContentView(R.layout.main);
		webview = (WebView) findViewById(R.id.webview);
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			path = getFilesDir().toString()+"/";
		} else {
			path = Environment.getExternalStorageDirectory().toString()+"/";
		}
		
		femCollection = new FEMCollection();
		createTruss();
	    webview.loadUrl("file://"+path+"femtruss.html");
    }
	public static Context getAppContext() {
        return appContext;
    }
	void createTruss(){
		FEMLine femL;
		FEMNode femN;
		float[] p=new float[10];
		p[0]=1000;
		p[1]=0.3f;
		p[2]=10;
		femL = femCollection.AddLine(0, 0, 0, 1000, 0, 0);
		femL.SetLineProp(p, 1);
		femL = femCollection.AddLine(500, 800, 0, 1000, 0, 0);
		femL.SetLineProp(p, 1);
		femL = femCollection.AddLine(0, 0, 0, 500, 800, 0);
		femL.SetLineProp(p, 1);
		
		femN = new FEMNode(0,0,0);
		p[0]=3; // hinge
		femN.SetNodeProp(p, 5);
		femCollection.AddUniqueNode(femN);
		
		femN = new FEMNode(1000,0,0);
		p[0]=2; // roller
		femN.SetNodeProp(p, 5);
		femCollection.AddUniqueNode(femN);
		
		femN = new FEMNode(500,800,0);
		p[0]=10; // Fx
		p[1]=-10; // Fy
		p[2]=0; // Fz
		femN.SetNodeProp(p, 1);
		femCollection.AddUniqueNode(femN);
		
		mathMod = new MathMod(femCollection.GetObjs());
		mathMod.RunStaticLinearSystem(2);
		mathMod.SaveHtml(path, "femtruss.html");
	}
}
