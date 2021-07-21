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

import com.espol.aguapol.R;
import com.espol.aguapol.databinding.FragmentSlideshowBinding;
import com.google.android.material.textfield.TextInputEditText;

public class SlideshowFragment extends Fragment {
    Button btnBombaA,btnBombaB;
    TextInputEditText tietTEA,tietTEB,tietTAA,tietTAB;
    private View root;

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




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}