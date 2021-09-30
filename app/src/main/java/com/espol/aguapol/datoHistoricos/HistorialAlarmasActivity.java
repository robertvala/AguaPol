package com.espol.aguapol.datoHistoricos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.Modelo.Comparador;
import com.espol.aguapol.R;
import com.espol.aguapol.adapters.sAdapterAlarmasActivas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistorialAlarmasActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Context context=this;
    String fechaInicio;
    String fechaFin;
    FirebaseDatabase database;
    List<Alarma> alarmaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_alarmas2);
        fechaInicio=getIntent().getStringExtra("fechaInicio");
        fechaFin=getIntent().getStringExtra("fechaFin");
        database=FirebaseDatabase.getInstance();
        recyclerView=findViewById(R.id.rvHistorialAlarmas);

        getData();

    }

    private void getData() {
        Query query=database.getReference("Alarmas solucionadas");
        query.orderByChild("fecha").startAt(fechaInicio).endAt(fechaFin).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alarmaList=new ArrayList<>();
                for(DataSnapshot ds: snapshot.getChildren()){
                    GenericTypeIndicator<Alarma> t = new GenericTypeIndicator<Alarma>() {};
                    Alarma alarma = ds.getValue(t);
                    //alarmas.put(snapshot.getKey(),alarma);
                    alarmaList.add(alarma);
                }
                Collections.sort(alarmaList, new Comparador() {
                });
                sAdapterAlarmasActivas adapterAlarmasActivas= new sAdapterAlarmasActivas(context, alarmaList,context.getString(R.string.tab_text_3),findViewById(R.id.rootAlarmas));
                recyclerView.setLayoutManager( new LinearLayoutManager(context));
                recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(adapterAlarmasActivas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}