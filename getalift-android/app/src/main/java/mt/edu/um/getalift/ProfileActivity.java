package mt.edu.um.getalift;

import android.content.Context;
<<<<<<< HEAD
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
=======
import android.content.SharedPreferences;
>>>>>>> fd41e4ca7494bd690db4cbe9be7b1b9aaf1c998d
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
<<<<<<< HEAD
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
=======
import android.widget.EditText;
>>>>>>> fd41e4ca7494bd690db4cbe9be7b1b9aaf1c998d

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

<<<<<<< HEAD
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

=======
>>>>>>> fd41e4ca7494bd690db4cbe9be7b1b9aaf1c998d
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

<<<<<<< HEAD
    private TextView txtUser;
    private TextView txtName;
    private TextView txtPhone;
    private TextView txtEmail;
    private TextView txtNote;

    private static final String TAG = "ProfileActivity";

    Intent intent_profile_activity;
    private int userID;




=======
    private static final String TAG = "ProfileActivity";

>>>>>>> fd41e4ca7494bd690db4cbe9be7b1b9aaf1c998d
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(getString(R.string.txt_title_profile));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

<<<<<<< HEAD
        txtUser = findViewById(R.id.textTitle);
        txtName = findViewById(R.id.textFirst_Last);
        txtPhone = findViewById(R.id.textPhoneNumber);
        txtEmail = findViewById(R.id.textEmail);
        txtNote = findViewById(R.id.textNote);

        intent_profile_activity = getIntent();

        if (intent_profile_activity != null) {
            userID = intent_profile_activity.getIntExtra("userId",0);
            txtNote.setText(Integer.toString(userID));
        }
        profil();
=======
        searchRide();

>>>>>>> fd41e4ca7494bd690db4cbe9be7b1b9aaf1c998d
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

<<<<<<< HEAD
    public void profil(){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/users/2";
        //Log.i(TAG,url);
        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {
                            Log.i(TAG, response);
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONArray(response).getJSONObject(0);

                            Log.i(TAG,jo.getString("username"));
                            txtUser.setText(jo.getString("surname"));
                            txtEmail.setText(jo.getString("email"));
                            txtPhone.setText(jo.getString("mobileNumber"));
                            txtName.setText(jo.getString("name")+ " " + jo.getString("username"));

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

=======
    public void searchRide(){
        Log.i(TAG,"starting searchRide...");
        RequestQueue queue = Volley.newRequestQueue(this);

        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/users/3";

        // We retrieve the search parameters
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Log.i("ProfileActivity", response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.i(TAG, error.toString());
                    }
>>>>>>> fd41e4ca7494bd690db4cbe9be7b1b9aaf1c998d
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
                params.put("x-access-token", sh.getString("token", null));
                return params;
<<<<<<< HEAD
            }
        };
        queue.add(sr);

    }

=======
            }};
        queue.add(postRequest);
    }
>>>>>>> fd41e4ca7494bd690db4cbe9be7b1b9aaf1c998d
}
