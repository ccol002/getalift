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

public class DriveActivity extends AppCompatActivity {

    ListView mListView;
    private String[] prenoms = new String[]{
            "Alban", "Olivier", "Anais", "Hermine", "Leo", "Fabien",
            "Julien", "Victor", "PA", "Camille", "Octave", "Justine",
            "Thomas", "Gaetan", "Alexandre Di", "Alexandre Do", "Gabriel", "Hans Greg"
    };
    // Tag utilsié pour les LOG
    private static final String TAG = "DriveTAG";

    //Log.i("Home",Integer.toString(user.getInt("id"),0));

    //Création de l'intent qui récupere l'Id de l'utilisateur
    Intent intent_drive_activity;
    private int userID;

    //Variable compteur pour les JSONObject

    private int compteurJSON;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        setTitle(getString(R.string.txt_title_drive));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.listView);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(DriveActivity.this,
                android.R.layout.simple_list_item_1, prenoms);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(DriveActivity.this,prenoms[i],Toast.LENGTH_LONG).show();

            }
        });

        // On recupere l'Id
        intent_drive_activity = getIntent();

        if (intent_drive_activity != null) {
            userID = intent_drive_activity.getIntExtra("userId",0);
        }
        Log.i(TAG,Integer.toString(userID,0));

        driver();
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



    public void driver(){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/driverroutes/" + Integer.toString(userID);


        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {

                            while (new JSONArray(response).getJSONObject(compteurJSON) != null){
                                compteurJSON++;
                                JSONObject joTest = new JSONArray(response).getJSONObject(compteurJSON);
                                Log.i(TAG,joTest.getString("distance"));
                                Log.i(TAG,Integer.toString(compteurJSON));
                            }

                            /*
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONArray(response).getJSONObject(0);
                            JSONObject jo2 = new JSONArray(response).getJSONObject(1);


                            Log.i(TAG,"Cc");
                            Log.i(TAG,jo.getString("distance"));
                            Log.i(TAG,jo2.getString("distance"));
                            */

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

    }

    }



