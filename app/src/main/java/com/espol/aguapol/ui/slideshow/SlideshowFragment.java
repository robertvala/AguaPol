package com.espol.aguapol.ui.slideshow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.espol.aguapol.R;


import com.espol.aguapol.databinding.FragmentSlideshowBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SlideshowFragment extends Fragment {
    private Switch switchManual;
    private SwitchCompat swOnA,swOnB;
    private View root;
    FirebaseDatabase database;
    DatabaseReference refBombas;
    TextView txtTiempoA,txtTiempoB,txtAmpsA,txtAmpsB;
    ProgressBar pbTiempoA,pbTiempoB,pbAmpsA,pbAmpsB;
    CardView cardA,cardB;



    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        root = binding.getRoot();


        switchManual=binding.switchManual;
        swOnA=binding.swBombaA;
        swOnB=binding.swBombaB;
        txtTiempoA=binding.txtTiempoA;
        txtTiempoB=binding.txtHorasB;
        txtAmpsA=binding.txtAmperajeA;
        txtAmpsB=binding.txtAmperajeB;
        pbTiempoA=binding.progressBarTiempoEncendidoA;
        pbTiempoB=binding.progressBarTiempoEncendidoB;
        pbAmpsA=binding.progressBarAmperajeA;
        pbAmpsB=binding.progressBarAmperajeB;
        cardA=binding.cardViewBombaA;
        cardB=binding.cardViewBombaB;
        database=FirebaseDatabase.getInstance();



        cargarDatos();
        activacionManual();
        botonesInit();




        return root;
    }

    private void botonesInit() {
        swOnA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    encendidoBombaA();
                }
                else{
                    apagadoBombaA();
                }
            }
        });


        swOnB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    encendidoBombaB();
                }
                else{
                    apagadoBombaB();
                }

            }
        });
    }

    private void apagadoBombaA() {
        refBombas.child("bomba a").child("estado").setValue(0);
        refBombas.child("bomba a").child("horaApagado").setValue(getHora());
    }

    private String getHora() {
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
        String hour= sdf.format(Calendar.getInstance().getTime());
        return hour;
    }

    private void apagadoBombaB() {
        refBombas.child("bomba b").child("estado").setValue(0);
        refBombas.child("bomba b").child("horaApagado").setValue(getHora());
    }

    void activacionManual(){
        switchManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                    builder.setTitle("Activacion Manual");
                    builder.setMessage("Deseas activar las bombas manualmente?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Aqui se debe cambiar el estado
                            swOnA.setVisibility(View.VISIBLE);
                            swOnB.setVisibility(View.VISIBLE);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog= builder.create();
                    alertDialog.show();
                }

                else{
                    swOnA.setVisibility(View.GONE);
                    swOnB.setVisibility(View.GONE);
                }
            }
        });
    }

    public void encendidoBombaA(){
        refBombas.child("bomba a").child("estado").setValue(1);
        refBombas.child("bomba a").child("amperaje").setValue(3);
        refBombas.child("bomba b").child("estado").setValue(0);

        refBombas.child("bomba b").child("amperaje").setValue(0);
        refBombas.child("bomba b").child("horaEncendido").setValue(0);

        refBombas.child("bomba a").child("horaEncendido").setValue(getHora());
       // RequestQueue queue = Volley.newRequestQueue(root.getContext());
        String url = "http://192.168.0.2:8001/on1";
        //String url = "https://www.google.com";
        //webView.loadUrl(url);
        OkHttpClient client= new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
        } catch (IOException e) {}


            new Thread(new Runnable() {
            @Override
            public void run() {


                URL url = null;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL("http://192.168.0.2:8001/on1");
                    urlConnection = (HttpURLConnection) url.openConnection();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
                //Uri uri= Uri.parse(url);
                //Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                //root.getContext().startActivity(intent);
            }
        }).start();
        //webView.destroy();


    }

    public void encendidoBombaB(){
        refBombas.child("bomba b").child("estado").setValue(1);
        refBombas.child("bomba b").child("amperaje").setValue(3);
        refBombas.child("bomba a").child("estado").setValue(0);

        refBombas.child("bomba a").child("amperaje").setValue(0);
        refBombas.child("bomba a").child("tiempoEncendido").setValue(0);

        refBombas.child("bomba b").child("horaEncendido").setValue(getHora());
        String url = "http://192.168.0.2:8001/on2";
        OkHttpClient client= new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        //webView.loadUrl(url);

        new Thread(new Runnable() {
            @Override
            public void run() {


                /*URL url = null;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL("http://192.168.0.2:8001/on2");
                    urlConnection = (HttpURLConnection) url.openConnection();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }*/
                Uri uri= Uri.parse(url);
                Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                //root.getContext().startActivity(intent);

            }
        }).start();
        //webView.destroy();
        /*Uri uri= Uri.parse(url);
        Intent intent= new Intent(Intent.ACTION_VIEW,uri);
        root.getContext().startActivity(intent);*/

        /*RequestQueue queue = Volley.newRequestQueue(root.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(root.getContext(), "Response is: "+ response.substring(0,500), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(root.getContext(), "This didnt work", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);*/
    }



    void cargarDatos(){
        refBombas=database.getReference("Tablero de control");
        refBombas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String estadoA=snapshot.child("bomba a").child("estado").getValue().toString();
                String estadoB=snapshot.child("bomba b").child("estado").getValue().toString();

                if(estadoA.equals("1")){
                    swOnA.setChecked(true);
                    cardA.setCardBackgroundColor(root.getContext().getResources().getColor(R.color.on_bomba));
                }

                else if(estadoA.equals("0")){
                    swOnA.setChecked(false);
                    cardA.setCardBackgroundColor(root.getContext().getResources().getColor(R.color.gris));
                }
                else{
                }

                if(estadoB.equals("1")){
                    swOnB.setChecked(true);
                    cardB.setCardBackgroundColor(root.getContext().getResources().getColor(R.color.on_bomba));

                }

                else if(estadoB.equals("0")){
                    swOnB.setChecked(false);
                    cardB.setCardBackgroundColor(root.getContext().getResources().getColor(R.color.gris));
                }
                else{
                }

                String tta=snapshot.child("bomba a").child("amperaje").getValue().toString();
                String tea=snapshot.child("bomba a").child("tiempoEncendido").getValue().toString();
                String unidadesA=snapshot.child("bomba a").child("unidades").getValue().toString();
                txtAmpsA.setText(tta+" "+"Amps");
                txtTiempoA.setText(tea+" "+unidadesA);
                pbAmpsA.setProgress(Integer.parseInt(tta));
                pbTiempoA.setProgress(Integer.parseInt(tea));

                String ttb=snapshot.child("bomba b").child("amperaje").getValue().toString();
                String teb=snapshot.child("bomba b").child("tiempoEncendido").getValue().toString();
                String unidadesB=snapshot.child("bomba b").child("unidades").getValue().toString();
                txtAmpsB.setText(ttb+" "+"Amps");
                txtTiempoB.setText(teb+" "+unidadesB);
                pbAmpsB.setProgress(Integer.parseInt(ttb));
                pbTiempoB.setProgress(Integer.parseInt(teb));


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}