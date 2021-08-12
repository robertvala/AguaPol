package com.espol.aguapol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.espol.aguapol.Modelo.ContorlCaudal;
import com.espol.aguapol.adapters.adapterListCaudal;
import com.espol.aguapol.adapters.adapterlista;

import java.util.ArrayList;

public class ShowlistCaudal extends AppCompatActivity {
    Context context=this;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlist_caudal);
        listView=findViewById(R.id.listCaudal);
        Intent i = getIntent();
        ArrayList<ContorlCaudal> list= (ArrayList<ContorlCaudal>) i.getSerializableExtra("list");
        displayList(list);
    }

    void displayList(ArrayList<ContorlCaudal> list){
        adapterListCaudal adaptador= new adapterListCaudal(list,context);
        listView.setAdapter(adaptador);
    }

}