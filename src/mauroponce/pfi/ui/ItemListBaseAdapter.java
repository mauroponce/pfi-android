package mauroponce.pfi.ui;

import java.util.List;

import mauroponce.pfi.domain.Student;
import mauroponce.pfi.utils.AppConstants;
import mauroponce.pfi.utils.FileUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ItemListBaseAdapter extends BaseAdapter {

	private static List<Student> students;
	private LayoutInflater l_Inflater;
	
	public ItemListBaseAdapter(Context context, List<Student> studentsList) {
		students = studentsList;
		l_Inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return students.size(); 
	}

	@Override
	public Object getItem(int position) {
		return students.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.item_details_view, null);
			holder = new ViewHolder();
			holder.txtFullName = (TextView) convertView.findViewById(R.id.full_name);
			holder.txtLU = (TextView) convertView.findViewById(R.id.lu);
			holder.photo = (ImageView) convertView.findViewById(R.id.photo);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtFullName.setText(students.get(position).getFullName());
		holder.txtLU.setText(students.get(position).getLU() + "");
		
		byte[] imageAsBytes = FileUtils.decodeFileBase64(students.get(position).getEncodedImage());
		
		Bitmap studentBitmap = BitmapFactory
				.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
		holder.photo.setImageBitmap(Bitmap.createScaledBitmap(studentBitmap, AppConstants.WIDTH_UADE, AppConstants.HEIGHT_UADE, false));

		return convertView;
	}
	static class ViewHolder {
		TextView txtFullName;
		TextView txtLU;
		ImageView photo;
	}
}
