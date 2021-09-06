package com.espol.aguapol;

<<<<<<< Updated upstream:app/src/main/java/com/espol/aguapol/MainActivity.java
import android.content.ClipData;
=======
import android.content.Context;
>>>>>>> Stashed changes:app/src/main/java/com/espol/aguapol/Activities/MainActivity.java
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< Updated upstream:app/src/main/java/com/espol/aguapol/MainActivity.java
import com.google.android.material.snackbar.Snackbar;
=======
import com.espol.aguapol.R;

import com.espol.aguapol.databinding.ActivityMainBinding;
import com.espol.aguapol.ui.alarmas.AlarmsActivity;
>>>>>>> Stashed changes:app/src/main/java/com/espol/aguapol/Activities/MainActivity.java
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.espol.aguapol.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.jetbrains.annotations.NotNull;
<<<<<<< Updated upstream:app/src/main/java/com/espol/aguapol/MainActivity.java
=======

import java.util.Locale;
>>>>>>> Stashed changes:app/src/main/java/com/espol/aguapol/Activities/MainActivity.java

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private NotificationBadge notificationBadge;
    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean b= Boolean.parseBoolean("FalSe");
        System.out.println(b);
        super.onCreate(savedInstanceState);

        firebaseAuth= FirebaseAuth.getInstance();
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null){
                    //User is signed in
                }
                else{
                    Toast.makeText(MainActivity.this, "Usuario ha cerrado sesion correctamente", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
            }
        };

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.mainMenu,R.id.alarmsActivity,R.id.controlFragment,R.id.tuberiaFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        View header= navigationView.getHeaderView(0);
        TextView txtUserEmail= header.findViewById(R.id.txtEmailUser);
<<<<<<< Updated upstream:app/src/main/java/com/espol/aguapol/MainActivity.java
        txtUserEmail.setText(firebaseAuth.getCurrentUser().getEmail());
=======
        TextView txtInicials=header.findViewById(R.id.txtInicials);
        String email=firebaseAuth.getCurrentUser().getEmail();
        String inicials= email.substring(0,2).toUpperCase(Locale.ROOT);
        txtInicials.setText(inicials);
        txtUserEmail.setText(email);
>>>>>>> Stashed changes:app/src/main/java/com/espol/aguapol/Activities/MainActivity.java
        NavigationUI.setupWithNavController(navigationView, navController);

        notificationBadge=binding.appBarMain.notificationMain;
        ConstraintLayout notificationMain= binding.appBarMain.linearNotificationMain;
        notificationMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlarmsActivity.class);
                startActivity(intent);

            }
        });
        countNotifications();



    }

    private void countNotifications() {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref= database.getReference("Alarmas");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cont=0;
                for(DataSnapshot ds: snapshot.getChildren()){
                    cont++;
                }
                notificationBadge.setNumber(cont,true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item= menu.getItem(0);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Atencion");
                builder.setMessage("Deseas cerrar sesion?");
                builder.setCancelable(false);
                builder.setIcon(getResources().getDrawable(R.drawable.alarmas));
                builder.setPositiveButton("CERRAR SESION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog= builder.create();
                dialog.show();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}