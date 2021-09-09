package com.espol.aguapol.ui.alarmas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.Modelo.Comparador;
import com.espol.aguapol.R;

import com.espol.aguapol.adapters.sAdapterAlarmasActivas;

import com.espol.aguapol.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class alarmasActivasFragment extends Fragment {

    private static final String ARG_PARAM1 ="tipo alarma" ;
    private static final String ARG_PARAM2 = "tipo adapter";
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private View root;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    List<Alarma> alarmaList;
    HashMap<String,Alarma> alarmas;
    alarmasActivasFragment fragment;
    String mParam1="";
    String mParam2="";
    String tipoAlarma="";
    String tipoAlarmaAdapter="";

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

    public alarmasActivasFragment(String param1, String param2) {
        // Required empty public constructor
        this.tipoAlarma=param1;
        this.tipoAlarmaAdapter=param2;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2
     * @return A new instance of fragment alarmasEnProcesoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static alarmasActivasFragment newInstance(String param1, String param2) {
        alarmasActivasFragment fragment = new alarmasActivasFragment(param1,param2);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getAlarmas(){

        Query refAlarmas=database.getReference(tipoAlarma);
        refAlarmas.orderByKey().limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List alarmaList=new ArrayList<>();
                for(DataSnapshot ds:snapshot.getChildren()){
                    GenericTypeIndicator<Alarma> t = new GenericTypeIndicator<Alarma>() {};
                    Alarma alarma = ds.getValue(t);
                    //alarmas.put(snapshot.getKey(),alarma);
                    alarmaList.add(alarma);
                }
                Collections.sort(alarmaList, new Comparador() {
                });
                sAdapterAlarmasActivas adapterAlarmasActivas= new sAdapterAlarmasActivas(root.getContext(), alarmaList,tipoAlarmaAdapter,root);
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