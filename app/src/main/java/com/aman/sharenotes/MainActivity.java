package com.aman.sharenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=2000;

    //animation
    View first,second,third,fourth,fifth,sixth,seventh;
    ImageView a;
    //Animations
    Animation topAnimantion,middleAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.activity_main);
        //this will bind your MainActivity.class file with activity_main.





        first = findViewById(R.id.first_line);
        second = findViewById(R.id.second_line);
        third = findViewById(R.id.third_line);
        fourth = findViewById(R.id.fourth_line);
        fifth = findViewById(R.id.fifth_line);
        sixth = findViewById(R.id.sixth_line);
        seventh=findViewById(R.id.seventh_line);
        a = findViewById(R.id.Mainlogo);


//        Animation initialize
        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_anim);



        first.setAnimation(topAnimantion);
        second.setAnimation(topAnimantion);
        third.setAnimation(topAnimantion);
        fourth.setAnimation(topAnimantion);
        fifth.setAnimation(topAnimantion);
        sixth.setAnimation(topAnimantion);
        seventh.setAnimation(topAnimantion);
        a.setAnimation(middleAnimation);

        Toast.makeText(this, "Before Handler", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {



                Intent i=new Intent(MainActivity.this,
                        Login.class);

                Toast.makeText(MainActivity.this, "Intent", Toast.LENGTH_SHORT).show();
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                Toast.makeText(MainActivity.this, "After Intent", Toast.LENGTH_SHORT).show();



                finish();

                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);





    }
}
