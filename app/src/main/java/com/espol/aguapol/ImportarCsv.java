package com.espol.aguapol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

public class ImportarCsv extends AppCompatActivity {
    Button btnImportar;
    Context context=this;
    HashMap<String,HashMap<String,String>> tramo1=new HashMap<>();
    HashMap<String,HashMap<String,String>> tramo2=new HashMap<>();
    HashMap<String,HashMap<String,String>> tramo3=new HashMap<>();
    HashMap<String,HashMap<String,String>> tramo4=new HashMap<>();
    HashMap<String,HashMap<String,String>> tramo5=new HashMap<>();
    HashMap<String,HashMap<String,String>> tramo6=new HashMap<>();
    HashMap<String,HashMap<String,String>> tramo7=new HashMap<>();
    HashMap<String,HashMap<String,String>> tramo8=new HashMap<>();
    HashMap<String,HashMap<String,String>> tramo9=new HashMap<>();
    FirebaseFirestore firebaseFirestore;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar_csv);
        pedirPermisos();
        btnImportar=findViewById(R.id.btnImportar);

        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //importarCSV();
                subirPromedio();
            }
        });
    }

    public void pedirPermisos() {
        // PERMISOS PARA ANDROID 6 O SUPERIOR
        if(ContextCompat.checkSelfPermission(ImportarCsv.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ImportarCsv.this, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);

        }
    }

    public void subirPromedio() {
        List<String> tramos = new ArrayList<>();
        tramos.add("tramo 3");
        tramos.add("tramo 4");
        tramos.add("tramo 5");
        tramos.add("tramo 6");
        tramos.add("tramo 7");
        tramos.add("tramo 8");
        tramos.add("tramo 9");
        for (String i : tramos) {
           /* CollectionReference tramo1 = db.collection(i);
            tramo1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
                            HashMap<String, String> valores = (HashMap<String, String>) data.get("valores");
                            Double suma = 0.0;
                            int num = 0;
                            for (Map.Entry<String, String> map : valores.entrySet()) {
                                Double valor = Double.parseDouble(map.getValue());
                                suma = suma + valor;
                                num++;
                            }
                            Double promedio = suma / num;
                            data.put("promedio", promedio);
                            tramo1.document(document.getId()).update("promedio", promedio).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });
        }

            */


        }
    }

}