package com.espol.aguapol.ui.gallery;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.Modelo.Herramientas;
import com.espol.aguapol.R;


import com.espol.aguapol.databinding.FragmentGalleryBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private FirebaseDatabase database;
    private Button btnS1,btnS2,btnS3;
    private Button btnS1Alto,btnS2Alto,btnS3Alto;
    private ProgressBar pbTanquesBajos,pbTanqueAlto;
    private TextView txtNivelTanqueBajos,txtNivelTanqueAlto;
    private View root;
    private Herramientas herramientas;
    private String CHANNEL_ID;
    private int Notification_ID=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        database=FirebaseDatabase.getInstance();
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        txtNivelTanqueBajos=binding.txtNivelTanqueBajo;
        btnS1=binding.btnS1;
        btnS2=binding.btnS2;
        btnS3=binding.btnS3;
        pbTanquesBajos=binding.progressBarTanquesBajos;
        txtNivelTanqueAlto=binding.txtNivelTanqueAlto;
        btnS1Alto=binding.btnS1Alto;
        btnS2Alto=binding.btnS2Alto;
        btnS3Alto=binding.btnS3Alto;
        pbTanqueAlto=binding.progressBarTanqueAlto;

        getDataTanquesBajos();
        getDataTanqueAlto();
         herramientas= new Herramientas(root.getContext());

        /*final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    private void getDataTanqueAlto() {
        DatabaseReference refTanqueAlto= database.getReference("Tanques");
        refTanqueAlto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                String S1=snapshot.child("Tanque alto").child("S1").getValue().toString();
                String S2=snapshot.child("Tanque alto").child("S2").getValue().toString();
                String S3=snapshot.child("Tanque alto").child("S3").getValue().toString();
                String nivel="";

                if(S1.equals("1")){ btnS1Alto.setBackgroundColor(root.getResources().getColor(R.color.on_sensor)); }
                else{ btnS1Alto.setBackgroundColor(root.getResources().getColor(R.color.off_sensor)); }

                if(S2.equals("1")){ btnS2Alto.setBackgroundColor(root.getResources().getColor(R.color.on_sensor)); }
                else{ btnS2Alto.setBackgroundColor(root.getResources().getColor(R.color.off_sensor)); }

                if(S3.equals("1")){ btnS3Alto.setBackgroundColor(root.getResources().getColor(R.color.on_sensor)); }
                else{ btnS3Alto.setBackgroundColor(root.getResources().getColor(R.color.off_sensor)); }

                if(S1.equals("1") && S2.equals("0") && S3.equals("0")){
                    nivel="Bajo";
                    txtNivelTanqueAlto.setText(nivel);
                    pbTanqueAlto.setProgress(10);
                    pbTanqueAlto.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);

                    SimpleDateFormat sdf= new SimpleDateFormat("HH:mm : dd/mm/yy");
                    String date= sdf.format(Calendar.getInstance().getTime());
                    DatabaseReference ref=database.getReference("Alarmas");
                    DatabaseReference newRef= ref.push();
                    Alarma alarma= new Alarma(root.getResources().getText(R.string.titulo_tanque_elevado_bajo).toString(),"El tanque elevado se encuentra en nivel bajo",date,R.drawable.tanquesbajos,newRef.getKey());
                    newRef.setValue(alarma);
                    herramientas.generarNotifiacion(root.getContext(), "Tanques",root.getResources().getString(R.string.titulo_tanque_elevado_bajo),R.drawable.tanquesbajos,2);
                    Notification_ID++;

                }
                else if(S1.equals("1") && S2.equals("1") && S3.equals("0")){
                    nivel="Medio";
                    txtNivelTanqueAlto.setText(nivel);
                    pbTanqueAlto.setProgress(50);
                    pbTanqueAlto.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);
                    //Snackbar snackbar= Snackbar.make(root,root.getResources().getText(R.string.titulo_tanque_elevado_medio),BaseTransientBottomBar.LENGTH_LONG);
                    //snackbar.show();
                    Notification_ID++;

                }
                else if(S1.equals("1") && S2.equals("1") && S3.equals("1")){
                    nivel="Alto";
                    txtNivelTanqueAlto.setText(nivel);
                    pbTanqueAlto.setProgress(100);
                    pbTanqueAlto.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);
                }
                else{
                    nivel="FALLA EN LOS SENSORES";
                    txtNivelTanqueAlto.setText(nivel);
                    pbTanqueAlto.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    herramientas.generarNotifiacion(root.getContext(), "Tanques",nivel,R.drawable.tanquesbajos,3);
                    SimpleDateFormat sdf= new SimpleDateFormat("HH:mm : dd/mm/yy");
                    String date= sdf.format(Calendar.getInstance().getTime());
                    DatabaseReference ref=database.getReference("Alarmas");
                    DatabaseReference newRef= ref.push();
                    Alarma alarma= new Alarma(root.getResources().getText(R.string.titulo_tanque_elevado_bajo).toString(),"ERROR EN LOS SENSORES",date,R.drawable.tanquesbajos,newRef.getKey());
                    newRef.setValue(alarma);
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void getDataTanquesBajos() {
        DatabaseReference refTanquesBajos= database.getReference("Tanques").child("Tanques bajos").child("T1");
        refTanquesBajos.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String S1=snapshot.child("S1").getValue().toString();
                String S2=snapshot.child("S2").getValue().toString();
                String S3=snapshot.child("S3").getValue().toString();
                String nivel="";

                if(S1.equals("1")){ btnS1.setBackgroundColor(root.getResources().getColor(R.color.on_sensor)); }
                else{ btnS1.setBackgroundColor(root.getResources().getColor(R.color.off_sensor)); }

                if(S2.equals("1")){ btnS2.setBackgroundColor(root.getResources().getColor(R.color.on_sensor)); }
                else{ btnS2.setBackgroundColor(root.getResources().getColor(R.color.off_sensor)); }

                if(S3.equals("1")){ btnS3.setBackgroundColor(root.getResources().getColor(R.color.on_sensor)); }
                else{ btnS3.setBackgroundColor(root.getResources().getColor(R.color.off_sensor)); }

                if(S1.equals("1") && S2.equals("0") && S3.equals("0")){
                    nivel="Bajo";
                    txtNivelTanqueBajos.setText(nivel);
                    pbTanquesBajos.setProgress(10);
                    pbTanquesBajos.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);
                    SimpleDateFormat sdf= new SimpleDateFormat("HH:mm : dd/mm/yy");
                    String date= sdf.format(Calendar.getInstance().getTime());
                    DatabaseReference ref=database.getReference("Alarmas");
                    DatabaseReference newRef= ref.push();
                    Alarma alarma= new Alarma(root.getResources().getText(R.string.titulo_tanque_bajo_bajo).toString(),"El tanque bajo esta en nivel bajo",date,R.drawable.tanquesbajos, newRef.getKey());
                    newRef.setValue(alarma);
                    herramientas.generarNotifiacion(root.getContext(), "Tanques",root.getResources().getString(R.string.titulo_tanque_bajo_bajo),R.drawable.tanquesbajos,0);
                    Notification_ID++;

                }
                else if(S1.equals("1") && S2.equals("1") && S3.equals("0")){
                    nivel="Medio";
                    txtNivelTanqueBajos.setText(nivel);
                    pbTanquesBajos.setProgress(50);
                    pbTanquesBajos.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);


                   // herramientas.generarNotifiacion(root.getContext(), "Tanques bajos",root.getResources().getString(R.string.titulo_tanque_bajo_medio),R.drawable.tanquesbajos,Notification_ID);
                    Notification_ID++;
                }
                else if(S1.equals("1") && S2.equals("1") && S3.equals("1")){
                    nivel="Alto";
                    txtNivelTanqueBajos.setText(nivel);
                    pbTanquesBajos.setProgress(100);
                    pbTanquesBajos.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);
                }
                else{
                    nivel="FALLA EN LOS SENSORES";
                    txtNivelTanqueBajos.setText(nivel);
                    pbTanquesBajos.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    herramientas.generarNotifiacion(root.getContext(), "Tanques",nivel,R.drawable.tanquesbajos,1);
                    SimpleDateFormat sdf= new SimpleDateFormat("HH:mm : dd/mm/yy");
                    String date= sdf.format(Calendar.getInstance().getTime());
                    DatabaseReference ref=database.getReference("Alarmas");
                    DatabaseReference newRef= ref.push();
                    Alarma alarma= new Alarma(root.getResources().getText(R.string.titulo_tanque_bajo_bajo).toString(),"ERROR EN LOS SENSORES",date,R.drawable.tanquesbajos, newRef.getKey());
                    newRef.setValue(alarma);

                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}