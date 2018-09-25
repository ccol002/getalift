package mt.edu.um.getalift;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**  Created by Jean-Louis Thessalene **/

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mLanguagePreference;

    private Button mValidButtonSettings;
    private Button mEditButton;

    private TextView mtxt_settings;

    //Création de l'intent qui récupere l'Id de l'utilisateur
    Intent intent_profile_activity;
    private int userID;
    private  String name_txt;
    private String username_txt;
    private String email_txt;
    private String surname_txt;
    private String password_txt;
    private int phone_txt;


    public static final String PREF_KEY_LANGUAGE = "PREF_KEY_LANGUAGE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_settings);

        //Set the ttitle of the tootlbar
        setTitle(getString(R.string.text_title_settings));

        //Display the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Display view buttons
        mValidButtonSettings = (Button) findViewById(R.id.btn_valid_settings);
        mEditButton = (Button) findViewById(R.id.btn_edit);

        mtxt_settings = (TextView) findViewById(R.id.txt_settings);

        // Recovering of all teh information of the user
        intent_profile_activity = getIntent();
        Bundle bundle = intent_profile_activity.getExtras();
        if (intent_profile_activity != null) {
            userID = intent_profile_activity.getIntExtra("userId",0);
            name_txt = intent_profile_activity.getStringExtra("name");
            username_txt = intent_profile_activity.getStringExtra("username");
            email_txt = intent_profile_activity.getStringExtra("email");
            surname_txt = intent_profile_activity.getStringExtra("surname");
            password_txt = intent_profile_activity.getStringExtra("password");
            phone_txt = intent_profile_activity.getIntExtra("mobileNumber", 000000000000);
            //mtxt_settings.setText("Id est :" + phone_txt);
        }

        // Display the settings screen in the frameLayout of the activity_settings (replace it by that)
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();

        //When the user click on validate the language of the application changes
        mValidButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage();
            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Transfer of the information of the user for the EditProfile page
                Intent intentEditProfile = new Intent(SettingsActivity.this, EditProfileActivity.class);
                startActivity(intentEditProfile);
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename), Context.MODE_PRIVATE);
                try {
                    JSONObject user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
                    Log.i("Home", Integer.toString(user.getInt("id"), 0));
                    intentEditProfile.putExtra("userId", user.getInt("id"));
                    intentEditProfile.putExtra("name", user.getString("name"));
                    intentEditProfile.putExtra("username", user.getString("username"));
                    intentEditProfile.putExtra("email", user.getString("email"));
                    intentEditProfile.putExtra("surname", user.getString("surname"));
                    intentEditProfile.putExtra("password", user.getString("password"));
                    intentEditProfile.putExtra("mobileNumber", user.getInt("mobileNumber"));
                    startActivity(intentEditProfile);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Change language
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //Save the language
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My lang", lang);
        editor.apply();
    }

//Load language saved in shared pref
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My lang","");
        setLocale(language);
    }

    //Preference Fragment for teh settings screen
    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_screen);
        }
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


    public void changeLanguage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String language = prefs.getString("change language", "Défaut");

        if(language.equals("fr")){
            //French
            Toast.makeText(getApplicationContext()," You choose :" + language, Toast.LENGTH_SHORT).show();
            setLocale("fr");
            recreate();
        }else if(language.equals("en")){
            //French
            Toast.makeText(getApplicationContext(), "Vous avez choisi :" + language, Toast.LENGTH_SHORT).show();
            setLocale("en");
            recreate();
        }if(language.equals("es")){
            //French
            Toast.makeText(getApplicationContext(), "Vous avez choisi :" + language, Toast.LENGTH_SHORT).show();
            setLocale("es");
            recreate();
        }

    }


}
