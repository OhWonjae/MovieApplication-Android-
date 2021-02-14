package com.example.startproject2;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {

    EditText name;
    RadioButton Male;
    RadioButton Female;
    int sex;
    EditText Birth;
    EditText email;
    EditText password;
    Calendar myCalendar = Calendar.getInstance();
    ImageView Picture;
    File file;
    public MyFragment() {

    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        Birth.setText(sdf.format(myCalendar.getTime()));
    }
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my, container, false);
        name = rootView.findViewById(R.id.editText);
        Male = rootView.findViewById(R.id.radioButton);
        Male.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sex = 1;
            }
        });
        Female = rootView.findViewById(R.id.radioButton2);
        Female.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sex = 0;
            }
        });
        Birth = rootView.findViewById(R.id.editText2);
        email = rootView.findViewById(R.id.editText3);
        Picture = rootView.findViewById(R.id.imageView4);
        password  = rootView.findViewById(R.id.editText4);
        Birth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(container.getContext(), myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        SharedPreferences sharedPreferences = getContext().getSharedPreferences( "pref", 0);
        String _name = sharedPreferences.getString("name",null);
       name.setText(_name);
        sex = sharedPreferences.getInt("sex",1);
        if(sex==1){Male.setChecked(true);Female.setChecked(false);}
        else{Male.setChecked(false);Female.setChecked(true);}
        String _birth = sharedPreferences.getString("birth",null);
        Birth.setText(_birth );
        String _email = sharedPreferences.getString("email",null);
        email.setText(_email);
        String _password = sharedPreferences.getString("password",null);
        password.setText(_password);
        Picture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 사진
            }
        });
        return rootView;
    }
    File createFile()
    {
        String filename = "capture.jpg";
        File storageDir = getExternalFilesDir(null);
        File outFile = new File(sorageDir,filename);
        return outFile;
    }
    void takePicture()
    {
        if(file==null)
        {
            file = createFile();
        }
        Uri fileUri = FileProvider.getUriForFile(getContext(),"com.example.samplecaptureintent.fileprovider",file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri );
        if(intent.resolveActivity(getPack))
        {

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        SharedPreferences  sharedPreferences = this.getContext().getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        String _name = name.getText().toString();
    //   int _sex = 0;
      // if(Male.isChecked()){_sex = 1;
        //    Log.i("male","male!!");}
      //  else if(Female.isChecked()){_sex = 0;
        //    Log.i("female","female!!");}
        String _birth = Birth.getText().toString();
        String _email = email.getText().toString();
        String _password = password.getText().toString();
        editor.putString("name",_name);
        editor.putInt("sex",sex);
        editor.putString("birth",_birth);
        editor.putString("email",_email);
        editor.putString("password",_password);
        editor.commit();
    }



}
