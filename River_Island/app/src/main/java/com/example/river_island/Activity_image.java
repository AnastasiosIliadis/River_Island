package com.example.river_island;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class Activity_image extends AppCompatActivity {

    ImageView ImageView;
    Button back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ImageView = (ImageView) findViewById(R.id.ImageView) ;
        back_btn = (Button) findViewById(R.id.back_btn);
        String imageurl = getIntent().getStringExtra("imageurl");
        Picasso.get().load(imageurl).into(ImageView);

        back_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                                            finish();
                                            startActivity(intent);
                                        }
                                    }
        );
    }
}