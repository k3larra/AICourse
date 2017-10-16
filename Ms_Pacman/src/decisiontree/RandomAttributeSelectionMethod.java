package decisiontree;

import java.util.ArrayList;

import decisiontree.Constants.LABEL;

public class RandomAttributeSelectionMethod extends AttributeSelectionMethod {

	@Override
	public LABEL method(String[][] D, ArrayList<LABEL> attribute_list) {
		// TODO Auto-generated method stub
		int i = (int)(Math.random()*(attribute_list.size())-1);
		return attribute_list.get(i+1);
		//Implement
	}

	@Override
	public ArrayList<LABEL> getAttributesWithZeroGain() {
		// TODO Auto-generated method stub
		return null;
	}

}
