package com.example.android.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/*
Activité d'accueil de notre application et accessible uniquement aux utilisateurs connectés
 */
public class MainActivity extends AppCompatActivity {
    private Button logout;
    //l'objet firebaseAuth nous servira de deconnecter l'utisateur
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nous deconnectons l'utilisateur actif dans l'application puis nous redemarrons le LoginActivity.
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }

}
