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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private ProgressBar pbTanquesBajos,pbTanqueAlto;
    private TextView txtNivelTanqueBajos,txtNivelTanqueAlto,txtPorNivelTanqueAlto;
    private View root;
    private Herramientas herramientas;


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
        txtPorNivelTanqueAlto=binding.txtPorNivelTanqueAlto;
        pbTanqueAlto=binding.progressBarTanquesAlto;

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
        DatabaseReference refTanqueAlto= database.getReference("Tanques").child("Tanque alto");
        refTanqueAlto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String unidades= snapshot.child("unidades").getValue().toString();
                float nivelActual=Float.parseFloat(snapshot.child("nivel actual").getValue().toString());
                float nivelMaximo=Float.parseFloat(snapshot.child("nivel maximo").getValue().toString());

                txtNivelTanqueAlto.setText(String.valueOf(nivelActual)+" "+unidades);
                float porcentaje=(nivelActual/nivelMaximo)*100;
                int roundPor= Math.round(porcentaje);
                pbTanqueAlto.setProgress(roundPor);
                txtPorNivelTanqueAlto.setText(String.valueOf(roundPor));

                if(roundPor>100){
                    Snackbar snackbar= Snackbar.make(root,getResources().getString(R.string.titulo_fallo_tanquelevado), BaseTransientBottomBar.LENGTH_INDEFINITE);
                    snackbar.show();
                    SimpleDateFormat format= new SimpleDateFormat("hh:mm a:dd/MM/yyyy");
                    Calendar calendar=Calendar.getInstance();
                    Date hoy=calendar.getTime();
                    String fechaHora=format.format(hoy);

                    pbTanqueAlto.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    Alarma alarma= new Alarma(getResources().getString(R.string.titulo_fallo_tanquelevado),getResources().getString(R.string.mensaje_fallo_tanquelevado),fechaHora,R.drawable.tanquesbajos);
                    DatabaseReference refAlarma=database.getReference("Alarmas");
                    DatabaseReference newRef= refAlarma.push();
                    newRef.setValue(alarma);

                    herramientas.generarNotifiacion(root.getContext(),getString(R.string.titulo_fallo_tanquelevado),getString(R.string.mensaje_fallo_tanquelevado),R.drawable.tanquesbajos,1);


                }
                else{
                    pbTanqueAlto.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);
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

                if(S1.equals("1")){ btnS1.setBackgroundColor(getResources().getColor(R.color.on_sensor)); }
                else{ btnS1.setBackgroundColor(getResources().getColor(R.color.off_sensor)); }

                if(S2.equals("1")){ btnS2.setBackgroundColor(getResources().getColor(R.color.on_sensor)); }
                else{ btnS2.setBackgroundColor(getResources().getColor(R.color.off_sensor)); }

                if(S3.equals("1")){ btnS3.setBackgroundColor(getResources().getColor(R.color.on_sensor)); }
                else{ btnS3.setBackgroundColor(getResources().getColor(R.color.off_sensor)); }

                if(S1.equals("1") && S2.equals("0") && S3.equals("0")){
                    nivel="Bajo";
                    txtNivelTanqueBajos.setText(nivel);
                    pbTanquesBajos.setProgress(10);
                    pbTanquesBajos.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);
                }
                else if(S1.equals("1") && S2.equals("1") && S3.equals("0")){
                    nivel="Medio";
                    txtNivelTanqueBajos.setText(nivel);
                    pbTanquesBajos.setProgress(50);
                    pbTanquesBajos.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);
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