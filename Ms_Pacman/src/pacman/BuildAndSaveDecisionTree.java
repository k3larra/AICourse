package pacman;

import decisiontree.SaveTreeToFirebase;
import decisiontree.TrainModel;

public class BuildAndSaveDecisionTree {

	public static void main(String[] args) {
		//DataTuple[] tuples = DataSaverLoader.LoadPacManData();
		//new TrainModel();
		String[][] trainMatrix= decisiontree.Constants.trainingMatrixRaw;
		decisiontree.Constants.exportDataRawAsNumberString(trainMatrix,"rawTrainingNumbers.txt");
		String[][] testMatrix= decisiontree.Constants.testMatrixRaw;
		decisiontree.Constants.exportDataRawAsNumberString(testMatrix,"rawTestNumbers.txt");	
	}
}
