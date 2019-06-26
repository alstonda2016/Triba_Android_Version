package com.davida.tatwpbnw;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/*
*
*
* This file is the chat message home

 */


public class homeChat extends AppCompatActivity {

    RecyclerView listOfMessage;
    postClass passedObject;
    DatabaseReference myRef;
    DatabaseReference nRef;

    DatabaseReference userLikeHistoryRef;
    String userName;
    boolean isPostObjectNoted = false;
    boolean passedIsPostObjectNotedInitialValue;
    ImageButton btnNotedPost;

    private homeChat.MyAdapter allDataAdapter;
    private RecyclerView postRecyclerview;


    RelativeLayout activity_group_main;
    private RecyclerView mList;
    private RecyclerView.LayoutManager mLayoutManager;
    View view;
    private FirebaseListAdapter<postChatObject> adapter;

    final List<String> userLikedHistoryList = new ArrayList<String>();
    final List<postChatObject> postList = new ArrayList<postChatObject>();


    Dialog MyDialog;

    FloatingActionButton fab;


    Button findEvent;
    Query query;

    Double userLat;
    Double userLong;
    String uName;
    public String userID;
    int[] backColor = {0};


    FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;

    EditText txtUserChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_chat);



        btnNotedPost = (ImageButton) findViewById(R.id.btnNotedPostChat);

        btnNotedPost.setEnabled(false);


      /*  new AlertDialog.Builder(homeChat.this).setTitle("Note num").setMessage(""+notedNumbers.getNumOfNotedPosts())
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                      //MyDialog.cancel();
                    dialog.cancel();


                    }
                }).show();
*/




        //initializes toolbar for the app :p
        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_chat_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorForTextInDarkColor));
        //setDisplayHomeAsUpEnabled displays the back button on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Sets the action that occurrs when the back button is pressed
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPostObjectNoted != passedIsPostObjectNotedInitialValue){

                    globalVarClass.setShouldReset(true);


                    if(isPostObjectNoted){
                        globalVarClass.setAddOrSubtractID("ADD");

                    }
                    else{
                        globalVarClass.setAddOrSubtractID("SUBTRACT");

                    }
                    globalVarClass.setPostID(passedObject.postID);


                }

                finish();
            }
        });



        //Obtains the passed object from the previous activity, which is a postClass
        Intent intent = getIntent();
        passedObject = (postClass) intent.getExtras().getSerializable("passedObject");
        backColor[0] = (int) intent.getExtras().getInt("backColor");

        View targetView;
        targetView = (View)findViewById(R.id.chatView);

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ Color.CYAN,Color.BLACK, Color.BLACK,  Color.CYAN});


        if(backColor[0] == 1){
            gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{getResources().getColor(R.color.tribaPurple),Color.BLACK, Color.BLACK,  getResources().getColor(R.color.tribaPurple)});

            View viewSeparator = (View) findViewById(R.id.viewChatTopSeparator);
            viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaPurple));
            View viewSeparatorB = (View) findViewById(R.id.viewChatBottomSeparator);
            viewSeparatorB.setBackgroundColor(getResources().getColor(R.color.tribaPurple));


        }
        else if(backColor[0] == 2){
            gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{getResources().getColor(R.color.tribaGreen),Color.BLACK, Color.BLACK,  getResources().getColor(R.color.tribaGreen)});
            View viewSeparator = (View) findViewById(R.id.viewChatTopSeparator);
            viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaGreen));

            View viewSeparatorB = (View) findViewById(R.id.viewChatBottomSeparator);
            viewSeparatorB.setBackgroundColor(getResources().getColor(R.color.tribaGreen));

        }
        else if(backColor[0] == 3){
            gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{getResources().getColor(R.color.tribaBlue),Color.BLACK, Color.BLACK,  getResources().getColor(R.color.tribaBlue)});
            View viewSeparator = (View) findViewById(R.id.viewChatTopSeparator);
            viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaBlue));

            View viewSeparatorB = (View) findViewById(R.id.viewChatBottomSeparator);
            viewSeparatorB.setBackgroundColor(getResources().getColor(R.color.tribaBlue));
        }
        else if(backColor[0] == 4){
            gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{getResources().getColor(R.color.tribaPink),Color.BLACK, Color.BLACK,  getResources().getColor(R.color.tribaPink)});
            View viewSeparator = (View) findViewById(R.id.viewChatTopSeparator);
            viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaPink));

            View viewSeparatorB = (View) findViewById(R.id.viewChatBottomSeparator);
            viewSeparatorB.setBackgroundColor(getResources().getColor(R.color.tribaPink));
        }
        else{
            gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{getResources().getColor(R.color.tribaPurple),Color.BLACK, Color.BLACK,  getResources().getColor(R.color.tribaPurple)});

            View viewSeparator = (View) findViewById(R.id.viewChatTopSeparator);
            viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaPurple));

            View viewSeparatorB = (View) findViewById(R.id.viewChatBottomSeparator);
            viewSeparatorB.setBackgroundColor(getResources().getColor(R.color.tribaPurple));
        }



       // targetView.setBackground(gradientDrawable);


       Button btnDetails = (Button) findViewById(R.id.btnHomeChatDetails);


        btnDetails.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){





                MyDialog = new Dialog(homeChat.this);

                MyDialog.setContentView(R.layout.post_details);
                MyDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                TextView txtText = (TextView) MyDialog.findViewById(R.id.txtPostDetailsText) ;
                TextView txtPoster = (TextView) MyDialog.findViewById(R.id.txtPostDetailsCreator) ;
                TextView txtTime= (TextView) MyDialog.findViewById(R.id.txtPostDetailsTime) ;

                txtText.setText(passedObject.postText);
                txtPoster.setText(passedObject.postUserName);



                TimeZone tyy = TimeZone.getDefault();


                String eFormatDateandTime = new SimpleDateFormat(" E MM/dd hh:mm a").format((passedObject.postTime * 1000) - tyy.getOffset(System.currentTimeMillis()));

                txtTime.setText(eFormatDateandTime);

                MyDialog.show();



            }

        });


        myToolbar.setTitle("");

        //Sets up the like/dislike button in the toolbar
        Button bt = new Button(this);
        bt.setText("Like/Dislike Chat");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.LEFT;


        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        postRecyclerview = findViewById(R.id.lstHomeChat);
        txtUserChat = (EditText) findViewById(R.id.textInptHomeChat);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        nRef = database.getReference();



        nRef.child("UsersNotedPostsIDList").child(user.getUid()).child(passedObject.postID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //bus number exists in Database
                    isPostObjectNoted = true;
                    //btn2.setTitle("unNote", for: .normal)
                    btnNotedPost.setColorFilter(getResources().getColor(R.color.tribaNote));

                    passedIsPostObjectNotedInitialValue = true;
                    btnNotedPost.setEnabled(true);
                } else {
                    isPostObjectNoted = false;
                    btnNotedPost.setColorFilter(Color.WHITE);
                    btnNotedPost.setEnabled(true);

                    passedIsPostObjectNotedInitialValue = false;

                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });


        nRef.child("deactivatedChat").child(passedObject.postID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    new AlertDialog.Builder(homeChat.this).setTitle("Post Inactive").setMessage("This post was deactivated by the user")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //MyDialog.cancel();
                                    dialog.cancel();
                                    finish();


                                }
                            }).show();





                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        nRef.child("Users").child(passedObject.postCreatorID).child("ActiveStatus").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    if(dataSnapshot.getValue().toString().equals("INACTIVE")){


                        new AlertDialog.Builder(homeChat.this).setTitle("Post Inactive").setMessage("This post was deactivated by the user")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //MyDialog.cancel();
                                        dialog.cancel();
                                        finish();


                                    }
                                }).show();




                    }



                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });




        nRef.child("ChatBanList").child(passedObject.postCreatorID).child(passedObject.postID).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {




                        new AlertDialog.Builder(homeChat.this).setTitle("").setMessage("You have been banned from this chat")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //MyDialog.cancel();
                                        dialog.cancel();
                                        finish();


                                    }
                                }).show();








                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });








        btnNotedPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if(isPostObjectNoted){
                    //If already noted, unnote
                    btnNotedPost.setColorFilter(Color.WHITE);

                    notedNumbers.setNumOfNotedPosts(notedNumbers.getNumOfNotedPosts()- 1);
                    isPostObjectNoted = false;

                    nRef.child("notedPosts").child(user.getUid()).child(passedObject.postID).setValue(null);
                    nRef.child("UsersNotedPostsIDList").child(user.getUid()).child(passedObject.postID).setValue(null);

                    //used when user is getting noted history for the box they're in

                }
                else{

                    if(notedNumbers.getNumOfNotedPosts() > 19){



                        new AlertDialog.Builder(getApplicationContext()).setTitle("Note Limit Reached").setMessage("You cannot have more than 20 posts pinned. Remove some of your pinned posts to add new ones.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                       // MyDialog.cancel();
                                        dialog.cancel();


                                    }
                                }).show();



                    }

                    else{
                        //If  unnoted, note
                        isPostObjectNoted = true;

                        notedNumbers.setNumOfNotedPosts(notedNumbers.getNumOfNotedPosts() + 1);
                        btnNotedPost.setColorFilter(getResources().getColor(R.color.tribaNote));

                        nRef.child("notedPosts").child(user.getUid()).child(passedObject.postID).setValue(passedObject);
                        nRef.child("UsersNotedPostsIDList").child(user.getUid()).child(passedObject.postID).setValue(passedObject.postID);
                    }
                }


            }

        });






