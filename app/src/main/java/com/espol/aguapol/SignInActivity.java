package com.espol.aguapol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends AppCompatActivity {
    private TextInputEditText tietNewUser,tietNewpassword,tietConfirmPassword;
    private TextInputLayout tilNewUser,tilNewPassword,tilConfirmPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private boolean passwordsAreEqual;
    private boolean correctUsername;
    private Button btnRegister;

    private String newPassword="";
    private String confirmPassword="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        tietNewUser=findViewById(R.id.tietNewUser);
        tietNewpassword=findViewById(R.id.tietRegisterPassword);
        tietConfirmPassword=findViewById(R.id.tietConfirmPassword);
        tilConfirmPassword=findViewById(R.id.tilConfirmar);
        tilNewPassword=findViewById(R.id.tilNuevaContra);
        tilNewUser=findViewById(R.id.tilNewUser);
        btnRegister=findViewById(R.id.btnSignUp);

        btnRegister.setEnabled(false);
        firebaseAuth= FirebaseAuth.getInstance();
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null){
                    //User is signed in
                    Toast.makeText(SignInActivity.this,"El usuario fue creado",Toast.LENGTH_SHORT).show();
                    }
                }
        };
        validarUsuario();
        comparePasswords();

    }

    private void validarUsuario() {
        tietNewUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().contains("@espol.edu.ec")){
                    tilNewUser.setError("no contiente @espol.edu.ec");
                    correctUsername=false;
                    btnRegister.setEnabled(false);


                }else{
                    correctUsername=true;
                    tilNewUser.setError(null);
                    allValid();
                }
            }
        });
    }

    void allValid() {
        if (correctUsername && passwordsAreEqual) {
            btnRegister.setEnabled(true);
        }
        else{
            btnRegister.setEnabled(false);
        }
    }

    void comparePasswords(){
        tietConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                newPassword=tietNewpassword.getText().toString();
                if(s.length()>0){ confirmPassword=s.toString();}
                else{confirmPassword="";}
                if(newPassword.equals(confirmPassword)){
                    passwordsAreEqual=true;
                    tilConfirmPassword.setError(null);
                    allValid();
                }
                else{
                    passwordsAreEqual=false;
                    tilConfirmPassword.setError("Las contraseñas no son iguales");
                    btnRegister.setEnabled(false);
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

    public void signUp(View view){
        String username= tietNewUser.getText().toString();
        String password= tietNewpassword.getText().toString();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        firebaseAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, "Hubo un error al crear un usuario", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseUser user= firebaseAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                AlertDialog.Builder builder= new AlertDialog.Builder(SignInActivity.this);
                                builder.setTitle("Verifique su correo electronico");
                                builder.setMessage("Le llegara a su correo electronico un link , para verificar su usuario.");
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                AlertDialog dialog= builder.create();
                                dialog.show();

                            }
                        }
                    });
                }
            }
        });

    }
}