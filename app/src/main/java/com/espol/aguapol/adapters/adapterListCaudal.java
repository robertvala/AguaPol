package com.espol.aguapol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.espol.aguapol.Modelo.ContorlCaudal;
import com.espol.aguapol.Modelo.HistorialCaudal;
import com.espol.aguapol.R;

import java.io.Serializable;
import java.util.ArrayList;

public class adapterListCaudal extends BaseAdapter implements Serializable {
    ArrayList<ContorlCaudal> listItems;
    Context context;

    public adapterListCaudal() {
    }

    public adapterListCaudal(ArrayList<ContorlCaudal> listItems, Context context) {
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
                    inflate(R.layout.adaptador_list_caudal, parent, false);
        }
        ContorlCaudal currentItem = (ContorlCaudal) getItem(position);
        TextView hora,valor;
        hora=convertView.findViewById(R.id.fechatxtCaudal);
        valor=convertView.findViewById(R.id.txtvalorCaudal);
        hora.setText(currentItem.getHora());
        valor.setText(currentItem.getValor());
        return convertView;
    }

}
