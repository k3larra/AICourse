package decisiontree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

import decisiontree.Constants.LEAF;

public class SaveTree {
	public static final String filePATH="myData/myNodes.txt";
	
	public static boolean saveTree(Node n){
        if (TrainModel.rootNode==null){
        	return false;
        }
    	try {
			FileOutputStream f = new FileOutputStream(new File(filePATH));
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(n);
			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
			return false;
		} catch (IOException e) {
			System.out.println(e.toString());
			return false;
		} 
        return true;
	}
	
	/**
	 * Loads data from a file as specified by the file name supplied.
	 * 
	 * @param fileName the file to be loaded
	 * @return the data contained in the file specified
	 */
	public static Node loadTree()
	{
		Node n = null;
		try {
			FileInputStream fi = new FileInputStream(new File(filePATH));
			ObjectInputStream oi = new ObjectInputStream(fi);
			// Read objects
			//Person pr1 = (Person) oi.readObject();
			//Person pr2 = (Person) oi.readObject();
			n = (Node)oi.readObject();
			//System.out.println(pr1.toString());
			//System.out.println(pr2.toString());
			oi.close();
			fi.close();

		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return n;
	}
	

}
