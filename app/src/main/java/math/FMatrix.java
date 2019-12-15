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
import java.io.*;

public class FMatrix 
{
	static public boolean isNumber(String str){
		// return str.matches("\\d+(?:\\.\\d+)?");
		// return str.matches("[-+]?[0-9]*\\.?[0-9]+");
		// return str.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
		return str.matches("[+-]?\\d+(\\.\\d+)?([Ee][+-]?\\d+)?");
	}
	static float[] KmulD(float[][] S, float[] D)
	{
		int i,j,n;
		float sum;
		float[] R;
		n=S.length;
		R=new float[n];
		for(i=0;i<n;i++){
			sum=0;
			for(j=0;j<n;j++)
				sum+=S[i][j]*D[j];
			R[i]=sum;
		}
		return R;
	}
	
	static int GeneralSolver(float[][] A, float[] b)
	{
		int i, j, k, n=A.length;
		float pivot, factor;
		/* ------------------------------------
		 The Gaussian elimination method
		 ------------------------------------ */
		for (i = 0; i < n; i++){  // Process row i
			/* ---------------------
			 (A) Select pivot
			 --------------------- */
   	    	pivot = A[i][i];
			if (pivot == 0)
				throw new RuntimeException("Zero stiffness found");
			/* ---------------------
			 (B) Normalize row i
			 --------------------- */
   	    	for (j = 0; j < n; j++)
				A[i][j] = A[i][j]/pivot;
   	    	b[i] = b[i]/pivot;
			/* ---------------------
			 (C) Sweep using row i
			 --------------------- */
   	    	for (k = 0; k < n; k++){
				if ( k != i ){
					factor = A[k][i];
					for (j = 0; j < n; j++)
						A[k][j] = A[k][j] - factor*A[i][j];
					b[k] = b[k] - factor*b[i];
				}
   	    	}
		}
		return 1;
	}
	
	
	static public void SaveKnF(String pathname, String fname, float[][] K, float[] F, boolean f)
	{
		String endl = "\r\n";
		int ndof, i, j;
		try{
		    //FileOutputStream outputStream = new FileOutputStream (new File(fname));
		    FileWriter out=new FileWriter(new File(pathname, fname));
			
			ndof=K.length;
			out.write(String.format("%d", ndof));
			out.write(endl);
			for(i=0;i<ndof;i++){
				for(j=0;j<ndof;j++){
					out.write(String.format("%+6.4f ", K[i][j]));
				}
				if (f)
				    out.write(String.format(" = %+6.4f", F[i]));
				out.write(endl);
			}
			out.write(endl);
			out.close();
		} catch (IOException ex){
		}
	}

	static public float[][] ReadK(String pathname, String fname, String Seperator){
		int i, j, ndof;
		float[][] S=null;

		try{
			BufferedReader br = new BufferedReader(new FileReader(pathname+java.io.File.separator+fname));
			// read the first line from the text file
			String fileRead = br.readLine();

			ndof = Integer.parseInt(fileRead.trim());
			String[] tokenize = new String[ndof+2];
			S = new float[ndof][ndof];
			fileRead = br.readLine();
			j=0;
			while (fileRead != null && j<ndof)
            {
				tokenize=fileRead.split(Seperator);
				for(i=0;i<ndof;i++)
					S[i][j]=Float.parseFloat(tokenize[i].trim());
				fileRead = br.readLine();
				j++;
			}
			br.close();
		}


		// handle exceptions
        catch (FileNotFoundException fnfe)
        {
            //System.out.println("file not found");
        }

        catch (IOException ioe)
        {
            //ioe.printStackTrace();
        }
		return S;
	}
}
