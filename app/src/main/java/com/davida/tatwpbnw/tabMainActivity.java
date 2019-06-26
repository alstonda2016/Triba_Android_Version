package com.davida.tatwpbnw;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/*
Ok...so basically, whenever you deal with an app with tabs at the bottom, you are dealing with
an activity that is hosting a bunch of fragments (look to the homeFrag file to see what those are) tabMainActivity is the host for the two
fragments used in the app: homeFrag and userProfileFrag. So rather than switching through a couple of activities
when you press the bottom, you are actually on the same activity that just opens up a different window.


 Along with hosting those fragments, I use this activity to check if the user's location and usernmae data are set correctly. If not, the app
 takes the appropriate measures to ensure that it has that data
 */




public class tabMainActivity extends AppCompatActivity implements LocationListener {

    private TextView mTextMessage;
    static final int REQUEST_LOCATION = 1;
  /*  LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    Double postLat;
    Double postLong;
    */
    Dialog MyDialog;


    DatabaseReference nRef;

    DatabaseReference myRef;
    DatabaseReference userLikeHistoryRef;
    RelativeLayout activity_group_main;
    private RecyclerView mList;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseListAdapter<postClass> adapter;
    ListView listOfMessage;
    final List<String > userLikedHistoryList = new ArrayList<String>();
    Double userLat;
    Double userLong;
    String userName;
    Double postLat;
    Double postLong;
    public Criteria criteria;
    public String bestProvider;
    List<bubbleClass> lstSelectedBubbles = new ArrayList<bubbleClass>();
    int createdClasses = 0;
    LocationManager locationManager;
    //long creDate = 0;
    public String userID;
    boolean showTopPosts;
    FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;


    /*
    This part controls the tabs part. navigation_home and navigation_dashboard are buttons at the bottom. If one of them is pressed, this
    function is called. Each one will open up their own respective fragments, either homeFrag() or  userProfileFrag()
     */

