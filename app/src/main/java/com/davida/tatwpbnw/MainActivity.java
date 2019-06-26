package com.davida.tatwpbnw;

import android.*;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

import static java.security.AccessController.getContext;


/*
*
*
* This part is used to get the user's location and determine if the user has a username
* It's a mindfucker
*
*
 */
public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView mTextMessage;
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    Double postLat;
    Double postLong;
    Dialog MyDialog;
    Boolean isAlreadyRunningLocationLoop = false;

    private FirebaseAuth firebaseAuth;
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase database;

    DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user;
        user = firebaseAuth.getCurrentUser();
        //  Toast.makeText(MainActivity.this, "ji", Toast.LENGTH_LONG).show();

        Log.d("A", "AAAAAAAAAAAAAAAA");




        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE) ;
        getLocation();

        //Location stuff. Just making sure that the location stuff is enabled
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Location Services Are Not Enabled");
            dialog.setMessage("We need your location to help create the best app experience");
            dialog.setPositiveButton("Open Location Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
          /*  dialog.setNegativeButton("Exit App", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                    finishAffinity();
                }
            });
            */
            dialog.setCancelable(false);
            dialog.show();
        }
        getLocation();


    }


    @Override
    public void onLocationChanged(Location location){

        locationManager.removeUpdates(this);

        //double longitude = location.getLongitude();
       // double latitude = location.getLatitude();

        double lat = location.getLatitude();
        double longi = location.getLongitude();
        postLat = lat;
        postLong = longi;
        //eventDesc.setText(""+lat+"_"+longi);
        //state: bestMatch.getAdminArea()
        //town: .getLocality()
        //borrow like queens getSubLocality()
        //New York: 40.696951, -73.880442
        //University of Illinois: 40.103276, -88.232111
        SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
        editor.putLong("UserLastLat", Double.doubleToRawLongBits(postLat));
        editor.putLong("UserLastLong", Double.doubleToRawLongBits(postLong));
        editor.apply();
        Intent intent = new Intent(MainActivity.this, account.class);
        Log.d("FUUUUUUUUUUUCK", "MOTHERFUCKERRRRRRRRRRRRR");
        Log.d("LAAAAT", ""+lat);
        Log.d("LOOOOONG", ""+longi);

        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplication(),
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        //startActivity(intent, bundle);

        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


        finish();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

void getLocation(){


    Log.d("B", "BBBBBBBBB");



    SharedPreferences userInfo = this.getSharedPreferences("USERINFO", MODE_PRIVATE);
    Boolean  hasPressedNverAgain = userInfo.getBoolean("hasPressedNeverAgain", false);


    if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
        //appcombat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        if (hasPressedNverAgain == true) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // show a dialog
                new AlertDialog.Builder(this).setTitle("You need to enable permissions to use this feature").setMessage("Go to settings, change the app's permissions, and restart the app to use location services").setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // navigate to settings
                        // startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        finishAffinity();
                            /*
                            *.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // leave?
                            // MyActivity.this.onBackPressed();
                            getActivity().finishAffinity();

                        }
                    })
                             */



                    }
                }).show();


                Log.d("C", "CCCCCCCCCCCCCCC");

            }
        }
        else{
            Log.d("D", "DDDDDDDDDDDDD");

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }

