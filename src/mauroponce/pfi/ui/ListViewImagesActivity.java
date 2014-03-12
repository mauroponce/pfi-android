package mauroponce.pfi.ui;

import java.util.ArrayList;
import java.util.List;

import mauroponce.pfi.domain.Student;
import mauroponce.pfi.service.RecognitionService;
import mauroponce.pfi.service.RemoteService;
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
	private static final int COURSE_NUMBER = 3;
	private Button btnAccept;

	@Override
    public void onCreate(Bundle savedInstanceState) {		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        final Context context = this;
        
        List<Student> students = getStudents();
        
        final ListView lv1 = (ListView) findViewById(R.id.listV_main);
        lv1.setAdapter(new ItemListBaseAdapter(this, students));
        
        lv1.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
        		Object item = lv1.getItemAtPosition(position);
        		final Student student = (Student)item;
//        		Toast.makeText(ListViewImagesActivity.this, "Alumno seleccionado: " + " " + student.getFullName(), Toast.LENGTH_LONG).show();
        		AlertDialog.Builder dialog = new AlertDialog.Builder(context);  
			    dialog.setTitle("Confirmación");  
				dialog.setMessage("¿Desea pasar asistencia a "+student.getFullName()+"?");            
				dialog.setCancelable(false);  
				dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
				    public void onClick(DialogInterface dialogo1, int id) {  
				        saveAttendance(student.getLU());
				        Toast.makeText(ListViewImagesActivity.this, "Pasada asistencia a: " + " " + student.getFullName(), Toast.LENGTH_LONG).show();
				        // if NO selecciono el primero
        					// desea enviar la imagen para entrenamiento?
				        	// si
				        		// conseguir donde esta la imagen actual
				        		// pasar imagen actual a base64 y pegarle al servicio sendTrainingData
				        		// post /attendance/send_training_data pasando json con studentLu, encodedImageBase64, fileExtension
        					
        					
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
    	remoteService.saveAttendance(studentLu, COURSE_NUMBER);		
	}  

	private List<Student> getStudents() { 	
		Intent intent = getIntent();
		List<Integer> studentLus = intent.getIntegerArrayListExtra(CameraActivity.STUDENTS_LUS_ARRAY);		
        
        RemoteService remoteService= RemoteService.GetInstance(ListViewImagesActivity.this);
    	return remoteService.getStudents(studentLus);
	}
	
	private List<Student> getCourseStudents() {		
		RemoteService remoteService= RemoteService.GetInstance(ListViewImagesActivity.this);
		return remoteService.getCourseStudents(COURSE_NUMBER);
	}
    
	private List<Student> getStudentsFromWebService(){
		List<Student> results = new ArrayList<Student>();
		Student studentMauro = new Student();
		studentMauro.setFirstName("Mauro Gabriel");
		studentMauro.setLastName("Ponce");
		studentMauro.setLU(131445);
		studentMauro.setEncodedImage("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");
		
		Student studentCho = new Student();
		studentCho.setFirstName("Juan Isaías");
		studentCho.setLastName("Valentino");
		studentCho.setEncodedImage("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");
		studentCho.setLU(131455);
		
		results.add(studentMauro);
		results.add(studentCho);
		
		return results;
	}
}
