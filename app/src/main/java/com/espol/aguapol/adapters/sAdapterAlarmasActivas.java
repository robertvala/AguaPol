package com.espol.aguapol.adapters;

import android.content.Context;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class sAdapterAlarmasActivas extends RecyclerView.Adapter<sAdapterAlarmasActivas.SwipeViewHolder> {
    private Context context;
    private List<Alarma> alarmas;
    private final ViewBinderHelper viewBinderHelper= new ViewBinderHelper();

    public sAdapterAlarmasActivas(Context context, List<Alarma> alarmas) {
        this.context = context;
        this.alarmas = alarmas;
    }

    private void setAlarmas(ArrayList<Alarma> alarmas){
        this.alarmas=new ArrayList<>();
        this.alarmas=alarmas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public sAdapterAlarmasActivas.SwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.res_alarmas_activas,parent,false);
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
        TextView txtFechaHora;
        SwipeRevealLayout swipeRevealLayout;
        public SwipeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMensaje=itemView.findViewById(R.id.txtMensajeAlarma);
            imgIcono=itemView.findViewById(R.id.imgIconAlarma);
            txtFechaHora=itemView.findViewById(R.id.txtFechaHora);
            swipeRevealLayout=itemView.findViewById(R.id.sRAlarmasActivas);
            txtCambio=itemView.findViewById(R.id.txtCambioActivas);
            imgCambio=itemView.findViewById(R.id.imgCambioProceso);

            imgCambio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Cambio a procesos", Toast.LENGTH_SHORT).show();
                }
            });

        }

        public void binData(Alarma item) {
            txtMensaje.setText(item.getMensaje());
            imgIcono.setImageResource(item.getUrlIcon());
            txtFechaHora.setText(getTiempoTranscurrido(item));
        }


    }

    private String getTiempoTranscurrido(Alarma item) {
        String fechaYHora=item.getFechaHora();
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm : dd/MM/yy");
        String date= sdf.format(Calendar.getInstance().getTime());
        long horas=0;
        String mensajeHoras="";
        try {
            Date alarmDate= sdf.parse(fechaYHora);
            Date today=Calendar.getInstance().getTime();
            long dif=today.getTime()-alarmDate.getTime();
            horas= TimeUnit.HOURS.convert(dif,TimeUnit.MINUTES);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (horas<5.0){
           mensajeHoras="Hace unos instantes";
        }
        else{
            mensajeHoras=String.valueOf(horas);
        }
        return mensajeHoras;
    }
}
