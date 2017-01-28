package com.modprobe.profit;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class EditProfileFragment extends Fragment {

	private static final int SELECT_PICTURE = 1;
	private CircularImageView civ;
	private Spinner bodyTypeSpinner;
	private AppCompatEditText fullNameEditText;
	private AppCompatEditText usernameEditText;
	private AppCompatEditText emailEditText;
	private AppCompatEditText ageEditText;
	private AppCompatEditText heightEditText;
	private AppCompatEditText weightEditText;
	private AppCompatEditText diseasesEditText;
	private Button updateButton;
	private String bodyTypeArr[] = { "Slim", "Fit", "Plum", "Obese" };
	private String updatedImagePath;
	public EditProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_edit_profile,
				container, false);
		fullNameEditText = (AppCompatEditText) rootView
				.findViewById(R.id.fullname);
		usernameEditText = (AppCompatEditText) rootView
				.findViewById(R.id.username);
		emailEditText = (AppCompatEditText) rootView.findViewById(R.id.email);
		ageEditText = (AppCompatEditText) rootView.findViewById(R.id.age);
		heightEditText = (AppCompatEditText) rootView.findViewById(R.id.height);
		weightEditText = (AppCompatEditText) rootView.findViewById(R.id.weight);
		diseasesEditText = (AppCompatEditText) rootView
				.findViewById(R.id.diseases);
		updateButton = (Button) rootView.findViewById(R.id.profile_save);
		bodyTypeSpinner = (Spinner) rootView
				.findViewById(R.id.body_type_spinner);
		String[] items = new String[] { "Slim", "Fit", "Plum", "Obese" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bodyTypeSpinner.setAdapter(adapter);

		bodyTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				TextView selectedText = (TextView) parent.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.WHITE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		civ = (CircularImageView) rootView.findViewById(R.id.profile_image);
		setupEditFields();
		civ.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				// in onCreate or any event where your want the user to
				// select a file
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);
			}
		});

		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (updateSharedPreferences()) {
					FragmentManager fragmentManager = getActivity()
							.getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.remove(EditProfileFragment.this);
					fragmentManager.popBackStack(null,
							FragmentManager.POP_BACK_STACK_INCLUSIVE);

					MainActivityFragment mainActivityFragment = new MainActivityFragment();
					fragmentTransaction.replace(R.id.container,
							mainActivityFragment).commit();
				}

			}
		});
		return rootView;

	}

	// UPDATED
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == AppCompatActivity.RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				setupImageViewAndUpdateImagePath(selectedImageUri);

			}
		}
	}

	protected void setupEditFields() {
		SharedPreferences sharedPreferences = AppController.getInstance().prefs;
		if (sharedPreferences.getBoolean("profile_completed", false)) {
			usernameEditText.setEnabled(false);
			emailEditText.setEnabled(false);
		}
		if (sharedPreferences.getString("image", null) != null
				&& !sharedPreferences.getString("image", null).equals("")) {
			Log.e("LOGS", sharedPreferences.getString("image", null));
			setupImageViewFromFile(sharedPreferences.getString("image", null));
			// TODO
		}
		fullNameEditText.setText(sharedPreferences.getString("name", ""));
		usernameEditText.setText(sharedPreferences.getString("username", ""));
		emailEditText.setText(sharedPreferences.getString("email", ""));
		if (sharedPreferences.getInt("age", -1) != -1) {
			ageEditText.setText(sharedPreferences.getInt("age", 0) + "");
		}
		if (sharedPreferences.getInt("height", -1) != -1) {
			heightEditText.setText(sharedPreferences.getInt("height", 0) + "");
		}
		if (sharedPreferences.getInt("weightKg", -1) != -1) {
			weightEditText
					.setText(sharedPreferences.getInt("weightKg", 0) + "");
		}
		if (sharedPreferences.getInt("bodyType", -1) != -1) {
			bodyTypeSpinner.setSelection(sharedPreferences
					.getInt("bodyType", 0));
		}
		diseasesEditText.setText(sharedPreferences.getString("diseases", ""));
	}

	protected boolean updateSharedPreferences() {
		String imageUri;
		String name = fullNameEditText.getText().toString();
		String email = emailEditText.getText().toString();
		String username = usernameEditText.getText().toString();
		String sage = ageEditText.getText().toString();
		String sheight = heightEditText.getText().toString();
		String sweight = weightEditText.getText().toString();
		int bodyType = bodyTypeSpinner.getSelectedItemPosition();
		String diseases = diseasesEditText.getText().toString();
		int age, height, weight;
		if (updatedImagePath == null || updatedImagePath.equals("")) {
			imageUri = "";
		} else {
			imageUri = updatedImagePath;
		}
		if (name == null || name.equals("") || email == null
				|| email.equals("") || username == null || username.equals("")
				|| sage == null || sage.equals("") || sheight == null
				|| sheight.equals("") || sweight == null || sweight.equals("")) {
			Toast.makeText(getActivity(), "Some fields are empty",
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			age = Integer.parseInt(sage);
			height = Integer.parseInt(sheight);
			weight = Integer.parseInt(sweight);
		}
		if (diseases == null) {
			diseases = "";
		}
		float bmi = (weight * 1.0f) / ((height / 100.0f) * (height / 100.0f));
		Log.e("LOGS_SAVE->", imageUri);
		Editor e = AppController.getInstance().prefs.edit();
		e.putFloat("bmi", bmi);
		e.putString("name", name);
		e.putInt("age", age);
		e.putInt("height", height);
		e.putInt("weightKg", weight);
		e.putString("diseases", diseases);
		e.putString("email", email);
		e.putString("username", username);
		e.putInt("bodyType", bodyType);
		e.putString("image", imageUri);
		e.putBoolean("profile_completed", true);
		e.commit();
		e.apply();
		Toast.makeText(getActivity(), "Profile updated!", Toast.LENGTH_SHORT)
				.show();
		return true;
	}

	public void setupImageViewAndUpdateImagePath(Uri uri) {
		Bitmap bitmap;
		try {
			bitmap = MediaStore.Images.Media.getBitmap(getActivity()
					.getContentResolver(), uri);
			civ.setImageBitmap(bitmap);
			File profile_image = createTemporaryFile("profile_image", "jpg");
			FileOutputStream outStream = new FileOutputStream(profile_image);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			//bitmap.recycle();
			outStream.flush();
			outStream.close();
			updatedImagePath = profile_image.getAbsolutePath();
		} catch (Exception e) {
			Toast.makeText(getActivity(),
					"Failed to set Image.Please try again", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}

	}

	public void setupImageViewFromFile(String filePath) {
		try {
			File imgFile = new File(filePath);
			if (imgFile.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
				civ.setImageBitmap(myBitmap);
				//myBitmap.recycle();
			}

		} catch (Exception e) {
			Toast.makeText(getActivity(),
					"Failed to set Image.Please try again", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}
	}

	private File createTemporaryFile(String part, String ext) throws Exception {
		File tempDir = Environment.getExternalStorageDirectory();
		tempDir = new File(tempDir.getAbsolutePath() + "/profit_images/");
		if (!tempDir.exists()) {
			tempDir.mkdir();
		}
		return File.createTempFile(part, ext, tempDir);
	}
}