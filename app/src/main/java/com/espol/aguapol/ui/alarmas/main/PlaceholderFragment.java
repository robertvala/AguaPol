package com.espol.aguapol.ui.alarmas.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.espol.aguapol.ui.alarmas.alarmasEnProcesoFragment;
import com.espol.aguapol.ui.alarmas.alarmasSolucionadasFragments;
import com.espol.aguapol.ui.alarmas.alarmasActivasFragment;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;


    public static Fragment newInstance(int index) {
        Fragment fragment = null;
        switch (index){
            case 1: fragment=new alarmasActivasFragment();break;
            case 2: fragment=new alarmasEnProcesoFragment();break;
            case 3: fragment=new alarmasSolucionadasFragments();break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
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

        View root = null;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}