/*
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // show a dialog
                new AlertDialog.Builder(getContext()).setMessage("You need to enable permissions to use this feature").setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // navigate to settings
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    }
                }).setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // leave?
                       // MyActivity.this.onBackPressed();
                        getActivity().finishAffinity();

                    }
                }).show();
            }
            */


    }
    else{

        Log.d("E", "EEEEE");

        criteria = new Criteria();
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();


        //I get Network_Provider and the GPS_Provider back to back for accuracy and because that's
        //what a bunch of stack overflow answers had. To question this is to question StackOverflow.
        //Do you really think you are better than Stack Overflow?
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, this);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        if(location != null){

            Log.d("F", "FFFFFFF");


            double lat = location.getLatitude();
            double longi = location.getLongitude();
            postLat = lat;
            postLong = longi;
            //eventDesc.setText(""+lat+"_"+longi);
            //state: bestMatch.getAdminArea()
            //town: .getLocality()
            //borrow like queens getSubLocality()
            //New York: 40.696951, -73.880442
            //University of Illinois: 40.103276, -88.232111
            SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
            editor.putLong("UserLastLat", Double.doubleToRawLongBits(postLat));
            editor.putLong("UserLastLong", Double.doubleToRawLongBits(postLong));





                /*
                Ok, so the geocoder basically uses the user's lat and long to find details on the user's town, state, and other stuff/
                 */

                /*Geocoder geoCoder = new Geocoder(this);

            try {

                List<Address> matches = geoCoder.getFromLocation(postLat, postLong, 1);

                //Bestmatch is the address that comes from geocoding
                Address bestMatch = (matches.isEmpty() ? null : matches.get(0));


                if(bestMatch.getAdminArea() != null) {
                    //If user's address is not null, it saves the adress state and city stuff to the device

                    editor.putString("userState", bestMatch.getAdminArea());
                    editor.putString("userCity", bestMatch.getLocality());

                    if(bestMatch.getSubLocality() != null && !bestMatch.getSubLocality().isEmpty()) {
                        editor.putString("userBorough", bestMatch.getSubLocality());


                    }
                    else{
                        editor.putString("userBorough", "");

                    }
                    //etxtUsername.setText(Username);
                    //alreadyHasUsername = true;
                    //uName = Username;
                    editor.apply();
                }



            } catch (NullPointerException e) {
                // TODO: handle exception
            } catch (NumberFormatException e) {
                // TODO: handle exception
            } catch (IOException e) {
                e.printStackTrace();
            }
*/

            //etxtUsername.setText(Username);
            //alreadyHasUsername = true;
            //uName = Username;
            editor.apply();


            Intent intent = new Intent(MainActivity.this, account.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            finish();



        }
        else{

            Location location2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            if(location2 != null) {

                Log.d("F", "FFFFFFF");


                double lat = location2.getLatitude();
                double longi = location2.getLongitude();
                postLat = lat;
                postLong = longi;
                //eventDesc.setText(""+lat+"_"+longi);
                //state: bestMatch.getAdminArea()
                //town: .getLocality()
                //borrow like queens getSubLocality()
                //New York: 40.696951, -73.880442
                //University of Illinois: 40.103276, -88.232111
                SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
                editor.putLong("UserLastLat", Double.doubleToRawLongBits(postLat));
                editor.putLong("UserLastLong", Double.doubleToRawLongBits(postLong));
                editor.apply();


                Intent intent = new Intent(MainActivity.this, account.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                finish();

                    }




            }


      /*  else{

            new AlertDialog.Builder(this).setTitle("Cellular Connection Error").setMessage("Your Phone's Location Services are Malfunctioning").setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // navigate to settings
                    // startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    finishAffinity();


                }
            }).show();


            Log.d("C", "CCCCCCCCCCCCCCC");



        }
        */

    }


}




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       /* switch (requestCode){
            case REQUEST_LOCATION:
                getLocation();
                break;

        }
*/
        Log.d("set to never ask again", "");

       // Toast.makeText(this, "DS", Toast.LENGTH_SHORT).show();


        for(String permission: permissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                //denied
             //  Toast.makeText(this, "THERES STILL A CHANCE", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

                SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
                editor.putBoolean("hasPressedNeverAgain", false);
                //etxtUsername.setText(Username);
                //alreadyHasUsername = true;
                //uName = Username;
                editor.apply();


            }else{
                if(ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
                    //allowed
                  //  Toast.makeText(this, "ALLOWED", Toast.LENGTH_LONG).show();
                    getLocation();

                } else{
                    //set to never ask again
                 //   Toast.makeText(this, "NeverAskAgain", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
                    editor.putBoolean("hasPressedNeverAgain", true);
                    //etxtUsername.setText(Username);
                    //alreadyHasUsername = true;
                    //uName = Username;
                    editor.apply();

                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // show a dialog
                        new AlertDialog.Builder(this).setTitle("You need to enable permissions to use this feature").setMessage("Go to settings, change the app's permissions, and restart the app to use location services").setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // navigate to settings
                                // startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                                finishAffinity();
                            /*
                            *.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // leave?
                            // MyActivity.this.onBackPressed();
                            getActivity().finishAffinity();

                        }
                    })
                             */

                            }
                        }).show();
                    }
                }

            }
        }
    }



   public void loopTillLocationAppears() {


       if (isAlreadyRunningLocationLoop) {
       } else {
           isAlreadyRunningLocationLoop = true;
           boolean keepLooping = true;
           while (keepLooping) {

               getLocation();
           }


       }
   }
}


