package com.davida.tatwpbnw;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by davida on 2/15/18.
 */


/*
READ THIS ONE LAST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!



This is the fragment used for user profile information. Nothing here is new code. Everything is reused from other files and functions.

 */

public class userProfileFrag extends Fragment {

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
    DatabaseReference nRef;

    private RecyclerView postRecyclerview;


    public String userID;


    FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;


    Button btnShowNotes;
    Button btnShowHistory;
    ImageView imgProfilePic;

    private notedAdapter allDataAdapter;

    final List<postClass> postListNoted = new ArrayList<postClass>();
    final List<postClass> postListHistory = new ArrayList<postClass>();


    // private userProfileFrag.MyPostHistoryAdapter postHistoryAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getActivity().setTitle("");

        setHasOptionsMenu(true);


        SharedPreferences prefs = this.getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);


        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        String currentLoc = "";

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

            BigDecimal bd = new BigDecimal(userLat).setScale(2, RoundingMode.DOWN);
            postLat = bd.doubleValue();
            BigDecimal bd2 = new BigDecimal(userLong).setScale(2, RoundingMode.DOWN);
            postLong = bd2.doubleValue();
            currentLoc = ((postLat) + "_" + (postLong)).replaceAll("\\.", "|");

        }

        myRef = database.getReference();
        nRef = database.getReference();

     displayNotedPosts();
     modChecker();

//displayHistoryPosts();

    }


    public void modChecker(){


        nRef.child("1A_TribaSystemData").child("karmaModLevel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("C", "minrating: " + " uRting: ");
                Log.d("C", "minrating: " + " uRting: ");
                Log.d("C", "minrating: " + " uRting: ");
                Log.d("C", "minrating: " + " uRting: ");
                Log.d("C", "minrating: " + " uRting: ");
                Log.d("C", "minrating: " + " uRting: ");


                if(dataSnapshot.getValue() != null) {

                    long uMinRating =  (Long) dataSnapshot.getValue();

                    SharedPreferences userInfoPref = getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);
                    String uAccessLevel = userInfoPref.getString("uName", "FUUUUCK");
                   // Long uRating = userInfoPref.getLong("KKK", 0);
                    int uRating = userInfoPref.getInt("userStrikes", 29);



                    SharedPreferences myPrefs;
                    myPrefs = getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);
                    long StoredValue=myPrefs.getLong("userRatingg", 0);



                    if (!uAccessLevel.equals("NORMAL") ) {

                        if(uRating > uMinRating){

                            adminObject proposedMod = new adminObject(user.getUid(), user.getDisplayName(), userLat, userLong);





                            nRef.child("modRequests").child("freeRange").child(userID).setValue(proposedMod);



                        }


                    }





                }



            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.iconGoToSettings) {

            Intent intent = new Intent(getActivity(), settingsActivity.class);

            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_tab, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.user_profile_fragment, container, false);
        TextView txtRealName = (TextView) view.findViewById(R.id.txtUserProfileName);
        TextView txtUserName = (TextView) view.findViewById(R.id.txtUserProfileName2);


        MyDialog = new Dialog(getContext());


        View viewSeparator = (View) view.findViewById(R.id.viewSeparator3);
        viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaPurple));


        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{getResources().getColor(R.color.tribaPurple),Color.BLACK, Color.BLACK, Color.BLACK,   Color.BLACK, getResources().getColor(R.color.tribaPurple)});


       // view.setBackground(gradientDrawable);


        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // navigation.setItemBackgroundResource(R.color.zxing_transparent);
       // navigation.setBackgroundColor( ColorUtils.blendARGB(getResources().getColor(R.color.tribaPurple), Color.BLACK, 0.5F));


        //Loads the toolbar
        MyDialog = new Dialog(getContext());
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.myProfile_Toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        postRecyclerview = view.findViewById(R.id.profileList);

        btnShowNotes = (Button) view.findViewById(R.id.btnShowNotesProfile);
        btnShowHistory = (Button) view.findViewById(R.id.btnShowMyHistoryProfile);

        btnShowNotes.setBackgroundResource(R.drawable.tab_button_back);
        btnShowNotes.setTextColor(Color.BLACK);
        btnShowHistory.setTextColor(Color.WHITE);
        btnShowHistory.setBackgroundColor(Color.TRANSPARENT);

       final TextView txtTabDescription = (TextView) view.findViewById(R.id.txtTabDescription);

        txtTabDescription.setText("Your Important Posts");


        btnShowNotes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                displayNotedPosts();

                btnShowNotes.setBackgroundResource(R.drawable.tab_button_back);
                btnShowNotes.setTextColor(Color.BLACK);
                btnShowHistory.setTextColor(Color.WHITE);
                btnShowHistory.setBackgroundColor(Color.TRANSPARENT);
                txtTabDescription.setText("Your Important Posts");


                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{getResources().getColor(R.color.tribaPurple),Color.BLACK, Color.BLACK, Color.BLACK,   Color.BLACK, getResources().getColor(R.color.tribaPurple)});


              //  view.setBackground(gradientDrawable);

                BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
                // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                // navigation.setItemBackgroundResource(R.color.zxing_transparent);
             //   navigation.setBackgroundColor( ColorUtils.blendARGB(getResources().getColor(R.color.tribaPurple), Color.BLACK, 0.5F));

                View viewSeparator = (View) view.findViewById(R.id.viewSeparator3);
                viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaPurple));


            }
        });


        btnShowHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                displayHistoryPosts();

                btnShowHistory.setBackgroundResource(R.drawable.tab_button_back);
                btnShowHistory.setTextColor(Color.BLACK);
                btnShowNotes.setTextColor(Color.WHITE);
                btnShowNotes.setBackgroundColor(Color.TRANSPARENT);
                txtTabDescription.setText("Posts You've Made");

                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{getResources().getColor(R.color.tribaGreen),Color.BLACK, Color.BLACK, Color.BLACK,   Color.BLACK, getResources().getColor(R.color.tribaGreen)});


             //   view.setBackground(gradientDrawable);

                BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
                // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                // navigation.setItemBackgroundResource(R.color.zxing_transparent);
              //  navigation.setBackgroundColor( ColorUtils.blendARGB(getResources().getColor(R.color.tribaGreen), Color.BLACK, 0.5F));

                View viewSeparator = (View) view.findViewById(R.id.viewSeparator3);
                viewSeparator.setBackgroundColor(getResources().getColor(R.color.tribaGreen));


            }
        });

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
       /* imgProfilePic = (ImageView) view.findViewById(R.id.imgProfileFrag);
        String photoUrl = "https://graph.facebook.com/" + fbPhotoID + "/picture?height=500";
        Picasso.with(getContext()).load(photoUrl).into(imgProfilePic);
        imgProfilePic.setBackgroundColor(Color.GRAY);

        */
