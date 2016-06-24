package com.example.android.sampleapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
Activité d'enregistrement d'un nouveau utilisateur
( email, mot de passe)
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText passwordBis;
    private Button signup;
    //l'objet firebaseAuth nous permettra ici d'effectuer les actions de creation de compte utilisateur
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //on initialiase l'objet firebaseAuth avec l'instance actuelle de FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        passwordBis = (EditText) findViewById(R.id.password_bis);
        signup = (Button) findViewById(R.id.signup);

        //nous definissons par ici les actions à effectué lorsque l'utilisateur clique sur le bouton s'enregistrer
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                String passwordBisStr = password.getText().toString();

                if(!emailStr.isEmpty()&&!passwordStr.isEmpty()&&!passwordBisStr.isEmpty()){
                    //nous créons un nouvel utilisateur avec les informations fournis par l'utilisateur
                    firebaseAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = authResult.getUser();
                            Toast.makeText(getApplicationContext(),"Utilisateur créé :"+user.getEmail(),Toast.LENGTH_SHORT).show();
                            Intent i = new Intent();
                            i.putExtra(Intent.EXTRA_TEXT,user.getEmail());
                            setResult(RESULT_OK,i);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // erreur ....
                        }
                    });

                }

            }
        });
    }
}
