package com.espol.aguapol.Modelo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.espol.aguapol.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Herramientas {
    String CHANNEL_ID="AguaPo";
    Context context;
    public Herramientas(Context context) {
        this.context=context;
        createNotificationChannel(context);
    }

    public Herramientas() {

    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void generarNotifiacion(Context context,String tittle,String text,int icon,int notificationId){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this.context, context.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(tittle)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

    public Date obtenerFecha(String formato, String fecha){
        SimpleDateFormat dateFormat= new SimpleDateFormat(formato);
        Date date=null;
        try {
            date= dateFormat.parse(fecha);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public long diferenciaFechas(Date fecha1, Date fecha2){
        long dif=fecha1.getTime()-fecha2.getTime();
        return dif;
    }
}
