package com.espol.aguapol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.espol.aguapol.Modelo.AlarmaManejada;
import com.espol.aguapol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AlarmaSolucionadaAdapter extends BaseAdapter {
    Context context;
    List<AlarmaManejada> alarmas;
    View convertView;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;



    public AlarmaSolucionadaAdapter(Context context, List<AlarmaManejada> alarmas) {
        this.context = context;
        this.alarmas=alarmas;
        database=FirebaseDatabase.getInstance();

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
        firebaseAuth=FirebaseAuth.getInstance();
        convertView= LayoutInflater.from(context).inflate(R.layout.res_alarm_solucionada,null);
        this.convertView=convertView;
        TextView txtTitulo=convertView.findViewById(R.id.txtTituloAlarmaSolucionada);
        TextView txtMensaje=convertView.findViewById(R.id.txtMensajeAlarmaSolucionada);
        ImageView imgIcono=convertView.findViewById(R.id.imgIconAlarmaSolucionada);
        TextView txtFechaHora= convertView.findViewById(R.id.txtFechaHoraSolucionada);
        TextView txtUser=convertView.findViewById(R.id.txtUser);

        AlarmaManejada itemAlarma= alarmas.get(position);
        //Set data in adapter
        txtTitulo.setText(itemAlarma.getTitulo());
        txtFechaHora.setText(itemAlarma.getFechaHora());
        txtMensaje.setText(itemAlarma.getMensaje());
        imgIcono.setImageResource(itemAlarma.getUrlIcon());
        txtUser.setText("Usuario encargado: "+itemAlarma.getUser());







        return this.convertView;
    }

    public static <K, V> K getSingleKeyFromValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
