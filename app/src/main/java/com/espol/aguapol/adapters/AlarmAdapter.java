package com.espol.aguapol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.R;

import org.w3c.dom.Text;

import java.util.List;

public class AlarmAdapter extends BaseAdapter {
    Context context;
    List<Alarma> alarmas;

    public AlarmAdapter(Context context, List<Alarma> alarmas) {
        this.context = context;
        this.alarmas = alarmas;
    }

    @Override
    public int getCount() {
        return alarmas.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.res_alarm,null);
        TextView txtTitulo=convertView.findViewById(R.id.txtTituloAlarma);
        TextView txtMensaje=convertView.findViewById(R.id.txtMensajeAlarma);
        ImageView imgIcono=convertView.findViewById(R.id.imgIconAlarma);
        TextView txtFechaHora= convertView.findViewById(R.id.txtFechaHora);

        Alarma itemAlarma= alarmas.get(position);
        //Set data in adapter
        txtTitulo.setText(itemAlarma.getTitulo());
        txtFechaHora.setText(itemAlarma.getFechaHora());
        txtMensaje.setText(itemAlarma.getMensaje());
        imgIcono.setImageResource(itemAlarma.getUrlIcon());

        return convertView;
    }
}
