package mt.edu.um.getalift;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongToIntFunction;

public class DriveActivity extends AppCompatActivity implements DriveList.OnClientSelectedListener{

    // Tag utilisé pour les LOG
    private static final String TAG = "DriveTAGAct";

    //Log.i("Home",Integer.toString(user.getInt("id"),0));

    //Création de l'intent qui récupere l'Id de l'utilisateur
    Intent intent_drive_activity;
    private int userID;

    // On envoie l'id de l'utilisateur pour avoir les infos dans le fragment
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    final Bundle bundle = new Bundle();



    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        setTitle(getString(R.string.txt_title_drive));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // On recupere l'Id
        intent_drive_activity = getIntent();

        if (intent_drive_activity != null) {
            userID = intent_drive_activity.getIntExtra("userId",0);
        }

        // On crée la drive list et on lui donne l'id de l'utilisateur
        DriveList fragInfo = new DriveList();
        fragInfo.setArguments(bundle);

        bundle.putInt("userID",userID);

        transaction.replace(R.id.layout, fragInfo);
        transaction.commit();
    }


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

    @Override
    public void onClientSelected(int id) {
        Intent intent = new Intent(DriveActivity.this,DriveDetails.class);
        startActivity(intent);
    }
}



