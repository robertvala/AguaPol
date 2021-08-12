package com.espol.aguapol.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.espol.aguapol.Modelo.HistorialCaudal;
import com.espol.aguapol.R;
import com.espol.aguapol.ShowlistCaudal;

import java.util.ArrayList;


public class adapterlista extends BaseAdapter {
    ArrayList<HistorialCaudal> listItems;
    Context context;

    public adapterlista() {
    }

    public adapterlista(ArrayList<HistorialCaudal> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.adaptador_list_historico, parent, false);
        }
        HistorialCaudal currentItem = (HistorialCaudal) getItem(position);
        TextView fecha,valor;
        CardView cvHistorico;
        fecha=convertView.findViewById(R.id.fechatxtHistorico);
        valor=convertView.findViewById(R.id.txtvalorHistorico);
        fecha.setText(currentItem.getFecha());
        valor.setText(currentItem.getPromedio().toString());
        cvHistorico=convertView.findViewById(R.id.cvHistorico);
        cvHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowlistCaudal.class);
                intent.putExtra("list", currentItem.getListcaudal());
                context.startActivity(intent);
            }
        });



        return convertView;
    }
}
