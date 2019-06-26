package com.davida.tatwpbnw;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class updateApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_app);



        Intent intent = getIntent();
        String uLink = intent.getExtras().getString("updateLink");
        if(uLink.isEmpty()){
            uLink = "https://www.google.com/";
        }
        final String uLinkF = uLink;

        Button btnChoosePost = (Button) findViewById(R.id.btnGoToUpdateLink) ;


        btnChoosePost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Loads the post functiion and passes the post string as well as the post type

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uLinkF));
                startActivity(browserIntent);
            }
        });




    }
}
