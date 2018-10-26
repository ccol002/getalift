package mt.edu.um.getalift;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RatingSystem extends AppCompatActivity {

    private String TAG = "RatingTAG";
    private RatingBar ratingBar;
    private Button commitBtn;
    private TextView result;
    private TextView confirmation;
    private Button validationBtn;
    private Button cancel;

    //Création de l'intent qui récupere l'Id de l'utilisateur
    Intent intentRating;
    private int userID;
    private int routeId;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        setTitle(getString(R.string.txt_title_profile));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_rating);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ratingBar = findViewById(R.id.ratingBar);
        commitBtn = findViewById(R.id.commitButton);
        result = findViewById(R.id.result);
        confirmation = findViewById(R.id.confirmation);
        validationBtn = findViewById(R.id.validationButton);
        cancel = findViewById(R.id.cancelBtn);

        // On recupere l'Id
        intentRating = getIntent();

        if (intentRating != null) {
            userID = intentRating.getIntExtra("userId",2);
            Log.i(TAG,String.valueOf(userID));
            routeId = intentRating.getIntExtra("routeId",0);
            Log.i(TAG,String.valueOf(routeId));
        }

        // récupération de l'utilisateur actuel
        SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename), Context.MODE_PRIVATE);
        try {
            JSONObject user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
            Log.i(TAG,Integer.toString(user.getInt("id"),0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // récupération de la date et l'heure

        Date currentTime = Calendar.getInstance().getTime();
        Log.i(TAG,currentTime.toString());


        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText("The rate you choose is  " + ratingBar.getRating() );
                confirmation.setText("Press the button Commit again if you're Ok");
                validationBtn.setVisibility(View.VISIBLE);
                commitBtn.setVisibility(View.INVISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                intent.putExtra("userId",userID);
                startActivity(intent);
            }
        });



    }




    public void signIn(View view){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/ratings";

        // We retrieve what the user select in the form
        final String commentaire = ((EditText) findViewById(R.id.editCommentaire)).getText().toString();
        final float password =  ratingBar.getRating();

        final Activity activity = this;


            // We create the Request. It's a StringRequest, and not directly a JSONObjectRequest because
            // it looks like it's more stable.
            StringRequest sr = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){

                        @Override
                        public void onResponse(String response) {
                            // We got a response from our server.
                            try {
                                Log.d("GetALift", response);
                                // We create a JSONObject from the server response.
                                JSONObject jo = new JSONObject(response);
                                // If the server respond with a success...
                                if (jo.getBoolean("success")){
                                    // We tell the user his account is now created...
                                    Toast.makeText(getApplicationContext(), getString(R.string.success_account_created), Toast.LENGTH_SHORT).show();
                                    // And we move him back to the login activity.
                                    NavUtils.navigateUpFromSameTask(activity);
                                } else {
                                    if (jo.getInt("errorCode") == 1){
                                        // We tell the user his account is now created...
                                        Toast.makeText(getApplicationContext(), getString(R.string.error_username_exists), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    },
                    new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("GetALift", error.toString());
                        }

                    }
            ){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    //params.put("username",username);
                    //params.put("password",password);
                    return params;
                }
            };
            queue.add(sr);



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
}
