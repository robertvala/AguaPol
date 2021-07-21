package com.espol.aguapol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText tietUser,tietPassword;
    private ConstraintLayout clLogin;
    private Button btnLogin,btnRegister;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private boolean userIsRegistered;
    private String user="";
    private String password="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        tietPassword= findViewById(R.id.tietPasswordLogin);
        tietUser=findViewById(R.id.tietUserLogin);
        clLogin=findViewById(R.id.clLogin);
        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);
        btnLogin.setEnabled(false);
        ingresarUserPassword();

        firebaseAuth= FirebaseAuth.getInstance();
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();

                if(user!=null){
                    if(!user.isEmailVerified()){
                        Snackbar snackbar= Snackbar.make(clLogin,"Porfavor verifique su correo electronico ", BaseTransientBottomBar.LENGTH_LONG);
                        snackbar.show();
                        firebaseAuth.signOut();
                    }

                    else {

                        //User is signed in
                        Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }

            }
        };

    }

    void ingresarUserPassword(){
        tietUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    user=s.toString();
                }
                if(s.length()>0 && password.length()>0){
                    btnLogin.setEnabled(true);
                }
                else{
                    btnLogin.setEnabled(false);
                }
            }
        });

        tietPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    password=s.toString();
                }
                if(s.length()>0 && user.length()>0){
                    btnLogin.setEnabled(true);
                }
                else{
                    btnLogin.setEnabled(false);
                }
            }
        });
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

    public void signin(View view){
        Intent intent= new Intent(LoginActivity.this,SignInActivity.class);
        intent.putExtra("user",tietUser.getText().toString());
        intent.putExtra("password",tietPassword.getText().toString());
        startActivity(intent);
    }

    public void login(View view){

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String user= tietUser.getText().toString();
        String password= tietPassword.getText().toString();
        verificarEmailInFirebase(user);

        firebaseAuth.signInWithEmailAndPassword(user,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(!task.isSuccessful()){

                    if(!userIsRegistered){
                        Snackbar snackbar= Snackbar.make(clLogin,"Usuario no registrado", BaseTransientBottomBar.LENGTH_LONG);
                        snackbar.setAction("Registrar",v -> signin(v));
                        snackbar.show();


                    }
                    else {
                        Snackbar snackbar= Snackbar.make(clLogin,"Contraseña incorrecta", BaseTransientBottomBar.LENGTH_LONG);
                        snackbar.show();
                    }


                }
            }
        });
    }

    public  void  verificarEmailInFirebase(String email){
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()){
                            boolean check =!task.getResult().getSignInMethods().isEmpty();
                            if (check){
                                //Toast.makeText(getApplicationContext(),"El email esta en uso",Toast.LENGTH_LONG).show();
                                userIsRegistered=true;
                            }
                            else {
                                //Toast.makeText(getApplicationContext(),"El email no esta en uso, por ende el usuario no existe",Toast.LENGTH_LONG).show();
                                userIsRegistered=false;
                            }
                        }
                    }
                });
    }
}