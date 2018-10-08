package mt.edu.um.getalift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ViewRideInfoActivity extends AppCompatActivity {

    //Creation of the intent which recovers the id of the driver selected by the user
    Intent intent_View_Ride_Info_activity;
    private int driverId;

    //Tag for the LOG
    private static final String TAG = "ViewRideInfoActivity";

    private TextView txt_driver_id;
    private TextView txt_driver_phoneNumber;
    private TextView txt_driver_email;
    private TextView txt_driver_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ride_info);
        setTitle("Ride's Info");

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //We assimilate the variables created above with the Id of the layout
        //txt_driver_id = (TextView) findViewById(R.id.txt_driver_name);
        txt_driver_phoneNumber = findViewById(R.id.txt_driver_phoneNumber);
        txt_driver_email = findViewById(R.id.txt_driver_email);
        txt_driver_name = findViewById(R.id.txt_driver_name);

        // Recovering the ride selected
        intent_View_Ride_Info_activity = getIntent();
        if (intent_View_Ride_Info_activity != null) {
            driverId = intent_View_Ride_Info_activity.getIntExtra("driver_id",0);
            //txt_driver_id.setText("The id of the driver : " + driverId);
        }

        //Complete the fields with the info of the driver
        completeDriverInfo();

    }

    // Return to the last page
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

    //Complete the fields with the info t=of teh driver
    public void completeDriverInfo(){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/users/" + Integer.toString(driverId);

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONArray(response).getJSONObject(0);

                            // Display the info of the user
                            Log.i("Test",response);
                            txt_driver_email.setText(jo.getString("email"));
                            txt_driver_phoneNumber.setText(jo.getString("mobileNumber"));
                            txt_driver_name.setText(jo.getString("name")+ " " + jo.getString("surname"));

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
