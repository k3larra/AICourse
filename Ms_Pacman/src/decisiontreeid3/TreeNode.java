package decisiontreeid3;

import java.util.Collection;

import dataRecording.DataTuple;
import decisiontreeid3.Constants.LABEL;

public class TreeNode<LABEL> {
	 LABEL Data;
	 Collection<TreeNode<LABEL>> myChildren;
	 public TreeNode(LABEL data, DataTuple[] tuples, Collection<LABEL> labels) {
		super();
		Data = data;
	 }

	
}
