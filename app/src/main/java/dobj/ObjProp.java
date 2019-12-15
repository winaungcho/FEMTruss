
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

class ObjProp implements java.io.Serializable {
    static final long serialVersionUID = 4L;
	public ObjProp(){}
	public long CheckVer()
    {
        return serialVersionUID;
    }
}
