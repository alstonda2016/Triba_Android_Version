package com.davida.tatwpbnw;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class account extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    LoginButton lgInButtin;
    EditText fNameSU;
    EditText emailSU;
    EditText pwrdSU;
    EditText collegeSU;
    EditText emailSI;
    EditText pwrdSI;
    String fNameSUT;
    String emailSUT;
    String pwrdSUT;
    String collegeSUT;
    String emailSIT;
    String pwrdSIT;
     Button loginButton;
    FirebaseDatabase database;

    DatabaseReference usersRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_account);

        database = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();

        //progress.setTitle("Loading");
        //progress.setMessage("Wait while loading...");
        // progress.setCancelable(false); // disable dismiss by tapping outside of the dialog



        final ProgressBar progressb =  (ProgressBar)findViewById(R.id.indeterminateBar);
        progressb.setVisibility(View.INVISIBLE);
        progressb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorForTextInDarkColor), PorterDuff.Mode.MULTIPLY);

         loginButton = (Button)findViewById(R.id.btnAccountLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(account.this, Arrays.asList("public_profile", "user_friends"));
            }
        });

        //final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        //loginButton.setReadPermissions("email", "public_profile");


        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                //FirebaseUser cUser = firebaseAuth.getCurrentUser();
                final FirebaseUser user = firebaseAuth.getCurrentUser();


                // usersRef = database.getReference().child("Users");
                //usersRef = database.getReference().child("Users").child(cUser.getUid().toString()).child("UserInfo");


                Log.d("!!!!!", "1");
                Log.d("!!!!!", "1");
                Log.d("!!!!!", "1");
                Log.d("!!!!!", "1");
                Log.d("!!!!!", "1");
                Log.d("!!!!!", "1");
                Log.d("!!!!!", "1");


                progressb.setVisibility(View.VISIBLE);

                loginButton.setVisibility(View.INVISIBLE);
                /*
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                String email = object.getString("birthday");
                                    Toast.makeText(account.this, email, Toast.LENGTH_LONG).show();

                                    // String birthday = object.getString("birthday"); // 01/31/1980 format
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "birthday");
                request.setParameters(parameters);
                request.executeAsync();
*/

                if(user != null) {

/*
Called when user logs in from Facebook
 */                    usersRef = database.getReference().child("Users").child(user.getUid().toString());


                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {






                            if(dataSnapshot.getValue() == null) {
                                //New User
                                String facebookUserId = "";




                                Log.d("$$$$$$$$$$$$$$", "ONE");

                                for(UserInfo profile : user.getProviderData()) {
                                    // check if the provider id matches "facebook.com"
                                    if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                                        facebookUserId = profile.getUid();
                                    }
                                }
                                String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                                String inviteKey = "P"+FirebaseDatabase.getInstance().getReference().push().getKey().toString();


                                userObject newUser = new userObject("ACTIVE", user.getUid().toString(),inviteKey, user.getDisplayName(), photoUrl,
                                        0,0, 0, "normal", "none", "none", "none",
                                        0, 0, 0, false);


                                usersRef.setValue(newUser);
                                        /*
                                        usersRef.child("userID").setValue(user.getUid().toString());
                                        usersRef.child("inviteKey").setValue(inviteKey);
                                        usersRef.child("userFullName").setValue(user.getDisplayName());
                                        usersRef.child("userProfilePic").setValue(photoUrl);
                                        usersRef.child("ActiveStatus").setValue("ACTIVE");
*/
                                Intent intent = new Intent(account.this, settingUsername.class);
                                intent.putExtra("alreadyHasUsername", false);
                                startActivity(intent);
                                finish();


                                finish();

                            }
                            else{
                                String facebookUserId = "";

                                if(dataSnapshot.child("userID").getValue() != null  && dataSnapshot.child("userName").getValue() != null &&
                                        dataSnapshot.child("userProfilePic").getValue() != null && dataSnapshot.child("ActiveStatus").getValue() != null &&  dataSnapshot.child("userFullName").getValue() != null  )
                                {

                                    Log.d("$$$$$$$$$$$$$$", "TWO");

                                    SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
                                    editor.putString("uName", dataSnapshot.child("userName").getValue().toString());
                                    //etxtUsername.setText(Username);
                                    //alreadyHasUsername = true;
                                    //uName = Username;
                                    editor.apply();

                                    //If account exists but was deactivated
                                    if(dataSnapshot.child("ActiveStatus").getValue().equals("INACTIVE")){

                                        for(UserInfo profile : user.getProviderData()) {
                                            // check if the provider id matches "facebook.com"
                                            if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                                                facebookUserId = profile.getUid();
                                            }
                                        }

                                        long uAccountResetDate = 0;
                                        int userStrikes = 0;

                                        if(dataSnapshot.child("userLastAccountResetDate").getValue() != null){
                                            uAccountResetDate = (long) dataSnapshot.child("userLastAccountResetDate").getValue();
                                        }
                                        if(dataSnapshot.child("userStrikes").getValue() != null){
                                            userStrikes = (int) dataSnapshot.child("userStrikes").getValue();

                                        }
                                        String uName = dataSnapshot.child("userName").getValue().toString();

                                        String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                                        String inviteKey = "P"+FirebaseDatabase.getInstance().getReference().push().getKey().toString();


                                        userObject newUser = new userObject("ACTIVE", user.getUid().toString(),inviteKey, user.getDisplayName(), photoUrl,
                                                0,0, 0, "normal", "none", "none", "none",
                                                uAccountResetDate, -uAccountResetDate, userStrikes, uName, false);


                                        usersRef.setValue(newUser);


                                    }

                                    Intent intent = new Intent(account.this, tabMainActivity.class);

                                    startActivity(intent);
                                    finish();

                                }
                                //Doesn't have username
                                else if(dataSnapshot.child("userID").getValue() != null  &&
                                        dataSnapshot.child("userProfilePic").getValue() != null && dataSnapshot.child("ActiveStatus").getValue() != null &&  dataSnapshot.child("userFullName").getValue() != null){


                                    Log.d("$$$$$$$$$$$$$$", "THREE");


                                    if(dataSnapshot.child("ActiveStatus").getValue().equals("INACTIVE")){

                                        for(UserInfo profile : user.getProviderData()) {
                                            // check if the provider id matches "facebook.com"
                                            if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                                                facebookUserId = profile.getUid();
                                            }
                                        }

                                        long uAccountResetDate = 0;
                                        int userStrikes = 0;

                                        if(dataSnapshot.child("userLastAccountResetDate").getValue() != null){
                                            uAccountResetDate = (long) dataSnapshot.child("userLastAccountResetDate").getValue();
                                        }
                                        if(dataSnapshot.child("userStrikes").getValue() != null){
                                            userStrikes = (int) dataSnapshot.child("userStrikes").getValue();

                                        }

                                        String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                                        String inviteKey = "P"+FirebaseDatabase.getInstance().getReference().push().getKey().toString();


                                        userObject newUser = new userObject("ACTIVE", user.getUid().toString(),inviteKey, user.getDisplayName(), photoUrl,
                                                0,0, 0, "normal", "none", "none", "none",
                                                uAccountResetDate, -uAccountResetDate, userStrikes, false);


                                        usersRef.setValue(newUser);


                                    }


                                    Intent intent = new Intent(account.this, settingUsername.class);
                                    intent.putExtra("alreadyHasUsername", false);
                                    startActivity(intent);
                                    finish();


                                }

                                else{


                                    Log.d("$$$$$$$$$$$$$$", "FOUR");


                                    for(UserInfo profile : user.getProviderData()) {
                                        // check if the provider id matches "facebook.com"
                                        if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                                            facebookUserId = profile.getUid();
                                        }
                                    }
                                    String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                                    String inviteKey = "P"+FirebaseDatabase.getInstance().getReference().push().getKey().toString();


                                    userObject newUser = new userObject("ACTIVE", user.getUid().toString(),inviteKey, user.getDisplayName(), photoUrl,
                                            0,0, 0, "normal", "none", "none", "none",
                                            0, 0, 0, false);


                                    usersRef.setValue(newUser);
                                        /*
                                        usersRef.child("userID").setValue(user.getUid().toString());
                                        usersRef.child("inviteKey").setValue(inviteKey);
                                        usersRef.child("userFullName").setValue(user.getDisplayName());
                                        usersRef.child("userProfilePic").setValue(photoUrl);
                                        usersRef.child("ActiveStatus").setValue("ACTIVE");
*/
                                    Intent intent = new Intent(account.this, settingUsername.class);
                                    intent.putExtra("alreadyHasUsername", false);
                                    startActivity(intent);
                                    finish();



                                }
                                //Toast.makeText(MainActivity.this, "Hi", Toast.LENGTH_LONG).show();


                            }






                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });






























                }
                else{

                    Toast.makeText(account.this, "Error", Toast.LENGTH_LONG);

                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
/*


        // Callback registration
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Toast.makeText(account.this, "Account made", Toast.LENGTH_LONG);
                FirebaseUser cUser = firebaseAuth.getCurrentUser();

                usersRef = database.getReference().child("Users");


                progress.setVisibility(View.VISIBLE);

                loginButton.setVisibility(View.INVISIBLE);

                if(cUser != null) {


                    usersRef.child(cUser.getUid().toString()).child("newUserStage").setValue(1);
                    usersRef.child(cUser.getUid().toString()).child("userID").setValue(cUser.getUid().toString());
                    usersRef.child(cUser.getUid().toString()).child("inviteKey").setValue(FirebaseDatabase.getInstance().getReference().push());
                    usersRef.child(cUser.getUid().toString()).child("userName").setValue(cUser.getDisplayName());
                    usersRef.child(cUser.getUid().toString()).child("picLink").setValue(cUser.getPhotoUrl());
                    Intent intent = new Intent(account.this, UserUniversityInfo.class);
                    intent.putExtra("allowBackPress", false);
                    startActivity(intent);
                }
                else{

                    Toast.makeText(account.this, "Error", Toast.LENGTH_LONG);

                }

            }

            @Override
            public void onCancel() {
                Toast.makeText(account.this, "Canceled", Toast.LENGTH_LONG);
                progress.setVisibility(View.INVISIBLE);

                loginButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(account.this, exception.toString(), Toast.LENGTH_LONG);
                progress.setVisibility(View.INVISIBLE);

                loginButton.setVisibility(View.VISIBLE);
            }
        });

        */
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
        //Called when activity loads.
        //If user is signed in already, will just run this
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                //  desc.setText(user.getUid());
                Log.d("!!!!!", "2");
                Log.d("!!!!!", "2");
                Log.d("!!!!!", "2");
                Log.d("!!!!!", "2");
                Log.d("!!!!!", "2");
                Log.d("!!!!!", "2");
                Log.d("!!!!!", "2");
                Log.d("!!!!!", "2");
                Log.d("!!!!!", "2");
                Log.d("!!!!!", "2");
                Log.d("!!!!!", "2");
                Log.d("!!!!!", "2");


                if(user != null) {

                    Log.d("$$$$$$$$$$$$$$", "FIVEEE");


                    final String uid = firebaseAuth.getCurrentUser().getUid();

                    usersRef = database.getReference().child("Users").child(user.getUid().toString());

                    //Toast.makeText(account.this, user.getUid(), Toast.LENGTH_LONG);

                    //final int userState;

                    loginButton.setVisibility(View.INVISIBLE);

                  /*  Intent intent = new Intent(account.this, tabMainActivity.class);

                    startActivity(intent);
                    finish();
*/


                    SharedPreferences userInfoPref = getSharedPreferences("USERINFO", MODE_PRIVATE);
                    String uName = userInfoPref.getString("uName", "");
                    if (!uName.equals("")) {


                        Intent intent = new Intent(account.this, tabMainActivity.class);

                        startActivity(intent);
                        finish();



                    } else {


                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if (dataSnapshot.getValue() == null) {
                                    //New User
                                    String facebookUserId = "";


                                    Log.d("$$$$$$$$$$$$$$", "ONE");

                                    for (UserInfo profile : user.getProviderData()) {
                                        // check if the provider id matches "facebook.com"
                                        if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                                            facebookUserId = profile.getUid();
                                        }
                                    }
                                    String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                                    String inviteKey = "P" + FirebaseDatabase.getInstance().getReference().push().getKey().toString();


                                    userObject newUser = new userObject("ACTIVE", user.getUid().toString(), inviteKey, user.getDisplayName(), photoUrl,
                                            0, 0, 0, "normal", "none", "none", "none",
                                            0, 0, 0, false);


                                    usersRef.setValue(newUser);
                                        /*
                                        usersRef.child("userID").setValue(user.getUid().toString());
                                        usersRef.child("inviteKey").setValue(inviteKey);
                                        usersRef.child("userFullName").setValue(user.getDisplayName());
                                        usersRef.child("userProfilePic").setValue(photoUrl);
                                        usersRef.child("ActiveStatus").setValue("ACTIVE");
*/
                                    Intent intent = new Intent(account.this, settingUsername.class);
                                    intent.putExtra("alreadyHasUsername", false);
                                    startActivity(intent);
                                    finish();


                                    finish();

                                } else {
                                    String facebookUserId = "";

                                    if (dataSnapshot.child("userID").getValue() != null && dataSnapshot.child("userName").getValue() != null &&
                                            dataSnapshot.child("userProfilePic").getValue() != null && dataSnapshot.child("ActiveStatus").getValue() != null && dataSnapshot.child("userFullName").getValue() != null) {

                                        Log.d("$$$$$$$$$$$$$$", "TWO");

                                        SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
                                        editor.putString("uName", dataSnapshot.child("userName").getValue().toString());
                                        //etxtUsername.setText(Username);
                                        //alreadyHasUsername = true;
                                        //uName = Username;
                                        editor.apply();

                                        //If account exists but was deactivated
                                        if (dataSnapshot.child("ActiveStatus").getValue().equals("INACTIVE")) {

                                            for (UserInfo profile : user.getProviderData()) {
                                                // check if the provider id matches "facebook.com"
                                                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                                                    facebookUserId = profile.getUid();
                                                }
                                            }

                                            long uAccountResetDate = 0;
                                            int userStrikes = 0;

                                            if (dataSnapshot.child("userLastAccountResetDate").getValue() != null) {
                                                uAccountResetDate = (long) dataSnapshot.child("userLastAccountResetDate").getValue();
                                            }
                                            if (dataSnapshot.child("userStrikes").getValue() != null) {
                                                userStrikes = (int) dataSnapshot.child("userStrikes").getValue();

                                            }
                                            String uName = dataSnapshot.child("userName").getValue().toString();

                                            String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                                            String inviteKey = "P" + FirebaseDatabase.getInstance().getReference().push().getKey().toString();


                                            userObject newUser = new userObject("ACTIVE", user.getUid().toString(), inviteKey, user.getDisplayName(), photoUrl,
                                                    0, 0, 0, "normal", "none", "none", "none",
                                                    uAccountResetDate, -uAccountResetDate, userStrikes, uName, false);


                                            usersRef.setValue(newUser);


                                        }

                                        Intent intent = new Intent(account.this, tabMainActivity.class);

                                        startActivity(intent);
                                        finish();

                                    }
                                    //Doesn't have username
                                    else if (dataSnapshot.child("userID").getValue() != null &&
                                            dataSnapshot.child("userProfilePic").getValue() != null && dataSnapshot.child("ActiveStatus").getValue() != null && dataSnapshot.child("userFullName").getValue() != null) {


                                        Log.d("$$$$$$$$$$$$$$", "THREE");


                                        if (dataSnapshot.child("ActiveStatus").getValue().equals("INACTIVE")) {

                                            for (UserInfo profile : user.getProviderData()) {
                                                // check if the provider id matches "facebook.com"
                                                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                                                    facebookUserId = profile.getUid();
                                                }
                                            }

                                            long uAccountResetDate = 0;
                                            int userStrikes = 0;

                                            if (dataSnapshot.child("userLastAccountResetDate").getValue() != null) {
                                                uAccountResetDate = (long) dataSnapshot.child("userLastAccountResetDate").getValue();
                                            }
                                            if (dataSnapshot.child("userStrikes").getValue() != null) {
                                                userStrikes = (int) dataSnapshot.child("userStrikes").getValue();

                                            }

                                            String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                                            String inviteKey = "P" + FirebaseDatabase.getInstance().getReference().push().getKey().toString();


                                            userObject newUser = new userObject("ACTIVE", user.getUid().toString(), inviteKey, user.getDisplayName(), photoUrl,
                                                    0, 0, 0, "normal", "none", "none", "none",
                                                    uAccountResetDate, -uAccountResetDate, userStrikes, false);


                                            usersRef.setValue(newUser);


                                        }


                                        Intent intent = new Intent(account.this, settingUsername.class);
                                        intent.putExtra("alreadyHasUsername", false);
                                        startActivity(intent);
                                        finish();


                                    } else {


                                        Log.d("$$$$$$$$$$$$$$", "FOUR");


                                        for (UserInfo profile : user.getProviderData()) {
                                            // check if the provider id matches "facebook.com"
                                            if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                                                facebookUserId = profile.getUid();
                                            }
                                        }
                                        String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                                        String inviteKey = "P" + FirebaseDatabase.getInstance().getReference().push().getKey().toString();


                                        userObject newUser = new userObject("ACTIVE", user.getUid().toString(), inviteKey, user.getDisplayName(), photoUrl,
                                                0, 0, 0, "normal", "none", "none", "none",
                                                0, 0, 0, false);


                                        usersRef.setValue(newUser);
                                        /*
                                        usersRef.child("userID").setValue(user.getUid().toString());
                                        usersRef.child("inviteKey").setValue(inviteKey);
                                        usersRef.child("userFullName").setValue(user.getDisplayName());
                                        usersRef.child("userProfilePic").setValue(photoUrl);
                                        usersRef.child("ActiveStatus").setValue("ACTIVE");
*/
                                        Intent intent = new Intent(account.this, settingUsername.class);
                                        intent.putExtra("alreadyHasUsername", false);
                                        startActivity(intent);
                                        finish();


                                    }
                                    //Toast.makeText(MainActivity.this, "Hi", Toast.LENGTH_LONG).show();


                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });


                    }
                }
                else{
                    // Toast.makeText(account.this, "Bless Up", Toast.LENGTH_LONG);
                    loginButton.setVisibility(View.VISIBLE);

                }


            }

        };


    }



    private void handleFacebookAccessToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(account.this);
                    dialog.setCancelable(false);
                    dialog.setMessage("Your account has been suspended" );
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //Action for "Delete".
                        }
                    });

                    final AlertDialog alert = dialog.create();
                    alert.show();
                    final ProgressBar progressb =  (ProgressBar)findViewById(R.id.indeterminateBar);

                    progressb.setVisibility(View.INVISIBLE);


                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            firebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }






}