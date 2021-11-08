package com.example.stock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubActivity_2 extends Activity {
    private static final String TAG = "";
    Map<String, Object> user = new HashMap<>();
    Button btn3;
    Button back;
    String status="냉장";
    int docexists=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df= new SimpleDateFormat("yyyy-MM-dd");

        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String date=intent.getStringExtra("date");

        RadioButton radioButton1,radioButton2,radioButton3;

        cal.add(Calendar.DATE,Integer.parseInt(date));

        EditText editText1=(EditText) findViewById(R.id.editText1);
        editText1.setText(name);
        EditText editText2=(EditText) findViewById(R.id.editText2);
        editText2.setText(df.format(cal.getTime()));
        EditText editText3=(EditText) findViewById(R.id.editText3);
        editText3.setText("1");

        radioButton1 = findViewById(R.id.radioButton1);
        radioButton1.setChecked(true);
        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });

        btn3 = findViewById(R.id.button_sub3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButton1.isChecked()){
                    status="냉장";
                }
                else if(radioButton2.isChecked()){
                    status="냉동";
                }
                else{
                    status="실온";
                }
                String strname=editText1.getText().toString();
                int amount=Integer.parseInt(editText3.getText().toString());
                String savedate=editText2.getText().toString();
                user.put(strname,amount);

                //if(
                docexists=0;
                db.collection(status)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (savedate.equals(document.getId())) {
                                            db.collection(status).document(savedate).update(strname,amount);
                                            docexists=1;
                                        }
                                    }
                                    if(docexists==0){
                                        db.collection(status).document(savedate).set(user);
                                    }
                                }
                            }
                        });


                Toast.makeText(getApplicationContext(),strname+" "+amount+"개 "+status+"보관", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        back= findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}