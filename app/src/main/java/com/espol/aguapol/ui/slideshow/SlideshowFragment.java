package com.espol.aguapol.ui.slideshow;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.espol.aguapol.Modelo.Alarma;
import com.espol.aguapol.R;


import com.espol.aguapol.databinding.FragmentSlideshowBinding;
import com.google.android.gms.common.internal.FallbackServiceBroker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SlideshowFragment extends Fragment {
    Button btnBombaA,btnBombaB;
    TextInputEditText tietTEA,tietTEB,tietTAA,tietTAB;
    private Switch switchManual;
    private View root;
    FirebaseDatabase database;


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
        switchManual=binding.switchManual;

        database=FirebaseDatabase.getInstance();


        cargarDatos();
        activacionManual();
        botonesInit();


        return root;
    }

    private void botonesInit() {
        btnBombaA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encendidoBombaA();
            }
        });
        btnBombaB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encendidoBombaB();
            }
        });
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
                            btnBombaA.setEnabled(true);
                            btnBombaB.setEnabled(true);
                            btnBombaA.setClickable(true);
                            btnBombaB.setClickable(true);
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
                    btnBombaA.setEnabled(false);
                    btnBombaB.setEnabled(false);
                }
            }
        });
    }

    public void encendidoBombaA(){
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
        Uri uri= Uri.parse(url);
        Intent intent= new Intent(Intent.ACTION_VIEW,uri);
        root.getContext().startActivity(intent);

        /*Request stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(root.getContext(), "Response is: "+ response.substring(0,500), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(root.getContext(), "This didnt work", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);*/

    }

    public void encendidoBombaB(){
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
                root.getContext().startActivity(intent);

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
        DatabaseReference refBombas=database.getReference("Tablero de control");
        refBombas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String estadoA=snapshot.child("bomba a").child("estado").getValue().toString();
                String estadoB=snapshot.child("bomba b").child("estado").getValue().toString();

                if(estadoA.equals("1")){
                    btnBombaA.setText("ON");
                    btnBombaA.setBackgroundColor(getResources().getColor(R.color.on_bomba));
                }

                else if(estadoA.equals("0")){
                    btnBombaA.setText("OFF");
                    btnBombaA.setBackgroundColor(getResources().getColor(R.color.off_bomba));
                }
                else{
                }

                if(estadoB.equals("1")){
                    btnBombaB.setText("ON");
                    btnBombaB.setBackgroundColor(getResources().getColor(R.color.on_bomba));
                }

                else if(estadoB.equals("0")){
                    btnBombaB.setText("OFF");
                    btnBombaB.setBackgroundColor(getResources().getColor(R.color.off_bomba));
                }
                else{
                }

                String tta=snapshot.child("bomba a").child("tiempoApagado").getValue().toString();
                String tea=snapshot.child("bomba a").child("tiempoEncendido").getValue().toString();
                String unidadesA=snapshot.child("bomba a").child("unidades").getValue().toString();
                tietTAA.setText(tta+" "+unidadesA);
                tietTEA.setText(tea+" "+unidadesA);

                String ttb=snapshot.child("bomba b").child("tiempoApagado").getValue().toString();
                String teb=snapshot.child("bomba b").child("tiempoEncendido").getValue().toString();
                String unidadesB=snapshot.child("bomba b").child("unidades").getValue().toString();
                tietTAB.setText(ttb+" "+unidadesB);
                tietTEB.setText(teb+" "+unidadesB);


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