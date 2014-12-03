package poncemoral.pfi.ui;

import poncemoral.pfi.service.ApplicationDataService;
import poncemoral.pfi.service.DetectionService;
import poncemoral.pfi.utils.AppConstants;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    
	EditText editTextUsr;
	Button btnAccept;
	Button btnOptions;
	ApplicationDataService applicationDataService;
	Handler loginHandler;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // INITIALIZE APPLICATION
		initializeApplication();
		loginHandler = new Handler();
        editTextUsr = (EditText)findViewById(R.id.editTextUsr);
        btnAccept = (Button)findViewById(R.id.buttonAccept);
        btnOptions = (Button)findViewById(R.id.buttonOptions);
        
        btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String usr = editTextUsr.getText().toString().trim();
				MainActivity mainActivity = (MainActivity) v.getContext();
				mainActivity.login(usr);
			}
        });
        
        btnOptions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
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

	public void login(final String usr) {
		final ProgressDialog loginDialog = ProgressDialog.show(MainActivity.this, "Iniciando Sesión",	"Por favor espere...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				applicationDataService.logIn(usr, MainActivity.this);
				loginDialog.dismiss();
				//start camera
				Intent intent = new Intent(MainActivity.this, CameraActivity.class);
				//intent.putExtra("lus", lus);
				startActivity(intent);
				
//				loginHandler.post(new Runnable() {
//					public void run() {						
//					}
//				});
			}
		}).start();
	}
}