package mt.edu.um.getalift;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mLanguagePreference;
    private Button mValidButtonSettings;
    public static final String PREF_KEY_LANGUAGE = "PREF_KEY_LANGUAGE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.text_title_settings));

        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mValidButtonSettings = (Button) findViewById(R.id.btn_valid_settings);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();

        mValidButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mValidButtonSettings_onClick(view);
            }
        });

    }


    public static class MyPreferenceFragment extends PreferenceFragment{
        @Override
        public void onCreate(final Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_screen);
        }
    }

    public void mValidButtonSettings_onClick (View view){
        mLanguagePreference = PreferenceManager.getDefaultSharedPreferences(this);
        String sSettings = mLanguagePreference.getString("change language","xxx");
        Toast.makeText(this, sSettings,Toast.LENGTH_SHORT).show();

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