// construct the URL to the profile picture, with a custom height
// alternatively, use '?type=small|medium|large' instead of ?height=

// (optional) use Picasso to download and show to image

        // Uri photoUrl = user.getPhotoUrl();
        //Uri photoUrl = user.getPhotoUrl();




        txtRealName.setText(user.getDisplayName());

        SharedPreferences userInfoPref = this.getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);
        String uName = userInfoPref.getString("uName", "");
        if (!uName.equals("")) {
            userName = uName;
            txtUserName.setText(userName);

        } else {


            Intent intent = new Intent(getContext(), settingUsername.class);
            intent.putExtra("allowBackPress", false);
            startActivity(intent);
        }





        Button btnGtHistory = (Button) view.findViewById(R.id.btnGoToUserHistory);
        btnGtHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), userPostHistory.class);
                intent.putExtra("allowBackPress", false);
                startActivity(intent);


            }
        });


        Button btnChooseTopic = (Button) view.findViewById(R.id.btnRemoveAccount);
        btnChooseTopic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE).edit();
                editor.putString("uName", null);
                editor.apply();

                Intent intent = new Intent(getContext(), settingUsername.class);
                intent.putExtra("allowBackPress", false);
                startActivity(intent);


            }
        });


        return view;

    }


    public void displayNotedPosts() {

        Query queryRef;
        //  queryRef = myRef.orderByChild("postIsActive").equalTo(true);


/*
        if(showTopPosts == true){
            queryRefRating = myRef.orderByChild("ratingBlend");

        }
        else{
            queryRefRating = myRef;
        }
*/


        //btnShowNotes.setBackgroundColor(Color.BLUE);
        // btnShowHistory.setBackgroundColor(Color.WHITE);
        postListNoted.clear();

        myRef.child("notedPosts").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postListNoted.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    postClass Post = dataSnapshot1.getValue(postClass.class);
                    postListNoted.add(Post);



                }

               // allDataAdapter = new notedAdapter(getContext(), postListNoted);
             notedAdapter you = new notedAdapter(view.getContext(), postListNoted);



                LinearLayoutManager llm = new LinearLayoutManager(view.getContext());



                llm.setOrientation(LinearLayoutManager.VERTICAL);
                postRecyclerview.setLayoutManager(llm);


                postRecyclerview.setAdapter(you);


                you.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void displayHistoryPosts() {

        Query queryRef;
        // queryRef = myRef.orderByChild("postIsActive").equalTo(true);


/*
        if(showTopPosts == true){
            queryRefRating = myRef.orderByChild("ratingBlend");

        }
        else{
            queryRefRating = myRef;
        }
*/

        // btnShowNotes.setBackgroundColor(Color.WHITE);
        //  btnShowHistory.setBackgroundColor(Color.BLUE);
        DatabaseReference historyRef = myRef.child("userCreatedPosts").child(userID);

        queryRef = historyRef.orderByChild("postTimeInverse");


        postListNoted.clear();

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postListHistory.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    postClass Post = dataSnapshot1.getValue(postClass.class);
                    postListHistory.add(Post);


                }


                MyPostHistoryAdapter postHistoryAdapter = new userProfileFrag.MyPostHistoryAdapter(view.getContext(), postListHistory);

                LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                postRecyclerview.setLayoutManager(llm);

                postRecyclerview.setAdapter(postHistoryAdapter);
                postHistoryAdapter.notifyDataSetChanged();


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





    public class MyPostHistoryAdapter extends RecyclerView.Adapter<userProfileFrag.MyPostHistoryAdapter.MyViewHolder> {
        private LayoutInflater inflater;
        private List<postClass> list;
        private List<postClass> copyList;
        Context ctx;

        public MyPostHistoryAdapter(Context context, List<postClass> feedItemList) {
            inflater = LayoutInflater.from(context);
            this.list = feedItemList;
            this.copyList = feedItemList;
        }



        @Override
        public userProfileFrag.MyPostHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parrent, int i) {
            View view = inflater.inflate(R.layout.profile_tab_my_post_cell, parrent, false);
            userProfileFrag.MyPostHistoryAdapter.MyViewHolder holder = new userProfileFrag.MyPostHistoryAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final userProfileFrag.MyPostHistoryAdapter.MyViewHolder viewHolder, int position) {
            final postClass postItem = postListHistory.get(position);
            //viewHolder.postText.setText(item.getPostText());
            viewHolder.postText.setText(postItem.postText);
           // viewHolder.txtActiveStatus.setText(postItem.postSpecs.isActive + "");

            if(postItem.postSpecs.isActive){
                viewHolder.txtActiveStatus.setText("active");
            }
            else{
                viewHolder.txtActiveStatus.setText("inactive");

            }

                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back_type2);



            viewHolder.btnRemovePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

  /*
            var pID = postChatListHistory[(indexPath?.row)!].postID
            var postSPecs = postChatListHistory[(indexPath?.row)!].postSpecs
            let currentTime:CLong = (TimeZone.current.secondsFromGMT() + CLong(Date().timeIntervalSince1970))

            let postSpecs:[String : AnyObject] = [
            "isActive":false as AnyObject,
            "hasBeenCleared":postSPecs?.hasBeenCleared as AnyObject,
                    "postTime":postSPecs?.postTime as AnyObject,
                    "postRemovalTime":currentTime as AnyObject,
                    "postRemovalReason":0 as AnyObject,
            "postRemover":"selfRemoved" as AnyObject


        ]

            ref?.child("userCreatedPosts").child(userID).child(pID).child("postSpecs").setValue(postSpecs);
            ref?.child("reportedPostIndex").child(pID).child("postStatus").setValue("REMOVED")

            cell.lblPostText1.text = "inactive"
            cell.btnDeactivate.isHidden = true
            postChatListHistory[(indexPath?.row)!].postSpecs?.isActive = false
*/

                    notedNumbers.setNumOfNotedPosts(notedNumbers.getNumOfNotedPosts() - 1);

                    String pID = postItem.postID;
                    postSpecDetails postSPecs = postItem.postSpecs;
                    long currentTime = System.currentTimeMillis();
                    long fakeLongEndTime = 0;


                    TimeZone tyy = TimeZone.getDefault();
                    long unixTime = (currentTime + tyy.getOffset(System.currentTimeMillis()))/1000;


                    postSpecDetails newPostSpecs = new postSpecDetails(false, postSPecs.hasBeenCleared, postSPecs.postTime, unixTime, "selfRemoved",  0);

                    myRef.child("userCreatedPosts").child(userID).child(pID).child("postSpecs").setValue(newPostSpecs);
                    myRef.child("reportedPostIndex").child(pID).child("postStatus").setValue("REMOVED");

                    viewHolder.txtActiveStatus.setText("inactive");
                    viewHolder.btnRemovePost.setVisibility(View.INVISIBLE);
                    postItem.postSpecs.setActive(false);



                }

            });

            if(!postItem.postSpecs.isActive){

                viewHolder.btnRemovePost.setVisibility(View.INVISIBLE);
            }
            else{
                viewHolder.btnRemovePost.setVisibility(View.VISIBLE);

            }


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //Goes to homeChat
                    Intent intent = new Intent(getActivity(), homeChat.class);
                    intent.putExtra("passedObject", (Serializable) postItem);
                    intent.putExtra("backColor", 2);

                    startActivity(intent);


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
            TextView txtActiveStatus;
            Button btnRemovePost;


            View mView;


            public MyViewHolder(View itemView) {
                super(itemView);
                //name = (TextView) itemView.findViewById(R.id.tvname);
                postText = (TextView) itemView.findViewById(R.id.txtMyProfileHistoryCellText);
                txtActiveStatus = (TextView) itemView.findViewById(R.id.txtMyProfileHistoryActiveStatus);

                btnRemovePost = (Button) itemView.findViewById(R.id.btnMyHistoryDeactivatePost);



                //The report button




            }
        }
    }



    public class notedAdapter extends RecyclerView.Adapter<notedAdapter.MyViewHolder> {
        private LayoutInflater inflater;
        private List<postClass> list;
        private List<postClass> copyList;
        Context ctx;

        public notedAdapter(Context context, List<postClass> feedItemList) {
            inflater = LayoutInflater.from(context);
            this.list = feedItemList;
            this.copyList = feedItemList;
        }


        @Override
        public notedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parrent, int i) {
            View view = inflater.inflate(R.layout.profile_tab_noted_posts_cell, parrent, false);
            notedAdapter.MyViewHolder holder = new notedAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final notedAdapter.MyViewHolder viewHolder, final int position) {
            final postClass postItem = postListNoted.get(position);
            //viewHolder.postText.setText(item.getPostText());
            viewHolder.postText.setText(postItem.postText);

                viewHolder.itemView.setBackgroundResource(R.drawable.custom_back_type1);



            viewHolder.btnRemovePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

/*
                    self.ref?.child("notedPosts").child(self.userID).child(self.postChatListNoted[(indexPath?.row)!].postID).setValue(nil);
                    self.ref?.child("UsersNotedPostsIDList").child(self.userID).child(self.postChatListNoted[(indexPath?.row)!].postID).setValue(nil)

                    //used when user is getting noted history for the box they're in
                    //self.ref?.child("notedPostsHistory").child((Auth.auth().currentUser?.uid)!).child(self.postChatList[sender.tag].postID).child("postSpecs").child("isActive").setValue(false);


                    //THIS LINE MIGHT BE IMPORTANT, WRITTEN 6/2/18
                    // sharedData.ModelData.bubbleLats.append(self.postChatList[sender.tag].postID)


                    //Line from old way where object was used
                    //  lstRemovedPosts.append(postChatList[sender.tag])

                    //  self.lstRemovedPosts.append(self.postChatList[sender.tag].postID)


                    //  self.postChatListNoted.remove(at: sender.tag)

                    //removes from personal list on device
                    self.postChatListNoted.remove(at: (indexPath?.row)!)


                    self.collectionViewNotedPosts.deleteItems(at: [indexPath!])

                    //sets reset when they go back to the explore tab
                    UserDefaults.standard.set(true , forKey: "resetOnAppear")

                    notedNumbers.numOfNotedPosts -= 1

                            */



                  myRef.child("notedPosts").child(userID).child(postItem.postID).setValue(null);
                 myRef.child("UsersNotedPostsIDList").child(userID).child(postItem.postID).setValue(null);

                    postListNoted.remove(postItem);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,postListNoted.size());



                }

            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //Goes to homeChat
                    Intent intent = new Intent(getActivity(), homeChat.class);
                    intent.putExtra("passedObject", (Serializable) postItem);
                    intent.putExtra("backColor", 1);

                    startActivity(intent);


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

            Button btnRemovePost;
          ;

            View mView;


            public MyViewHolder(View itemView) {
                super(itemView);
                //name = (TextView) itemView.findViewById(R.id.tvname);
                postText = (TextView) itemView.findViewById(R.id.txtNotedCellProfile);

                btnRemovePost = (Button) itemView.findViewById(R.id.btnRemoveNotedPost);



                //The report button



            }
        }


    }

}