package com.espol.aguapol.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.R;

import com.espol.aguapol.databinding.FragmentSlideshowBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class SlideshowFragment extends Fragment {
    Button btnBombaA,btnBombaB;
    TextInputEditText tietTEA,tietTEB,tietTAA,tietTAB;
    private View root;
    FirebaseDatabase database;

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        btnBombaA=binding.btnBombaA;
        btnBombaB=binding.btnBombaB;
        tietTAA=binding.tietTiempoApagadoA;
        tietTAB=binding.tietTiempoApagadoB;
        tietTEA=binding.tietTiempoEncendidoA;
        tietTEB=binding.tietTiempoEncendidoB;
        database=FirebaseDatabase.getInstance();

        cargarDatos();


        return root;
    }

    void cargarDatos(){
        DatabaseReference refBombas=database.getReference("Tablero de control");
        refBombas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String estadoA=snapshot.child("bomba a").child("estado").getValue().toString();
                String estadoB=snapshot.child("bomba b").child("estado").getValue().toString();

                if(estadoA.equals("1")){
                    btnBombaA.setText("ON");
                    btnBombaA.setBackgroundColor(getResources().getColor(R.color.on_bomba));
                }

                else if(estadoA.equals("0")){
                    btnBombaA.setText("OFF");
                    btnBombaB.setBackgroundColor(getResources().getColor(R.color.off_bomba));
                }
                else{
                }

                if(estadoB.equals("1")){
                    btnBombaB.setText("ON");
                    btnBombaB.setBackgroundColor(getResources().getColor(R.color.on_bomba));
                }

                else if(estadoB.equals("0")){
                    btnBombaB.setText("OFF");
                    btnBombaB.setBackgroundColor(getResources().getColor(R.color.off_bomba));
                }
                else{
                }

                String tta=snapshot.child("bomba a").child("tiempoApagado").getValue().toString();
                String tea=snapshot.child("bomba a").child("tiempoEncendido").getValue().toString();
                String unidadesA=snapshot.child("bomba a").child("unidades").getValue().toString();
                tietTAA.setText(tta+" "+unidadesA);
                tietTEA.setText(tea+" "+unidadesA);

                String ttb=snapshot.child("bomba b").child("tiempoApagado").getValue().toString();
                String teb=snapshot.child("bomba b").child("tiempoEncendido").getValue().toString();
                String unidadesB=snapshot.child("bomba b").child("unidades").getValue().toString();
                tietTAB.setText(ttb+" "+unidadesB);
                tietTEB.setText(teb+" "+unidadesB);


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