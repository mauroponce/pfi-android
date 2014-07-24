package mauroponce.pfi.ui;

import mauroponce.pfi.service.ApplicationDataService;
import mauroponce.pfi.service.DetectionService;
import mauroponce.pfi.utils.AppConstants;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    
	EditText editTextUsr;
	Button btnAccept;
	ApplicationDataService applicationDataService;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // INITIALIZE APPLICATION
		initializeApplication();
        
        editTextUsr = (EditText)findViewById(R.id.editTextUsr);
        btnAccept = (Button)findViewById(R.id.buttonAccept);
        
        btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(getPublicIpAdress());
				String usr = editTextUsr.getText().toString().trim();
				applicationDataService.logIn(usr, MainActivity.this);
				
				//start camera
				Intent intent = new Intent(MainActivity.this, CameraActivity.class);
				//intent.putExtra("lus", lus);
		        startActivity(intent);
			}
        });               
    }

	private void initializeApplication() {
		SharedPreferences preferences = this.getSharedPreferences(AppConstants.PREFERENCES, MODE_PRIVATE);	
        applicationDataService = ApplicationDataService.getInstance();
        applicationDataService.initialize(preferences, MainActivity.this);
		//When the appliction starts we need to save in the storage the haar classifier to can get it for the detection
		DetectionService.saveHaarCascadeClasifierToInternalStorage(MainActivity.this);
	}    
   
    private String getPublicIpAdress(){
    	WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
    	WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
    	int ip = wifiInfo.getIpAddress();
    	return Formatter.formatIpAddress(ip);
    }
}