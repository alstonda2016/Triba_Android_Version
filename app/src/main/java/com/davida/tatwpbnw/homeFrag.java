package com.davida.tatwpbnw;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by davida on 2/13/18.
 *
 *
 *
 *
 * This is the macDaddy of all files in this version of the app
 *
 *
 * 1. Fragments are not activities, they are like the "lite" version of a fragment
 *    Fragments have all the capabilities of activities but the oncreate is split into an onCreateView
 *    and an OnCreateView.
 * 2. This fragment also has onCreateOptionsMenu and onOptionsItemSelected, which are for the toolbar
 *     at the top. They use "custom" files which can be found in MENU -> main.xml. the onCreateOptionsMenu
 *     loads the toolbar and the onOptionsItemSelected used to load the pop-up that the users use to
 *     create a post
 *
 * 3.Inflators are basically the way in which you load the xml part of something like a button or a menu
 *      when they aren't directly tied to them in the way an activity is. Since a fragment is not tied to
 *      an xml file like an activity is, this uses an Inflator to get everything first. The menu also uses an Inflator
 *
 *
 *
 */

public class homeFrag extends Fragment {

    View view;
    Dialog MyDialog;
    DatabaseReference myRef;
    DatabaseReference nRef;
    DatabaseReference userLikeHistoryRef;
    RelativeLayout activity_group_main;
    private RecyclerView postRecyclerview;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseListAdapter<postClass> adapter;
    ListView listOfMessage;
    final List<String> userLikedHistoryList = new ArrayList<String>();
    final List<String> userNotedPostsList = new ArrayList<String>();

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
    String currentLoc;
    int[] currentOrder = {1};
    boolean isModerator = false;

    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    Button btnShowNew;

    Button btnShowHotTopic;


    Button btnShowPopular;

    Button btnShowCloseBy;

    TextView txtOrderDesc;


    private MyAdapter allDataAdapter;
    private List<postClass> postList = new ArrayList<>();


    DatabaseReference usersRef;


    //THIS IS THE TOOLBAR AT THE TOP
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Loads when the users press the create button
        if (item.getItemId() == R.id.addEvent) {

            loadNotedPosts();
          //  displayPosts();


        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getActivity().setTitle("");
        setHasOptionsMenu(true);



        Log.d("!!!!!", "HF");
        Log.d("!!!!!", "HF");
        Log.d("!!!!!", "HF");
        Log.d("!!!!!", "HF");
        Log.d("!!!!!", "HF");
        Log.d("!!!!!", "HF");
        Log.d("!!!!!", "HF");
        Log.d("!!!!!", "HF");
        Log.d("!!!!!", "HF");
        Log.d("!!!!!", "HF");


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        SharedPreferences prefs = this.getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);


        //Gets the saved latitude and longitude from the user
        Double uLat = Double.longBitsToDouble(prefs.getLong("UserLastLat", Double.doubleToLongBits(1.001)));
        Double uLong = Double.longBitsToDouble(prefs.getLong("UserLastLong", Double.doubleToLongBits(1.001)));
        //If user doesn't have anything saved
        if (uLat == 1.001 || uLong == 1.001) {
           /* Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("allowBackPress", false);
            startActivity(intent);
            */
            currentLoc = " ";


        } else {


            //PostLat and userLate are used differently
            postLat = uLat;
            postLong = uLong;
            userLat = uLat;
            userLong = uLong;


            //postLat = 40.109861002678;
           // postLong = -88.2303859246549;

            //truncates the the full lat and long to 2 decimals
            /*BigDecimal bd = new BigDecimal(userLat).setScale(1, RoundingMode.DOWN);
            postLat = bd.doubleValue();
            BigDecimal bd2 = new BigDecimal(userLong).setScale(1, RoundingMode.DOWN);
            postLong = bd2.doubleValue();
            */

            BigDecimal postLatDec = BigDecimal.valueOf(postLat);
            BigDecimal postLongDec = BigDecimal.valueOf(postLong);

            DecimalFormat df = new DecimalFormat("##.#");
            df.setRoundingMode(RoundingMode.DOWN);
            double postLatDecTrunc = Double.parseDouble(df.format(postLatDec));
            double postLongDecTrunc = Double.parseDouble(df.format(postLongDec));

            BigDecimal bdLAT = BigDecimal.valueOf(postLatDecTrunc);
            BigDecimal bdLONG = BigDecimal.valueOf(postLongDecTrunc);
            postLat = bdLAT.doubleValue();
            postLong = bdLONG.doubleValue();

            //currentloc is the string version of one's location. All decimals are replaced by "\" since
            //firebase doesn't accept a decimal as a node
            currentLoc = ((postLat) + "_" + (postLong)).replaceAll("\\.", "|");

        }


        showTopPosts = false;

        Log.d("LOCCCCC", currentLoc);
        Log.d("LOCCCCC1", currentLoc);
        Log.d("LOCCCCC2", currentLoc);
        Log.d("LOCCCCC3", currentLoc);
        Log.d("LOCCCCC4", currentLoc);
        Log.d("LOCCCCC5", currentLoc);
        Log.d("LOCCCCC6", currentLoc);
        Log.d("LOCCCCC7", currentLoc);
        Log.d("LOCCCCC8", currentLoc);


        myRef = database.getReference().child("POSTS").child(currentLoc);
        userLikeHistoryRef = database.getReference();
        nRef = database.getReference();



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);

        View viewSeparator = (View) view.findViewById(R.id.viewSeparator);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{getResources().getColor(R.color.tribaPurple),Color.BLACK, Color.BLACK, Color.BLACK,   Color.BLACK, getResources().getColor(R.color.tribaPurple)});


       //view.setBackground(gradientDrawable);
        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
       // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // navigation.setItemBackgroundResource(R.color.zxing_transparent);
       // navigation.setBackgroundColor( ColorUtils.blendARGB(getResources().getColor(R.color.tribaPurple), Color.BLACK, 0.5F));

        viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaPurple));


        //Loads the toolbar
        MyDialog = new Dialog(getContext());
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.MyProfile_Toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

