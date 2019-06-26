package com.davida.tatwpbnw;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class homePost extends AppCompatActivity {
    ListView listOfMessage;

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference userLikeHistoryRef;


    RelativeLayout activity_group_main;
    private RecyclerView mList;
    private RecyclerView.LayoutManager mLayoutManager;
    View view;
    private FirebaseListAdapter<postClass> adapter;

    final List<String > userLikedHistoryList = new ArrayList<String>();

    Dialog MyDialog;



    Button findEvent;
    Query query;
    postClass passedObject;

    Double userLat;
    Double userLong;
    String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_post);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_post_toolbar);
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




        Intent intent = getIntent();
        passedObject = (postClass) intent.getExtras().getSerializable("passedObject");
        //myToolbar.setTitle(passedObject.getPostTitle());
        myToolbar.setTitle("");


        final FirebaseDatabase database  = FirebaseDatabase.getInstance();
        myRef =  database.getReference().child("Users").child(passedObject.postCreatorID).child("POSTS").child(passedObject.getPostID()).child("COMMENTS");
        userLikeHistoryRef = database.getReference();


        SharedPreferences userInfoPref = this.getSharedPreferences("USERINFO", MODE_PRIVATE);
        String uName = userInfoPref.getString("uName", "");
        if(!uName.equals("")){
            userName = uName;
            loadPostLikeHistory();

        }
        else{
            Intent intentSetUsername = new Intent(homePost.this, settingUsername.class);
            intentSetUsername.putExtra("allowBackPress", false);
            startActivity(intentSetUsername);
        }


