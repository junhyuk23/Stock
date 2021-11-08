package com.example.stock;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PopupActivity extends Activity {
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> user = new HashMap<>(); // 해쉬맵 생성
    String strname;
    String strdate;
    String strstatus;
    String pop;
    int docexists;
    int amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activitiy_popup);




        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df= new SimpleDateFormat("yyyy-MM-dd");


        //데이터 가져오기
        Intent intent = getIntent();
        strname = intent.getStringExtra("name");
        strdate = intent.getStringExtra("date");
        strstatus=intent.getStringExtra("status");
        String num = intent.getStringExtra("num");

        amount=Integer.parseInt(num);

        EditText editText4=(EditText) findViewById(R.id.editText4);
        editText4.setText(strname);
        EditText editText5=(EditText) findViewById(R.id.editText5);
        editText5.setText(strdate);
        EditText editText6=(EditText) findViewById(R.id.editText6);
        editText6.setText(num);

    }

    //확인 버튼 클릭
    public void mOnClose(View v){

        db.collection(strstatus).document(strdate).update(strname, FieldValue.delete());
        db.collection(strstatus)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (strdate.equals(document.getId())) {
                                    document.getData().size();
                                    Toast.makeText(getApplicationContext(), ""+ document.getData().size(), Toast.LENGTH_LONG).show();
                                    if(0==document.getData().size())
                                    db.collection(strstatus).document(strdate).delete();
                                }
                            }
                        }
                    }
                });



        EditText editText4=(EditText) findViewById(R.id.editText4);
        EditText editText5=(EditText) findViewById(R.id.editText5);
        EditText editText6=(EditText) findViewById(R.id.editText6);
        strname=editText4.getText().toString();
        strdate=editText5.getText().toString();
        amount=Integer.parseInt(editText6.getText().toString());
        user.put(strname,amount);

        docexists=0;
        if(amount==0)
        {
        }
        else{
            db.collection(strstatus).document(strdate).set(user);
            db.collection(strstatus)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (strdate.equals(document.getId())) {
                                        db.collection(strstatus).document(strdate).update(strname,amount);
                                        docexists=1;
                                    }
                                }
                                if(docexists==0){
                                    db.collection(strstatus).document(strdate).set(user);
                                }
                            }
                        }
                    });
        }
        ((MainActivity)MainActivity.context_main).bringdata();
        //액티비티(팝업) 닫기
        finish();
    }
    public void mOnClose2(View v){
        //액티비티(팝업) 닫기
        finish();
    }

    public void RecipeBtn(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/results?search_query="+strname+" 요리"));
        startActivity(intent);
    }

    //바깥레이어 클릭시 안닫히게
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    //안드로이드 백버튼 막기
    @Override
    public void onBackPressed() {
        return;
    }



}
