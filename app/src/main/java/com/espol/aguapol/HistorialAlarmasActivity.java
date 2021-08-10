package com.espol.aguapol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.Modelo.AlarmaManejada;
import com.espol.aguapol.adapters.AlarmaSolucionadaAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistorialAlarmasActivity extends AppCompatActivity {
    FirebaseDatabase database;
     List<AlarmaManejada> alarmas;
     ListView listAlarmas;
     Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_historial_alarmas);
        listAlarmas=findViewById(R.id.lvAlarmasGuardadas);
        database=FirebaseDatabase.getInstance();
        alarmas=new ArrayList<>();
        loadAlarmas();

    }

    void loadAlarmas(){
        DatabaseReference ref= database.getReference("Alarmas solucionadas");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot ds: snapshot.getChildren()){
                   GenericTypeIndicator<AlarmaManejada> t = new GenericTypeIndicator<AlarmaManejada>() {};
                   AlarmaManejada alarma = ds.getValue(t);
                   //alarmas.put(snapshot.getKey(),alarma);
                   alarmas.add(alarma);
               }
                AlarmaSolucionadaAdapter alarmaSolucionadaAdapter= new AlarmaSolucionadaAdapter(context,alarmas);
                listAlarmas.setAdapter(alarmaSolucionadaAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}