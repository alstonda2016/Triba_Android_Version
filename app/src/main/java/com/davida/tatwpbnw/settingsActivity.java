package com.davida.tatwpbnw;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static java.security.AccessController.getContext;

public class settingsActivity extends AppCompatActivity {

    private RecyclerView postRecyclerview;

    private settingsAdapter allDataAdapter;
    FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference myRef;
    String userID;
    Dialog MyDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //initializes toolbar for the app :p
        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_settings_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setTitle("Settings");
        //setDisplayHomeAsUpEnabled displays the back button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Sets the action that occurrs when the back button is pressed
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        postRecyclerview = findViewById(R.id.recyclerviewSettingsActivity);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        myRef = database.getReference();



//= new ArrayList<String>(
        List<String> settingList =  Arrays.asList("ACCOUNT SETTINGS", "Advanced Account Info", "Change Username", "Log Out", "ABOUT",
                "App Info", "Privacy Policy", "   ", "Deactivate" );



        allDataAdapter = new settingsAdapter(settingsActivity.this, settingList);

        LinearLayoutManager llm = new LinearLayoutManager(settingsActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        postRecyclerview.setLayoutManager(llm);

        postRecyclerview.setAdapter(allDataAdapter);
        allDataAdapter.notifyDataSetChanged();


    }



    public class settingsAdapter extends RecyclerView.Adapter<settingsAdapter.MyViewHolder> {
        private LayoutInflater inflater;
        private List<String> settingList ;

        Context ctx;

        public settingsAdapter(Context context, List<String> feedItemList) {
            inflater = LayoutInflater.from(context);
            this.settingList = feedItemList;
        }


        @Override
        public settingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parrent, int i) {
            View view = inflater.inflate(R.layout.setting_cell, parrent, false);
            settingsAdapter.MyViewHolder holder = new settingsAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final settingsAdapter.MyViewHolder viewHolder, final int position) {
            final String postItem = settingList.get(position);
            //viewHolder.postText.setText(item.getPostText());
            viewHolder.postText.setText(postItem);


            if(postItem.equals("ACCOUNT SETTINGS") || postItem.equals("ABOUT") || postItem.equals("   ")){

                viewHolder.postText.setTextColor(Color.WHITE);
                viewHolder.postText.setTextSize(12);
                viewHolder.itemView.setBackgroundColor(Color.GRAY);
                viewHolder.itemView.setBackgroundColor(Color.BLACK);



            }
            else{

                viewHolder.postText.setTextColor(Color.WHITE);
                viewHolder.postText.setTextSize(20);
              //  viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.tribaBackSettings));
                viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);

            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //Goes to homeChat
                   // Intent intent = new Intent(settingsActivity.this, homeChat.class);
                  //  intent.putExtra("passedObject", (Serializable) postItem);

                  //  startActivity(intent);




                    Log.d("###########", "" + viewHolder.postText.getText().toString());
                    Log.d("###########", "" + viewHolder.postText.getText().toString());
                    Log.d("###########", "" + viewHolder.postText.getText().toString());
                    Log.d("###########", "" + viewHolder.postText.getText().toString());
                    Log.d("###########", "" + viewHolder.postText.getText().toString());
                    Log.d("###########", "" + viewHolder.postText.getText().toString());



                    if(viewHolder.postText.getText().toString().equals("Advanced Account Info")){

                        Intent intent = new Intent(settingsActivity.this, advancedInfoActivity.class);

                        startActivity(intent);


                    }
                    else if(viewHolder.postText.getText().toString().equals("Change Username")){

                        Intent intent = new Intent(settingsActivity.this, settingUsername.class);
                        intent.putExtra("alreadyHasUsername", true);

                        startActivity(intent);


                    }
                    else if(viewHolder.postText.getText().toString().equals("Log Out")){

                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(settingsActivity.this, account.class);
                        startActivity(intent);
                        finish();

                    }
                    else if(viewHolder.postText.getText().toString().equals("App Info")){
                        MyDialog = new Dialog(settingsActivity.this);

                        MyDialog.setContentView(R.layout.app_info_popover);
                        MyDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                        final int[] checkIndex = {0};


                      final  LinearLayout llFirst = (LinearLayout) MyDialog.findViewById(R.id.layoutFirstInfo) ;
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
                    else if(viewHolder.postText.getText().toString().equals("Privacy Policy")){

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://triba.co/privacyPolicy.html"));
                        startActivity(browserIntent);

                    }
                    else if(viewHolder.postText.getText().toString().equals("Deactivate")){


                        long currentTime = System.currentTimeMillis();


                        TimeZone tyy = TimeZone.getDefault();

                        long unixTime = (currentTime + tyy.getOffset(System.currentTimeMillis()))/1000;


                        long inverseTime = -unixTime;
                        myRef.child("Users").child(user.getUid()).child("ActiveStatus").setValue("INACTIVE");
                        myRef.child("Users").child(user.getUid()).child("userLastAccountResetDate").setValue(unixTime);
                        myRef.child("Users").child(user.getUid()).child("userLastAccountResetDateInverse").setValue(inverseTime);
                        myRef.child("notedPosts").child(user.getUid()).setValue(null);
                        myRef.child("UsersNotedPostsIDList").child(user.getUid()).setValue(null);


                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(settingsActivity.this, account.class);
                        startActivity(intent);
                        finish();

                    }




                }

            });


        }


        @Override
        public int getItemCount() {
            if (settingList == null) {



                return 0;
            }
            else {
                Log.d("******", "" + settingList.size());
                Log.d("******", "" + settingList.size());
                Log.d("******", "" + settingList.size());
                Log.d("******", "" + settingList.size());
                Log.d("******", "" + settingList.size());
                Log.d("******", "" + settingList.size());
                Log.d("******", "" + settingList.size());

                return settingList.size();
            }
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView postText;

            Button btnRemovePost;
            ;

            View mView;


            public MyViewHolder(View itemView) {
                super(itemView);
                //name = (TextView) itemView.findViewById(R.id.tvname);
                postText = (TextView) itemView.findViewById(R.id.txtSettingCellText);




                //The report button



            }
        }


    }


}
