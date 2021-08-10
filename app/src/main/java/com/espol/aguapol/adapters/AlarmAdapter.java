package com.espol.aguapol.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.Modelo.AlarmaManejada;
import com.espol.aguapol.R;
import com.espol.aguapol.databinding.FragmentControlBinding;
import com.espol.aguapol.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AlarmAdapter extends BaseAdapter {
    Context context;
    List<Alarma> alarmas;
    View convertView;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    HomeFragment fragment;


    public AlarmAdapter(Context context, List<Alarma> alarmas, HomeFragment fragment) {
        this.context = context;
        this.alarmas=alarmas;
        this.fragment=fragment;
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
        convertView= LayoutInflater.from(context).inflate(R.layout.res_alarm,null);
        this.convertView=convertView;
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

        CardView cardAlarma=convertView.findViewById(R.id.cardAlarma);
        View finalConvertView = convertView;
        cardAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle(itemAlarma.getTitulo());
                builder.setMessage(itemAlarma.getMensaje());
                builder.setIcon(context.getResources().getDrawable(itemAlarma.getUrlIcon()));
                builder.setCancelable(false);
                builder.setPositiveButton("Solucionado", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference alarmasGuardadas= database.getReference("Alarmas solucionadas");
                        AlarmaManejada alarmaManejada= new AlarmaManejada(itemAlarma.getTitulo(),itemAlarma.getMensaje(),itemAlarma.getFechaHora(),itemAlarma.getUrlIcon(),itemAlarma.getId(),firebaseAuth.getCurrentUser().getEmail());
                        DatabaseReference refPush= alarmasGuardadas.push();
                        refPush.setValue(alarmaManejada).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DatabaseReference ref= database.getReference("Alarmas");
                                ref.child(itemAlarma.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        fragment.getAlarmas();
                                    }
                                });
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference ref= database.getReference("Alarmas");
                        ref.child(itemAlarma.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                fragment.getAlarmas();
                            }
                        });
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog= builder.create();
                dialog.show();
            }

        });



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
