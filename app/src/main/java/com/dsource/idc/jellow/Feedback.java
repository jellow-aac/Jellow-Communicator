package com.dsource.idc.jellow;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.dsource.idc.jellow.Utility.SessionManager;

public class Feedback extends AppCompatActivity {

    private RatingBar easy_to_use, pictures, voice, navigate;
    private Button bSubmit;
    EditText comments;
    String u, p, vo, n;
    private SessionManager session;
    TextView tv1, tv2, tv3, tv4, tv5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        tv1= (TextView)findViewById(R.id.tv1) ;
        tv2= (TextView)findViewById(R.id.tv2) ;
        tv3= (TextView)findViewById(R.id.tv3) ;
        tv4= (TextView)findViewById(R.id.tv4) ;
        tv5= (TextView)findViewById(R.id.tv5) ;
        bSubmit=(Button)findViewById(R.id.bSubmit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>Feedback</font>"));
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        if (dpHeight >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        session = new SessionManager(getApplicationContext());

        if (session.getLanguage()==1){
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>प्रतिक्रिया</font>"));
            tv1.setText("इस्तेमाल में आसान");
            tv2.setText("स्पष्ट चित्र");
            tv3.setText("स्पष्ट आवाज़");
            tv4.setText("मार्गनिर्देशन की सरलता");
            tv5.setText("टिप्पणी/सुझाव");
            bSubmit.setText("सबमिट करें");
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        addListenerOnRatingBar();
        addListenerOnButton();
    }

    public void addListenerOnRatingBar() {

        easy_to_use = (RatingBar) findViewById(R.id.easy_to_use);
        Drawable progress1 = easy_to_use.getProgressDrawable();
        DrawableCompat.setTint(progress1, Color.argb(255, 255, 204, 20));
        pictures = (RatingBar)findViewById(R.id.pictures);
        Drawable progress2 = pictures.getProgressDrawable();
        DrawableCompat.setTint(progress2, Color.argb(255, 255, 204, 20));
        voice = (RatingBar)findViewById(R.id.voice);
        Drawable progress3 = voice.getProgressDrawable();
        DrawableCompat.setTint(progress3, Color.argb(255, 255, 204, 20));
        navigate = (RatingBar)findViewById(R.id.navigate);
        Drawable progress4 = navigate.getProgressDrawable();
        DrawableCompat.setTint(progress4, Color.argb(255, 255, 204, 20));

        easy_to_use.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                u = String.valueOf(rating);
            }
        });
        pictures.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                p = String.valueOf(rating);
            }
        });
        voice.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                vo = String.valueOf(rating);
            }
        });
        navigate.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                n = String.valueOf(rating);
            }
        });
    }

    public void addListenerOnButton() {

        easy_to_use = (RatingBar) findViewById(R.id.easy_to_use);
        comments= (EditText)findViewById(R.id.comments);
        bSubmit = (Button) findViewById(R.id.bSubmit);
        bSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String cs  = comments.getText().toString();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"dsource.in@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Jellow Feedback");
                email.putExtra(Intent.EXTRA_TEXT, "Easy to use: "+u+"\nClear Pictures: "+p+"\nClear Voices: "+vo+"\nEasy to Navigate: "+n+ "\n\nComments and Suggestions:-\n"+cs);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(Feedback.this, Setting.class);
                startActivity(intent);
                finish();
                break;
            case R.id.info:
                Intent i = new Intent(Feedback.this, About_Jellow.class);
                startActivity(i);
                finish();
                break;
            case R.id.profile:
                Intent intent1 = new Intent(Feedback.this, Profile_form.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.usage:
                Intent intent3 = new Intent(Feedback.this, Tutorial.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.reset:
                Intent intent4 = new Intent(Feedback.this, Reset__preferences.class);
                startActivity(intent4);
                finish();
                break;
            case android.R.id.home:
                Intent intent5 = new Intent(Feedback.this, MainActivity.class);
                startActivity(intent5);
                break;
            case R.id.keyboardinput:
                Intent intent6 = new Intent(Feedback.this, Keyboard_Input.class);
                startActivity(intent6);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(Feedback.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