/*


        observeSingleEvent(of: .value, with: { (snapshot) in
            print("snappy:" + snapshot.value.debugDescription)
            if((snapshot.exists())){

                self.isPostObjectNoted = true
                //btn2.setTitle("unNote", for: .normal)
                self.btnNotePost.tintColor = #colorLiteral(red: 0.831372549, green: 0.6862745098, blue: 0.2156862745, alpha: 1)


            }
            else{

                self.isPostObjectNoted = false
                self.btnNotePost.tintColor = #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)

            }
        })
*/


        bt.setLayoutParams(params);
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(homeChat.this);
                final View mView = getLayoutInflater().inflate(R.layout.home_chat_like_dislike, null);


                Button btnChooseDislike = (Button) mView.findViewById(R.id.btnHomeChatMainDislike);
                Button btnChooselike = (Button) mView.findViewById(R.id.btnHomeChatMainLike);


                final String ratingLink = "Users/" + passedObject.getPostCreatorID() + "/POSTS/" + passedObject.getPostID() + "/ratingBlend";


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                btnChooseDislike.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        userLikeDislikeClass userLikePost = new userLikeDislikeClass("L", ratingLink, userName, passedObject.getPostID(), passedObject.getPostLocations(), null);

                        FirebaseDatabase.getInstance().getReference().child("LIKESDISLIKES").child(passedObject.getPostID()).child("LDNOTES").child(userName).setValue(userLikePost);


                        dialog.cancel();

                    }
                });
                btnChooselike.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        userLikeDislikeClass userLikePost = new userLikeDislikeClass("Y", ratingLink, userName, passedObject.getPostID(), passedObject.getPostLocations(), null);


                        FirebaseDatabase.getInstance().getReference().child("LIKESDISLIKES").child(passedObject.getPostID()).child("LDNOTES").child(userName).setValue(userLikePost);
                        dialog.cancel();

                    }
                });

                dialog.show();

            }
        });


        //This is the SEND button in the chat
        Button btnSendMessage = (Button) findViewById(R.id.btnChatHomeSend);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.textInptHomeChat);
                // FirebaseDatabase.getInstance().getReference().child("Chats").child(ChatID).push().setValue(new Message(input.getText().toString(), user.getDisplayName(), user.getUid()));
                //FirebaseDatabase.getInstance().getReference().child("Groups").child(ChatID).child("lastMessage").setValue(input.getText().toString());
                String postID =  FirebaseDatabase.getInstance().getReference().push().getKey();


                long currentTime = System.currentTimeMillis();


                TimeZone tyy = TimeZone.getDefault();

                long unixTime = (currentTime + tyy.getOffset(System.currentTimeMillis()))/1000;



                String postText = input.getText().toString();

               // let postTime:CLong = (TimeZone.current.secondsFromGMT() + CLong(Date().timeIntervalSince1970))


                if (postText.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(homeChat.this);
                    builder.setMessage("Post is Empty")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else {

                    postChatObject chatPost = new postChatObject(userID, postText, postID, userLat, userLong, unixTime, -unixTime, userName, "CHATCOMMENT_ANDROID",
                            "",
                            user.getDisplayName(),
                            0,
                            passedObject.postLocations,
                            0L,
                            0L,
                            0,
                            0,
                            true,
                            passedObject.postSpecs,
                            passedObject.postCreatorID,
                            false,
                            passedObject.postID,
                            1);


                    //ref?.child("b").childByAutoId().setValue("yo")
                    //txtUserChat.text = ""


                    // ref?.child("Users").child((passedObject?.postCreatorID)!).child("POSTS").child((passedObject?.postID)!).child("CHATS").child(postID).setValue(post)
                    FirebaseDatabase.getInstance().getReference().child("Chats").child((passedObject.postCreatorID)).child((passedObject.postID)).child(postID).setValue(chatPost);


                    input.setText("");
                    View v = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                }
                /*
                InputMethodManager inputMethodManager = (InputMethodManager)getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
*/

            }

        });


        //myToolbar.setTitle(passedObject.getPostTitle());
        listOfMessage = (RecyclerView) findViewById(R.id.lstHomeChat);


        final TextView txtPassedObjectText = (TextView) findViewById(R.id.txtHomeChatText);
        txtPassedObjectText.setText(passedObject.getPostText());


        myRef = database.getReference().child("Chats").child((passedObject.postCreatorID)).child((passedObject.postID));

        //myRef = database.getReference().child("Users").child(passedObject.getPostCreatorID()).child("POSTS").child(passedObject.getPostID()).child("CHATS");
        userLikeHistoryRef = database.getReference();

        //Gets username from storage
        SharedPreferences userInfoPref = this.getSharedPreferences("USERINFO", MODE_PRIVATE);
        String uName = userInfoPref.getString("uName", "");
        if (!uName.equals("")) {
            userName = uName;
            loadPostLikeHistory();

        } else {
            Intent intentSetUsername = new Intent(homeChat.this, settingUsername.class);
            intentSetUsername.putExtra("allowBackPress", false);
            startActivity(intentSetUsername);
        }

        //Gets saved user Lat and Long
        SharedPreferences prefs2 = this.getSharedPreferences("USERINFO", MODE_PRIVATE);


        userLat = Double.longBitsToDouble(prefs2.getLong("UserLastLat", Double.doubleToLongBits(1.001)));
        userLong = Double.longBitsToDouble(prefs2.getLong("UserLastLong", Double.doubleToLongBits(1.001)));

        loadPostLikeHistory();
