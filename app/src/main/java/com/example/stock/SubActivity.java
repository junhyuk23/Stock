package com.example.stock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.perfmark.Tag;

public class SubActivity extends Activity {
    String location="";
    Button btn_sub;
    Button btn_scan;
    private IntentIntegrator qrScan;
    String barcodenum;
    String a="";
    int count;
    int size;
    int num;
    Iterator iterator;
    String[] array = null;
    String[] arrkey=null;
    String barcodevalue;
    String barcodekey;

    private static final String TAG = "";
    HashSet<Integer> hash = new HashSet<Integer>();

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        btn_sub = findViewById(R.id.button_sub);
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MainActivity.context_main).bringdata();
                finish();
                }
        });
        btn_scan = findViewById(R.id.button_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {      //화면 전환
                qrScan.initiateScan();
                //Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                //startActivity(intent);

            }
        });

        qrScan = new IntentIntegrator(this);
        qrScan.setPrompt("바코드를 찍어주세요");
        qrScan.setOrientationLocked(false);

        makebutton();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Log.v("qrcode :::::::::::", "no contents");
            } else { //QR코드, 내용 존재
                try {
                    /* QR 코드 내용*/
                    barcodenum = result.getContents();

                    Log.v("", barcodenum);
                    //Toast.makeText(getApplicationContext(), barcodenum, Toast.LENGTH_LONG).show();
                    //Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                    //startActivity(intent);
                    barcodekey="인식불가";
                    barcodevalue="1";
                    barcodedb();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v("Exception :::::::::::::", "QR code fail");
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void barcodedb(){

        db.collection("바코드").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(barcodenum.equals(document.getId())){
                                    Set set=document.getData().keySet();
                                    iterator=set.iterator();
                                    while(iterator.hasNext()){
                                        String key=(String)iterator.next();
                                        barcodevalue=""+document.get(key);
                                        barcodekey=key;
                                    }
                                }
                            }
                            if(barcodekey.equals("인식불가")){
                                Toast.makeText(getApplicationContext(), "저장되지 않은 값", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), barcodenum, Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(getApplicationContext(),SubActivity_2.class);
                                intent.putExtra("name",barcodekey);
                                intent.putExtra("date",barcodevalue);
                                startActivity(intent);
                            }
                        }
                        else{
                            finish();
                        }

                    }
                });

    }
    public void makebutton1(View V){
        location="채소";
        makebutton();
    }
    public void makebutton2(View V){
        location="과일";
        makebutton();
    }
    public void makebutton3(View V){
        location="곡류";
        makebutton();
    }
    public void makebutton4(View V){
        location="유제품";
        makebutton();
    }
    public void makebutton5(View V){
        location="육류";
        makebutton();
    }
    public void makebutton6(View V){
        location="수산물";
        makebutton();
    }
    public void makebutton7(View V){
        location="음료";
        makebutton();
    }
    public void makebutton8(View V){
        location="인스턴트";
        makebutton();
    }
    public void makebutton9(View V){
        location="제과";
        makebutton();
    }
    public void makebutton10(View V){
        location="조미료";
        makebutton();
    }
    public void makebutton11(View V){
        location="반찬";
        makebutton();
    }
    public void makebutton12(View V){
        location="";
        makebutton();
    }
    public void makebutton() {
        db.collection("식재료")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(location!=""){
                                num=0;
                            }
                            count=0;
                            for (QueryDocumentSnapshot document : task.getResult()){
                                count=count+document.getData().size();
                                array=new String[count];
                                arrkey=new String[count];
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(location.equals(document.getId()))
                                {
                                    size=document.getData().size();
                                    array=new String[size];
                                    arrkey=new String[size];
                                    Set set=document.getData().keySet();
                                    iterator=set.iterator();
                                    while(iterator.hasNext()){
                                        String key=(String)iterator.next();
                                        array[num]=""+document.get(key);
                                        arrkey[num]=key;
                                        num++;
                                    }
                                    num=0;
                                }else if(location.equals("")){
                                    Set set=document.getData().keySet();
                                    iterator=set.iterator();
                                    while(iterator.hasNext()){
                                        String key=(String)iterator.next();
                                        array[num]=""+document.get(key);
                                        arrkey[num]=key;
                                        num++;
                                    }
                                }
                            }
                            Layoutmake();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        num=0;
                    }
                });
    }
    public void Layoutmake(){

        final LinearLayout lm = (LinearLayout) findViewById(R.id.ll);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lm.removeAllViews();
        if(location.equals("")){
            size=count;
        }
        for (int j = 0; j < size; j++) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            final Button btn = new Button(this);
            btn.setText(arrkey[j]);
            btn.setLayoutParams(params);
            int finalJ = j;
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View V) {
                    //Toast.makeText(getApplicationContext(), arrkey[finalJ], Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),SubActivity_2.class);
                    intent.putExtra("name",arrkey[finalJ]);
                    intent.putExtra("date",array[finalJ]);
                    startActivity(intent);
                }
            });
            ll.addView(btn);

            j++;

            if (j < size) {
                final Button btn2 = new Button(this);
                btn2.setText(arrkey[j]);
                btn2.setLayoutParams(params);
                btn2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View V) {
                        //Toast.makeText(getApplicationContext(), arrkey[finalJ+1], Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(),SubActivity_2.class);
                        intent.putExtra("name",arrkey[finalJ+1]);
                        intent.putExtra("date",array[finalJ+1]);
                        startActivity(intent);
                    }
                });
                ll.addView(btn2);
            }

            j++;

            if (j < size) {
                final Button btn3 = new Button(this);
                btn3.setText(arrkey[j]);
                btn3.setLayoutParams(params);
                btn3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View V) {
                        //Toast.makeText(getApplicationContext(), arrkey[finalJ+2], Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(),SubActivity_2.class);
                        intent.putExtra("name",arrkey[finalJ+2]);
                        intent.putExtra("date",array
                                [finalJ+2]);
                        startActivity(intent);
                    }
                });
                ll.addView(btn3);
            }
            lm.addView(ll);
        }

    }
}



