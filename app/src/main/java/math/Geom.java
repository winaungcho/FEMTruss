
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
import com.structsoftlab.edu.femtruss.*;
import dobj.*;
public class Geom{
	public static final float PI = (float)Math.PI;
    public static final float EPSILON = 0.0005f;
	public static final float SNAPTOL = 25.4f;
	
	public static float[] Plane2Real(float x, float y, int pl, float d)
	{
		float[] a={0,0,0};
		switch(pl){
			case 0:a[0]=x;a[1]=y;a[2]=d;break;
			case 1:a[0]=x;a[1]=-d;a[2]=y;break;
			case 2:a[0]=d;a[1]=x;a[2]=y;break;
		}
		return a;
	}
	public static float[] Real2Plane(float x, float y, float z, int pl)
	{
		float[] a={0,0,0};
		switch(pl){
			case 0:a[0]=x;a[1]=y;a[2]=z;break;
			case 1:a[0]=x;a[1]=z;a[2]=-y;break;
			case 2:a[0]=y;a[1]=z;a[2]=x;break;
		}
		return a;
	}
    public static boolean isLeft(float x1,float y1,float x2,float y2,float x,float y){
        return ((x2 - x1)*(y - y1) - (y2 - y1)*(x - x1)) > 0;
    }
    public static void pointrelto(float x1,float y1,float r,float q, float []xy)
	// q in radian
    {
        xy[0] = x1+r*(float)Math.cos(q);
        xy[1] = y1+r*(float)Math.sin(q);
    }
    public static boolean pointonline(float x1,float y1,float x2,float y2,float x,float y)
	// check if point on line
    {
        float sx=((x1<x2)?x1:x2)-SNAPTOL;
        float ex=((x1>x2)?x1:x2)+SNAPTOL;
        float sy=((y1<y2)?y1:y2)-SNAPTOL;
        float ey=((y1>y2)?y1:y2)+SNAPTOL;
		
        if ((sx<=x && ex>=x) && (sy<=y && ey>=y)){
			
            float[] xy = new float[4];
            perpmirpoint(x1, y1, x2, y2, x, y, xy);
			FEMNode n = new FEMNode(x, y, 0);
            if (n.Dist(xy[0], xy[1]) < SNAPTOL)
               return true;
        }
		
        return false;
    }
    public static int perpmirpoint(float x1,float y1,float x2,float y2,float x3,float y3, float
								   [] xy)
	// perpendicular and mirror point
    {
        float dx1, dy1, m, yo, t, x, y;
        boolean intersect;

        dx1 = x2 - x1;
        dy1 = y2 - y1;
        if (x2!=x1) {
            m = dy1 / dx1;
            if (m!=0) {
                yo = y1 - m * x1;
                t = y3 + (1 / m) * x3;
                // m*x+yo = -1/m*x+t
                x = (t - yo) / (m + 1 / m);
                y = -1 / m * x + t;

                dx1 = x3 - x;
                dy1 = y3 - y;

                xy[0] = x;
                xy[1] = y;
                xy[2] = x - dx1;
                xy[3] = y - dy1;
            } else {
                xy[0] = x3;
                xy[1] = y2;
                xy[2] = x3;
                xy[3] = y2+y2-y3;
            }
        } else
        {
            xy[0] = x2;
            xy[1] = y3;
            xy[2] = x2+x2-x3;
            xy[3] = y3;
        }
        return 2;
    }
    public static boolean segintersect(float x1,float y1,float x2,float y2,float x3,float
									   y3,float x4,float y4, float[] xy)
    {
        float dx1, dy1, dx2, dy2, t, s;
        boolean intersect;

        dx1 = x2 - x1;
        dy1 = y2 - y1;
        dx2 = x4 - x3;
        dy2 = y4 - y3;
        if ((dx2 * dy1 - dy2 * dx1) == 0) return false;

        s = (dx1 * (y3 - y1) + dy1 * (x1 - x3)) / (dx2 * dy1 - dy2 * dx1);
        t = (dx2 * (y1 - y3) + dy2 * (x3 - x1)) / (dy2 * dx1 - dx2 * dy1);
        xy[0] = x1 + t * dx1;
        xy[1] = y1 + t * dy1;
        intersect = ((s >= 0f) && (s <= 1f) && (t >= 0f) && (t <= 1f));
        return intersect;
    }
    public static boolean segcircleintersect(float x1,float y1,float x2,float y2,float xc,float
											 yc,float r, float[] xy)
    {
        boolean intersect=false;
        float y0,n;
        float xc2=xc*xc,yc2=yc*yc,r2=r*r,y02,n2,A,B;
        float x11,x21,y11,y21;
        int i=0;
        if (x2==x1)
            return intersect;
        y0=y1+(x1*y1-x1*y2)/(x2-x1);y02=y0*y0;
        n=(y2-y1)/(x2-x1);n2=n*n;

        A=-2*y0*n+2*xc+2*yc*n;
        B=(float) (2*Math.sqrt(-2*y0*n*xc+2*xc*yc*n-yc2+r2-y02+2*yc*y0+n2*r2-n2*xc2));
        x11 = (A+B)/2/(1+n2);
        y11 = y0+n*(A+B)/2/(1+n2);
        x21 = (A-B)/2/(1+n2);
        y21 = y0+n*(A-B)/2/(1+n2);

        if (((x11-x1)/(x2-x1) >= 0) && ((x11-x1)/(x2-x1) < 1) &&
			((y11-y1)/(y2-y1) < 1) && ((y11-y1)/(y2-y1) >= 0)){
            xy[0] = x11; xy[1] = y11;
            i=2;
            intersect =true;
        }
        if (((x21-x1)/(x2-x1) >= 0) && ((x21-x1)/(x2-x1) < 1) &&
			((y21-y1)/(y2-y1) < 1) && ((y21-y1)/(y2-y1) >= 0)){
            xy[i] = x21; xy[i+1] = y21;
            intersect =true;
        }
        return intersect;
    }
    static float [] Rect2Polar(float x, float y)
    {
		float dx, dy, dl, ang;
        dx = x; dy = y;
        dl = (float)Math.sqrt(dx*dx+dy*dy);
        if (Math.abs(dl) < 0.001){
            x=y=0;
            return new float[] {x, y};
        }
        ang = (float)Math.asin(Math.abs(dy/dl));
        if ((dx >= 0) && (dy < 0))
            ang = 2*Geom.PI-ang;
        else
        if ((dx <= 0) && (dy >= 0))
            ang = Geom.PI-ang;
        else
        if ((dy <= 0) && (dx <= 0))
            ang = Geom.PI+ang;
        x = dl; y = ang;
        return new float[] {x, y};
    }

