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
Activité de mère et d'authentification de l'utilisateur dans l'application
 */
public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private Button signup;
    private Button reset;
    //l'objet firebaseAuth, pour gérer l'authentification d'un utilisateur et la reinitialisation du mot de passe
    private FirebaseAuth firebaseAuth;
    // authStateListener nous permet de mettre en écoute l'utilisateur actif, de détecter tout changement d'etat
    private FirebaseAuth.AuthStateListener authStateListener;
    //ce request code est utilisé pour identifier le lancement du RegisterActivity
    private static int REQUEST_SIGNUP = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //on initialiase l'objet firebaseAuth avec l'instance actuelle de FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        reset = (Button) findViewById(R.id.reset);

        //le processus d'authentification de l'utilisateur est defini par ici
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                if(!emailStr.isEmpty()&&!passwordStr.isEmpty()){
                    // on authentifie l'utilisateur
                    firebaseAuth.signInWithEmailAndPassword(emailStr,passwordStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //Si ok, on recuper son email et on affiche un message de notification corespondante
                            FirebaseUser user = authResult.getUser();
                            Toast.makeText(getApplicationContext(),"Utilisateur connecté :"+user.getEmail(),Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Sinon on affiche un message d'erreur
                            Toast.makeText(getApplicationContext(),"Authentification echoué",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nous demarrons le RegisterActivity avec le request code associé, startActivityForResult parce que
                // nous attendons un resultat provenant du RegisterAcivity qui est l'email de l'utilisateur
                startActivityForResult(new Intent(getApplicationContext(),RegisterActivity.class),REQUEST_SIGNUP);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                //On implement ici la reinitialisation du mot de passe de l'utilisateur, s'il fournit une adresse email valide
                if(!emailStr.isEmpty())
                    firebaseAuth.sendPasswordResetEmail(emailStr).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Le mail de reinitialisation est envoyé",Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });

        //On ecoute le changement d'etat de l'utilisateur
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    //Si l'utilisateur est connecte on lance l'activity d'accueil qui est le MainActivity
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else {
                    //Message d'erreur ou action à effectué lorsque l'utilisateur n'est pas connecté
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        //lorsque le Login activité rentre dans son etat de lancement, on ajoute le listener sur l'objet firebasAuth
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Et lors de l'arret de l'activite on supprime le listener
        if(authStateListener!=null)
            firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //lorsque l'utilisateur revient du RegisterActivity, on intercepte l'email de l'utilisateur
        //au cas ou nous avons un resultat
        if(requestCode==REQUEST_SIGNUP&&resultCode==RESULT_OK)
            email.setText(data.getStringExtra(Intent.EXTRA_TEXT));
    }
}
