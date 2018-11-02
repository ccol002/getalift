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
    private int targetID;
    private int routeId;

    // Données à envoyer
    Date currentTime;
    int rideId;
    int userID;


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
            targetID = intentRating.getIntExtra("userId",2);
            //Log.i(TAG + "target ID",String.valueOf(targetID));
            routeId = intentRating.getIntExtra("routeId",0);
            //Log.i(TAG + "target ID",String.valueOf(routeId));
        }

        // récupération de l'utilisateur actuel
        SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename), Context.MODE_PRIVATE);
        try {
            JSONObject user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
            userID = user.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // récupération de la date et l'heure

        currentTime = Calendar.getInstance().getTime();
        //Log.i(TAG,currentTime.toString());


        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText("The rate you choose is  " + ratingBar.getRating() );
                confirmation.setText("Press the button Commit again if you're Ok");
                validationBtn.setVisibility(View.VISIBLE);
                commitBtn.setVisibility(View.INVISIBLE);
            }
        });

        validationBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                rateThis(view);
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                intent.putExtra("userId",targetID);
                intent.putExtra("routeId",routeId);
                intent.putExtra("canRate",1);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                intent.putExtra("userId",targetID);
                startActivity(intent);
            }
        });

    }
    /*
    public int getRideId(int idRoute){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/rides/test/" + Integer.toString(idRoute);

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONArray(response).getJSONObject(0);
                            rideId = jo.getInt("id");
                            Log.i(TAG + "ntm",String.valueOf(jo.getInt("id")));
                            Log.i(TAG + "ntm",String.valueOf(rideId));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }

                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
                params.put("x-access-token", sh.getString("token", null));
                return params;
            }
        };
        queue.add(sr);
        Log.i(TAG + "ride ID",String.valueOf(rideId));
        return rideId;
    }*/


    public void rateThis(View view){

        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/ratings/existingRate";

        // We retrieve what the user select in the form
        final String comment = ((EditText) findViewById(R.id.editCommentaire)).getText().toString();
        final String stars = String.valueOf((int)ratingBar.getRating());
        final String author = String.valueOf(userID);
        final String ride = String.valueOf(29);
        final String target = String.valueOf(targetID);
        //final String postDate = currentTime.toString();
        final String postDate = "2018-11-02 00:00:00";

        /*
        Log.i(TAG,comment);
        Log.i(TAG,stars);
        Log.i(TAG,author);
        Log.i(TAG,ride);
        Log.i(TAG,target);
        Log.i(TAG,postDate);
        */

        //On vérifie que les données ne sont pas vide ou incompatible au format
        if (comment.length() < 5) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_commentRate_long), Toast.LENGTH_SHORT).show();
        } else {


            // We create the Request. It's a StringRequest, and not directly a JSONObjectRequest because
            // it looks like it's more stable.
            StringRequest sr = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            // We got a response from our server.
                            try {
                                Log.d("GetALift", response);
                                // We create a JSONObject from the server response.
                                JSONObject jo = new JSONObject(response);
                                // If the server respond with a success...
                                if (jo.getBoolean("success")) {
                                    // We tell the user his rate is created and back to the last page
                                    Toast.makeText(getApplicationContext(), getString(R.string.success_rate_created), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    intent.putExtra("userId", userID);
                                    intent.putExtra("routeId", routeId);
                                    intent.putExtra("canRate", 1);
                                    startActivity(intent);
                                } else {
                                    if (jo.getInt("errorCode") == 1) {
                                        // We tell the user he can't rate 2 times
                                        Toast.makeText(getApplicationContext(), getString(R.string.error_rate_exists), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("GetALift", error.toString());
                        }

                    }
            ) {

                @Override
                public Map<String, String> getHeaders()
                {
                    Map<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/json");
                    //or try with this:
                    headers.put("x-access-token", "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJkb2RvIiwicGFzc3dvcmQiOiIkMmIkMTAkTGhNLnVCZ1YyL2JkYW9nbHpRUkNVZS5XL2Z0QTdnUG5mdEp2NC5JWFlGeGtCamplNVhVOHEiLCJuYW1lIjoiZG9kbyIsInN1cm5hbWUiOiJkb2RvIiwiZW1haWwiOiJkb2RvQGdtYWlsLmNvbSIsIm1vYmlsZU51bWJlciI6IjA2MDYwNjA2MDYiLCJpc1ZlcmlmaWVkIjowfQ.kWqjMDwA6iwcNDXEYYzgHHnMwnCOwBHBX9aDHHi3gKo");
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("author", author);
                    params.put("target", target);
                    params.put("ride", ride);
                    params.put("stars", stars);
                    params.put("comment", comment);
                    params.put("postDate", postDate);
                    return params;
                }
            };
            queue.add(sr);
        }
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
