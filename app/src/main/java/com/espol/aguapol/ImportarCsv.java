package com.espol.aguapol;

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
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar_csv);
        pedirPermisos();
        btnImportar=findViewById(R.id.btnImportar);

        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importarCSV();
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

    public void importarCSV() {
        String cadena;
        String[] arreglo;

        try {
            FileReader fileReader = new FileReader(context.getExternalFilesDir(null) + "/fakedata.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((cadena = bufferedReader.readLine()) != null) {

                arreglo = cadena.split(",");
                String fechaYhora=arreglo[10];
                String[] arreglo2=fechaYhora.split(" ");
                String fecha=arreglo2[0];
                String hora= arreglo2[1];

                String valorT1=arreglo[1];
                String valorT2=arreglo[2];
                String valorT3=arreglo[3];
                String valorT4=arreglo[4];
                String valorT5=arreglo[5];
                String valorT6=arreglo[6];
                String valorT7=arreglo[7];
                String valorT8=arreglo[8];
                String valorT9=arreglo[9];
                /*

                if (!tramo2.containsKey(fecha)){
                    HashMap<String,String> v2=new HashMap<>();
                    v2.put(hora,valorT2);
                    tramo2.put(fecha,v2);
                }
                else{
                    HashMap<String,String> v1=tramo2.get(fecha);
                    v1.put(hora,valorT2);

                }


                if (!tramo3.containsKey(fecha)){
                    HashMap<String,String> v3=new HashMap<>();
                    v3.put(hora,valorT3);
                    tramo3.put(fecha,v3);
                }
                else{
                    HashMap<String,String> v3=tramo3.get(fecha);
                    v3.put(hora,valorT3);
                }


                if (!tramo4.containsKey(fecha)){
                    HashMap<String,String> v4=new HashMap<>();
                    v4.put(hora,valorT4);
                    tramo4.put(fecha,v4);
                }
                else{
                    HashMap<String,String> v4=tramo4.get(fecha);
                    v4.put(hora,valorT4);
                }

                if (!tramo5.containsKey(fecha)){
                    HashMap<String,String> v5=new HashMap<>();
                    v5.put(hora,valorT5);
                    tramo5.put(fecha,v5);
                }
                else{
                    HashMap<String,String> v5=tramo5.get(fecha);
                    v5.put(hora,valorT5);
                }

                if (!tramo6.containsKey(fecha)){
                    HashMap<String,String> v6=new HashMap<>();
                    v6.put(hora,valorT6);
                    tramo6.put(fecha,v6);
                }
                else{
                    HashMap<String,String> v6=tramo6.get(fecha);
                    v6.put(hora,valorT6);
                }

                if (!tramo7.containsKey(fecha)){
                    HashMap<String,String> v7=new HashMap<>();
                    v7.put(hora,valorT7);
                    tramo7.put(fecha,v7);
                }
                else{
                    HashMap<String,String> v7=tramo7.get(fecha);
                    v7.put(hora,valorT7);
                }



                if (!tramo8.containsKey(fecha)){
                    HashMap<String,String> v8=new HashMap<>();
                    v8.put(hora,valorT8);
                    tramo8.put(fecha,v8);
                }
                else{
                    HashMap<String,String> v5=tramo8.get(fecha);
                    v5.put(hora,valorT8);
                }

                 */

                if (!tramo9.containsKey(fecha)){
                    HashMap<String,String> v5=new HashMap<>();
                    v5.put(hora,valorT9);
                    tramo9.put(fecha,v5);
                }
                else{
                    HashMap<String,String> v9=tramo9.get(fecha);
                    v9.put(hora,valorT9);
                }




            }
            FirebaseDatabase database= FirebaseDatabase.getInstance();
            /*DatabaseReference ref2= database.getReference("Control caudal").child("tramo B-C").child("historial");
            ref2.setValue(tramo2);
            DatabaseReference ref3= database.getReference("Control caudal").child("tramo C-D").child("historial");
            ref3.setValue(tramo3);

            DatabaseReference ref4= database.getReference("Control caudal").child("tramo D-E").child("historial");
            ref4.setValue(tramo4);
            DatabaseReference ref5= database.getReference("Control caudal").child("tramo E-F").child("historial");
            ref5.setValue(tramo5);
            DatabaseReference ref6= database.getReference("Control caudal").child("tramo F-G").child("historial");
            ref6.setValue(tramo6);

            DatabaseReference ref7= database.getReference("Control caudal").child("tramo G-H").child("historial");
            ref7.setValue(tramo7);
            DatabaseReference ref8= database.getReference("Control caudal").child("tramo H-I").child("historial");
            ref8.setValue(tramo8);*/
            DatabaseReference ref9= database.getReference("Control caudal").child("tramo I-J").child("historial");
            ref9.setValue(tramo9);
        } catch(Exception e) { Toast.makeText(context, "The specified file was not found", Toast.LENGTH_SHORT).show();}
/*
        try {
            File file= new File(context.getExternalFilesDir(null),"prueba.csv");
            String csvfileString =  context.getExternalFilesDir(null) + "/fakedata.csv";
            Toast.makeText(context, csvfileString, Toast.LENGTH_SHORT).show();
            File csvfile = new File(csvfileString);
            CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsolutePath()));
            Toast.makeText(context, csvfile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                Toast.makeText(this, String.valueOf(nextLine.length), Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }*/
    }
}