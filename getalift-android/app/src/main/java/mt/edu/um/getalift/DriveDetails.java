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

    private String TAG = "DriveDetails";
    Intent intentDetails;
    private int id;

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
        /*
        txtTest = findViewById(R.id.textView);
        txtTest.setText(String.valueOf(id));
        */
        origins = findViewById(R.id.idOrigins);
        destionation = findViewById(R.id.idDestination);
        driver = findViewById(R.id.idDriver);
        druration = findViewById(R.id.idDuration);

        driveDetailsList();
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


}