/*
        //Useless right now
        Button btnLaunchProfile = (Button) view.findViewById(R.id.btnLauchProfileHome) ;
        btnLaunchProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyDialog.setContentView(R.layout.user_profile_fragment);
                ImageView profilePicture = (ImageView) MyDialog.findViewById(R.id.imgProfileFrag);
                myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("UserInfo");


                MyDialog.show();

            }
        });
*/

/*
        //Useless right now
        Button btnChooseScroll = (Button) view.findViewById(R.id.btnChangeOrder) ;
        btnChooseScroll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyDialog.setContentView(R.layout.change_order_popup);
                Button btnChoosePost = (Button) MyDialog.findViewById(R.id.btnNewestPosts) ;
                btnChoosePost.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        showTopPosts = false;
                        displayPosts();

                        MyDialog.cancel();
                        // startActivity(new Intent(getContext(), writePost.class));

                    }
                });

                Button btnChooseEvent = (Button) MyDialog.findViewById(R.id.btnTopLocalPosts) ;
                btnChooseEvent.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        showTopPosts = true;
                        displayPosts();
                        MyDialog.cancel();
                        // startActivity(new Intent(getContext(), writePost.class));

                    }
                });
                MyDialog.show();

            }
        });
*/

        txtOrderDesc = (TextView) view.findViewById(R.id.txtHomeFragTitleText);


        btnShowNew = (Button) view.findViewById(R.id.button3);
        btnShowNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               // btnShowNew.setBackgroundColor(Color.WHITE);
                btnShowNew.setBackgroundResource(R.drawable.tab_button_back);
                btnShowHotTopic.setBackgroundColor(Color.TRANSPARENT);
                btnShowPopular.setBackgroundColor(Color.TRANSPARENT);
                btnShowCloseBy.setBackgroundColor(Color.TRANSPARENT);

                btnShowHotTopic.setTextColor(Color.WHITE);
                btnShowCloseBy.setTextColor(Color.WHITE);
                btnShowNew.setTextColor(Color.BLACK);
                btnShowPopular.setTextColor(Color.WHITE);

                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{getResources().getColor(R.color.tribaPurple),Color.BLACK, Color.BLACK, Color.BLACK,   Color.BLACK, getResources().getColor(R.color.tribaPurple)});
                View viewSeparator = (View) view.findViewById(R.id.viewSeparator);
                viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaPurple));


                //view.setBackground(gradientDrawable);

               // view.setBackground(gradientDrawable);
                BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
                // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                // navigation.setItemBackgroundResource(R.color.zxing_transparent);


              //  navigation.setBackgroundColor( ColorUtils.blendARGB(getResources().getColor(R.color.tribaPurple), Color.BLACK, 0.5F));
                txtOrderDesc.setText("Newest Posts");
                currentOrder[0] = 1;
                displayPosts();


            }
        });
        btnShowHotTopic = (Button) view.findViewById(R.id.button4);
        btnShowHotTopic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {




                btnShowHotTopic.setBackgroundResource(R.drawable.tab_button_back);
                btnShowNew.setBackgroundColor(Color.TRANSPARENT);
                btnShowPopular.setBackgroundColor(Color.TRANSPARENT);
                btnShowCloseBy.setBackgroundColor(Color.TRANSPARENT);

                btnShowHotTopic.setTextColor(Color.BLACK);
                btnShowCloseBy.setTextColor(Color.WHITE);
                btnShowNew.setTextColor(Color.WHITE);
                btnShowPopular.setTextColor(Color.WHITE);

                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{getResources().getColor(R.color.tribaGreen),Color.BLACK, Color.BLACK, Color.BLACK,   Color.BLACK,getResources().getColor(R.color.tribaGreen)});


               // view.setBackground(gradientDrawable);
                BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
                // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                // navigation.setItemBackgroundResource(R.color.zxing_transparent);

               // navigation.setBackgroundColor( ColorUtils.blendARGB(getResources().getColor(R.color.tribaGreen), Color.BLACK, 0.5F));

                View viewSeparator = (View) view.findViewById(R.id.viewSeparator);
                viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaGreen));



                txtOrderDesc.setText("Post With The Most Replies");

                currentOrder[0] = 2;
                displayPosts();

            }
        });

        btnShowPopular = (Button) view.findViewById(R.id.button5);
        btnShowPopular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                btnShowPopular.setBackgroundResource(R.drawable.tab_button_back);
                btnShowHotTopic.setBackgroundColor(Color.TRANSPARENT);
                btnShowNew.setBackgroundColor(Color.TRANSPARENT);
                btnShowCloseBy.setBackgroundColor(Color.TRANSPARENT);
                btnShowHotTopic.setTextColor(Color.WHITE);
                btnShowCloseBy.setTextColor(Color.WHITE);
                btnShowNew.setTextColor(Color.WHITE);
                btnShowPopular.setTextColor(Color.BLACK);

                View viewSeparator = (View) view.findViewById(R.id.viewSeparator);
                viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaBlue));


                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{getResources().getColor(R.color.tribaBlue),Color.BLACK, Color.BLACK, Color.BLACK,   Color.BLACK,  getResources().getColor(R.color.tribaBlue)});


              //  view.setBackground(gradientDrawable);

                BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
                // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                // navigation.setItemBackgroundResource(R.color.zxing_transparent);


               // navigation.setBackgroundColor( ColorUtils.blendARGB(getResources().getColor(R.color.tribaBlue), Color.BLACK, 0.5F));



                txtOrderDesc.setText("Highest Rated Posts");

                currentOrder[0] = 3;
                displayPosts();
            }
        });
        btnShowCloseBy = (Button) view.findViewById(R.id.button6);
        btnShowCloseBy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                btnShowCloseBy.setBackgroundResource(R.drawable.tab_button_back);
                btnShowHotTopic.setTextColor(Color.WHITE);
                btnShowCloseBy.setTextColor(Color.BLACK);
                btnShowNew.setTextColor(Color.WHITE);
                btnShowPopular.setTextColor(Color.WHITE);
                btnShowHotTopic.setBackgroundColor(Color.TRANSPARENT);
                btnShowNew.setBackgroundColor(Color.TRANSPARENT);
                btnShowPopular.setBackgroundColor(Color.TRANSPARENT);


                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{getResources().getColor(R.color.tribaPink),Color.BLACK, Color.BLACK, Color.BLACK,   Color.BLACK, getResources().getColor(R.color.tribaPink)});


                View viewSeparator = (View) view.findViewById(R.id.viewSeparator);
                viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaPink));

              //  view.setBackground(gradientDrawable);

                BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
                // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                // navigation.setItemBackgroundResource(R.color.zxing_transparent);


             //   navigation.setBackgroundColor( ColorUtils.blendARGB(getResources().getColor(R.color.tribaPink), Color.BLACK, 0.5F));


                txtOrderDesc.setText("Only The Posts That Are Close By");

                currentOrder[0] = 4;
                displayPosts();
            }
        });


        //Loads the saved username. If not, it goes to the SetUsername activity
        SharedPreferences userInfoPref = this.getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);
        String uName = userInfoPref.getString("uName", "");
        if (!uName.equals("")) {
            userName = uName;
            loadPostLikeHistory();

        } else {
           /* Intent intent = new Intent(getContext(), settingUsername.class);
            intent.putExtra("allowBackPress", false);
            startActivity(intent);
            */
            checkUname();
        }
        String uProfileAccessLevel = userInfoPref.getString("userAccessLevel", "NORMAL");

        if(uProfileAccessLevel.equals("NORMAL")){

            isModerator = false;
        }
        else if(uProfileAccessLevel.equals("MODLEVELOFFERED")){
            isModerator = false;

        }
        else if(uProfileAccessLevel.equals("LOCALMODERATOR")){
            isModerator = true;

        }
        else if(uProfileAccessLevel.equals("NORMALREJECTED")){
            isModerator = false;

        }else if(uProfileAccessLevel.equals("ADMIN")){
            isModerator = true;

        }
        else{

            isModerator = false;

        }



        //   displayPosts();


        postRecyclerview = view.findViewById(R.id.recyclerView_of_posts_home);
        txtOrderDesc.setText("Newest Posts");

