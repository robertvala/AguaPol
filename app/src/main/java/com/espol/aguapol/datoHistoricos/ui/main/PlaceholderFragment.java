package com.espol.aguapol.datoHistoricos.ui.main;

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


import com.espol.aguapol.Fragments.DatosFragment;
import com.espol.aguapol.databinding.FragmentDatosHistoticosBinding;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_PARAM1 = "param1";

    private PageViewModel pageViewModel;
    private FragmentDatosHistoticosBinding binding;

    public static Fragment newInstance(int index) {
        Fragment fragment = new DatosFragment();
        Bundle bundle = new Bundle();
        String mparam1="";
        if(index==1){ mparam1="Caudal";}
        else if(index==2){
            mparam1="Alarmas";
        }
        else if(index==3){
            mparam1="Riego";
        }
        bundle.putString(ARG_PARAM1, mparam1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentDatosHistoticosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.sectionLabel;
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
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