    public void appVersionCheck(){

        final Double currentVersion = 1.19;

        nRef.child("1A_TribaSystemData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


              double minVersion = (Double) dataSnapshot.child("minimumAppVersion").getValue();
                String  updateLink = (String) dataSnapshot.child("updateLink").getValue();


                Log.d("C", "minrating: " + " uRting: ");
                Log.d("C", "minrating: " + " uRting: ");
                Log.d("C", "minrating: " + " uRting: ");
                Log.d("C", "minrating: " + " uRting: ");
                Log.d("C", "minrating: " + " uRting: ");
                Log.d("C", "minrating: " + " uRting: ");


                if(dataSnapshot.getValue() != null) {



                    if ( minVersion > currentVersion ) {

                        Intent intent = new Intent(tabMainActivity.this, updateApp.class);
                        intent.putExtra("updateLink", updateLink);
                        startActivity(intent);

                        finish();

                    }





                }



            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    //
                    homeFrag fragment = new homeFrag();
                    //                       ^^^^^^^^ The fragment that will be opened
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.mainFragmentPiece, fragment, "FragmentNamt");
                    fragmentTransaction1.commit();
                    return true;
                case R.id.navigation_Search:
                   /* searchFrag fragment1 = new searchFrag();
                    //                               ^^^^^^^^ The fragment that will be opened

                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.mainFragmentPiece, fragment1, "FragmentNamt");
                    fragmentTransaction2.commit();
                    return true;
                    */


                    MyDialog = new Dialog(tabMainActivity.this);

                    MyDialog.setContentView(R.layout.write_post_popup);

                    MyDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    MyDialog.setCanceledOnTouchOutside(false);


                   final EditText btnChooseChat = (EditText) MyDialog.findViewById(R.id.etxtPostText) ;



                  //  InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                   // imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    btnChooseChat.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);



                    Button btnClosePopover = (Button) MyDialog.findViewById(R.id.btnClosePopover) ;
                    btnClosePopover.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            final EditText editTextPost = (EditText) MyDialog.findViewById(R.id.etxtPostText) ;

                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editTextPost.getWindowToken(), 0);
                            MyDialog.cancel();


                        }
                    });


                    //loads the "Post" button and the button's actions that show up in the layout
                    Button btnChoosePost = (Button) MyDialog.findViewById(R.id.btnWritePostUpload) ;
                    btnChoosePost.setText("Post");
                    btnChoosePost.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //Loads the post functiion and passes the post string as well as the post type
                            postText(btnChooseChat.getText().toString(), "CHAT");
                            // This part removes the pop-up


                        }
                    });



                    MyDialog.show();




                    return false;
                case R.id.navigation_Noted:
                    userProfileFrag fragment3 = new userProfileFrag();
                    //                               ^^^^^^^^ The fragment that will be opened

                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.mainFragmentPiece, fragment3, "FragmentNamt");
                    fragmentTransaction3.commit();
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main);
        //Opens the home fragment when this activity launches
        homeFrag fragment = new homeFrag();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.mainFragmentPiece, fragment, "FragmentNamt");
        fragmentTransaction1.commit();


        Log.d("STRINGNAMEEEEEE",globalVarClass.getShouldReset() + "");
        Log.d("STRINGNAMEEEEEE1",globalVarClass.getShouldReset() + "");
        Log.d("STRINGNAMEEEEEE2",globalVarClass.getNameString());
        Log.d("STRINGNAMEEEEEE3",globalVarClass.getNameString());
        Log.d("STRINGNAMEEEEEE4",globalVarClass.getNameString());



        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();


        //Loads the username
        SharedPreferences userInfoPref = this.getSharedPreferences("USERINFO", MODE_PRIVATE);
    String uName = userInfoPref.getString("uName", "");
         if(!uName.equals("") ){
             userName = uName;

       }
        else{
           /* Intent intent = new Intent(tabMainActivity.this, settingUsername.class);
            intent.putExtra("allowBackPress", false);
            startActivity(intent);
            */
           checkUname();
        }




        boolean firstTimeUser = userInfoPref.getBoolean("firstTimeUser", false);
       // firstTimeUser = true;

        if(firstTimeUser){
           SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
            editor.putBoolean("firstTimeUser", false);
            editor.apply();


            MyDialog = new Dialog(tabMainActivity.this);

            MyDialog.setContentView(R.layout.app_info_popover);
            MyDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            final int[] checkIndex = {0};


            final LinearLayout llFirst = (LinearLayout) MyDialog.findViewById(R.id.layoutFirstInfo) ;
            final LinearLayout llSecond = (LinearLayout) MyDialog.findViewById(R.id.layoutSecondInfo) ;

            final ImageView img = (ImageView) MyDialog.findViewById(R.id.imgInfoTab) ;

            img.setBackgroundResource(R.drawable.pagetab2);

            llFirst.setVisibility(View.VISIBLE);
            llSecond.setVisibility(View.INVISIBLE);

            //loads the "Post" button and the button's actions that show up in the layout
            final Button btnChooseRight = (Button) MyDialog.findViewById(R.id.btnInfoPopoverRight) ;
            final Button btnChooseleft = (Button) MyDialog.findViewById(R.id.btnInfoPopoverLeft) ;

            btnChooseRight.setText("next");
            btnChooseleft.setText("");




            btnChooseRight.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    checkIndex[0]++;



                    if(checkIndex[0] > 1){

                        MyDialog.cancel();

                    }

                    else if(checkIndex[0] == 1) {
                        img.setBackgroundResource(R.drawable.pagetab);
                        img.setColorFilter(Color.WHITE);

                        llFirst.setVisibility(View.INVISIBLE);
                        llSecond.setVisibility(View.VISIBLE);
                        btnChooseRight.setText("close");
                        btnChooseleft.setText("back");


                    }


                    //Loads the post functiion and passes the post string as well as the post type
                    // This part removes the pop-up


                    // MyDialog.cancel();

                }
            });

            btnChooseleft.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    btnChooseRight.setText("next");
                    btnChooseleft.setText("");


                    if(checkIndex[0] != 0){
                        checkIndex[0]--;

                    }


                    img.setBackgroundResource(R.drawable.pagetab2);
                    img.setColorFilter(Color.WHITE);


                    llFirst.setVisibility(View.VISIBLE);
                    llSecond.setVisibility(View.INVISIBLE);

                    //Loads the post functiion and passes the post string as well as the post type
                    // This part removes the pop-up


                    // MyDialog.cancel();

                }
            });


            MyDialog.show();



        }


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