    public static int FilletPolyLine(float radi, float[] X, float[] Y, int n, float [] bulge, boolean c)
    {
        int i=0, j, k=0;
	    int x1, x2, x3, y1, y2, y3;
        float  p2lx, p2ly, p2rx, p2ry;
        float[] fpx = new float[2*n];
        float[] fpy = new float[2*n];
        float x, y, l1, t1, l2, t2, ang, ang0, dl;
        int CLW;
        x1=x2=x3=-1;
        y1=y2=y3=-1;
        bulge[k] = 0;
        fpx[k] = X[0];
        fpy[k] = Y[0];k++;
        for (j=0; j<= n;j++) {
            x1 = x2;y1 = y2;
            x2 = x3;y2 = y3;
            if (j < n){
                x3 = j;
                y3 = j;
            }
            else {
                if (c){ // fpx, fpy
                    x3 = 1;
                    y3 = 1;
                }
                else{
                    x3 = -1;
                    y3 = -1;
                }
            }
            if (x3>-1 && y3>-1){
                i++;
                if ((x1>-1) && (x2>-1)) {

                    if (radi != 0) {
                        p2lx = p2rx = X[x2];
                        p2ly = p2ry = Y[y2];
                        x = p2lx - X[x1];
                        y = p2ly - Y[y1];
                        float[] rq = Rect2Polar(x, y);
                        x = rq[0];
                        y = rq[1];
                        l1 = x;
                        t1 = y;
                        if (c && j >= n) {
                            x = fpx[x3] - p2rx;
                            y = fpy[y3] - p2ry;
                        } else {
                            x = X[x3] - p2rx;
                            y = Y[y3] - p2ry;
                        }
                        rq = Rect2Polar(x, y);
                        x = rq[0];
                        y = rq[1];

                        l2 = x;
                        t2 = y;
                        if (t1 > 3 * Geom.PI / 2)
                            t1 = t1 - 2 * Geom.PI;
                        ang = Geom.PI + t1 - t2;

                        CLW = 1;

                        if (ang < 0)
                            ang += Geom.PI * 2;
                        if (ang > Geom.PI * 2)
                            ang = ang - 2 * Geom.PI;
                        if (ang > Geom.PI) {
                            CLW = 0;
                            ang = 2 * Geom.PI - ang;
                        }
                        ang0 = (Geom.PI - ang) / 4.0f;
                        ang /= 2.0f;
                        dl = radi / (float) Math.sin(ang);
                        dl = ((float) Math.cos(ang) * dl);
                        if ((Math.abs(l1) > Math.abs(dl)) && (Math.abs(l2) > Math.abs(dl))) {
                            p2lx += -dl * Math.cos(t1);
                            p2ly += -dl * Math.sin(t1);

                            p2rx += dl * Math.cos(t2);
                            p2ry += dl * Math.sin(t2);
                            //DrawLine(*p1, p2l);
                            bulge[k] = (float) ((CLW == 1) ? Math.tan(ang0) : -Math.tan(ang0));
                            fpx[k] = p2lx;
                            fpy[k] = p2ly;
                            k++;

                            if ((j == n) && c) {
                                bulge[0] = 0;
                                fpx[0] = p2rx;
                                fpy[0] = p2ry;
                            }
                            {
                                //DrawArc(pDC, p2l, p2r, radi); //CLW==1
                                bulge[k] = 0;
                                fpx[k] = p2rx;
                                fpy[k] = p2ry;
                                k++;
                            }
                            X[x2] = p2rx;
                            Y[y2] = p2ry;
                        } else {
                            //DrawLine(*p1, *p2);
                            bulge[k] = 0;
                            fpx[k] = X[x2];
                            fpy[k] = Y[y2];
                            k++;
                        }
                    } else {
                        //DrawLine(*p1, *p2);
                        bulge[k] = 0;
                        fpx[k] = X[x2];
                        fpy[k] = Y[y2];
                        k++;
                    }
                }
            } else if ((x1>-1) && (x2>-1)){
                //DrawLine(*p1, *p2);
                bulge[k] = 0;
                fpx[k] = X[x2];
                fpy[k] = Y[y2];k++;
                break;
            }
        };
        for (j=0; j<k;j++) {
            X[j]=fpx[j];
            Y[j]=fpy[j];
        }
        n=k;
        return n;
    }
}

