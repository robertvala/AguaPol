package com.espol.aguapol.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.espol.aguapol.R;


public class MainMenu extends Fragment {

    View root;
    CardView cvAlarmas,cvTanques,cvTablero,cvTuberias,cvRiego,cvControl,cvDatos;
    public MainMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenu newInstance(String param1, String param2) {
        MainMenu fragment = new MainMenu();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_main_menu, container, false);
        cvAlarmas=root.findViewById(R.id.cvAlarmas);
        cvTanques=root.findViewById(R.id.cvTanks);
        cvTablero=root.findViewById(R.id.cvTableros);
        cvTuberias=root.findViewById(R.id.cvTuberias);
        cvRiego=root.findViewById(R.id.cvRiego);
        cvControl=root.findViewById(R.id.cvControl);
        cvDatos=root.findViewById(R.id.cvDatos);

        cvAlarmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_mainMenu_to_alarmsActivity);
            }
        });

        cvTanques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_mainMenu_to_nav_gallery);
            }
        });

        cvTablero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_mainMenu_to_nav_slideshow);
            }
        });

        cvTuberias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_mainMenu_to_tuberiaFragment);
            }
        });

        cvRiego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_mainMenu_to_riegoFragment);
            }
        });

        cvControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_mainMenu_to_controlFragment);
            }
        });

        cvDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_mainMenu_to_datosFragment);
            }
        });

        return root;
    }
}