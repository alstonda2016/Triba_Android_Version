package com.davida.tatwpbnw;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by davida on 3/20/18.
 */

public class searchFrag extends Fragment {




    View view;
    Dialog MyDialog;
    Double userLat;
    Double userLong;
    String userName;
    Double postLat;
    Double postLong;
    List<bubbleClass> lstSelectedBubbles = new ArrayList<bubbleClass>();
    int createdClasses = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getActivity().setTitle("");

        setHasOptionsMenu(true);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        SharedPreferences prefs = this.getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);

        String currentLoc = "";

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

            BigDecimal bd = new BigDecimal(userLat).setScale(2, RoundingMode.DOWN);
            postLat = bd.doubleValue();
            BigDecimal bd2 = new BigDecimal(userLong).setScale(2, RoundingMode.DOWN);
            postLong = bd2.doubleValue();
            currentLoc = ((postLat)+"_"+(postLong)).replaceAll("\\.", "|");

        }



    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.addEvent){
            MyDialog.setContentView(R.layout.write_post_popup);
            // MyDialog.setTitle("yoyoyo");
            Button btnChoosePost = (Button) MyDialog.findViewById(R.id.btnWritePostUpload) ;
            btnChoosePost.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    EditText btnChooseChat = (EditText) MyDialog.findViewById(R.id.etxtPostText) ;
                    postText(btnChooseChat.getText().toString(), "CHAT");

                    MyDialog.cancel();
                    // startActivity(new Intent(getContext(), writePost.class));

                }
            });




            MyDialog.show();        }
        return super.onOptionsItemSelected(item);



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.search_frag, container, false);

        MyDialog = new Dialog(getContext());
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.postSearch_Toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        SharedPreferences userInfoPref = this.getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);
        String uName = userInfoPref.getString("uName", "");
        if(!uName.equals("")){
            userName = uName;
            //txtUserName.setText(userName);

        }
        else{
            Intent intent = new Intent(getContext(), settingUsername.class);
            intent.putExtra("allowBackPress", false);
            startActivity(intent);
        }



        return view;

    }



    public void postText(String passedText, String passedTextType){
        long unixTime = System.currentTimeMillis();
        long fakeLongEndTime = 0;


        String postID = "P"+ FirebaseDatabase.getInstance().getReference().push().getKey();




        if(passedText.equals("")){
            Toast.makeText(getContext(), "Post is empty", Toast.LENGTH_LONG).show();
        }
        else if(userLong == null){
            getLocation();
        }


        else {

            createdClasses = 1;
            //  editor.putInt("CreNum", createdClasses++);
            // editor.putLong("creDay",  System.currentTimeMillis() );
            // editor.apply();

            //DecimalFormat newFormat = new DecimalFormat("######.##");
            //postLat =  Double.valueOf(newFormat.format(userLat));
            // postLong =  Double.valueOf(newFormat.format(userLong));
            BigDecimal bd = new BigDecimal(userLat).setScale(2, RoundingMode.DOWN);
            postLat = bd.doubleValue();
            BigDecimal bd2 = new BigDecimal(userLong).setScale(2, RoundingMode.DOWN);
            postLong = bd2.doubleValue();
           /* BigDecimal bd2 = new BigDecimal(userLong).setScale(2, RoundingMode.HALF_EVEN);
            postLong = bd2.doubleValue();
*/


            String postText = passedText;


            BigDecimal postLatDec = BigDecimal.valueOf(postLat);
            BigDecimal postLongDec = BigDecimal.valueOf(postLong);

            BigDecimal pointOne = BigDecimal.valueOf(0.01d);
            //double gigjf = commission.doubleValue();


            String box00 = (postLatDec.subtract(pointOne).doubleValue())+"_"+(postLongDec.subtract(pointOne).doubleValue());
            String box01 = (postLat )+"_"+(postLongDec.subtract(pointOne).doubleValue());
            String box02 = (postLatDec.add(pointOne).doubleValue())+"_"+(postLongDec.subtract(pointOne).doubleValue());
            String box10 = (postLatDec.subtract(pointOne).doubleValue())+"_"+(postLong);
            String box11 = (postLat)+"_"+(postLong);
            String box12 = (postLatDec.add(pointOne).doubleValue())+"_"+(postLong );
            String box20 = (postLatDec.subtract(pointOne).doubleValue())+"_"+(postLongDec.add(pointOne).doubleValue());
            String box21 = (postLat )+"_"+(postLongDec.add(pointOne).doubleValue());
            String box22 = (postLatDec.add(pointOne).doubleValue())+"_"+(postLongDec.add(pointOne).doubleValue());






            String bo00new = box00.replaceAll("\\.", "|");
            String bo01new = box01.replaceAll("\\.", "|");
            String bo02new = box02.replaceAll("\\.", "|");
            String bo10new = box10.replaceAll("\\.", "|");
            String bo11new = box11.replaceAll("\\.", "|");
            String bo12new = box12.replaceAll("\\.", "|");
            String bo20new = box20.replaceAll("\\.", "|");
            String bo21new = box21.replaceAll("\\.", "|");
            String bo22new = box22.replaceAll("\\.", "|");

            bubbleClass postBubble;

            if(lstSelectedBubbles.size() == 0){
                postBubble = new bubbleClass("",
                        "",
                        0 ,
                        "",
                        "",
                        "");
            }
            else{
                postBubble = lstSelectedBubbles.get(0);

            }


            bubbleClass blankBubble  = new bubbleClass("",
                    "",
                    0 ,
                    "",
                    "",
                    "");

          //  postSpecDetails postSpecs = new postSpecDetails(true, false, unixTime, fakeLongEndTime);
            postBubblesClass bubbles = new postBubblesClass(postBubble, blankBubble,blankBubble,blankBubble,blankBubble );
            postLocationsClass locations = new postLocationsClass(bo00new, bo01new,bo02new,bo10new,bo11new,bo12new,bo20new,bo21new,bo22new );
      /*      postClass usersPost = new postClass( userName,postText  , postID,  userLat,
                    userLong, unixTime, userName ,passedTextType , userName,
                    locations, "NONE", bubbles, postSpecs);

            FirebaseDatabase.getInstance().getReference().child("Users").child(userName).child("POSTS").child(postID).child("POSTDATA").setValue(usersPost );
//            ref?.child("Users").child((Auth.auth().currentUser?.uid)!).child("POSTS").child(postID).child("POSTDATA").setValue(post);

*/
            //FirebaseDatabase.getInstance().getReference().child("University").child("Illinois").child("EVENTS").child(eventType).child(className).child(classNum).child("Events").child(markKey).setValue(newEvent);


        }

    }

    public void getLocation(){

        SharedPreferences prefs = this.getActivity().getSharedPreferences("USERINFO", MODE_PRIVATE);

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


}
