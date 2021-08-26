package com.espol.aguapol.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.espol.aguapol.Fragments.HistorialAlarmasActivity;
import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.adapters.AlarmAdapter;

import com.espol.aguapol.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private View root;
    ListView lvAlarmas;
    TextView txtAlarmas;
    FirebaseDatabase database;
    List<Alarma> alarmaList;
    HashMap<String,Alarma> alarmas;
    HomeFragment fragment;
    Button btnHistorial;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        lvAlarmas=binding.lvAlarmas;
        txtAlarmas=binding.txtAlarmas;
        btnHistorial=binding.btnHistorial;
        alarmas= new HashMap<>();
        database=FirebaseDatabase.getInstance();
        fragment=this;
        getAlarmas();
        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(root.getContext(), HistorialAlarmasActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getAlarmas(){
        List alarmaList=new ArrayList<>();
        DatabaseReference refAlarmas=database.getReference("Alarmas");
        refAlarmas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    GenericTypeIndicator<Alarma> t = new GenericTypeIndicator<Alarma>() {};
                    Alarma alarma = ds.getValue(t);
                    //alarmas.put(snapshot.getKey(),alarma);
                    alarmaList.add(alarma);
                }
                AlarmAdapter adapter=new AlarmAdapter(root.getContext(),alarmaList,fragment);
                lvAlarmas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private List<Alarma> hashToList(HashMap<String,Alarma> hashAlarmas){
        List<Alarma> listAlarmas= new ArrayList<>();
        for(Map.Entry<String,Alarma> m :hashAlarmas.entrySet()){
            listAlarmas.add(m.getValue());
        }
        return listAlarmas;
    }
}