//ref?.child("Users").child((passedObject?.postCreator)!).child("POSTS").child((passedObject?.postID)!).child("COMMENTS").

        TextView postTitle = (TextView) findViewById(R.id.txtHomePost);
        Button btnUp = (Button) findViewById(R.id.btnHomePostUp);
        Button btnDown = (Button) findViewById(R.id.btnHomePostDown);
        Button btnWriteComment = (Button) findViewById(R.id.btnHomePostWriteComment);
        listOfMessage = (ListView)findViewById(R.id.lstHomePost);

        postTitle.setText(passedObject.getPostText());
        btnWriteComment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(homePost.this, homePostWriteComment.class);
                //intent.putExtra("EventID", messageID.getText().toString());
                //intent.putExtra("passedObject", (Serializable) model);
                intent.putExtra("passedObject", (Serializable) passedObject);

                startActivity(intent);

            }

        });

        final String uLink = "Users/"+passedObject.getPostCreatorID()+"/POSTS/"+passedObject.getPostID()+"/ratingBlend";

        btnUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                userLikeDislikeClass userLikePost = new userLikeDislikeClass("Y", uLink, userName, passedObject.getPostID(), passedObject.getPostLocations(), null);


                FirebaseDatabase.getInstance().getReference().child("LIKESDISLIKES").child(passedObject.getPostID()).child("LDNOTES").child(userName).setValue(userLikePost);


            }

        });
        btnDown.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                userLikeDislikeClass userLikePost = new userLikeDislikeClass("L", uLink, userName, passedObject.getPostID(), passedObject.getPostLocations(), null);


                FirebaseDatabase.getInstance().getReference().child("LIKESDISLIKES").child(passedObject.getPostID()).child("LDNOTES").child(userName).setValue(userLikePost);



            }

        });


        loadPostLikeHistory();


    }


    public void loadPostLikeHistory(){
        userLikeHistoryRef.child("LIKEDPOSTCOMMENTS").child(passedObject.getPostID()).child(userName).limitToFirst(350).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> td = (HashMap<String,String>) dataSnapshot.getValue();


                if(td != null) {
                    List<String> tempList = new ArrayList<String>(td.values());
                    userLikedHistoryList.addAll(tempList);
                }
                displayPosts();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }


    public void displayPosts(){


        adapter = new FirebaseListAdapter<postClass>(this, postClass.class, R.layout.user_post,myRef) {
            @Override
            protected void populateView(View v, final postClass model, int position) {

                View mView;
                mView = v;

                TextView postText = (TextView)mView.findViewById(R.id.userText);
                final  ImageButton btnLike = (ImageButton) mView.findViewById(R.id.btnUPLike);
                final ImageButton btnDislike = (ImageButton) mView.findViewById(R.id.btnUPDislike);

                //Long datee = model.getEventTime() * 1000 ;
                //TimeZone tyy = TimeZone.getDefault();
                // Date d = new Date(datee - tyy.getOffset(System.currentTimeMillis()));
                //  String vv = new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(d);

                String strHasLikedPost = model.getPostID()+":Y";
                String strHasDislikedPost = model.getPostID()+":L";

                if(userLikedHistoryList.contains(strHasLikedPost)){
                    btnLike.setBackgroundColor(Color.GREEN);
                    btnDislike.setBackgroundColor(Color.GRAY);

                }
                else if (userLikedHistoryList.contains(strHasDislikedPost)){
                    btnLike.setBackgroundColor(Color.GRAY);
                    btnDislike.setBackgroundColor(Color.GREEN);

                }
                else{
                    btnLike.setBackgroundColor(Color.GRAY);
                    btnDislike.setBackgroundColor(Color.GRAY);

                }


                Double postLat = model.getPostLat();
                Double postLong = model.getPostLong();

                postText.setText(model.getPostText());



                 /*
            let post:[String : AnyObject] = [
            //"eventTitle":txtEventName.text! as AnyObject,
            "rating":"Y" as AnyObject,
            "postLink":"Users/"+passedObject!.postCreator+"/POSTS/"+passedObject!.postID+"/POSTDATA/RATINGBLEND" as AnyObject
"Users/"+(passedObject?.postCreator)!+"/POSTS/"+(passedObject?.postID)!+"/COMMENTS/"+commentID+"/RATINGBLEND"
        ]
        ref?.child("LIKESDISLIKES").child((passedObject?.postID)!).child("LDNOTES").child((Auth.auth().currentUser?.uid)!).setValue(post);

         */


                final String uLink = "Users/"+ passedObject.getPostCreatorID()+"/POSTS/"+passedObject.postID+"/COMMENTS/"+model.getPostID()+"/RATINGBLEND";





                btnLike.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        btnLike.setBackgroundColor(Color.GREEN);
                        btnDislike.setBackgroundColor(Color.GRAY);
                        userLikeDislikeClass userLikePost = new userLikeDislikeClass("Y", uLink, userName, passedObject.getPostID(), model.getPostLocations(), model.getPostID());



                        FirebaseDatabase.getInstance().getReference().child("COMMENTLIKESDISLIKES").child(model.getPostID()).child("LDNOTES").child(userName).setValue(userLikePost);
    /*
                       *Removes the Liked/Disliked value from the userLikedHistoryList and replaces it with the newest value
                       * This occurs because the list of liked values is only retrieved once, so this is the only way for the
                       * the list to be up to date
                        */
                        if(userLikedHistoryList.contains(model.getPostID()+":L")){
                            userLikedHistoryList.remove(model.getPostID()+":L");
                        }
                        userLikedHistoryList.add(model.getPostID()+":Y");

                    }

                });
                btnDislike.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        btnLike.setBackgroundColor(Color.GRAY);
                        btnDislike.setBackgroundColor(Color.GREEN);
                        userLikeDislikeClass userLikePost = new userLikeDislikeClass("L", uLink, userName, passedObject.getPostID(), model.getPostLocations(), model.getPostID());


                        FirebaseDatabase.getInstance().getReference().child("COMMENTLIKESDISLIKES").child(model.getPostID()).child("LDNOTES").child(userName).setValue(userLikePost);
    /*
                       *Removes the Liked/Disliked value from the userLikedHistoryList and replaces it with the newest value
                       * This occurs because the list of liked values is only retrieved once, so this is the only way for the
                       * the list to be up to date
                        */
                        if(userLikedHistoryList.contains(model.getPostID()+":Y")){
                            userLikedHistoryList.remove(model.getPostID()+":Y");
                        }
                        userLikedHistoryList.add(model.getPostID()+":L");

                    }

                });




            }
        };
        listOfMessage.setAdapter(adapter);


    }
}
