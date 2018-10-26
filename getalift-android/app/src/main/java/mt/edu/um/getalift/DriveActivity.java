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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongToIntFunction;

public class DriveActivity extends AppCompatActivity implements DriveList.OnClientSelectedListener, PassengerList.OnClientSelectedListener{

    // Tag utilisé pour les LOG
    private static final String TAG = "DriveTAGAct";

    //Création de l'intent qui récupere l'Id de l'utilisateur
    Intent intent_drive_activity;
    private int userID;

    // On envoie l'id de l'utilisateur pour avoir les infos dans le fragment
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    final Bundle bundle = new Bundle();

    //On fait la même chose pour la deuxième liste
    FragmentTransaction transactionPas = getSupportFragmentManager().beginTransaction();
    final Bundle bundlePas = new Bundle();

    //On test des trucs
    Intent intentDriveResult;


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

        transaction.replace(R.id.layout_1, fragInfo);
        transaction.commit();

        //On fait la même chose pour la liste des passengers

        PassengerList fragInfoPass = new PassengerList();
        fragInfoPass.setArguments(bundlePas);

        bundlePas.putInt("userID",userID);

        transactionPas.replace(R.id.layout_2,fragInfoPass);
        transactionPas.commit();
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
        Log.i(TAG,String.valueOf(id));
        Intent intent = new Intent(DriveActivity.this,DriveDetails.class);
        intent.putExtra("userId",id);
        startActivity(intent);
    }

    //On doit faire la même chose pour le deuxième liste
    @Override
    public void onClientSelectedPassenger(int id) {
        Intent intent = new Intent(DriveActivity.this,DriveDetails.class);
        intent.putExtra("userId",userID);
        startActivity(intent);
    }
}


