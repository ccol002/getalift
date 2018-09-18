package mt.edu.um.getalift;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
        mChangeLanguageButton = (Button) findViewById(R.id.btn_change_language);

        // Display the settings screen
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();

        //Display alert dialog to choose the language
        mChangeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });

        mValidButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void showChangeLanguageDialog() {
        final String[] listItem  ={"English","French","Spanish"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(listItem, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    //French
                    setLocale("en");
                    recreate();
                }
                else if (i == 1){
                    //English
                    setLocale("fr");
                    recreate();
                }
                else if (i == 2){
                    //Espagnol
                    setLocale("es-rEs");
                    recreate();
                }
                dialogInterface.dismiss();

            }
        });
        AlertDialog mDialog = mBuilder.create();
        //Show alert
        mDialog.show();

    }

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

}
