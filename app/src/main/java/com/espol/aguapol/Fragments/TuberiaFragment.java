package com.espol.aguapol.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.espol.aguapol.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TuberiaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TuberiaFragment extends Fragment {
    TextInputEditText tietAB,tietBC,tietCD,tietDE,tietED,tietFG,tietGH,tietHI,tietIJ;
    TextInputLayout tilAB,tilBC,tilCD,tilDE,tilEF,tilFG,tilGH,tilHI,tilIJ;
    FragmentContainerView containerView;
    private MapsFragment mapsFragment;
    private View root;
    FirebaseDatabase database;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TuberiaFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TuberiaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TuberiaFragment newInstance(String param1, String param2) {
        TuberiaFragment fragment = new TuberiaFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_tuberia, container, false);
        mapsFragment=new MapsFragment();
        database=FirebaseDatabase.getInstance();
        tietAB=root.findViewById(R.id.tietlAB);
        tietBC=root.findViewById(R.id.tietlBC);
        tietCD=root.findViewById(R.id.tietlCD);
        tietDE=root.findViewById(R.id.tietlDE);
        tietED=root.findViewById(R.id.tietEF);
        tietFG=root.findViewById(R.id.tietFG);
        tietGH=root.findViewById(R.id.tietGH);
        tietHI=root.findViewById(R.id.tietHI);
        tietIJ=root.findViewById(R.id.tietIJ);

        tilAB=root.findViewById(R.id.tilAB);
        tilBC=root.findViewById(R.id.tilBC);
        tilCD=root.findViewById(R.id.tilCD);
        tilDE=root.findViewById(R.id.tilDE);
        tilEF=root.findViewById(R.id.tilEF);
        tilFG=root.findViewById(R.id.tilFG);
        tilGH=root.findViewById(R.id.tilGH);
        tilHI=root.findViewById(R.id.tilHI);
        tilIJ=root.findViewById(R.id.tilIJ);



        containerView=root.findViewById(R.id.mapContainerView);

        configMap();
        configTable();
        configClick();
        getData();
        //eliminarHistorial();
        return root;
    }

    private void configClick() {
        tietAB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                builder.setMessage("Ubicación: Salida de bombeo en tanques prosperina");
                builder.setTitle("Tramo 1");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= builder.create();
                if (hasFocus){
                alertDialog.show();}

            }
        });
        tietBC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                builder.setMessage("Ubicación: Entrada a Tanque alto ");
                builder.setTitle("Tramo 2");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= builder.create();
                if (hasFocus){
                    alertDialog.show();}

            }
        });
        tietCD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                builder.setMessage("Ubicación: Fadcom");
                builder.setTitle("Tramo 3");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= builder.create();
                if (hasFocus){
                    alertDialog.show();}

            }
        });
        tietDE.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                builder.setMessage("Ubicación: Tecnologías ");
                builder.setTitle("Tramo 4");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= builder.create();
                if (hasFocus){
                    alertDialog.show();}

            }
        });
        tietED.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                builder.setMessage("Ubicación: Área Conduespol, Protal Copol ");
                builder.setTitle("Tramo 5");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= builder.create();
                if (hasFocus){
                    alertDialog.show();}

            }
        });
        tietFG.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                builder.setMessage("Ubicación: Inicio Ingenierías y Rectorado");
                builder.setTitle("Tramo 6");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= builder.create();
                if (hasFocus){
                    alertDialog.show();}

            }
        });
        tietGH.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                builder.setMessage("Ubicación: Canchas de Ingenierías, Cenae ");
                builder.setTitle("Tramo 7");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= builder.create();
                if (hasFocus){
                    alertDialog.show();}

            }
        });
        tietHI.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                builder.setMessage("Ubicación: Facultad de economía, Edificio Celex");
                builder.setTitle("Tramo 8");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= builder.create();
                if (hasFocus){
                    alertDialog.show();}

            }
        });
        tietIJ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                builder.setMessage("Ubicación: Facultad Marítima");
                builder.setTitle("Tramo 9");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= builder.create();
                if (hasFocus){
                    alertDialog.show();}

            }
        });
    }

    private void eliminarHistorial(){
        DatabaseReference ref= database.getReference("Control caudal");
        List<String> tramos= new ArrayList<>();
        tramos.add("tramo A-B");
        tramos.add("tramo B-C");
        tramos.add("tramo C-D");
        tramos.add("tramo D-E");
        tramos.add("tramo E-F");
        tramos.add("tramo F-G");
        tramos.add("tramo G-H");
        tramos.add("tramo H-I");
        tramos.add("tramo I-J");

        for(String i:tramos){
            ref.child(i).child("historial").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(root.getContext(), "Eliminado correctamente "+ i, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getData() {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        String date= sdf.format(Calendar.getInstance().getTime());
        DatabaseReference ref= database.getReference("Control caudal");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String tab=snapshot.child("tramo A-B").child("estado").getValue().toString();
                    String tbc=snapshot.child("tramo B-C").child("estado").getValue().toString();
                    String tcd=snapshot.child("tramo C-D").child("estado").getValue().toString();
                    String tde=snapshot.child("tramo D-E").child("estado").getValue().toString();
                    String tef=snapshot.child("tramo E-F").child("estado").getValue().toString();
                    String tfg=snapshot.child("tramo F-G").child("estado").getValue().toString();
                    String tgh=snapshot.child("tramo G-H").child("estado").getValue().toString();
                    String thi=snapshot.child("tramo H-I").child("estado").getValue().toString();
                    String tij=snapshot.child("tramo I-J").child("estado").getValue().toString();

                    if(Double.parseDouble(tab)>40.0){
                        tilAB.setError("Cadual excede el valor");
                    }
                    else{
                        tilAB.setError(null);
                    }
                    if(Double.parseDouble(tbc)>40.0){
                        tilBC.setError("Cadual excede el valor");
                    }
                    else{
                        tilBC.setError(null);
                    }
                    if(Double.parseDouble(tcd)>40.0){
                        tilCD.setError("Cadual excede el valor");
                    }
                    else{
                        tilCD.setError(null);
                    }
                    if(Double.parseDouble(tde)>40.0){
                        tilDE.setError("Cadual excede el valor");
                    }
                    else{
                        tilDE.setError(null);
                    }
                    if(Double.parseDouble(tef)>40.0){
                        tilEF.setError("Cadual excede el valor");
                    }
                    else{
                        tilEF.setError(null);
                    }
                    if(Double.parseDouble(tfg)>40.0){
                        tilFG.setError("Cadual excede el valor");
                    }
                    else{
                        tilFG.setError(null);
                    }
                    if(Double.parseDouble(tgh)>40.0){
                        tilGH.setError("Cadual excede el valor");
                    }
                    else{
                        tilGH.setError(null);
                    }
                    if(Double.parseDouble(thi)>40.0){
                        tilHI.setError("Cadual excede el valor");
                    }
                    else{
                        tilHI.setError(null);
                    }
                    if(Double.parseDouble(tij)>40.0){
                        tilIJ.setError("Cadual excede el valor");
                    }
                    else{
                        tilIJ.setError(null);
                    }


                    tietAB.setText(tab);
                    tietBC.setText(tbc);
                    tietCD.setText(tcd);
                    tietDE.setText(tde);
                    tietED.setText(tef);
                    tietFG.setText(tfg);
                    tietGH.setText(tgh);
                    tietHI.setText(thi);
                    tietIJ.setText(tij);
                }


                else {
                    Toast.makeText(root.getContext(), "ERROR DATOS NO SUBIDOS", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(root.getContext(), date  , Toast.LENGTH_SHORT).show();

    }

    private void configTable() {

    }

    private void configMap() {
        tilAB.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerView.setVisibility(View.VISIBLE);
                MapsFragment mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentByTag("map_container");
                double lat=Double.parseDouble(root.getResources().getString(R.string.latTramoAB).toString());
                double lng=Double.parseDouble(root.getResources().getString(R.string.longTramoAB).toString());
                String tramo=root.getResources().getString(R.string.tramo1).toString();
                LatLng sydney = new LatLng(lat, lng );
                Toast.makeText(root.getContext(),tramo, Toast.LENGTH_SHORT).show();
                mapsFragment.changLatLon(sydney,tramo);
            }
        });
        tilBC.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerView.setVisibility(View.VISIBLE);
                MapsFragment mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentByTag("map_container");
                double lat=Double.parseDouble(root.getResources().getString(R.string.latTramoBC).toString());
                double lng=Double.parseDouble(root.getResources().getString(R.string.longTramoBC).toString());
                String tramo=root.getResources().getString(R.string.tramo2).toString();
                LatLng sydney = new LatLng(lat, lng );
                Toast.makeText(root.getContext(),tramo, Toast.LENGTH_SHORT).show();
                mapsFragment.changLatLon(sydney,tramo);
            }
        });
        tilCD.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerView.setVisibility(View.VISIBLE);
                MapsFragment mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentByTag("map_container");
                double lat=Double.parseDouble(root.getResources().getString(R.string.latTramoCD).toString());
                double lng=Double.parseDouble(root.getResources().getString(R.string.longTramoCD).toString());
                String tramo=root.getResources().getString(R.string.tramo3).toString();
                LatLng sydney = new LatLng(lat, lng );
                Toast.makeText(root.getContext(),tramo, Toast.LENGTH_SHORT).show();
                mapsFragment.changLatLon(sydney,tramo);
            }
        });
        tilDE.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerView.setVisibility(View.VISIBLE);
                MapsFragment mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentByTag("map_container");
                double lat=Double.parseDouble(root.getResources().getString(R.string.latTramoDE).toString());
                double lng=Double.parseDouble(root.getResources().getString(R.string.longTramoDE).toString());
                String tramo=root.getResources().getString(R.string.tramo4).toString();
                LatLng sydney = new LatLng(lat, lng );
                Toast.makeText(root.getContext(),tramo, Toast.LENGTH_SHORT).show();
                mapsFragment.changLatLon(sydney,tramo);
            }
        });
        tilEF.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerView.setVisibility(View.VISIBLE);
                MapsFragment mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentByTag("map_container");
                double lat=Double.parseDouble(root.getResources().getString(R.string.latTramoEF).toString());
                double lng=Double.parseDouble(root.getResources().getString(R.string.longTramoEF).toString());
                String tramo=root.getResources().getString(R.string.tramo5).toString();
                LatLng sydney = new LatLng(lat, lng );
                Toast.makeText(root.getContext(),tramo, Toast.LENGTH_SHORT).show();
                mapsFragment.changLatLon(sydney,tramo);
            }
        });
        tilFG.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerView.setVisibility(View.VISIBLE);
                MapsFragment mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentByTag("map_container");
                double lat=Double.parseDouble(root.getResources().getString(R.string.latTramoFG).toString());
                double lng=Double.parseDouble(root.getResources().getString(R.string.longTramoFG).toString());
                String tramo=root.getResources().getString(R.string.tramo6).toString();
                LatLng sydney = new LatLng(lat, lng );
                Toast.makeText(root.getContext(),tramo, Toast.LENGTH_SHORT).show();
                mapsFragment.changLatLon(sydney,tramo);
            }
        });
        tilGH.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerView.setVisibility(View.VISIBLE);
                MapsFragment mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentByTag("map_container");
                double lat=Double.parseDouble(root.getResources().getString(R.string.latTramoGH).toString());
                double lng=Double.parseDouble(root.getResources().getString(R.string.longTramoGH).toString());
                String tramo=root.getResources().getString(R.string.tramo7).toString();
                LatLng sydney = new LatLng(lat, lng );
                Toast.makeText(root.getContext(),tramo, Toast.LENGTH_SHORT).show();
                mapsFragment.changLatLon(sydney,tramo);
            }
        });
        tilHI.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerView.setVisibility(View.VISIBLE);
                MapsFragment mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentByTag("map_container");
                double lat=Double.parseDouble(root.getResources().getString(R.string.latTramoHI).toString());
                double lng=Double.parseDouble(root.getResources().getString(R.string.longTramoHI).toString());
                String tramo=root.getResources().getString(R.string.tramo8).toString();
                LatLng sydney = new LatLng(lat, lng );
                Toast.makeText(root.getContext(),tramo, Toast.LENGTH_SHORT).show();
                mapsFragment.changLatLon(sydney,tramo);
            }
        });
        tilIJ.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerView.setVisibility(View.VISIBLE);
                MapsFragment mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentByTag("map_container");
                double lat=Double.parseDouble(root.getResources().getString(R.string.latTramoIJ).toString());
                double lng=Double.parseDouble(root.getResources().getString(R.string.longTramoIJ).toString());
                String tramo=root.getResources().getString(R.string.tramo9).toString();
                LatLng sydney = new LatLng(lat, lng );
                Toast.makeText(root.getContext(),tramo, Toast.LENGTH_SHORT).show();
                mapsFragment.changLatLon(sydney,tramo);
            }
        });
    }
}