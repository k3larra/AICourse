package decisiontree;

import java.util.ArrayList;
import java.util.Arrays;

import decisiontree.Constants.LABEL;

public class ID3AttributeSelectionMethod extends AttributeSelectionMethod {
	
	ArrayList<LABEL> labelsWithZeroGain = new ArrayList<LABEL>();
	
	@Override
	public LABEL method(String[][] D, ArrayList<LABEL> attribute_list) {
		//Compute info_D
		//double info_d = 
		//System.out.println("INfoGain: "+infoGain(D, attribute_list));
		//inforeq per attribute'
		double info_req_for_attribute[]= new double[attribute_list.size()]; 
		info_req_for_attribute[0]=infoGain(D, attribute_list); ////so place 0 is is infoGain total;
		for (int i = 1; i<attribute_list.size();i++){
			String[] attributeValues = getLabelAttributeValues(D,i);
			System.out.println("*******FOR attribute: "+attribute_list.get(i).toString());
			int total = 0;
			double info_req_for_attribute_value=0.0;
			for (String attributeValue : attributeValues) {
				//found holds total number of class values for an attribute value (found[5]) and 
				//and how the class values are distributed over the attribute values
				//found[0] up, found[1] down
				info_req_for_attribute_value=0.0;
				double[] found = numberAttributeValues(D,attributeValue,i);
				System.out.print("** for Attribute value: "+attributeValue+": ");
				testNumbers(found, D[0].length);
				
				int numberTuples = D[0].length;
				double infodj = 0.0;
				for (int j=0;j<found.length-1;j++) {
					if ( found[j]!=0){ //so not log 0 (-infinity)
						infodj = infodj-(found[j]/found[5])*log2(found[j]/found[5]);
						System.out.print(found[j]+" : ");
						System.out.print(infodj+" : ");
					}
				}
				info_req_for_attribute_value=(found[5]/numberTuples)*infodj;
				System.out.println();
			}
			info_req_for_attribute[i]=info_req_for_attribute[i]+info_req_for_attribute_value;
		}
		//Gain for attributes (InfoD - Info attr); (pos 0 is InfoD
		for (int i = 1; i< info_req_for_attribute.length; i++) {
			info_req_for_attribute[i] = info_req_for_attribute[0] - info_req_for_attribute[i];
		}
		labelsWithZeroGain.clear();
		for (int i = 0; i< info_req_for_attribute.length; i++) {
			System.out.println("InfoGain for: "+attribute_list.get(i).toString()+" is: " +info_req_for_attribute[i]);
		}
		//Create a list that contains attributes with zero gain
		for (int i = 1; i< info_req_for_attribute.length; i++) {
			if(info_req_for_attribute[i]==0.0){
				labelsWithZeroGain.add(attribute_list.get(i));
			}
		}
	
		//test();
		//test2();
		int indexForMAx= getIndexForMaxValueExcludePositionZero(info_req_for_attribute);
		if (indexForMAx>-1){
			System.out.println("Maxinfo gain if selecting: "+attribute_list.get(indexForMAx).toString()+ " that has Info Gain: "+info_req_for_attribute[indexForMAx] );
			return attribute_list.get(indexForMAx);
		}else{
			return null;
		}
	}

	private double[] numberAttributeValues(String[][] D,String value,int column){
		int total[] = new int[6];
		String s = "";
		for (int i = 0; i<D[0].length;i++) {
			s = D[column][i];
			if (s.equals(value)){

				total[5]++;
				switch (D[0][i]) {
					case "UP":
						total[0]++;
						break;
					case "DOWN":
						total[1]++;
						break;
					case "LEFT":
						total[2]++;
						break;
					case "RIGHT":
						total[3]++;
						break;
					case "NEUTRAL":
						total[4]++;
						break;
					default:
						System.out.println("OUUUUP");
						break;
				}
			}
		}
		return Arrays.stream(total).asDoubleStream().toArray();
	}

	private double infoGain(String[][] D, ArrayList<LABEL> attribute_list){
		int[] info_D = new int[5]; //up,down,left,right,neutral
		int rows = D[0].length;
		//numberups
		for (String s : D[0]) {
			switch (s) {
			case "UP":
				info_D[0]++;
				break;
			case "DOWN":
				info_D[1]++;
				break;
			case "LEFT":
				info_D[2]++;
				break;
			case "RIGHT":
				info_D[3]++;
				break;
			case "NEUTRAL":
				info_D[4]++;
				break;
			default:
				System.out.println("OUUUUP");
				break;
			}
		}
		double info_D_result = 0.0;
		for (int i : info_D) {
			double part = (double)i/(double)rows;
			if(part!=0){ //so not log 0 (-infinity)
				info_D_result = info_D_result-(part)*log2(part);
			}
		}
		return info_D_result;
	}
	
	public static double log2(double n)
	{
	    return (Math.log(n) / Math.log(2));
	}
	
	private String[] getLabelAttributeValues(String[][] d, int column){
		ArrayList<String> result = new ArrayList<String>();
		if (column>-1&&d!=null){
			for (int i = 0; i<d[0].length; i++ ){ //Max en massa
				if (!result.contains(d[column][i])){
					result.add(d[column][i]);
				}
			}
		}
		return result.toArray(new String[result.size()]);
	}
	
	
	private void test(){
		//VERY_HIGH pacmanposition
		//VERY_HIGH: UP=975.0  : DOWN=3410.0 : LEFT=1607.0 : RIGHT= 3905.0 :  NEUTRAL=10968.0  :
		double InfoD = -(2666.0/15149.0)*log2(2666.0/15149.0)-(1974.0/15149.0)*log2(1974.0/15149.0)-(4189.0/15149.0)*log2(4189.0/15149.0)-(2409.0/15149.0)*log2(2409.0/15149.0)-(3911.0/15149.0)*log2(3911.0/15149.0);
		//double InfoD = -(2666.0/15149.0)*log2(2666.0/15149.0);
		System.out.println(-(1974.0/15149.0)*log2(1974.0/15149.0));
		System.out.println("INFO D by hand: "+InfoD);
	}
	
	private void test2(){
		//VERY_HIGH pacmanposition
		//VERY_HIGH: UP=975.0  : DOWN=3410.0 : LEFT=1607.0 : RIGHT= 3905.0 :  NEUTRAL=10968.0  :
		double InfoDCurrLevelTime = log2(0.0/1095.0);
		System.out.println();
		System.out.println("INFO D currleveltime by hand: "+InfoDCurrLevelTime);
	}
	private void testNumbers(double[] found,int totalnumbertuples){
		//VERY_HIGH pacmanposition
		//VERY_HIGH: UP=975.0  : DOWN=3410.0 : LEFT=1607.0 : RIGHT= 3905.0 :  NEUTRAL=10968.0  :
		System.out.println("up,down,left,right,neutral,total");
		double sum=0;
		for (int i = 0; i<found.length-1;i++) {
			System.out.print(found[i]+",");
			sum = sum+found[i];
		}
		System.out.println();
		System.out.println("ADD these to "+ sum+" should be equal "+found[5] + " and less than total tuples that is: "+ totalnumbertuples);
	}
	
	private int getIndexForMaxValueExcludePositionZero(double[] d){
		double max = Double.MIN_VALUE;
		int maxIndex =-1;
		for(int i=1;i<d.length;i++){
			if (d[i]>max){
				max = d[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	@Override
	public ArrayList<LABEL> getAttributesWithZeroGain() {
		// TODO Auto-generated method stub
		return labelsWithZeroGain;
	}
}
