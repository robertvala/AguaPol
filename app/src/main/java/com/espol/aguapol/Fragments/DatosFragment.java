package com.espol.aguapol.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.espol.aguapol.ImportarCsv;
import com.espol.aguapol.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    TextInputEditText tietFechaInicio,tietFechaFin,tietTramos;
    Date dateIncio;
    Date dateFin;
    String fechaInicio="";
    String fechaFin="";
    View root;
    Calendar calendar= Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    String [] selectTramos;
    boolean [] checkedItems;
    List<Integer> selectIndex;
    List<String> tramosSeleccionados;
    Button btnImportar,btnCargar;
    TextInputLayout tilTramos,tilFechaIncio,tilFechaFin;
    FirebaseDatabase database;
    public DatosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosFragment newInstance(String param1, String param2) {
        DatosFragment fragment = new DatosFragment();
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
        root=inflater.inflate(R.layout.fragment_datos, container, false);

        tietFechaFin=root.findViewById(R.id.tietFechaFin);
        tietFechaInicio=root.findViewById(R.id.tietFechaInicio);
        tietTramos=root.findViewById(R.id.tietTramos);
        selectTramos=root.getResources().getStringArray(R.array.selectTramos);
        checkedItems=new boolean[selectTramos.length];
        tramosSeleccionados=new ArrayList<>();
        selectIndex= new ArrayList<>();
        btnCargar=root.findViewById(R.id.btnCargarHistorico);
        btnCargar.setEnabled(true);
        tilFechaFin=root.findViewById(R.id.tilFechaFin);
        tilFechaIncio=root.findViewById(R.id.tilFechaInicio);
        tilTramos=root.findViewById(R.id.tilElegirTramos);
        database=FirebaseDatabase.getInstance();



        btnImportar=root.findViewById(R.id.btnImportarCSV);
        //importarCSV();
        cargarDatosHistoricos();
        pickDates();
        pickTramos();
        return root;
    }

    private void cargarDatosHistoricos() {
        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH)+1;
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                String date=year+"-"+month+"-"+day;


                if(!fechaInicio.equals("")&&!fechaFin.equals("")&&tramosSeleccionados.size()>0){
                    try{
                        if(diferenciaDias(fechaFin,fechaInicio)>=0){
                            tilFechaIncio.setError(null);
                            tilFechaFin.setError(null);
                            tilTramos.setError(null);
                            obtenerDatosFechas();
                        }
                        else{
                            tilFechaFin.setError("La fecha fin debe ser mayor o igual a la fehca de inicio ");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else if(tramosSeleccionados.size()<=0){
                    tilTramos.setError("Seleccionar los tramos a inspeccionar");
                }


                else{
                    Toast.makeText(root.getContext(), "NO hay seleccionado la fecha", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    private void obtenerDatosFechas() {
        for(String i: tramosSeleccionados){
            DatabaseReference ref=database.getReference("Control caudal").child(i).child("historial");

        }
    }

    private void importarCSV() {
        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(root.getContext(), ImportarCsv.class);
                startActivity(intent);
            }
        });
    }

    public static int diferenciaDias(String fecha1,String fecha2) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date date1= format.parse(fecha1);
        Date date2=format.parse(fecha2);
        int dias = (int) ((date1.getTime() - date2.getTime()) / 86400000);
        return  dias;

    }

    private void pickTramos() {
        tietTramos.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                builder.setTitle("Seleccionar tramos")
                        .setMultiChoiceItems(selectTramos, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if(isChecked){
                                    selectIndex.add(which);
                                }else if(selectIndex.contains(which)){
                                    selectIndex.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String msg="";
                                for(int i:selectIndex){
                                    msg=msg+"\n"+selectTramos[i];
                                    tramosSeleccionados.add(selectTramos[i]);
                                }
                                tietTramos.setText(msg);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog=builder.create();
                if(hasFocus){
                    builder.show();
                    tietTramos.clearFocus();
                }
            }
        });
    }

    private void pickDates() {
        Calendar calendar= Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH)+1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tietFechaFin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        root.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date= year+"-"+month+"-"+day;
                        fechaFin=date;
                        tietFechaFin.setText(date);
                        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            dateFin=dateFormat.parse(tietFechaInicio.getText().toString());
                            if(!fechaInicio.equals("")&&!fechaFin.equals("")){
                                btnCargar.setEnabled(true);
                            }
                            else{
                                btnCargar.setEnabled(false);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },year,month,day);

                if(hasFocus) {
                    datePickerDialog.show();
                }

                else{
                    datePickerDialog.dismiss();
                }
                tietFechaFin.clearFocus();
            }
        });
        tietFechaInicio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        root.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date= year+"-"+month+"-"+day;
                        tietFechaInicio.setText(date);
                        fechaInicio=date;
                        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            dateIncio=dateFormat.parse(tietFechaInicio.getText().toString());
                            if(!fechaInicio.equals("")&&!fechaFin.equals("")){
                                btnCargar.setEnabled(true);
                            }
                            else{
                                btnCargar.setEnabled(false);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },year,month,day);

                if(hasFocus) {
                    datePickerDialog.show();
                }

                else{
                    datePickerDialog.dismiss();
                }
                tietFechaInicio.clearFocus();
            }
        });
    }

}