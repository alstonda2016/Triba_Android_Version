package com.davida.tatwpbnw;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static java.security.AccessController.getContext;


public class settingUsername extends AppCompatActivity {

    Button btnSetUsername;
    EditText etxtUsername;
    private FirebaseAuth firebaseAuth;
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    FirebaseDatabase database;
    DatabaseReference myRef;
    RelativeLayout activity_group_main;

    View view;

    Query query;
    Query classNameQuery;
    FirebaseUser user;
    boolean alreadyHasUsername = false;
    String uName;

    public boolean checkPostContent(String post){
        String badwords = "obama bush 9/11  obama trump suicide kill 9/11 clinton nigger niggers jews blacks shoot nigga death cunt republican democrat liberal conservative";


        char[] specialSymbols = {'.', '#', '$', ']', '['};
        for(int i = 0; i < specialSymbols.length; i++){

            if(post.indexOf(specialSymbols[i]) != -1){

                AlertDialog.Builder builder = new AlertDialog.Builder(settingUsername.this);
                builder.setMessage("You cannot use the symbol: " + specialSymbols[i])
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }


        }

        String postWords[] = post.toLowerCase().split(" ");
        for(int i =0 ; i < postWords.length ; i++ ){

            if ( badwords.toLowerCase().indexOf(postWords[i]) != -1 ) {

                AlertDialog.Builder builder = new AlertDialog.Builder(settingUsername.this);
                builder.setMessage("You cannot use: " + postWords[i])
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return false;

            }
        }






        return true;



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_username);

        Intent intent = getIntent();
        alreadyHasUsername = intent.getExtras().getBoolean("alreadyHasUsername");
        if(alreadyHasUsername != true){
            alreadyHasUsername = false;
        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.setUsernameToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorForTextInDarkColor));
        //setDisplayHomeAsUpEnabled displays the back button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setTitle(" ");
        alreadyHasUsername = false;
        if(!alreadyHasUsername){
            myToolbar.setVisibility(View.INVISIBLE);
        }
        else{
            myToolbar.setVisibility(View.VISIBLE);

        }

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alreadyHasUsername == true) {
                    finish();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(settingUsername.this);
                    builder.setMessage("You must complete setup of this account to continue")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        btnSetUsername = (Button) findViewById(R.id.btnEnterNewUsername);
        etxtUsername = (EditText) findViewById(R.id.editTextUsername);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if(alreadyHasUsername == true)
        {

            SharedPreferences prefs = getSharedPreferences("USERINFO", MODE_PRIVATE);
            uName = prefs.getString("uName", null);
            if (uName == null) {
                alreadyHasUsername = false;
            }
            else{
               // etxtUsername.setText(uName);
            }

        }
        else{
            SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
            editor.putBoolean("firstTimeUser", true);
            editor.apply();

        }


        btnSetUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etxtUsername.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(settingUsername.this);
                    builder.setMessage("Textbox is Empty")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(etxtUsername.getText().toString().contains(" ")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(settingUsername.this);
                    builder.setMessage("Username cannot include open spaces")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else if(!checkPostContent(etxtUsername.getText().toString())){



                }

                else {


                    String uName = etxtUsername.getText().toString().toLowerCase();

                    FirebaseDatabase.getInstance().getReference().child("username_lookup").child(uName).setValue(user.getUid().toString(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error != null) {


                                AlertDialog.Builder builder = new AlertDialog.Builder(settingUsername.this);
                                builder.setMessage("Sorry, this username is already taken")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                String newuName = etxtUsername.getText().toString();
                                setUnameInAccount(newuName);


                            }
                        }
                    });

                }
/*
                ref!.child("username_lookup").child((username?.lowercased())!).setValue(Auth.auth().currentUser?.uid) { (error, ref) in
                    if error == nil {

                        self.setUsernameInAccount(username: username!)
                    }
            else{
                        let alert = UIAlertController(title: "", message: "Sorry, that username is taken", preferredStyle: .alert)
                        let okAction = UIAlertAction(title: "OK", style: .default) { (alert: UIAlertAction!) -> Void in
                            // self.canvas.image = nil
                        }

                        alert.addAction(okAction)
                        self.present(alert, animated: true, completion:nil)

                    }

                }
                */

            }
        });

    }


    public void setUnameInAccount(String Username){


        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid().toString()).child("userName").setValue(Username);
        if(alreadyHasUsername){
            FirebaseDatabase.getInstance().getReference().child("username_lookup").child(uName).removeValue();

        }
        SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
        editor.putString("uName", Username);
        //etxtUsername.setText(Username);
        //alreadyHasUsername = true;
        //uName = Username;
        editor.apply();

        //uName = username
        // alreadyHasUsername = true

        Toast.makeText(settingUsername.this, "Welcome, "+ Username, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(settingUsername.this, tabMainActivity.class);
        intent.putExtra("allowBackPress", false);
        startActivity(intent);

    }

    @Override
    public void onBackPressed(){
        if(alreadyHasUsername == true) {
            finish();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(settingUsername.this);
            builder.setMessage("You must complete setup of this account to continue")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
}
