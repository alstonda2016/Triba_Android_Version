package com.davida.tatwpbnw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



/*
*
*
*
*
* Not used, do not read
*
*
*
 */

public class homePostWriteComment extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    RelativeLayout activity_group_main;
    View view;
    double currentLat;
    double currentLong;
    //String eventType;
    String eventID;
    String className;
    String classNum;
    Long eventTime;
    Long eventEndTime;
    Long currentTime;
    String[] whichTime = new String[1];
    String collegeName;
    int createdClasses = 0;
    long creDate = 0;
    String eventTypeNum = "";
    RadioGroup rgp;
    String uName;
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    Double postLat;
    Double postLong;
    Double userLat;
    Double userLong;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_post_write_comment);

        SharedPreferences userInfoPref = this.getSharedPreferences("USERINFO", MODE_PRIVATE);
        String uName = userInfoPref.getString("uName", "");
        if(!uName.equals("")){
            userName = uName;
        }
        else{
            Intent intent = new Intent(this, settingUsername.class);
            intent.putExtra("allowBackPress", false);
            startActivity(intent);
        }

        Intent intent = getIntent();
        final postClass passedObject = (postClass) intent.getExtras().getSerializable("passedObject");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_post_write_comment_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorForTextInDarkColor));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        myToolbar.setTitle(passedObject.getPostText());


        Button bt = new Button(this);
        bt.setText("A Button");
        SharedPreferences uPrefs = getSharedPreferences("USERINFO", MODE_PRIVATE);
        uName = uPrefs.getString("WaiveUsername", null);

        currentTime = System.currentTimeMillis();
        //eventType = intent.getExtras().getString("eventType");

        SharedPreferences prefs = this.getSharedPreferences("USERINFO", MODE_PRIVATE);


        userLat = Double.longBitsToDouble(prefs.getLong("UserLastLat", Double.doubleToLongBits(1.001)));
        userLong = Double.longBitsToDouble(prefs.getLong("UserLastLong", Double.doubleToLongBits(1.001)));






        myToolbar.addView(bt);
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText txtPost = (EditText) findViewById(R.id.txtHomePostWriteComment);

                long unixTime = System.currentTimeMillis();
                long fakeLongEndTime = 0;

                String postText = txtPost.getText().toString();
                String postID = "P"+ FirebaseDatabase.getInstance().getReference().push().getKey();

              //  postSpecDetails postSpecs = new postSpecDetails(true, false, unixTime, fakeLongEndTime);
/*

                postClass post = new postClass( userName,postText  , postID,  userLat,
                        userLong, unixTime, userName ,"POST" , userName,
                        null, "NONE", null, postSpecs);

              //  ref?.child("Users").child((passedObject?.postCreator)!).child("POSTS").child((passedObject?.postID)!).child("COMMENTS").child(postID).setValue(post);
                FirebaseDatabase.getInstance().getReference().child("Users").child(passedObject.getPostCreator()).child("POSTS").child(passedObject.getPostID()).child("COMMENTS").child(postID).setValue(post);
*/
                finish();

            }
        });
    }
}
