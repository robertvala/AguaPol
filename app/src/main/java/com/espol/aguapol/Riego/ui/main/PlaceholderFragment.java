package com.espol.aguapol.Riego.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.espol.aguapol.databinding.FragmentRiego2Binding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_REF = "referencia";

    private PageViewModel pageViewModel;
    private FragmentRiego2Binding binding;
    String [] agua;
    String referencia;

    public static PlaceholderFragment newInstance(int index) {
        String [] agua= {"agua del lago","agua potable"};
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString(ARG_REF,agua[index-1]);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        referencia="";
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            referencia=getArguments().getString(ARG_REF);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentRiego2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextInputEditText tietCanchas= binding.tietCanchas;
        TextInputEditText tietRectorado= binding.tietRectorado;
        TextInputEditText tietSalida=binding.tietSalidaLago;
        TextInputEditText tietTecnologias= binding.tietTecnologias;

        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Control riego").child(referencia);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cnachas =snapshot.child("Canchas de ingenieria").getValue().toString();
                String rectorado =snapshot.child("Rectorado").getValue().toString();
                String salida =snapshot.child("Salida del lago").getValue().toString();
                String tecnologias =snapshot.child("Tecnologias").getValue().toString();

                tietCanchas.setText(cnachas);
                tietRectorado.setText(rectorado);
                tietSalida.setText(salida);
                tietTecnologias.setText(tecnologias);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}