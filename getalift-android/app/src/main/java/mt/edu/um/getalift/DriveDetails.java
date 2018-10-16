package mt.edu.um.getalift;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

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

public class DriveDetails extends AppCompatActivity {

    private static final String TAG = "DriveTAGDetails";
    Intent intentDetails;
    private int id;

    //Varaiable contenant l' id du driver obtenu par la première requete (elle est sous forme d'un string car la requete le donne dans ce format )
    private int driverIdQ;

    private TextView origins;
    private TextView destionation;
    private TextView driver;
    private TextView druration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intentDetails= getIntent();
        if (intentDetails != null) {
            id = intentDetails.getIntExtra("userId",0);
        }

        origins = findViewById(R.id.idOrigins);
        destionation = findViewById(R.id.idDestination);
        driver = findViewById(R.id.idDriver);
        druration = findViewById(R.id.idDuration);

        //On met les requetes les unes apres les autres
        driveDetailsList();
        Log.i(TAG,"HALO?");


    }

    public void driveDetailsList(){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/routes/" + Integer.toString(id);
        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {
                            JSONObject jo = new JSONArray(response).getJSONObject(0);
                            origins.setText(jo.getString("originAdress"));
                            destionation.setText(jo.getString("destinationAdress"));
                            druration.setText(jo.getString("duration"));

                            Log.i(TAG,jo.getString("driver"));
                            driverIdQ = Integer.parseInt(jo.getString("driver"));
                            Log.i(TAG,String.valueOf(driverIdQ));

                            driverDetails(driverIdQ);

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
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename), Context.MODE_PRIVATE);
                params.put("x-access-token", sh.getString("token", null));
                return params;
            }
        };
        queue.add(sr);


    }

    // On parametre la requete pour qu'elle est besoin de la donnée que l'on recupère dans la première requete pour un problème de temporalité
    public void driverDetails(int driverId){

    // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/users/" + driverId;

        StringRequest srDriver = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        // We got a response from our server.
                        try {
                            JSONObject jo = new JSONArray(response).getJSONObject(0);
                            Log.i(TAG,jo.getString("username"));
                            driver.setText(jo.getString("username"));


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
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename), Context.MODE_PRIVATE);
                params.put("x-access-token", sh.getString("token", null));
                return params;
            }
        };
        queue.add(srDriver);

    }


}
