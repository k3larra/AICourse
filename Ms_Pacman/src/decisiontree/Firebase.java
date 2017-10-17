package decisiontree;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Firebase {
	public Firebase() {
		try {
			FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");
//			FirebaseOptions options = new FirebaseOptions.Builder()
//			  .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
//			  .setDatabaseUrl("https://try2-405b1.firebaseio.com")
//			  .build();
//			FirebaseApp.initializeApp(options);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
