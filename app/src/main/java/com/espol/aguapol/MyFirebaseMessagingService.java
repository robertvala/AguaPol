package com.espol.aguapol;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.espol.aguapol.Modelo.Alarma;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String NOTIFICATION_CHANNEL_ID = "Nuevo canal" ;
    private static final CharSequence NOTIFICATION_CHANNEL_NAME = "Aguapol";
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    Context context=this;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationManager notificationManager = (NotificationManager) getSystemService ( Context.NOTIFICATION_SERVICE );
        if (!Objects.equals ( null, remoteMessage.getNotification () )) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel ( NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH );
                notificationManager.createNotificationChannel ( notificationChannel );
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder ( this, NOTIFICATION_CHANNEL_ID );
            notificationBuilder.setAutoCancel ( true )
                    .setStyle ( new NotificationCompat.BigTextStyle ().bigText ( remoteMessage.getNotification ().getBody () ) )
                    .setDefaults ( Notification.DEFAULT_ALL )
                    .setWhen ( System.currentTimeMillis () )
                    .setSmallIcon (R.drawable.aguapol_logo)
                    .setTicker ( remoteMessage.getNotification ().getTitle () )
                    .setPriority ( Notification.PRIORITY_MAX )
                    .setContentTitle ( remoteMessage.getNotification ().getTitle () )
                    .setContentText ( remoteMessage.getNotification ().getBody () );
            notificationManager.notify ( 1, notificationBuilder.build () );
        }

        generarAlarmas(remoteMessage);



        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //sendNotification(remoteMessage.getNotification().getBody());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void generarAlarmas(RemoteMessage remoteMessage) {
        //Tanque elevado alto
        if(remoteMessage.getNotification().getBody().equals(context.getString(R.string.advertencia_tanque_elevado_alto))){
            DatabaseReference ref=database.getReference("Alarmas");
            DatabaseReference newRef= ref.push();
            SimpleDateFormat sdf= new SimpleDateFormat("HH:mm : dd/MM/yy");
            String date= sdf.format(Calendar.getInstance().getTime());
            Alarma alarma= new Alarma(context.getResources().getString(R.string.tipo_alarma_advertencia),context.getResources().getText(R.string.advertencia_tanque_elevado_alto).toString(),date,R.drawable.icono_2,newRef.getKey(),"",context.getResources().getString(R.string.tipo_alarma_advertencia));
            newRef.setValue(alarma).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DatabaseReference refTanqueAlto= database.getReference("Tanques");
                    refTanqueAlto.child("Tanque alto").child("S1").setValue("1");
                    refTanqueAlto.child("Tanque alto").child("S2").setValue("1");
                    refTanqueAlto.child("Tanque alto").child("S2").setValue("1");
                }
            });

        }
        //Tanque elevado medio
        else if(remoteMessage.getNotification().getBody().equals(context.getString(R.string.alarma_tanque_elevado_medio))){
            DatabaseReference ref=database.getReference("Alarmas");
            DatabaseReference newRef= ref.push();
            Alarma alarma=generAlarma(context.getResources().getText(R.string.alarma_tanque_elevado_medio).toString(),R.drawable.icono_2, newRef.getKey(),context.getResources().getText(R.string.tipo_alarma_alarma).toString());
            newRef.setValue(alarma).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DatabaseReference refTanqueAlto= database.getReference("Tanques");
                    refTanqueAlto.child("Tanque alto").child("S1").setValue("1");
                    refTanqueAlto.child("Tanque alto").child("S2").setValue("1");
                    refTanqueAlto.child("Tanque alto").child("S2").setValue("0");
                }
            });
        }
        //Tanque elevado bajo
        else if(remoteMessage.getNotification().getBody().equals(context.getString(R.string.alarma_tanque_elevado_bajo))){
            DatabaseReference ref=database.getReference("Alarmas");
            DatabaseReference newRef= ref.push();
            Alarma alarma=generAlarma(context.getResources().getText(R.string.alarma_tanque_elevado_bajo).toString(),R.drawable.icono_2, newRef.getKey(),context.getResources().getText(R.string.tipo_alarma_alarma).toString());
            newRef.setValue(alarma).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DatabaseReference refTanqueAlto= database.getReference("Tanques");
                    refTanqueAlto.child("Tanque alto").child("S1").setValue("1");
                    refTanqueAlto.child("Tanque alto").child("S2").setValue("0");
                    refTanqueAlto.child("Tanque alto").child("S2").setValue("0");
                }
            });
        }
        //Tanque bajo medio advertencia
        else if(remoteMessage.getNotification().getBody().equals(context.getString(R.string.advertencia_tanque_bajo_medio))){
            DatabaseReference ref=database.getReference("Alarmas");
            DatabaseReference newRef= ref.push();
            Alarma alarma=generAlarma(context.getResources().getText(R.string.advertencia_tanque_bajo_medio).toString(),R.drawable.icono_2, newRef.getKey(),context.getResources().getText(R.string.tipo_alarma_advertencia).toString());
            newRef.setValue(alarma).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DatabaseReference refTanqueAlto= database.getReference("Tanques").child("Tanques bajos").child("T1");
                    refTanqueAlto.child("S1").setValue("1");
                    refTanqueAlto.child("S2").setValue("1");
                    refTanqueAlto.child("S3").setValue("0");
                }
            });
        }
        //tanque bajo bajo
        else if(remoteMessage.getNotification().getBody().equals(context.getString(R.string.advertencia_tanque_bajo_bajo))){
            DatabaseReference ref=database.getReference("Alarmas");
            DatabaseReference newRef= ref.push();
            Alarma alarma=generAlarma(context.getResources().getText(R.string.advertencia_tanque_bajo_bajo).toString(), R.drawable.icono_1, newRef.getKey(),context.getResources().getText(R.string.tipo_alarma_advertencia).toString());
            newRef.setValue(alarma).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DatabaseReference refTanqueAlto= database.getReference("Tanques").child("Tanques bajos").child("T1");
                    refTanqueAlto.child("S1").setValue("1");
                    refTanqueAlto.child("S2").setValue("0");
                    refTanqueAlto.child("S3").setValue("0");
                }
            });
        }
        //amperaje bomba a
        else if(remoteMessage.getNotification().getBody().equals(context.getString(R.string.advertencia_amperaje_bomba_a))){
            DatabaseReference ref=database.getReference("Alarmas");
            DatabaseReference newRef= ref.push();
            Alarma alarma=generAlarma(context.getResources().getText(R.string.advertencia_amperaje_bomba_a).toString(),R.drawable.icono_3, newRef.getKey(),context.getResources().getText(R.string.tipo_alarma_advertencia).toString());
            newRef.setValue(alarma).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DatabaseReference refTanqueAlto= database.getReference("Tablero de control").child("bomba a").child("amperaje");
                    refTanqueAlto.setValue("100");
                }
            });
        }
        //tiempo encendido bomba b
        else if(remoteMessage.getNotification().getBody().equals(context.getString(R.string.advertencia_tiempo_bomba_b))){
            DatabaseReference ref=database.getReference("Alarmas");
            DatabaseReference newRef= ref.push();
            Alarma alarma=generAlarma(context.getResources().getText(R.string.advertencia_tiempo_bomba_b).toString(),R.drawable.icono_3, newRef.getKey(),context.getResources().getText(R.string.tipo_alarma_advertencia).toString());
            newRef.setValue(alarma).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DatabaseReference refTanqueAlto= database.getReference("Tablero de control").child("bomba b").child("tiempoEncendido");
                    refTanqueAlto.setValue("9");
                }
            });
        }
    }

    public Alarma generAlarma(String mensaje,int Icono,String id,String tipo){
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm : dd/MM/yy");
        String date= sdf.format(Calendar.getInstance().getTime());
        Alarma alarma= new Alarma("Alarma",mensaje,date, Icono,id,"",tipo);
        return alarma;
    }
    // [END receive_message]


    // [START on_new_token]
    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
       /* // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
        // [END dispatch_job]*/
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");

    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, com.espol.aguapol.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.aguapol_logo)
                        .setContentTitle("Prueba")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
