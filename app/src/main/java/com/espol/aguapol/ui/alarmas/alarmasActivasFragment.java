package com.espol.aguapol.ui.alarmas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.R;
import com.espol.aguapol.adapters.AlarmAdapter;

import com.espol.aguapol.adapters.sAdapterAlarmasActivas;

import com.espol.aguapol.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class alarmasActivasFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private View root;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    List<Alarma> alarmaList;
    HashMap<String,Alarma> alarmas;
    alarmasActivasFragment fragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        recyclerView=binding.rvAlarmas;
        alarmas= new HashMap<>();
        database=FirebaseDatabase.getInstance();
        fragment=this;
        getAlarmas();


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
        refAlarmas.orderByValue().limitToFirst(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    GenericTypeIndicator<Alarma> t = new GenericTypeIndicator<Alarma>() {};
                    Alarma alarma = ds.getValue(t);
                    //alarmas.put(snapshot.getKey(),alarma);
                    alarmaList.add(alarma);
                }
                sAdapterAlarmasActivas adapterAlarmasActivas= new sAdapterAlarmasActivas(root.getContext(), alarmaList);
                recyclerView.setLayoutManager( new LinearLayoutManager(root.getContext()));
                recyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(),DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(adapterAlarmasActivas);
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