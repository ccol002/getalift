package mt.edu.um.getalift;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mLanguagePreference;

    private Button mValidButtonSettings;
    private Button mChangeLanguageButton;


    public static final String PREF_KEY_LANGUAGE = "PREF_KEY_LANGUAGE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.text_title_settings));

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mValidButtonSettings = (Button) findViewById(R.id.btn_valid_settings);

        // Display the settings screen
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();


        mValidButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choixLangue();
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
        //Save
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

    //Preference Fragment for teh settins screen
    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_screen);
        }
    }


    // Retourner a la page d'accueil en cliquant sur retour
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


    public void choixLangue() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String language = prefs.getString("change language", "Défaut");
        Log.i("salut salut",language);

        if(language.equals("fr")){
            //French
            Toast.makeText(getApplicationContext(), "Vous avez choisi :" + language, Toast.LENGTH_SHORT).show();
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
