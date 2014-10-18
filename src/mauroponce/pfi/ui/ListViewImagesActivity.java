package mauroponce.pfi.ui;

import java.util.ArrayList;
import java.util.List;

import mauroponce.pfi.domain.Student;
import mauroponce.pfi.service.ApplicationDataService;
import mauroponce.pfi.service.RemoteService;
import mauroponce.pfi.utils.FileUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewImagesActivity extends Activity {
	private Button btnAccept;
	ApplicationDataService applicationDataService;

	@Override
    public void onCreate(Bundle savedInstanceState) {		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);	
        applicationDataService = ApplicationDataService.getInstance();
        final Context context = this;
        
        List<Student> students = getStudents();
        
        final ListView lv1 = (ListView) findViewById(R.id.listV_main);
        lv1.setAdapter(new ItemListBaseAdapter(this, students));
        
        lv1.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
        		Object item = lv1.getItemAtPosition(position);
        		final Student student = (Student)item;
        		final Integer row = position;
//        		Toast.makeText(ListViewImagesActivity.this, "Alumno seleccionado: " + " " + student.getFullName(), Toast.LENGTH_LONG).show();
        		AlertDialog.Builder dialog = new AlertDialog.Builder(context);  
			    dialog.setTitle("Confirmación");  
				dialog.setMessage("¿Desea pasar asistencia a "+student.getFullName()+"?");            
				dialog.setCancelable(false);  
				dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
				    public void onClick(DialogInterface dialogo1, int id) {
				        Toast.makeText(ListViewImagesActivity.this, "Pasada asistencia a: " + " " + student.getFullName(), Toast.LENGTH_LONG).show();
				        if (row > 0){
					        AlertDialog.Builder sendImagedialog = new AlertDialog.Builder(context);  
						    sendImagedialog.setTitle("Confirmación");  
							sendImagedialog.setMessage("¿Desea enviar la imagen de "+student.getFullName()+" para entrenamiento?");            
							sendImagedialog.setCancelable(false);  
							sendImagedialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
							    public void onClick(DialogInterface dialogo1, int id) {
						        	sendTrainingData(student.getLU());
							        Toast.makeText(ListViewImagesActivity.this, "Imagen enviada de: " + " " + student.getFullName(), Toast.LENGTH_LONG).show();	
							        //start camera
							        Intent intent = new Intent(ListViewImagesActivity.this, CameraActivity.class);
							        startActivity(intent);
							    }					
							});  
							sendImagedialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
							    public void onClick(DialogInterface dialogo1, int id) {
							        //start camera
							        Intent intent = new Intent(ListViewImagesActivity.this, CameraActivity.class);
							        startActivity(intent);
							    }  
							});            
							sendImagedialog.show(); 
				        }else{
				        	//start camera
				        	Intent intent = new Intent(ListViewImagesActivity.this, CameraActivity.class);
				        	startActivity(intent);
				        }				        
				    }					
				});  
				dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
				    public void onClick(DialogInterface dialogo1, int id) {
				    }  
				});            
				dialog.show();
        	}  
        });
        
        btnAccept = (Button)findViewById(R.id.buttonMoreStudents);
        
        btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		        List<Student> students = getCourseStudents();		        
//		        final ListView lv1 = (ListView) findViewById(R.id.listV_main);
		        if (students != null){
		        	lv1.setAdapter(new ItemListBaseAdapter(context, students));
		        }
			}     
        });
        
    }
	
	private void saveAttendance(Integer studentLu) {
        RemoteService remoteService= RemoteService.GetInstance(ListViewImagesActivity.this);
    	remoteService.saveAttendance(studentLu, applicationDataService.getCourseNumber());		
	}  
	
	private void sendTrainingData(Integer studentLu) { 	
		Intent intent = getIntent();
		String imagePath = intent.getStringExtra(CameraActivity.IMAGE_PATH);
		RemoteService remoteService= RemoteService.GetInstance(ListViewImagesActivity.this);
		String encodedImageBase64 = FileUtils.encodeFileBase64(imagePath);
		remoteService.sendTrainingData(studentLu, encodedImageBase64 , "jpg");		
	}  

	private List<Student> getStudents() { 	
		Intent intent = getIntent();
		List<Integer> studentLus = intent.getIntegerArrayListExtra(CameraActivity.STUDENTS_LUS_ARRAY);		
        List<Student> courseStudents = this.getCourseStudents();
        List<Student> aux = new ArrayList<Student>();
        for (Integer studentLu : studentLus) {			
	        for (Student student : courseStudents) {
				if (student.getLU().equals(studentLu)){
					aux.add(student);
				}
			}
        }
        return aux;
	}
	
	private List<Student> getCourseStudents() {		
		return applicationDataService.getCourseStudents();
	}
}
