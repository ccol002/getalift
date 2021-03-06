package mt.edu.um.getalift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**  Created by Jean-Louis Thessalene **/

public class HelpActivity extends AppCompatActivity {

    private ImageButton mFaqButton;
    private ImageButton mContactUs;
    private ImageButton mAbout;

    //Creation of the intent which recover the id of the user
    Intent intent_profile_activity;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle(getString(R.string.txt_title_help));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recovering of the ID of the current user
        intent_profile_activity = getIntent();
        if (intent_profile_activity != null) {
            userID = intent_profile_activity.getIntExtra("userId",0);
        }

        //Display the buttons
        mAbout = (ImageButton) findViewById(R.id.image_about);
        mContactUs = (ImageButton) findViewById(R.id.image_contact_us);
        mFaqButton = (ImageButton) findViewById(R.id.image_faq_button);

        //By clicking on the FAQ image the user will be redirected to the Faq page
        mFaqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadNewActivity = new Intent(HelpActivity.this, PageFaqActivity.class);
                startActivity(intentLoadNewActivity);
            }
        });

        //By clicking on the "Contact Us" image the user will be redirected to the "Contact Us" page
        mContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentContactUs = new Intent(HelpActivity.this, ContactUsActivity.class);
                startActivity(intentContactUs);

                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
                try {
                    JSONObject user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
                    Log.i("Home",Integer.toString(user.getInt("id"),0));
                    intentContactUs.putExtra("userId", user.getInt("id"));

                    startActivity(intentContactUs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AboutActivity = new Intent(HelpActivity.this, PageAboutActivity.class);
                startActivity(AboutActivity);
            }
        });
    }


    // Come back to the home page
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If we select the "Go back" button
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    }




