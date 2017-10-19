package decisiontree;

import java.util.ArrayList;
import java.util.Arrays;

import decisiontree.Constants.LABEL;

public class ID3AttributeSelectionMethod extends AttributeSelectionMethod {
	private boolean outputInfo = false;
	ArrayList<LABEL> labelsWithZeroGain = new ArrayList<LABEL>();
	public ID3AttributeSelectionMethod(boolean outputInfo) {
		this.outputInfo = outputInfo;
	}
	@Override
	public LABEL method(String[][] D, ArrayList<LABEL> attribute_list) {
		double info_req_for_attribute[]= new double[attribute_list.size()]; 
		info_req_for_attribute[0]=infoGain(D, attribute_list); ////so place 0 is is infoGain total;
		for (int i = 1; i<attribute_list.size();i++){
			String[] attributeValues = getLabelAttributeValues(D,i);
			int total = 0;
			double info_req_for_attribute_value=0.0;
			for (String attributeValue : attributeValues) {
				//found holds total number of class values for an attribute value (found[5]) and 
				//and how the class values are distributed over the attribute values
				info_req_for_attribute_value=0.0;
				double[] found = numberAttributeValues(D,attributeValue,i);
				int numberTuples = D[0].length;
				double infodj = 0.0;
				for (int j=0;j<found.length-1;j++) {
					if ( found[j]!=0){ //so not log 0 (-infinity)
						infodj = infodj-(found[j]/found[5])*log2(found[j]/found[5]);
					}
				}
				info_req_for_attribute_value=(found[5]/numberTuples)*infodj;
			}
			info_req_for_attribute[i]=info_req_for_attribute[i]+info_req_for_attribute_value;
		}
		//Gain for attributes (InfoD - Info attr); (pos 0 is InfoD
		for (int i = 1; i< info_req_for_attribute.length; i++) {
			info_req_for_attribute[i] = info_req_for_attribute[0] - info_req_for_attribute[i];
		}
		labelsWithZeroGain.clear();
		//Create a list that contains attributes with zero gain
		for (int i = 1; i< info_req_for_attribute.length; i++) {
			if(info_req_for_attribute[i]==0.0){
				labelsWithZeroGain.add(attribute_list.get(i));
			}
		}
		if(outputInfo){
			for(int j=0; j<attribute_list.size(); j++){
				System.out.println("Attribute: "+attribute_list.get(j).name()+ " has gain: "+info_req_for_attribute[j]);
			}
		}
		int indexForMAx= getIndexForMaxValueExcludePositionZero(info_req_for_attribute);
		if (indexForMAx>-1){
			if(outputInfo){System.out.println("Maxinfo gain if selecting: "+attribute_list.get(indexForMAx).toString()+ " that has Info Gain: "+info_req_for_attribute[indexForMAx]);}
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
