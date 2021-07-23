package com.espol.aguapol.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.R;
import com.espol.aguapol.adapters.AlarmAdapter;

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
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private View root;
    ListView lvAlarmas;
    TextView txtAlarmas;
    FirebaseDatabase database;
    List<Alarma> alarmaList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        lvAlarmas=binding.lvAlarmas;
        txtAlarmas=binding.txtAlarmas;
        alarmaList=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        getAlarmas();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void getAlarmas(){
        DatabaseReference refAlarmas=database.getReference("Alarmas");
        refAlarmas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    GenericTypeIndicator<Alarma> t = new GenericTypeIndicator<Alarma>() {};
                    Alarma alarma = ds.getValue(t);
                    alarmaList.add(alarma);
                }
                AlarmAdapter adapter=new AlarmAdapter(root.getContext(),alarmaList);
                lvAlarmas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}