loadNotedPosts();
      //  displayPosts();

        Log.d("!!!!!", "1");

 /*  Query queryRefRating ;

        if(showTopPosts == true){
            queryRefRating = myRef.orderByChild("ratingBlend");

        }
        else{
            queryRefRating = myRef;
        }
        */


        return view;

    }


    public void loadPostLikeHistory() {
        //truncates the the full lat and long to 2 decimals
/*
        BigDecimal bd = new BigDecimal(userLat).setScale(2, RoundingMode.DOWN);
        postLat = bd.doubleValue();
        BigDecimal bd2 = new BigDecimal(userLong).setScale(2, RoundingMode.DOWN);
        postLong = bd2.doubleValue();
        //currentloc is the string version of one's location. All decimals are replaced by "\" since
        //firebase doesn't accept a decimal as a node
        String currentLoc = ((postLat)+"_"+(postLong)).replaceAll("\\.", "|");
*/

        //Loads the user's history that is saved in their current location box
        userLikeHistoryRef.child("USERLIKELOC").child(currentLoc).child(user.getUid()).limitToFirst(350).addListenerForSingleValueEvent(new ValueEventListener() {

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

                //displayPosts();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });




    }


    public void loadNotedPosts() {
        //truncates the the full lat and long to 2 decimals
/*
        BigDecimal bd = new BigDecimal(userLat).setScale(2, RoundingMode.DOWN);
        postLat = bd.doubleValue();
        BigDecimal bd2 = new BigDecimal(userLong).setScale(2, RoundingMode.DOWN);
        postLong = bd2.doubleValue();
        //currentloc is the string version of one's location. All decimals are replaced by "\" since
        //firebase doesn't accept a decimal as a node
        String currentLoc = ((postLat)+"_"+(postLong)).replaceAll("\\.", "|");
*/

        //Loads the user's history that is saved in their current location box
        userLikeHistoryRef.child("UsersNotedPostsIDList").child(user.getUid()).limitToFirst(350).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();

                    //If hashmap isn't null, it takes the important part of the hashmap and adds it to a list of the user's previously liked posts

                    if (td != null) {
                        List<String> tempList = new ArrayList<String>(td.values());
                        userNotedPostsList.addAll(tempList);
                    }

                    notedNumbers.setNumOfNotedPosts(userNotedPostsList.size());

                    displayPosts();
                }
                else{
                    displayPosts();


                }
                    //Loads the values into a hashmap
                //Values loaded are a string that has the likedpostid concatenated with the the user's rating and a string that is useless



                //Once the like history is loaded, the posts are called

                //displayPosts();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }


    public void displayPosts() {

        Query queryRef;
        queryRef = myRef.orderByChild("postTimeInverse");
        postList.clear();
        allDataAdapter = new MyAdapter(view.getContext(), postList);

      LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        postRecyclerview.setLayoutManager(llm);

        postRecyclerview.setAdapter(allDataAdapter);
        allDataAdapter.notifyDataSetChanged();



        if (currentOrder[0] == 1) {
            queryRef = myRef.orderByChild("postTimeInverse").limitToFirst(500);

            //btnShowNew.setBackgroundColor(Color.BLUE);



            btnShowNew.setBackgroundResource(R.drawable.tab_button_back);
            btnShowNew.setTextColor(Color.BLACK);
            btnShowCloseBy.setTextColor(Color.WHITE);
            btnShowPopular.setTextColor(Color.WHITE);
            btnShowHotTopic.setTextColor(Color.WHITE);
            btnShowHotTopic.setBackgroundColor(Color.TRANSPARENT);
            btnShowPopular.setBackgroundColor(Color.TRANSPARENT);
            btnShowCloseBy.setBackgroundColor(Color.TRANSPARENT);

            /*btnShowHotTopic.setBackgroundColor(Color.TRANSPARENT);
            btnShowPopular.setBackgroundColor(Color.TRANSPARENT);
            btnShowCloseBy.setBackgroundColor(Color.TRANSPARENT);
            */


        } else if (currentOrder[0] == 2) {
            queryRef = myRef.orderByChild("messageRanking").limitToFirst(500);

            btnShowHotTopic.setBackgroundResource(R.drawable.tab_button_back);
            btnShowHotTopic.setTextColor(Color.BLACK);
            btnShowCloseBy.setTextColor(Color.WHITE);
            btnShowNew.setTextColor(Color.WHITE);
            btnShowPopular.setTextColor(Color.WHITE);
            btnShowNew.setBackgroundColor(Color.TRANSPARENT);
            btnShowPopular.setBackgroundColor(Color.TRANSPARENT);
            btnShowCloseBy.setBackgroundColor(Color.TRANSPARENT);




        } else if (currentOrder[0] == 3) {
            queryRef = myRef.orderByChild("ratingRanking").limitToFirst(500);


            btnShowPopular.setBackgroundResource(R.drawable.tab_button_back);
            btnShowPopular.setTextColor(Color.BLACK);

            btnShowCloseBy.setTextColor(Color.WHITE);
            btnShowNew.setTextColor(Color.WHITE);
            btnShowHotTopic.setTextColor(Color.WHITE);

            btnShowNew.setBackgroundColor(Color.TRANSPARENT);
            btnShowHotTopic.setBackgroundColor(Color.TRANSPARENT);
            btnShowCloseBy.setBackgroundColor(Color.TRANSPARENT);


        } else {



            btnShowCloseBy.setBackgroundResource(R.drawable.tab_button_back);
            btnShowCloseBy.setTextColor(Color.BLACK);
            btnShowNew.setTextColor(Color.WHITE);
            btnShowPopular.setTextColor(Color.WHITE);
            btnShowHotTopic.setTextColor(Color.WHITE);

            btnShowNew.setBackgroundColor(Color.TRANSPARENT);
            btnShowPopular.setBackgroundColor(Color.TRANSPARENT);
            btnShowHotTopic.setBackgroundColor(Color.TRANSPARENT);


            queryRef = myRef.orderByChild("postTimeInverse").limitToFirst(500);

        }
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
                     if(dataSnapshot1.getValue(postClass.class).verifyPost()) {


                    postClass Post = dataSnapshot1.getValue(postClass.class);

                    Log.d("!!!!!", "2");
                    if (currentOrder[0] != 4) {

                        postList.add(Post);


                    } else {
                        if (((Math.abs(Post.getPostLat() - userLat)) < 0.008) && ((Math.abs(Post.getPostLong() - userLong)) < 0.008)) {

                            postList.add(Post);


                        }


                    }
                }

                }
                Log.d("!!!!!", "3");

               allDataAdapter = new MyAdapter(view.getContext(), postList);

                LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
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


    //If location didn't work before, this is called again to get the saved location values
    public void getLocation() {

        SharedPreferences prefs = this.getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);

        Double uLat = Double.longBitsToDouble(prefs.getLong("UserLastLat", Double.doubleToLongBits(1.001)));
        Double uLong = Double.longBitsToDouble(prefs.getLong("UserLastLong", Double.doubleToLongBits(1.001)));
        if (uLat == 1.001 || uLong == 1.001) {
          /*  Intent intent = new Intent(getContext(), UserUniversityInfo.class);
            intent.putExtra("allowBackPress", false);
            startActivity(intent);
            */
        } else {
            //txtProfUniversity.setText(uLat+"_"+uLong);

            postLat = uLat;
            postLong = uLong;
            userLat = uLat;
            userLong = uLong;


        }


    }
//    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener {


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private LayoutInflater inflater;
        private List<postClass> list;
        private List<postClass> copyList;
        Context ctx;

        public MyAdapter(Context context, List<postClass> feedItemList) {
            inflater = LayoutInflater.from(context);
            this.list = feedItemList;
            this.copyList = feedItemList;
        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parrent, int i) {
            View view = inflater.inflate(R.layout.user_post, parrent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder viewHolder, int position) {
           final postClass postItem = postList.get(position);
            //viewHolder.postText.setText(item.getPostText());
            viewHolder.postText.setText(postItem.postText);

            TimeZone tyy = TimeZone.getDefault();


            String eFormatDateandTime = new SimpleDateFormat(" MMM dd ").format((postItem.postTime * 1000) - tyy.getOffset(System.currentTimeMillis()));

            viewHolder.txtPostTime.setText(eFormatDateandTime);


            String strHasLikedPost = postItem.postID+":L";
            String strHasDislikedPost = postItem.postID+":D";
            String strHasSuperLikedPost = postItem.postID+":LL";
            String strHasSuperDislikedPost = postItem.postID+":DD";


            if(currentOrder[0] == 1){
                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back_type1);

            }
            else if(currentOrder[0] == 2){
                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back_type2);

            }
            else if(currentOrder[0] == 3){
                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back_type3);

            }
            else if(currentOrder[0] == 4){
                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back_type4);

            }
            else{
                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back);
            }


            // GradientDrawable gradientDrawable=new GradientDrawable();
           // gradientDrawable.setStroke(4,getResources().getColor(R.color.colorForTextInDarkColor));
          //  viewHolder.itemView.setBackground(gradientDrawable);



            if(postItem.postWasFlagged){
            viewHolder.cViewFlaggedPost.setVisibility(View.VISIBLE);

            }
            else{
                viewHolder.cViewFlaggedPost.setVisibility(View.GONE);


            }


            viewHolder.btnViewFlaggedPost.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){


                    viewHolder.cViewFlaggedPost.setVisibility(View.GONE);



                }

            });



            if (userNotedPostsList.contains(postItem.postID)){
                viewHolder.btnNotePost.setColorFilter(getResources().getColor(R.color.tribaNote));


            }
        else{
                viewHolder.btnNotePost.setColorFilter(Color.WHITE);
            }








            if (userLikedHistoryList.contains(strHasLikedPost) ){
                viewHolder.btnLike.setColorFilter(getResources().getColor(R.color.tribaUp));
                viewHolder.btnDislike.setColorFilter(Color.WHITE);




            }
        else if (userLikedHistoryList.contains(strHasDislikedPost)) {



                viewHolder.btnLike.setColorFilter(Color.WHITE);
                viewHolder.btnDislike.setColorFilter(getResources().getColor(R.color.tribaDown));



            }
        else if ( userLikedHistoryList.contains(strHasSuperLikedPost)) {

                viewHolder.btnLike.setColorFilter(getResources().getColor(R.color.tribaUp));
                viewHolder.btnDislike.setColorFilter(Color.WHITE);




            }
        else if (userLikedHistoryList.contains(strHasSuperDislikedPost) ){



                viewHolder.btnLike.setColorFilter(Color.WHITE);
                viewHolder.btnDislike.setColorFilter(getResources().getColor(R.color.tribaDown));



            }
        else{



                viewHolder.btnLike.setColorFilter(Color.WHITE);
                viewHolder.btnDislike.setColorFilter(Color.WHITE);



            }



            viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                        boolean isNoted = false;
                        //Goes to homeChat
                        Intent intent = new Intent(getActivity(), homeChat.class);
                        intent.putExtra("passedObject", (Serializable) postItem);


                    if(currentOrder[0] == 1){
                        intent.putExtra("backColor", 1);

                    }
                    else if(currentOrder[0] == 2){
                        intent.putExtra("backColor", 2);

                    }
                    else if(currentOrder[0] == 3){
                        intent.putExtra("backColor", 3);


                    }
                    else if(currentOrder[0] == 4){
                        intent.putExtra("backColor", 4);


                    }
                    else{
                        intent.putExtra("backColor", 0);

                    }


                        startActivity(intent);






                }

            });



            viewHolder.btnLike.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                   ImageButton btnLike = viewHolder.btnLike;
                    ImageButton btnDislike = viewHolder.btnDislike;

                  //  int viewColor = ((ColorDrawable) btnLike.getBackground()).getColor();
                    //ColorFilter test = btnLike.getColorFilter();




                   if( userLikedHistoryList.contains(postItem.postID +":L")){
                        userLikedHistoryList.remove(postItem.postID +":L");
                     /*   btnLike.setBackgroundColor(Color.WHITE);
                       btnDislike.setBackgroundColor(Color.WHITE);
*/
                       viewHolder.btnLike.setColorFilter(Color.WHITE);
                       viewHolder.btnDislike.setColorFilter(Color.WHITE);



                       if( userLikedHistoryList.contains(postItem.postID +":L"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":L");
                       }
                           if( userLikedHistoryList.contains(postItem.postID +":LL"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":LL");
                       }
                               if( userLikedHistoryList.contains(postItem.postID +":D"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":D");
                       }
                                   if( userLikedHistoryList.contains(postItem.postID +":DD"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":DD");
                       }
                                       if( userLikedHistoryList.contains(postItem.postID +":+N"))
                                           {
                                               userLikedHistoryList.remove(postItem.postID + ":+N");
                                           }
                                           if (userLikedHistoryList.contains(postItem.postID + ":-N"))
                                               {
                                                   userLikedHistoryList.remove(postItem.postID + ":-N");
                                               }


                                               userLikedHistoryList.add((postItem.postID + ":-N"));

                                               String uLink = "userCreatedPosts/" + postItem.postCreatorID + "/" + postItem.postID + "/ratingBlend";
                                               String pID = postItem.postID;

                                               likeDislikePostObject likePost = new likeDislikePostObject("-N", uLink, postItem.postLocations, userID, pID);


                                               database.getReference().child("LIKESDISLIKES").child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);


                                           }


                   else if( userLikedHistoryList.contains(postItem.postID +":LL")){
                        userLikedHistoryList.remove(postItem.postID +":LL");
                     //   btnLike.setBackgroundColor(Color.WHITE);
                       // btnDislike.setBackgroundColor(Color.WHITE);

                       viewHolder.btnLike.setColorFilter(Color.WHITE);
                       viewHolder.btnDislike.setColorFilter(Color.WHITE);



                       if( userLikedHistoryList.contains(postItem.postID +":L"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":L");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":LL"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":LL");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":D"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":D");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":DD"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":DD");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":+N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":+N");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":-N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":-N");
                        }


                        userLikedHistoryList.add((postItem.postID + ":-N"));

                        String uLink = "userCreatedPosts/" + postItem.postCreatorID + "/" + postItem.postID + "/ratingBlend";
                        String pID = postItem.postID;

                        likeDislikePostObject likePost = new likeDislikePostObject("-N", uLink, postItem.postLocations, userID, pID);


                        database.getReference().child("LIKESDISLIKES").child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);


                    }

                   else if( userLikedHistoryList.contains(postItem.postID +":D")){
                       userLikedHistoryList.remove(postItem.postID +":D");
                    //   btnLike.setBackgroundColor(Color.BLUE);
                     //  btnDislike.setBackgroundColor(Color.WHITE);

                       viewHolder.btnLike.setColorFilter(getResources().getColor(R.color.tribaUp));
                       viewHolder.btnDislike.setColorFilter(Color.WHITE);

                       if( userLikedHistoryList.contains(postItem.postID +":L"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":L");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":LL"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":LL");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":D"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":D");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":DD"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":DD");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":+N"))
                       {
                           userLikedHistoryList.remove(postItem.postID + ":+N");
                       }
                       if (userLikedHistoryList.contains(postItem.postID + ":-N"))
                       {
                           userLikedHistoryList.remove(postItem.postID + ":-N");
                       }


                       userLikedHistoryList.add((postItem.postID + ":LL"));

                       String uLink = "userCreatedPosts/" + postItem.postCreatorID + "/" + postItem.postID + "/ratingBlend";
                       String pID = postItem.postID;

                       likeDislikePostObject likePost = new likeDislikePostObject("LL", uLink, postItem.postLocations, userID, pID);


                       database.getReference().child("LIKESDISLIKES").child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);


                   }
                   else if( userLikedHistoryList.contains(postItem.postID +":DD")){
                       userLikedHistoryList.remove(postItem.postID +":DD");
                    //   btnLike.setBackgroundColor(Color.BLUE);
                     //  btnDislike.setBackgroundColor(Color.WHITE);

                       viewHolder.btnLike.setColorFilter(getResources().getColor(R.color.tribaUp));
                       viewHolder.btnDislike.setColorFilter(Color.WHITE);


                       if( userLikedHistoryList.contains(postItem.postID +":L"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":L");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":LL"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":LL");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":D"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":D");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":DD"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":DD");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":+N"))
                       {
                           userLikedHistoryList.remove(postItem.postID + ":+N");
                       }
                       if (userLikedHistoryList.contains(postItem.postID + ":-N"))
                       {
                           userLikedHistoryList.remove(postItem.postID + ":-N");
                       }


                       userLikedHistoryList.add((postItem.postID + ":LL"));

                       String uLink = "userCreatedPosts/" + postItem.postCreatorID + "/" + postItem.postID + "/ratingBlend";
                       String pID = postItem.postID;

                       likeDislikePostObject likePost = new likeDislikePostObject("LL", uLink, postItem.postLocations, userID, pID);


                       database.getReference().child("LIKESDISLIKES").child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);


                   }

                   else {
                     //  btnLike.setBackgroundColor(Color.BLUE);
                      // btnDislike.setBackgroundColor(Color.WHITE);

                       viewHolder.btnLike.setColorFilter(getResources().getColor(R.color.tribaUp));
                       viewHolder.btnDislike.setColorFilter(Color.WHITE);



                       if( userLikedHistoryList.contains(postItem.postID +":L"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":L");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":LL"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":LL");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":D"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":D");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":DD"))
                       {
                           userLikedHistoryList.remove(postItem.postID +":DD");
                       }
                       if( userLikedHistoryList.contains(postItem.postID +":+N"))
                       {
                           userLikedHistoryList.remove(postItem.postID + ":+N");
                       }
                       if (userLikedHistoryList.contains(postItem.postID + ":-N"))
                       {
                           userLikedHistoryList.remove(postItem.postID + ":-N");
                       }


                       userLikedHistoryList.add((postItem.postID + ":L"));

                       String uLink = "userCreatedPosts/" + postItem.postCreatorID + "/" + postItem.postID + "/ratingBlend";
                       String pID = postItem.postID;

                       likeDislikePostObject likePost = new likeDislikePostObject("L", uLink, postItem.postLocations, userID, pID);


                       database.getReference().child("LIKESDISLIKES").child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);


                   }







                }

            });

            viewHolder.btnDislike.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    ImageButton btnLike = viewHolder.btnLike;
                    ImageButton btnDislike = viewHolder.btnDislike;

                    //  int viewColor = ((ColorDrawable) btnLike.getBackground()).getColor();
                    //ColorFilter test = btnLike.getColorFilter();


                    if( userLikedHistoryList.contains(postItem.postID +":L")){
                        userLikedHistoryList.remove(postItem.postID +":L");
                       // btnLike.setBackgroundColor(Color.WHITE);
                       // btnDislike.setBackgroundColor(Color.BLUE);

                        viewHolder.btnLike.setColorFilter(Color.WHITE);
                        viewHolder.btnDislike.setColorFilter(getResources().getColor(R.color.tribaDown));


                        if( userLikedHistoryList.contains(postItem.postID +":L"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":L");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":LL"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":LL");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":D"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":D");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":DD"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":DD");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":+N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":+N");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":-N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":-N");
                        }


                        userLikedHistoryList.add((postItem.postID + ":DD"));

                        String uLink = "userCreatedPosts/" + postItem.postCreatorID + "/" + postItem.postID + "/ratingBlend";
                        String pID = postItem.postID;

                        likeDislikePostObject likePost = new likeDislikePostObject("DD", uLink, postItem.postLocations, userID, pID);


                        database.getReference().child("LIKESDISLIKES").child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);


                    }


                    else if( userLikedHistoryList.contains(postItem.postID +":LL")){
                        userLikedHistoryList.remove(postItem.postID +":LL");
                      //  btnLike.setBackgroundColor(Color.WHITE);
                      //  btnDislike.setBackgroundColor(Color.BLUE);

                        viewHolder.btnLike.setColorFilter(Color.WHITE);
                        viewHolder.btnDislike.setColorFilter(getResources().getColor(R.color.tribaDown));


                        if( userLikedHistoryList.contains(postItem.postID +":L"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":L");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":LL"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":LL");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":D"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":D");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":DD"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":DD");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":+N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":+N");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":-N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":-N");
                        }


                        userLikedHistoryList.add((postItem.postID + ":DD"));

                        String uLink = "userCreatedPosts/" + postItem.postCreatorID + "/" + postItem.postID + "/ratingBlend";
                        String pID = postItem.postID;

                        likeDislikePostObject likePost = new likeDislikePostObject("DD", uLink, postItem.postLocations, userID, pID);


                        database.getReference().child("LIKESDISLIKES").child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);


                    }

                    else if( userLikedHistoryList.contains(postItem.postID +":D")){
                        userLikedHistoryList.remove(postItem.postID +":D");
                        //btnLike.setBackgroundColor(Color.WHITE);
                       // btnDislike.setBackgroundColor(Color.WHITE);

                        viewHolder.btnLike.setColorFilter(Color.WHITE);
                        viewHolder.btnDislike.setColorFilter(Color.WHITE);


                        if( userLikedHistoryList.contains(postItem.postID +":L"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":L");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":LL"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":LL");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":D"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":D");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":DD"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":DD");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":+N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":+N");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":-N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":-N");
                        }


                        userLikedHistoryList.add((postItem.postID + ":+N"));

                        String uLink = "userCreatedPosts/" + postItem.postCreatorID + "/" + postItem.postID + "/ratingBlend";
                        String pID = postItem.postID;

                        likeDislikePostObject likePost = new likeDislikePostObject("+N", uLink, postItem.postLocations, userID, pID);


                        database.getReference().child("LIKESDISLIKES").child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);


                    }
                    else if( userLikedHistoryList.contains(postItem.postID +":DD")){
                        userLikedHistoryList.remove(postItem.postID +":DD");
                       // btnLike.setBackgroundColor(Color.WHITE);
                       // btnDislike.setBackgroundColor(Color.WHITE);
                        viewHolder.btnLike.setColorFilter(Color.WHITE);
                        viewHolder.btnDislike.setColorFilter(Color.WHITE);



                        if( userLikedHistoryList.contains(postItem.postID +":L"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":L");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":LL"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":LL");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":D"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":D");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":DD"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":DD");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":+N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":+N");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":-N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":-N");
                        }


                        userLikedHistoryList.add((postItem.postID + ":+N"));

                        String uLink = "userCreatedPosts/" + postItem.postCreatorID + "/" + postItem.postID + "/ratingBlend";
                        String pID = postItem.postID;

                        likeDislikePostObject likePost = new likeDislikePostObject("+N", uLink, postItem.postLocations, userID, pID);


                        database.getReference().child("LIKESDISLIKES").child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);


                    }

                    else {
                      //  btnLike.setBackgroundColor(Color.WHITE);
                      //  btnDislike.setBackgroundColor(Color.BLUE);
                        viewHolder.btnLike.setColorFilter(Color.WHITE);
                        viewHolder.btnDislike.setColorFilter(getResources().getColor(R.color.tribaDown));



                        if( userLikedHistoryList.contains(postItem.postID +":L"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":L");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":LL"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":LL");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":D"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":D");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":DD"))
                        {
                            userLikedHistoryList.remove(postItem.postID +":DD");
                        }
                        if( userLikedHistoryList.contains(postItem.postID +":+N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":+N");
                        }
                        if (userLikedHistoryList.contains(postItem.postID + ":-N"))
                        {
                            userLikedHistoryList.remove(postItem.postID + ":-N");
                        }


                        userLikedHistoryList.add((postItem.postID + ":D"));

                        String uLink = "userCreatedPosts/" + postItem.postCreatorID + "/" + postItem.postID + "/ratingBlend";
                        String pID = postItem.postID;

                        likeDislikePostObject likePost = new likeDislikePostObject("D", uLink, postItem.postLocations, userID, pID);


                        database.getReference().child("LIKESDISLIKES").child(postItem.postID).child("LDNOTES").child(userID).setValue(likePost);


                    }







                }

            });

            viewHolder.btnNotePost.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    final globalClass globalVariable = (globalClass) globalClass.getInstance();

                    if(userNotedPostsList.contains(postItem.postID)){

                        database.getReference().child("notedPosts").child(userID).child(postItem.postID).setValue(null);
                        //used when user is getting noted history for the box they're in

                        database.getReference().child("UsersNotedPostsIDList").child(userID).child(postItem.postID).setValue(null);
                        userNotedPostsList.remove(postItem.postID);
                       // globalVariable.setNotedPostsNumber(globalVariable.getNotedPostsNumber()- 1);
                      notedNumbers.setNumOfNotedPosts(notedNumbers.getNumOfNotedPosts() - 1);
                        viewHolder.btnNotePost.setColorFilter(Color.WHITE);

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




                           // postSpecDetails notedPostSpecs = new postSpecDetails(true, false, 0L,0L, "", 0);

                            //notePostObject notedPost = new notePostObject(userID, postItem.postID, postItem.postLocations, notedPostSpecs);
                            database.getReference().child("notedPosts").child(userID).child(postItem.postID).setValue(postItem);
                            database.getReference().child("UsersNotedPostsIDList").child(userID).child(postItem.postID).setValue(postItem.postID);

                            viewHolder.btnNotePost.setColorFilter(getResources().getColor(R.color.tribaNote));

                            userNotedPostsList.add(postItem.postID);

                /*ref?.child("notedPostsHistory").child((Auth.auth().currentUser?.uid)!).child((passedObject?.postID)!).setValue(post);
                 */
                            //globalVariable.setNotedPostsNumber(globalVariable.getNotedPostsNumber()+ 1);
                            notedNumbers.setNumOfNotedPosts(notedNumbers.getNumOfNotedPosts() + 1);
                        }

                    }


                }

            });

            viewHolder.btnReport.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    MyDialog = new Dialog(getContext());

                    MyDialog.setContentView(R.layout.report_post_popover);


                    MyDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);







                    //loads the "Post" button and the button's actions that show up in the layout
                    Button btnChoosePostReason1 = (Button) MyDialog.findViewById(R.id.btnBreaksRule1) ;
                    final Button btnChoosePostReason2 = (Button) MyDialog.findViewById(R.id.btnBreaksRule2) ;

                    TextView txtTitle = (TextView) MyDialog.findViewById(R.id.textView8) ;
                    TextView txtReportFor = (TextView) MyDialog.findViewById(R.id.textView9) ;

                    if(isModerator == true) {
                        txtReportFor.setText("Remove For: ");
                    }
                    else{
                        txtReportFor.setText("Report For: ");


                    }
                        txtTitle.setText(postItem.postText);
                    btnChoosePostReason1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {




                            if(isModerator == true){



                                new AlertDialog.Builder(getContext()).setTitle("Remove Post").setMessage("reason: political")
                                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // navigate to settings
                                        // startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                                        long cTime = System.currentTimeMillis();


                                        TimeZone tyy = TimeZone.getDefault();

                                        long unixTime = (cTime + tyy.getOffset(System.currentTimeMillis()))/1000;

                                        postSpecDetails postSpecss = new postSpecDetails(false, postItem.postSpecs.hasBeenCleared,  postItem.postSpecs.postTime, unixTime, user.getUid(), 1 );

                                        adminPostSpecs admnPostSpecs = new adminPostSpecs(postItem, unixTime, 1, user.getUid(), postItem.getPostCreatorID());
                                        strikedPostObject strikedPost = new strikedPostObject("moderator", user.getUid(), 1, postItem);


                                        nRef.child("userCreatedPosts").child(postItem.postCreatorID).child(postItem.postID).child("postSpecs").setValue(postSpecss);
                                        nRef.child("reportedPostIndex").child(postItem.postID).child("postStatus").setValue("REMOVED");
                                        nRef.child("strikedPostHistory").child(postItem.postCreatorID).child(postItem.postID).setValue(strikedPost);

                                        nRef.child("Users").child(postItem.postCreatorID).child("showUserStrikeAlert").setValue(true);


                                        //This one goes to the user's admin history
                                        nRef.child("modHistory").child(user.getUid()).child(postItem.postID).setValue(admnPostSpecs);
                                        Toast.makeText(getContext(), "Post Removed", Toast.LENGTH_LONG).show();

                                        MyDialog.cancel();



                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // leave?
                                        // MyActivity.this.onBackPressed();
                                        MyDialog.cancel();


                                    }
                                }).show();



                            }
                            else{

                                reportPostObject reportPost = new reportPostObject(1, postItem,user.getUid() );

                                nRef.child("reportedPostIndex").child(postItem.postID).child("latestPost").setValue(reportPost);

                                // ref?.child("reportedPostIndex").child((passedObject?.postID)!).child("testToBEDeleted").setValue(reportTypeList[indexPath.row])
                                Toast.makeText(getContext(), "Post Reported", Toast.LENGTH_LONG).show();

                            }









                            //Loads the post functiion and passes the post string as well as the post type
                          //  postText(btnChooseChat.getText().toString(), "CHAT");
                            // This part removes the pop-up
                            MyDialog.cancel();

                        }
                    });


                    btnChoosePostReason2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {




                            if(isModerator == true){



                                new AlertDialog.Builder(getContext()).setTitle("Remove Post").setMessage("reason: political")
                                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // navigate to settings
                                                // startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                                                long cTime = System.currentTimeMillis();


                                                TimeZone tyy = TimeZone.getDefault();

                                                long unixTime = (cTime + tyy.getOffset(System.currentTimeMillis()))/1000;

                                                postSpecDetails postSpecss = new postSpecDetails(false, postItem.postSpecs.hasBeenCleared,  postItem.postSpecs.postTime, unixTime, user.getUid(), 2 );

                                                adminPostSpecs admnPostSpecs = new adminPostSpecs(postItem, unixTime, 2, user.getUid(), postItem.getPostCreatorID());
                                                strikedPostObject strikedPost = new strikedPostObject("moderator", user.getUid(), 2, postItem);


                                                nRef.child("userCreatedPosts").child(postItem.postCreatorID).child(postItem.postID).child("postSpecs").setValue(postSpecss);
                                                nRef.child("reportedPostIndex").child(postItem.postID).child("postStatus").setValue("REMOVED");
                                                nRef.child("strikedPostHistory").child(postItem.postCreatorID).child(postItem.postID).setValue(strikedPost);

                                                nRef.child("Users").child(postItem.postCreatorID).child("showUserStrikeAlert").setValue(true);


                                                //This one goes to the user's admin history
                                                nRef.child("modHistory").child(user.getUid()).child(postItem.postID).setValue(admnPostSpecs);
                                                Toast.makeText(getContext(), "Post Removed", Toast.LENGTH_LONG).show();

                                                MyDialog.cancel();



                                            }
                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // leave?
                                        // MyActivity.this.onBackPressed();
                                        MyDialog.cancel();


                                    }
                                }).show();



                            }
                            else{

                                reportPostObject reportPost = new reportPostObject(2, postItem,user.getUid() );

                                nRef.child("reportedPostIndex").child(postItem.postID).child("latestPost").setValue(reportPost);

                                // ref?.child("reportedPostIndex").child((passedObject?.postID)!).child("testToBEDeleted").setValue(reportTypeList[indexPath.row])
                                Toast.makeText(getContext(), "Post Reported", Toast.LENGTH_LONG).show();

                            }








                            //Loads the post functiion and passes the post string as well as the post type
                            //  postText(btnChooseChat.getText().toString(), "CHAT");
                            // This part removes the pop-up
                            MyDialog.cancel();

                        }
                    });

                    MyDialog.show();





                }

            });



        }


        @Override
        public int getItemCount() {
            if (list == null)
                return 0;
            else

                Log.d("!!!!!!!!!!", "&&&&&&&&&");
            Log.d("!!!!!!!!!!", "&&&&&&&&&");
            Log.d("!!!!!!!!!!", list.size()+"");
            Log.d("!!!!!!!!!!", "&&&&&&&&&");
            Log.d("!!!!!!!!!!", "&&&&&&&&&");
                return list.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView postText;
            TextView txtPostTime;
            ConstraintLayout cViewFlaggedPost;


            Button btnReport;
            Button btnViewFlaggedPost;

            ImageButton btnLike;
            ImageButton btnDislike;
            ImageButton btnNotePost;

            View mView;


            public MyViewHolder(View itemView) {
                super(itemView);
                //name = (TextView) itemView.findViewById(R.id.tvname);
                postText = (TextView) itemView.findViewById(R.id.userText);
                txtPostTime = (TextView) itemView.findViewById(R.id.txtUserName);

                btnReport = (Button) itemView.findViewById(R.id.btnReportPost);
                btnLike = (ImageButton) itemView.findViewById(R.id.btnUPLike);
                btnDislike = (ImageButton) itemView.findViewById(R.id.btnUPDislike);
                btnViewFlaggedPost = (Button) itemView.findViewById(R.id.btnViewFlaggedPost);

                btnNotePost = (ImageButton) itemView.findViewById(R.id.btnNotePost);
                cViewFlaggedPost = (ConstraintLayout) itemView.findViewById(R.id.constraintLayoutViewFlagged);

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
    public void onResume(){
        super.onResume();
        // put your code here...





        if(globalVarClass.getShouldReset()) {





            globalVarClass.setShouldReset(false);
            String passedUID = globalVarClass.getPostIDD();
            //userNotedPostsList.clear();
            if(globalVarClass.getAddOrSubtractIDD().equals("ADD")){
                userNotedPostsList.add(passedUID);
                displayPosts();


            }
            else{
                userNotedPostsList.remove(passedUID);
                displayPosts();

            }

            //loadNotedPosts();

           /* postList.clear();
            allDataAdapter = new MyAdapter(view.getContext(), postList);

            LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            postRecyclerview.setLayoutManager(llm);

            postRecyclerview.setAdapter(allDataAdapter);
            allDataAdapter.notifyDataSetChanged();

            */
        }

    }


    public void checkUname(){



        SharedPreferences userInfoPref = this.getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);
        String uName = userInfoPref.getString("uName", "");
        if (!uName.equals("")) {
            userName = uName;
            loadPostLikeHistory();
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

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
                    // editor.putString("yaya", dataSnapshot.child("userRating").getValue().toString());
                    //  editor.putLong("b",(long) dataSnapshot.child("userRating").getValue());

                    //etxtUsername.setText(Username);
                    //alreadyHasUsername = true;
                    //uName = Username;

                    String facebookUserId = "";
                    //SharedPreferences.Editor editor = getSharedPreferences("USERINFO", MODE_PRIVATE).edit();









                    if(  dataSnapshot.child("userName").getValue() != null){
                        Log.d("HEEEEEEEEY", "GONE CATCH YOU SLEEPIN");

                        SharedPreferences userDetails = getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);
                        SharedPreferences.Editor edittt = userDetails.edit();
                        edittt.putString("uName ",dataSnapshot.child("userName").getValue().toString());

                        edittt.apply();
                        loadPostLikeHistory();
                        return;



                    }
                    else{
                        editor.apply();

                        Intent intent = new Intent(getContext(), settingUsername.class);
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
