package decisiontree;

import java.util.ArrayList;

import decisiontree.Constants.LABEL;

public abstract class AttributeSelectionMethod {
	public abstract LABEL method(String[][] D, ArrayList<LABEL> attribute_list);
}
