package mt.edu.um.getalift;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class ProfileActivity extends AppCompatActivity {


    // Creation de variable pour stocker celle du Layout
    //Variable create for the layout
    private TextView txtUser;
    private TextView txtName;
    private TextView txtPhone;
    private TextView txtEmail;
    private TextView txtNote;
    private TextView txtRate;

    // Tag utilsié pour les LOG
    private static final String TAG = "ProfileActivityTag";


    Intent intent_profile_activity;
    private int userID;

    private String email, name, surname, username;

    //Variable utilisée pour passer l'appel
    //Variable used for the phone call
    private int phoneNumber;

    //Variable utilisée pour la route (rating)
    // Variable used for the route's Id (use in the rating system)
    private int routeId;

    //Variable utilsiée pour savoir si l'utilisateur peut noter la route
    //Variable used to know if the user can rate the route
    private int canRate;

    //Driver or passenger
    private int role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(getString(R.string.txt_title_profile));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // On assimile les variables créées plus haut avec les Id des layout
        // We link the variables with the id of the layout
        txtUser = findViewById(R.id.textTitle);
        txtName = findViewById(R.id.textFirst_Last);
        txtPhone = findViewById(R.id.textPhoneNumber);
        txtEmail = findViewById(R.id.textEmail);
        txtNote = findViewById(R.id.textNote);
        txtRate = findViewById(R.id.textAdd);

        //Retrieve info of the current user
        SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
        JSONObject user = null;
        try {
            user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
            userID = user.getInt("id");
            username = user.getString("username");
            name = user.getString("name");
            surname = user.getString("surname");
            email = user.getString("email");
            phoneNumber =user.getInt("mobileNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        intent_profile_activity = getIntent();
        if (intent_profile_activity != null) {
           // userID = intent_profile_activity.getIntExtra("userId",0);
            routeId = intent_profile_activity.getIntExtra("routeId",0);
            canRate = intent_profile_activity.getIntExtra("canRate",0);
            role = intent_profile_activity.getIntExtra("role",3);
        }

        // On transforme le TextView en boutton pour pouvoir appeller quand on appuie dessus
        // We change the txtPhone into a button
        txtPhone.setOnClickListener(txtPhoneBtn);

        // On fait pareil pour la note
        // Same thing for the rate
        txtRate.setOnClickListener(txtRateBtn);

        Log.i("TAG_profile", "profile");
        profil();
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

    public void profil(){
        txtEmail.setText(email);
        txtName.setText(name + " " + surname);
        txtPhone.setText(" " + phoneNumber);
        txtUser.setText(username);
    }

    // Pour passer l'appel
    //Function for the phone call
    private View.OnClickListener txtPhoneBtn = new View.OnClickListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onClick(View view) {
            Intent appel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 0" + phoneNumber));
            // Pourquoi 0 + phone ? Aucune idée
            startActivity(appel);
        }
    };

    // Pour noter un utilisateur
    //Fucntion for the rating system
    private View.OnClickListener txtRateBtn = new View.OnClickListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onClick(View view) {
            if (canRate == 0){
                Toast.makeText(getApplicationContext(), getString(R.string.error_rate_autorisation), Toast.LENGTH_SHORT).show();
            }else {
            Intent intent = new Intent(getApplicationContext(),RatingSystem.class);
            Log.i(TAG,String.valueOf(userID));
            intent.putExtra("routeId",routeId);
            intent.putExtra("ratedId",userID);
            intent.putExtra("role",role);
            startActivity(intent);
            }
        }
    };

}