//ref?.child("Users").child((passedObject?.postCreator)!).child("POSTS").child((passedObject?.postID)!).child("CHATS")

    }


    /*
    *
    *
    * Gets and loads a list of texts that the user has liked in the past
    *
     */

    public void loadPostLikeHistory() {

        userLikeHistoryRef.child("LIKEDPOSTCHAT").child(passedObject.getPostID()).child(userID).limitToFirst(350).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Loads the values into a hashmap
                //Values loaded are a string that has the likedpostid concatenated with the the user's rating and a string that is useless

                Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();

                //If hashmap isn't null, it takes the important part of the hashmap and adds it to a list of the user's previously liked posts
                if (td != null) {
                    List<String> tempList = new ArrayList<String>(td.values());

                    userLikedHistoryList.addAll(tempList);
                }

                //Once the like history is loaded, the posts are called
                displayPosts();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });


    }


//Loads the posts

    public void displayPosts() {

        Query queryRef;
        queryRef = myRef.orderByChild("postIsActive").equalTo(true);


/*
        if(showTopPosts == true){
            queryRefRating = myRef.orderByChild("ratingBlend");

        }
        else{
            queryRefRating = myRef;
        }
*/


        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    postChatObject Post = dataSnapshot1.getValue(postChatObject.class);
                    postList.add(Post);

                    Log.d("!!!!!", "2");

                }
                Log.d("!!!!!", "3");

                allDataAdapter = new homeChat.MyAdapter(homeChat.this, postList);

                LinearLayoutManager llm = new LinearLayoutManager(homeChat.this);
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


    public class MyAdapter extends RecyclerView.Adapter<homeChat.MyAdapter.MyViewHolder> {
        private LayoutInflater inflater;
        private List<postChatObject> list;
        Context ctx;

        public MyAdapter(Context context, List<postChatObject> feedItemList) {
            inflater = LayoutInflater.from(context);
            this.list = feedItemList;
        }



        @Override
        public homeChat.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parrent, int i) {
            View view = inflater.inflate(R.layout.chat_home_post, parrent, false);
            homeChat.MyAdapter.MyViewHolder holder = new homeChat.MyAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final homeChat.MyAdapter.MyViewHolder viewHolder, final int position) {
            final postChatObject postItem = postList.get(position);
            //viewHolder.postText.setText(item.getPostText());
            viewHolder.postText.setText(postItem.postText);
            viewHolder.txtUserName.setText(postItem.postUserName);


            String strHasLikedPost = postItem.postID + ":L";
            String strHasNotlikedPost = postItem.postID + ":NL";



            if(passedObject.postCreatorID.equals(userID)){
                viewHolder.btnReport.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.btnReport.setVisibility(View.INVISIBLE);

            }


            if(backColor[0] == 1){
                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back_type1);

            }
            else if(backColor[0] == 2){
                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back_type2);

            }
            else if(backColor[0] == 3){
                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back_type3);

            }
            else if(backColor[0] == 4){
                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back_type4);

            }
            else{
                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back);
            }


            if(postItem.postCreatorID.equals(user.getUid())){
                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back);



            }


            if (userLikedHistoryList.contains(strHasLikedPost)) {
                viewHolder.btnLike.setColorFilter(Color.BLUE);


            }  else if (userLikedHistoryList.contains(strHasNotlikedPost)) {

                viewHolder.btnLike.setColorFilter(Color.WHITE);


            } else {

                viewHolder.btnLike.setColorFilter(Color.WHITE);


            }


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //Do Nothing


                }

            });

            viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImageButton btnLike = viewHolder.btnLike;

                    //  int viewColor = ((ColorDrawable) btnLike.getBackground()).getColor();
                    //ColorFilter test = btnLike.getColorFilter();









                    if (userLikedHistoryList.contains(postItem.postID + ":NL"))   {
                        //(userLikedHistoryList.contains(postItem.postID + ":NL"))

                        userLikedHistoryList.remove(postItem.postID + ":NL");
                        btnLike.setColorFilter(Color.BLUE);


                        if (userLikedHistoryList.contains(postItem.postID + ":L")) {
                            userLikedHistoryList.remove(postItem.postID + ":L");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":LL")) {
                            userLikedHistoryList.remove(postItem.postID + ":LL");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":D")) {
                            userLikedHistoryList.remove(postItem.postID + ":D");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":DD")) {
                            userLikedHistoryList.remove(postItem.postID + ":DD");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":+N")) {
                            userLikedHistoryList.remove(postItem.postID + ":+N");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":-N")) {
                            userLikedHistoryList.remove(postItem.postID + ":-N");
                        }


                        String userRating = "L";

                        String commentID = postItem.postID;
                        String postLink = "Chats/"+(postItem.postCreatorID)+"/"+(passedObject.postID)+"/"+commentID+"/ratingBlend";
                        postChatLikeObject likePost = new postChatLikeObject(userRating, postLink, postItem.postLocations, userID, passedObject.postID, commentID);


                        database.getReference().child("CHATLIKESDISLIKES").child((passedObject.postID)).child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);



                    }

                   else {

                        // if (userLikedHistoryList.contains(postItem.postID + ":L"))

                        userLikedHistoryList.remove(postItem.postID + ":L");
                        btnLike.setColorFilter(Color.WHITE);


                        if (userLikedHistoryList.contains(postItem.postID + ":L")) {
                            userLikedHistoryList.remove(postItem.postID + ":L");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":LL")) {
                            userLikedHistoryList.remove(postItem.postID + ":LL");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":D")) {
                            userLikedHistoryList.remove(postItem.postID + ":D");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":DD")) {
                            userLikedHistoryList.remove(postItem.postID + ":DD");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":+N")) {
                            userLikedHistoryList.remove(postItem.postID + ":+N");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":-N")) {
                            userLikedHistoryList.remove(postItem.postID + ":-N");
                        }


                        String userRating = "NL";

                        String commentID = postItem.postID;
                        String postLink = "Chats/"+(postItem.postCreatorID)+"/"+(passedObject.postID)+"/"+commentID+"/ratingBlend";
                        postChatLikeObject likePost = new postChatLikeObject(userRating, postLink, postItem.postLocations, userID, passedObject.postID, commentID);


                        database.getReference().child("CHATLIKESDISLIKES").child((passedObject.postID)).child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);



                    }

                }

            });




            viewHolder.btnReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(homeChat.this).setTitle("Post Inactive").setMessage("This post was deactivated by the user")
                            .setPositiveButton("Remove Post", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    nRef.child("Chats").child(passedObject.postCreatorID).child(passedObject.postID).child(postItem.postID).child("postIsActive").setValue(false);

                                    postList.remove(postItem);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,postList.size());
                                    dialog.cancel();



                                }
                            })
                            .setNeutralButton("Ban User From Chat", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //MyDialog.cancel();





                                    String postID = "P" + FirebaseDatabase.getInstance().getReference().push().getKey();


                                    long currentTime = System.currentTimeMillis();


                                    TimeZone tyy = TimeZone.getDefault();

                                    long unixTime = (currentTime + tyy.getOffset(System.currentTimeMillis()))/1000;



                                    String postTextt = postItem.postUserName+ " was banned from this chat";

                                    // let postTime:CLong = (TimeZone.current.secondsFromGMT() + CLong(Date().timeIntervalSince1970))




                                    postChatObject chatPost = new  postChatObject( "" , postTextt,postID , 0.0 , 0.0 ,unixTime, -unixTime, "chatAdmin" , "CHATBANUPDATE",
                                            "",
                                            user.getDisplayName(),
                                            0,
                                            passedObject.postLocations,
                                            0L,
                                            0L,
                                            0,
                                            0,
                                            true,
                                            passedObject.postSpecs,
                                            passedObject.postCreatorID,
                                            false,
                                            passedObject.postID,
                                            1);



                                    //ref?.child("b").childByAutoId().setValue("yo")
                                    //txtUserChat.text = ""




                                    // ref?.child("Users").child((passedObject?.postCreatorID)!).child("POSTS").child((passedObject?.postID)!).child("CHATS").child(postID).setValue(post)
                                    FirebaseDatabase.getInstance().getReference().child("Chats").child((passedObject.postCreatorID)).child((passedObject.postID)).child(postID).setValue(chatPost);



                                    FirebaseDatabase.getInstance().getReference().child("ChatBanList").child(passedObject.postCreatorID).child(passedObject.postID).child(postItem.postCreatorID).setValue(true);

















                                    dialog.cancel();


                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //MyDialog.cancel();
                                    dialog.cancel();


                                }
                            }).show();


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
            TextView txtUserName;

            Button btnReport;
            ImageButton btnLike;

            View mView;


            public MyViewHolder(View itemView) {
                super(itemView);
                //name = (TextView) itemView.findViewById(R.id.tvname);
                postText = (TextView) itemView.findViewById(R.id.txtChatText);
                txtUserName = (TextView) itemView.findViewById(R.id.txtChatUserName);

                btnReport = (Button) itemView.findViewById(R.id.btnChatHomeReport);
                btnLike = (ImageButton) itemView.findViewById(R.id.btnChatHomeLike);



                //The report button


/*

        private  List<postClass> mUserLsit=new ArrayList<>();
        private Context mContext;

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment,parent,false);
            return new ItemViewHolder(view);
        }

        public DisplayAllData(Context mContext,List<postClass> mUserLsit) {
            this.mContext=mContext;
            this.mUserLsit = mUserLsit;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            postClass user=mUserLsit.get(position);
            holder.mTvName.setText(user.postText);
           // holder.mTvEmail.setText(user.email);
          //  holder.mTvPwd.setText(user.pwd);
        }

        @Override
        public int getItemCount() {
            return mUserLsit.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView mTvName,mTvEmail,mTvPwd;
            public ItemViewHolder(View itemView) {
                super(itemView);
                mTvEmail=itemView.findViewById(R.id.rlTvEmail);
                mTvName=itemView.findViewById(R.id.rlTvName);
                mTvPwd=itemView.findViewById(R.id.rlTvPwd);

            }
    }
*/

            }
        }

    }


    @Override
    public void onBackPressed() {


 if(isPostObjectNoted != passedIsPostObjectNotedInitialValue){


             globalVarClass.setShouldReset(true);

     if(isPostObjectNoted){
                 globalVarClass.setAddOrSubtractID("ADD");

             }
            else{
                globalVarClass.setAddOrSubtractID("SUBTRACT");

            }
           globalVarClass.setPostID(passedObject.postID);



 }



       finish();
    }



}