accountDetails();
getLocationGPS();

        final FirebaseDatabase database  = FirebaseDatabase.getInstance();

        SharedPreferences prefs = this.getSharedPreferences("USERINFO", MODE_PRIVATE);

        String currentLoc = "";

        //Gets the saved latitude and longitude from the user
        Double uLat = Double.longBitsToDouble(prefs.getLong("UserLastLat", Double.doubleToLongBits(1.001)));
        Double uLong = Double.longBitsToDouble(prefs.getLong("UserLastLong", Double.doubleToLongBits(1.001)));
        //If user doesn't have anything saved
        if (uLat == 1.001 || uLong == 1.001) {
          /*  Intent intent = new Intent(getContext(), UserUniversityInfo.class);
            intent.putExtra("allowBackPress", false);
            startActivity(intent);
            */
        }
        else {




            //PostLat and userLate are used differently
            postLat = uLat;
            postLong = uLong;
            userLat = uLat;
            userLong = uLong;

            //truncates the the full lat and long to 2 decimals
            BigDecimal bd = new BigDecimal(userLat).setScale(2, RoundingMode.DOWN);
            postLat = bd.doubleValue();
            BigDecimal bd2 = new BigDecimal(userLong).setScale(2, RoundingMode.DOWN);
            postLong = bd2.doubleValue();
            //currentloc is the string version of one's location. All decimals are replaced by "\" since
            //firebase doesn't accept a decimal as a node
            currentLoc = ((postLat)+"_"+(postLong)).replaceAll("\\.", "|");

        }






        myRef =  database.getReference().child("POSTS").child(currentLoc);
        userLikeHistoryRef = database.getReference();

        nRef = database.getReference();
        appVersionCheck();







        //loads the bottom tab bar stuff
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       // navigation.setItemBackgroundResource(R.color.zxing_transparent);
       // navigation.setBackgroundColor( ColorUtils.blendARGB(getResources().getColor(R.color.tribaPurple), Color.BLACK, 0.5F));
       // ColorUtils.blendARGB(getResources().getColor(R.color.tribaBlue), Color.BLACK, 0.5F);
       // BottomNavigationView bottomNavigationView = (BottomNavigationView) activity.findViewById(R.id.bottom_navigation_view);
      /*  BottomNavigationView menuView = (BottomNavigationView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
*/
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            // If it is my special menu item change the size, otherwise take other size
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, displayMetrics);
            /*
            if (i == 1){
                // set your height here
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
                // set your width here
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
            }
            else {
                // set your height here
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
                // set your width here
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            }
            */
            iconView.setLayoutParams(layoutParams);
        }


    }







    //If location didn't work before, this is called again to get the saved location values
    public void getLocation(){

        SharedPreferences prefs = this.getSharedPreferences("USERINFO", MODE_PRIVATE);

        Double uLat = Double.longBitsToDouble(prefs.getLong("UserLastLat", Double.doubleToLongBits(1.001)));
        Double uLong = Double.longBitsToDouble(prefs.getLong("UserLastLong", Double.doubleToLongBits(1.001)));
        if (uLat == 1.001 || uLong == 1.001) {
          /*  Intent intent = new Intent(getContext(), UserUniversityInfo.class);
            intent.putExtra("allowBackPress", false);
            startActivity(intent);
            */
        }
        else {
            //txtProfUniversity.setText(uLat+"_"+uLong);

            postLat = uLat;
            postLong = uLong;
            userLat = uLat;
            userLong = uLong;


        }


    }



    //Called when the user wants to post something
    public void postText(String passedText, String passedTextType) {
        //gets current time
        long currentTime = System.currentTimeMillis();
        long fakeLongEndTime = 0;


        TimeZone tyy = TimeZone.getDefault();

           long unixTime = (currentTime + tyy.getOffset(System.currentTimeMillis()))/1000;



        //sets postID
        String postID = FirebaseDatabase.getInstance().getReference().push().getKey();


        //If text is empty or the user has no location data
        if (passedText.equals("")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(tabMainActivity.this);
            builder.setMessage("Post is Empty")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (userLong == null) {
            getLocation();
        }

       /* else if(!checkPostContent(passedText)){



        }
        */
        else {

            BigDecimal postLatDec = BigDecimal.valueOf(postLat);
            BigDecimal postLongDec = BigDecimal.valueOf(postLong);
            DecimalFormat df = new DecimalFormat("##.#");
            df.setRoundingMode(RoundingMode.DOWN);
            double postLatDecTrunc = Double.parseDouble(df.format(postLatDec));
            double postLongDecTrunc = Double.parseDouble(df.format(postLongDec));

            BigDecimal bdLAT = BigDecimal.valueOf(postLatDecTrunc);
            BigDecimal bdLONG = BigDecimal.valueOf(postLongDecTrunc);
            BigDecimal pointOne = BigDecimal.valueOf(0.1d);


/*
            BigDecimal bd = new BigDecimal(userLat).setScale(1, RoundingMode.DOWN);
            postLat = bd.doubleValue();
            BigDecimal bd2 = new BigDecimal(userLong).setScale(1, RoundingMode.DOWN);
            postLong = bd2.doubleValue();

            BigDecimal pointOne = BigDecimal.valueOf(0.1d);

            BigDecimal postLatDec = BigDecimal.valueOf(postLat);
            BigDecimal postLongDec = BigDecimal.valueOf(postLong);
*/

/*
            BigDecimal bd = new BigDecimal(postLatDec.doubleValue()).setScale(1, RoundingMode.DOWN);
            postLat = bd.doubleValue();
            BigDecimal bd2 = new BigDecimal(postLatDec.doubleValue()).setScale(1, RoundingMode.DOWN);
            postLong = bd2.doubleValue();
*/



            String postText = passedText;




            //double gigjf = commission.doubleValue();

            //1 string for each box (remember the 9 boxes??)
         String box00 = (bdLAT.subtract(pointOne).doubleValue()) + "_" + (bdLONG.subtract(pointOne).doubleValue());
            String box01 = (bdLAT) + "_" + (bdLONG.subtract(pointOne).doubleValue());
            String box02 = (bdLAT.add(pointOne).doubleValue()) + "_" + (bdLONG.subtract(pointOne).doubleValue());
            String box10 = (bdLAT.subtract(pointOne).doubleValue()) + "_" + (bdLONG);
            String box11 = (bdLAT) + "_" + (bdLONG);
            String box12 = (bdLAT.add(pointOne).doubleValue()) + "_" + (bdLONG);
            String box20 = (bdLAT.subtract(pointOne).doubleValue()) + "_" + (bdLONG.add(pointOne).doubleValue());
            String box21 = (bdLAT) + "_" + (bdLONG.add(pointOne).doubleValue());
            String box22 = (bdLAT.add(pointOne).doubleValue()) + "_" + (bdLONG.add(pointOne).doubleValue());


/*
               //1 string for each box (remember the 9 boxes??)
            String box00 = (postLatDec.subtract(pointOne).doubleValue()) + "_" + (postLongDec.subtract(pointOne).doubleValue());
            String box01 = (postLat) + "_" + (postLongDec.subtract(pointOne).doubleValue());
            String box02 = (postLatDec.add(pointOne).doubleValue()) + "_" + (postLongDec.subtract(pointOne).doubleValue());
            String box10 = (postLatDec.subtract(pointOne).doubleValue()) + "_" + (postLong);
            String box11 = (postLat) + "_" + (postLong);
            String box12 = (postLatDec.add(pointOne).doubleValue()) + "_" + (postLong);
            String box20 = (postLatDec.subtract(pointOne).doubleValue()) + "_" + (postLongDec.add(pointOne).doubleValue());
            String box21 = (postLat) + "_" + (postLongDec.add(pointOne).doubleValue());
            String box22 = (postLatDec.add(pointOne).doubleValue()) + "_" + (postLongDec.add(pointOne).doubleValue());

*/

            //replaces the decimals for each box string
            String bo00new = box00.replaceAll("\\.", "|");
            String bo01new = box01.replaceAll("\\.", "|");
            String bo02new = box02.replaceAll("\\.", "|");
            String bo10new = box10.replaceAll("\\.", "|");
            String bo11new = box11.replaceAll("\\.", "|");
            String bo12new = box12.replaceAll("\\.", "|");
            String bo20new = box20.replaceAll("\\.", "|");
            String bo21new = box21.replaceAll("\\.", "|");
            String bo22new = box22.replaceAll("\\.", "|");



            DateFormat getTimeZoneLong = new SimpleDateFormat("zzz", Locale.US);
            String timeZoneBranch1 = getTimeZoneLong.format(Calendar.getInstance().getTime());

            if (timeZoneBranch1.isEmpty()) {

                timeZoneBranch1 = "unavailableAtTimeOFTZCall";
            }
/*
            let postLatRoundedToNearestTen = 10 * Int((postLatDOUBLE / 10.0).rounded())


            let postLatRegionWithDecimal = String(round(Double(postLatRoundedToNearestTen)))

            let postLatRegionNODecimal = postLatRegionWithDecimal.replacingOccurrences(of: ".", with: "|", options: .literal, range: nil)

            */

// USE THIS AT THE BOTTOM TO DO THE ANDROID VERSION OF WHAT'S ABOVE
            //is that the correct rounding point??? FIND OUT
            BigDecimal bdBranch2 = new BigDecimal(userLat).setScale(1, RoundingMode.DOWN);
            double branch2Pt2 = bdBranch2.doubleValue();
            BigDecimal branch2Pt3 = BigDecimal.valueOf(branch2Pt2);
            String branch2Pt4 = branch2Pt3.toString();
            String postBranch2 = branch2Pt4.replaceAll("\\.", "|");




            //Loads bubbles

            if(userName.isEmpty()){
                SharedPreferences userInfoPref = getSharedPreferences("USERINFO", MODE_PRIVATE);
                String uName = userInfoPref.getString("uName", "");
                if(!uName.equals("") ){
                    userName = uName;

                }
                else{
                    userName = "UNAVAILABLE";
                }
            }


            postSpecDetails postSpecs = new postSpecDetails(true, false, unixTime, fakeLongEndTime, "", 0);
            postLocationsClass locations = new postLocationsClass(bo00new, bo01new, bo02new, bo10new, bo11new, bo12new, bo20new, bo21new, bo22new);
         postClass newPost = new postClass( false , user.getUid() , postText ,postID , userLat , userLong , unixTime , -unixTime , userName ,
                    "STANDARDTEXTANDROID" , "" ,user.getDisplayName() , 0 ,  unixTime  , 0 , 0 ,  locations ,
                    postSpecs , timeZoneBranch1  ,postBranch2  , 2 , 0 , 0 );

            FirebaseDatabase.getInstance().getReference().child("userCreatedPosts").child(user.getUid()).child(postID).setValue(newPost);
            final EditText editTextPost = (EditText) MyDialog.findViewById(R.id.etxtPostText) ;

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextPost.getWindowToken(), 0);
            MyDialog.cancel();


        }



    }




    public void accountDetails(){



      final DatabaseReference  nRef = database.getReference();





/*
Called when user logs in from Facebook
 */


        nRef.child("Users").child(user.getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if(dataSnapshot.getValue() != null) {

                        SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
                       // editor.putString("yaya", dataSnapshot.child("userRating").getValue().toString());
                      //  editor.putLong("b",(long) dataSnapshot.child("userRating").getValue());

                        //etxtUsername.setText(Username);
                        //alreadyHasUsername = true;
                        //uName = Username;







                        String facebookUserId = "";
                        //SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();

                      if(  dataSnapshot.child("userStrikes").getValue() != null){

                          SharedPreferences userDetails = getSharedPreferences("USERINFO", MODE_PRIVATE);
                          SharedPreferences.Editor edittt = userDetails.edit();
                          edittt.putLong("userStrikes ", (long)dataSnapshot.child("userStrikes").getValue());
                       edittt.apply();

                       long userStrikeNum = (long)dataSnapshot.child("userStrikes").getValue();

                       if(userStrikeNum > 2){

                           Intent intent = new Intent(tabMainActivity.this, bannedUser.class);
                           startActivity(intent);
                           finish();

                       }



                      }
                      else{
                          SharedPreferences userDetails = getSharedPreferences("USERINFO", MODE_PRIVATE);
                          SharedPreferences.Editor edittt = userDetails.edit();
                          edittt.putInt("userStrikes ",0);

                          edittt.apply();
                        }




                        if(  dataSnapshot.child("showUserStrikeAlert").getValue() != null){
                            Boolean showUserStrikeAlert = (boolean) dataSnapshot.child("showUserStrikeAlert").getValue();
                            if(showUserStrikeAlert){

                                new AlertDialog.Builder(getApplicationContext()).setTitle("You have recently had a post striked").setMessage("Once you earn three strikes, you are banned. You can view the striked post in the advanced account info section in your settings")
                                        .setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                adminObject newMod = new adminObject(user.getUid(), user.getDisplayName(), userLat, userLong);
                                                nRef.child("Users").child(user.getUid()).child("showUserStrikeAlert").setValue(false);

                                                // MyDialog.cancel();
                                                dialog.cancel();


                                            }
                                        }).show();


                            }
                        }



                        if(  dataSnapshot.child("userName").getValue() != null){

                            SharedPreferences userDetails = getSharedPreferences("USERINFO", MODE_PRIVATE);
                            SharedPreferences.Editor edittt = userDetails.edit();
                            edittt.putString("uName ",dataSnapshot.child("userName").getValue().toString());

                            edittt.apply();


                        }
                        else{
                            editor.apply();

                            Intent intent = new Intent(tabMainActivity.this, settingUsername.class);
                            intent.putExtra("alreadyHasUsername", false);
                            startActivity(intent);
                            finish();

                        }



                        if(  dataSnapshot.child("userRating").getValue() != null){


                            SharedPreferences userDetails = getSharedPreferences("USERINFO", MODE_PRIVATE);
                            SharedPreferences.Editor edittt = userDetails.edit();
                            edittt.putLong("userRatingg", (long)dataSnapshot.child("userRating").getValue() + 10);

                            edittt.apply();





                        }
                        else{

                            SharedPreferences userDetails = getSharedPreferences("USERINFO", MODE_PRIVATE);
                            SharedPreferences.Editor edittt = userDetails.edit();
                            edittt.putLong("userRatingg", (long)0);

                            edittt.apply();


                        }





                        if(  dataSnapshot.child("userAccessLevel").getValue() != null){
                            String uLevel = dataSnapshot.child("userAccessLevel").getValue().toString();

                            if(uLevel.equals("MODLEVELOFFERED")) {


                                new AlertDialog.Builder(getApplicationContext()).setTitle("Become Moderator!").setMessage("Want power? Become a moderator and help keep your community in check! Each of your reports will permanently remove a post.")
                                        .setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();

                                                adminObject newMod = new adminObject(user.getUid(), user.getDisplayName(), userLat, userLong);
                                                nRef.child("Users").child(user.getUid()).child("userAccessLevel").setValue("LOCALMODERATOR");
                                                editor.putString("userAccessLevel ","LOCALMODERATOR");
                                                editor.apply();

                                                nRef.child("mods").child("freeRange").child(user.getUid()).setValue(newMod);
                                                // MyDialog.cancel();
                                                dialog.cancel();


                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();


                                                nRef.child("Users").child(user.getUid()).child("userAccessLevel").setValue("NORMALREJECTED");
                                                editor.putString("userAccessLevel ","NORMALREJECTED");
                                                editor.apply();
                                                // MyDialog.cancel();
                                                dialog.cancel();


                                            }
                                        }


                                ).show();



                            }
                            else{
                                editor.putString("userAccessLevel ",uLevel);




                            }

                        }
                        else{

                            nRef.child("Users").child(user.getUid()).child("userAccessLevel").setValue("NORMAL");

                            editor.putString("userAccessLevel ","NORMAL");

                        }











                        editor.apply();
                        SharedPreferences userInfoPref = getSharedPreferences("USERINFO", MODE_PRIVATE);
                        long uAccessLevel = userInfoPref.getLong("yaya", 19);
                        Long uRating = userInfoPref.getLong("userRating", 29);
                        Log.d("XXXXXXXXXXXXXXX", "XXXXminrating: "+ uAccessLevel + " uRting: "+uAccessLevel);
                        Log.d("XXXXXXXXXXXXXXX", "XXXXminrating: "+ uAccessLevel + " uRting: "+uRating);
                        Log.d("XXXXXXXXXXXXXXX", "XXXXminrating: "+ uAccessLevel + " uRting: "+uRating);
                        Log.d("XXXXXXXXXXXXXXX", "XXXXminrating: "+ uAccessLevel + " uRting: "+uRating);
                        Log.d("XXXXXXXXXXXXXXX", "XXXXminrating: "+ uAccessLevel + " uRting: "+uRating);









                    }






                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });







    }



    public void getLocationGPS(){


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

                Log.d("F", ""+lat);
                Log.d("F", ""+longi);


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
    public void onLocationChanged(Location location){

        locationManager.removeUpdates(this);

       /* double longitude = location.getLongitude();
        double latitude = location.getLatitude();
*/
        double lat = location.getLatitude();
        double longi = location.getLongitude();
        postLat = lat;
        postLong = longi;

        Log.d("LATTTTTTTTT", ""+lat);
        Log.d("LONGGGGGGGG", ""+longi);





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

    public boolean checkPostContent(String post){
        String badwords =  "obama trump suicide kill 9/11 clinton nigger niggers jews blacks shoot death cunt republican democrat liberal conservative";

        String postWords[] = post.toLowerCase().split(" ");
        for(int i =0 ; i < postWords.length ; i++ ){

            if ( badwords.toLowerCase().indexOf(postWords[i]) != -1 ) {

                AlertDialog.Builder builder = new AlertDialog.Builder(tabMainActivity.this);
                builder.setMessage("You cannot use the word: "+ postWords[i])
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                System.out.println("I found the keyword");
                return false;

            }
        }
        return true;



    }

    public void checkUname(){

        SharedPreferences userInfoPref = this.getSharedPreferences("USERINFO", MODE_PRIVATE);


        String uName = userInfoPref.getString("uName", "");
        if (!uName.equals("")) {
            userName = uName;
            Log.d("HEEEEEEEEY", "STAY WOKEEEEE");
            return;


        }
        Log.d("HEEEEEEEEY", "NIGGAS CREEPIN");

        Log.d("HEEEEEEEEY", "THEY GONE FIND YA");


/*
Called when user logs in from Facebook
 */


        nRef.child("Users").child(user.getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue() != null) {

                    SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
                    // editor.putString("yaya", dataSnapshot.child("userRating").getValue().toString());
                    //  editor.putLong("b",(long) dataSnapshot.child("userRating").getValue());

                    //etxtUsername.setText(Username);
                    //alreadyHasUsername = true;
                    //uName = Username;

                    String facebookUserId = "";
                    //SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();









                    if(  dataSnapshot.child("userName").getValue() != null){
                        Log.d("HEEEEEEEEY", "GONE CATCH YOU SLEEPIN");

                        SharedPreferences userDetails = getSharedPreferences("USERINFO", MODE_PRIVATE);
                        SharedPreferences.Editor edittt = userDetails.edit();
                        edittt.putString("uName ",dataSnapshot.child("userName").getValue().toString());

                        edittt.apply();
                        return;



                    }
                    else{
                        editor.apply();

                        Intent intent = new Intent(tabMainActivity.this, settingUsername.class);
                        intent.putExtra("alreadyHasUsername", false);
                        startActivity(intent);


                    }



















                    editor.apply();







                }






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });







    }



}


