package com.davida.tatwpbnw;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;




/*


This loads the post history. It's all reused code basically.


 */

public class userPostHistory extends AppCompatActivity {

    View view;
    Dialog MyDialog;
    DatabaseReference myRef;
    DatabaseReference userLikeHistoryRef;
    RelativeLayout activity_group_main;
    private RecyclerView mList;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseListAdapter<historyPostClass> adapter;
    ListView listOfMessage;
    final List<String> userLikedHistoryList = new ArrayList<String>();
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


    boolean showTopPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post_history);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.MyHistory_Toolbar);
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



        SharedPreferences userInfoPref = this.getSharedPreferences("USERINFO", MODE_PRIVATE);
        String uName = userInfoPref.getString("uName", "");
        if (!uName.equals("")) {
            userName = uName;
            myToolbar.setTitle(userName+"'s History");

            displayPosts();
        } else {
            Intent intent = new Intent(userPostHistory.this, settingUsername.class);

            startActivity(intent);
        }


    }


    public void displayPosts() {

        listOfMessage = (ListView) findViewById(R.id.list_of_posts_history);

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        myRef = database.getReference().child("Users").child(userName).child("POSTS");
        userLikeHistoryRef = database.getReference();



        adapter = new FirebaseListAdapter<historyPostClass>(userPostHistory.this, historyPostClass.class, R.layout.history_post,myRef) {
            @Override
            protected void populateView(View v, historyPostClass modelObject, int position) {

                Toast.makeText(userPostHistory.this, "HIHI", Toast.LENGTH_LONG).show();
                View mView;

                mView = v;
                final historyPostClass mudel = modelObject;
                 final postClass model = mudel.getPOSTDATA();

                TextView postText = (TextView) mView.findViewById(R.id.txtHistoryPostText);
                final TextView postRating = (TextView) mView.findViewById(R.id.txtPostRating);

                final ImageButton btnDelete = (ImageButton) mView.findViewById(R.id.btnDeleteHistoryPost);

                final TextView txtIsActivated = (TextView) mView.findViewById(R.id.txtIsDeleted);

              //  txtIsActivated.setText(""+model.getIsActivated());

                Double postLat = model.getPostLat();
               Double postLong = model.getPostLong();

                postText.setText(model.getPostText());
                postRating.setText(model.getRatingBlend() + "");

/*
                if(!model.getIsActivated()){
                   btnDelete.setImageResource(R.drawable.ic_deletedtxt);
                }
                */




                final String uLink = "Users/" + model.getPostCreatorID() + "/POSTS/" + model.getPostID() + "/ratingBlend";


                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    if(txtIsActivated.getText().toString().equals("true")){
                        myRef.child(model.getPostID()).child("POSTDATA").child("isActivated").setValue(false);
                        txtIsActivated.setText("false");



                    }

/*
                        int viewColor = ((ColorDrawable) btnLike.getBackground()).getColor();
                        ColorFilter test = btnLike.getColorFilter();


                        String userRating = "";
                        if (btnLike.equals(true)) {
                        } else {

                            userLikeDislikeClass userLikePost = new userLikeDislikeClass("Y", uLink, userName, model.getPostID(), model.getPostLocations(), "");

                            FirebaseDatabase.getInstance().getReference().child("LIKESDISLIKES").child(model.getPostID()).child("LDNOTES").child(userName).setValue(userLikePost);

                            if (userLikedHistoryList.contains(model.getPostID() + ":L")) {
                                userLikedHistoryList.remove(model.getPostID() + ":L");
                            }
                            userLikedHistoryList.add(model.getPostID() + ":Y");

                            btnLike.setColorFilter(getResources().getColor(R.color.PurBlu));
                            btnDislike.setColorFilter(Color.BLACK);

                            btnLike.setTag(true);
                            btnDislike.setTag(false);



                        }

*/
                    }



                });


                mView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        String pType = model.getPostType();

                        if(pType.equals("CHAT")){
                            Intent intent = new Intent(userPostHistory.this, homeChat.class);
                            //intent.putExtra("EventID", messageID.getText().toString());
                            intent.putExtra("passedObject", (Serializable) model);

                            startActivity(intent);

                        /*    Intent intent = new Intent(getActivity(), homeChat.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("passedObject", model);
                            //Intent intent = new Intent(appContext, AdminControls.class);
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                            */

                        }
                        else{
                            // Intent intent = new Intent(getActivity(), homePost.class);
                            // intent.putExtra("EventID", messageID.getText().toString());
                            // intent.putExtra("passedObject", (Serializable) model);

                            Toast.makeText(userPostHistory.this, "I got rid of this type of post. Everything is a groupchat now. I'll explain on Friday.", Toast.LENGTH_LONG).show();
                            // startActivity(intent);

                        }



                    }

                });






            }


        };

        listOfMessage.setAdapter(adapter);

    }
}
