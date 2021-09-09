package com.espol.aguapol.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class sAdapterAlarmasActivas extends RecyclerView.Adapter<sAdapterAlarmasActivas.SwipeViewHolder> {
    private Context context;
    private List<Alarma> alarmas;
    private final ViewBinderHelper viewBinderHelper= new ViewBinderHelper();
    private String tipoAlarma;
    private View view;
    View root;

    public sAdapterAlarmasActivas(Context context, List<Alarma> alarmas, String tipoAlarma,View root) {
        this.context = context;
        this.alarmas = alarmas;
        this.tipoAlarma=tipoAlarma;
        this.root=root;
    }

    private void setAlarmas(ArrayList<Alarma> alarmas){
        this.alarmas=new ArrayList<>();
        this.alarmas=alarmas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public sAdapterAlarmasActivas.SwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(R.layout.res_alarmas_activas,parent,false);
        return new sAdapterAlarmasActivas.SwipeViewHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull SwipeViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull sAdapterAlarmasActivas.SwipeViewHolder holder, int position) {
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout,String.valueOf(alarmas.get(position).getId()));
        viewBinderHelper.closeLayout(String.valueOf(alarmas.get(position).getId()));
        holder.binData(alarmas.get(position));
    }

    @Override
    public int getItemCount() {
        return alarmas.size();
    }

    public class SwipeViewHolder extends  RecyclerView.ViewHolder {
        TextView txtMensaje,txtCambio;
        ImageView imgIcono,imgCambio;
        TextView txtFechaHora,txtPerfil;
        SwipeRevealLayout swipeRevealLayout;
        View itemView;
        public SwipeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            txtMensaje=itemView.findViewById(R.id.txtMensajeAlarma);
            imgIcono=itemView.findViewById(R.id.imgIconAlarma);
            txtFechaHora=itemView.findViewById(R.id.txtFechaHora);
            swipeRevealLayout=itemView.findViewById(R.id.sRAlarmasActivas);
            txtCambio=itemView.findViewById(R.id.txtCambioActivas);
            imgCambio=itemView.findViewById(R.id.imgCambioProceso);
            txtPerfil=itemView.findViewById(R.id.txtAlarmaPerfil);




        }

        private void imageViewSetUp(Alarma item) {
            if(tipoAlarma.equals(context.getResources().getString(R.string.tab_text_1))){
                txtPerfil.setVisibility(View.GONE);
                imgCambio.setImageResource(R.drawable.cambio);
                imgCambio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      moverAlarma(context.getString(R.string.ref_alarmas_activas),context.getString(R.string.ref_alarmas_proceso),item);
                    }
                });
            }
            else if(tipoAlarma.equals(context.getResources().getString(R.string.tab_text_2))){
                txtPerfil.setVisibility(View.VISIBLE);
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference(context.getString(R.string.ref_alarmas_proceso)).child(item.getId());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email=item.getCorreoUsuario();
                        String iniciales=email.substring(0,2).toUpperCase(Locale.ROOT);
                        txtPerfil.setText(iniciales);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                imgCambio.setImageResource(R.drawable.solucionado_cambio);
                imgCambio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moverAlarma(context.getString(R.string.ref_alarmas_proceso),context.getString(R.string.ref_alarmas_solucionadas),item);
                    }
                });

            }
            else {
                swipeRevealLayout.setLockDrag(true);
                txtPerfil.setVisibility(View.VISIBLE);
                String email=item.getCorreoUsuario();
                String iniciales=email.substring(0,2).toUpperCase(Locale.ROOT);
                txtPerfil.setText(iniciales);
            }

        }

        public void binData(Alarma item) {
            txtMensaje.setText(item.getMensaje());
            //imgIcono.setImageResource(item.getUrlIcon());
            txtFechaHora.setText(getTiempoTranscurrido(item));
            imageViewSetUp(item);
        }


    }

    private void moverAlarma(String referencia,String referenciaNueva,Alarma item) {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref= database.getReference(referenciaNueva);
        DatabaseReference refActivas=database.getReference(referencia);
        DatabaseReference newRef= ref.push();
        refActivas.child(item.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                String oldId=item.getId();
                String oldEmail=item.getCorreoUsuario();
                String oldHora=item.getFechaHora();
                SimpleDateFormat sdf= new SimpleDateFormat("HH:mm : dd/MM/yy");
                String date= sdf.format(Calendar.getInstance().getTime());
                item.setFechaHora(date);
                item.setId(newRef.getKey());
                String correo= FirebaseAuth.getInstance().getCurrentUser().getEmail();
                item.setCorreoUsuario(correo);
                newRef.setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Snackbar snackbar= Snackbar.make(context,root.findViewById(R.id.rootAlarmas),referenciaNueva, BaseTransientBottomBar.LENGTH_LONG);
                        snackbar.setAction("Deshacer", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                newRef.removeValue();
                                item.setFechaHora(oldHora);
                                item.setId(oldId);
                                item.setCorreoUsuario(oldEmail);
                                refActivas.child(oldId).setValue(item);
                            }
                        });
                        snackbar.show();
                    }
                });
            }
        });
    }

    private String getTiempoTranscurrido(Alarma item) {
        String fechaYHora=item.getFechaHora();
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm : dd/MM/yy");
        long horas=0;
        String mensajeHoras="";
        String unidad="";
        try {
            Date alarmDate= sdf.parse(fechaYHora);
            Date today=Calendar.getInstance().getTime();
            long dif=today.getTime()-alarmDate.getTime();
            horas= TimeUnit.MILLISECONDS.convert(dif,TimeUnit.MINUTES);
            horas=dif/60000;
            if(horas<60){
                unidad="min";
            }
            else{
                horas=horas/60;
                unidad="hrs";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
            mensajeHoras=String.valueOf(horas+" "+unidad);
        return mensajeHoras;
    }
}
