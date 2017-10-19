package decisiontree;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import decisiontree.Constants.LABEL;
import decisiontree.Constants.LEAF;
import pacman.game.Constants.MOVE;

public class SaveTreeToFirebase {
		   private static final String DATABASE_URL = "https://try2-405b1.firebaseio.com";
		   private static DatabaseReference database;
		   public static void main(String[] args) throws IOException {
			   org.apache.log4j.BasicConfigurator.configure();
		        try {
		            FileInputStream serviceAccount = new FileInputStream("myData/try2-194acfc32e89.json");
		            FirebaseOptions options = new FirebaseOptions.Builder()
		                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		                    //.setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
		                    .setDatabaseUrl(DATABASE_URL)
		                    .build();
		            FirebaseApp.initializeApp(options);
		        } catch (IOException e) {
		            System.out.println("ERROR: invalid service account credentials. See README.");
		            System.out.println(e.getMessage());
		        }
		        database = FirebaseDatabase.getInstance().getReference();
		        Node n = SaveTree.loadTree();
		        database.child("DecisionTree2").child(n.getLabelData().toString()).setValue(n,new DatabaseReference.CompletionListener() {
					@Override
					public void onComplete(DatabaseError error, DatabaseReference ref) {
						// TODO Auto-generated method stub
						System.out.println("Done: "+error.getDetails());
					}
				});
		        
		        try {
					Thread.sleep(6000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		   
}
