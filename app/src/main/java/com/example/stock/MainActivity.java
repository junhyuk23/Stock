package com.example.stock;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "";
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> user = new HashMap<>(); // 해쉬맵 생성
    Button btn_main; // 버튼객체생성
    String a="";
    String location="냉장";
    int num=0;
    int count=0;
    int size=0;
    long diffDay=2;

    String startdate="";
    String enddate="";
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    Iterator iterator;
    String[] arrkey = null;
    String[] arrvalue=null;
    String[] arrdoc=null;

    public static Context context_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_main = findViewById(R.id.button_main);
        btn_main.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {      //화면 전환
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent);
            }
        });
        context_main=this;
        bringdata();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void makelayout() throws ParseException, ParseException {
        final LinearLayout lm = (LinearLayout) findViewById(R.id.ll);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lm.removeAllViews();


        Calendar cal=Calendar.getInstance();
        // LinearLayout 생성 (버튼클릭했을 시)
        for (int j = 0; j < size; j++) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            enddate=arrdoc[j];

            //Date Startdate = sdf.parse(startdate);
            //Date Enddate = sdf.parse(enddate);
            final Date targetDate = sdf.parse(enddate);
            final Date todayDate = new Date();
            final String todayDay = sdf.format(todayDate);
            diffDay = (targetDate.getTime() - todayDate.getTime())/ (24*60*60*1000) +1;

            // TextView 생성 - D-day
            TextView tvDay = new TextView(this);
            tvDay.setText("   D-" +diffDay+ " ");
            tvDay.setTextColor(0xAA000000);
            tvDay.setWidth(220);
            ll.addView(tvDay);

            // TextView 생성 - 재료명
            TextView tvName = new TextView(this);
            tvName.setText("   " + arrkey[j] + " ");
            tvName.setTextColor(0xAA000000);
            tvName.setWidth(250);
            ll.addView(tvName);

            // TextView 생성 - 갯수
            TextView tvNum = new TextView(this);
            tvNum.setText(" " + arrvalue[j] + " ");
            tvNum.setTextColor(0xAA000000);
            tvNum.setWidth(200);
            ll.addView(tvNum);


            // 버튼 생성 - 수정
            final Button btn = new Button(this);
            int btnID = j+1;
            // setId 버튼에 대한 키값
            btn.setId(btnID);
            btn.setText("수정/상세");
            btn.setWidth(20);
            btn.setLayoutParams(params);


            /*
            final ImageButton btn2 = new ImageButton(this);
            int btn2ID = j+1;
            btn2.setId(btn2ID);
            btn2.setBackgroundResource((R.drawable.settings));
            btn2.setLayoutParams(new LayoutParams(80,80));
             */


            int finalJ = j;

            btn.setOnClickListener(new OnClickListener() {
                public void onClick(View V) {
                    Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                    intent.putExtra("name",arrkey[finalJ]);
                    intent.putExtra("date",arrdoc[finalJ]);
                    intent.putExtra("num",arrvalue[finalJ]);
                    intent.putExtra("status",location);
                    startActivity(intent);
                }
            });

            //버튼 add
            ll.addView(btn);
            //LinearLayout 정의된거 add
            lm.addView(ll);
        }
    }


    public void Button1(View V){
        location="냉장";
        bringdata();
    }
    public void Button2(View V){
        location="냉동";
        bringdata();
    }
    public void Button3(View V){
        location="실온";
        bringdata();
    }
    public void bringdata(){

        //db.collection("식재료").document().delete();
        size=0;
        db.collection(location)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                size=size+document.getData().size();
                                arrvalue=new String[size];
                                arrkey=new String[size];
                                arrdoc=new String[size];
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Set set=document.getData().keySet();
                                iterator=set.iterator();
                                while(iterator.hasNext()) {
                                    String key = (String) iterator.next();
                                    arrkey[count] = key;
                                    arrvalue[count] = "" + document.get(key);
                                    arrdoc[count] = document.getId();
                                    count++;
                                }
                                //a=a+"\n"+document.getData().toString();
                                //a=a+"\n"+document.getData().keySet().toString();
                                //num++;
                            }
                            count=0;
                            try {
                                makelayout();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                        else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        //TextView textView = findViewById(R.id.text1);
                        //textView.setText(a);
                        //textView.setMovementMethod(new ScrollingMovementMethod());
                    }

                });


    }
}