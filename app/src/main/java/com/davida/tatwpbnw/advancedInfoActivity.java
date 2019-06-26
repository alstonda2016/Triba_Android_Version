package com.davida.tatwpbnw;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class advancedInfoActivity extends AppCompatActivity {

    View view;
    Dialog MyDialog;
    Double userLat;
    Double userLong;
    String userName;
    Double postLat;
    Double postLong;
    List<bubbleClass> lstSelectedBubbles = new ArrayList<bubbleClass>();
    int createdClasses = 0;
    DatabaseReference myRef;

    private RecyclerView postRecyclerview;

    private MyAdapter allDataAdapter;

    public String userID;


    FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;


    Button btnShowNotes;
    Button btnShowHistory;
    ImageView imgProfilePic;


    final List<postClass> postListNoted = new ArrayList<postClass>();
    final List<strikedPostObject> postList = new ArrayList<strikedPostObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_info);


        //initializes toolbar for the app :p
        Toolbar myToolbar = (Toolbar) findViewById(R.id.advancedInfo_Toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorForTextInDarkColor));
        //setDisplayHomeAsUpEnabled displays the back button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Sets the action that occurrs when the back button is pressed
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        myToolbar.setTitle(" ");



        SharedPreferences prefs = this.getSharedPreferences("USERINFO", MODE_PRIVATE);


        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        String currentLoc = "";

        myRef = database.getReference();



        TextView txtRealName = (TextView) findViewById(R.id.txtUserProfileNameAdvancedInfo);
        TextView txtUserName = (TextView) findViewById(R.id.txtUserProfileName2AdvancedInfo);


        final TextView txtAccountStatus = (TextView) findViewById(R.id.txtAdvancedStatus);
        final TextView txtStrikeNum = (TextView) findViewById(R.id.txtAdvancedStrikedNumber);


        myRef.child("Users").child(user.getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot != null){

                    if(  dataSnapshot.child("userStrikes").getValue() != null){


                        long userStrikeNum = (long)dataSnapshot.child("userStrikes").getValue();

                     txtStrikeNum.setText("Strikes: "+userStrikeNum);



                    }


                    if(  dataSnapshot.child("userAccessLevel").getValue() != null){
                        String uLevel = dataSnapshot.child("userAccessLevel").getValue().toString();

                        if(uLevel.equals("LOCALMODERATOR")) {


                         txtAccountStatus.setText("Status: Local Moderator");


                        }

                        else{
                            txtAccountStatus.setText("Status: Active User");




                        }

                    }
                    else{
                        txtAccountStatus.setText("Status: Active User");


                    }


                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        SharedPreferences userInfoPref = this.getSharedPreferences("USERINFO", MODE_PRIVATE);
        String uName = userInfoPref.getString("uName", "");
        if (!uName.equals("")) {
            userName = uName;
            txtUserName.setText(userName);

        } else {
            Intent intent = new Intent(advancedInfoActivity.this, settingUsername.class);
            intent.putExtra("allowBackPress", false);
            startActivity(intent);
        }





        postRecyclerview = findViewById(R.id.profileListAdvanced);



    /*    GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ Color.MAGENTA,Color.BLACK, Color.BLACK,  Color.MAGENTA});
        getActivity().findViewById(R.id.profileFragLayout).setBackground(gradientDrawable);
*/

        String fbPhotoID = "";

        for(UserInfo profile : user.getProviderData()) {
            // check if the provider id matches "facebook.com"
            if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                fbPhotoID = profile.getUid();
            }
        }
        imgProfilePic = (ImageView) findViewById(R.id.advancedInfoImgProfile);
// construct the URL to the profile picture, with a custom height
// alternatively, use '?type=small|medium|large' instead of ?height=
        String photoUrl = "https://graph.facebook.com/" + fbPhotoID + "/picture?height=500";

// (optional) use Picasso to download and show to image
        Picasso.with(getApplicationContext()).load(photoUrl).into(imgProfilePic);
        // Uri photoUrl = user.getPhotoUrl();
        //Uri photoUrl = user.getPhotoUrl();




        txtRealName.setText(user.getDisplayName());

        myRef.child("strikedPostHistory").child(userID).limitToFirst(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    strikedPostObject Post = dataSnapshot1.getValue(strikedPostObject.class);
                    postList.add(Post);

                    Log.d("!!!!!", "2");

                }
                Log.d("!!!!!", "3");

                allDataAdapter = new MyAdapter(advancedInfoActivity.this, postList);

                LinearLayoutManager llm = new LinearLayoutManager(advancedInfoActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                postRecyclerview.setLayoutManager(llm);

                postRecyclerview.setAdapter(allDataAdapter);
                allDataAdapter.notifyDataSetChanged();
                /*
                if (mUserList.isEmpty())
                    mTvEmpty.setVisibility(View.VISIBLE);
                else
                    mTvEmpty.setVisibility(View.GONE);
                    */
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
 /*   ref?.child("strikedPostHistory").child(userID).queryLimited(toLast: 10).observe(.childAdded, with: { (snapshot) in
        if let strikedPostObjectDict = snapshot.value as? [String: AnyObject]{
            let dict = strikedPostObjectDict["postObject"] as! [String: Any]
*/
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private LayoutInflater inflater;
        private List<strikedPostObject> list;
        Context ctx;

        public MyAdapter(Context context, List<strikedPostObject> feedItemList) {
            inflater = LayoutInflater.from(context);
            this.list = feedItemList;
        }



        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parrent, int i) {
            View view = inflater.inflate(R.layout.advanced_info_striked_post_cell, parrent, false);
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder viewHolder, int position) {
            final strikedPostObject postItem = postList.get(position);
            //viewHolder.postText.setText(item.getPostText());
            viewHolder.postText.setText(postItem.postObject.postText);
            //viewHolder.txtUserName.setText(postItem.postUserName);
            viewHolder.txtRemovalReason.setText(postItem.postRemovalReason + "");


            //String strHasLikedPost = postItem.postID + ":L";
           // String strHasNotlikedPost = postItem.postID + ":NL";








            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //Do Nothing


                }

            });







        }


        @Override
        public int getItemCount() {
            if (list == null)
                return 0;
            else
                return list.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView postText;
            TextView txtRemovalReason;



            View mView;


            public MyViewHolder(View itemView) {
                super(itemView);
                //name = (TextView) itemView.findViewById(R.id.tvname);
                postText = (TextView) itemView.findViewById(R.id.txtAdvancedInfoStrikedPostText);
                txtRemovalReason = (TextView) itemView.findViewById(R.id.txtAdvancedInfoStrikedPostReason);







            }
        }

    }


}
