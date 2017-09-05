package se.mah.k3lara;

import se.mah.k3lara.control.OutputLevel;
import se.mah.k3lara.model.State;

public class Settings {
	public static int nbrRowsColumns = 8;
	public static OutputLevel outPutLevel = OutputLevel.VERBOSE;
	public static State humanPlayerMin= State.BLACK;
	public static State computerPlayerMax= State.WHITE;
	public static boolean isColumnOrRowValueLegal(int rowOrColumn){
		if(rowOrColumn>-1&&rowOrColumn<nbrRowsColumns){
			return true;
		}else{
			return false;
		}
	}
}
