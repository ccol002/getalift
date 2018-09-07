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

public class DriveActivity extends AppCompatActivity {

    // Tag utilisé pour les LOG
    private static final String TAG = "DriveTAGAct";

    //Log.i("Home",Integer.toString(user.getInt("id"),0));

    //Création de l'intent qui récupere l'Id de l'utilisateur
    Intent intent_drive_activity;
    private int userID;

    //Variable compteur pour les JSONObject
    private int compteurJSON;

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
        Log.i(TAG,Integer.toString(userID,0));


        DriveListItem();




        DriveList fragInfo = new DriveList();
        fragInfo.setArguments(bundle);
        transaction.replace(R.id.layout, fragInfo);
        transaction.commit();

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


    }

    public void DriveListItem(){


        String myMessage = "Stackoverflow is cool!";
        bundle.putString("message", myMessage );
        bundle.putInt("userID",userID);


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
                            Log.i(TAG,response);
                            // We create a JSONObject from the server response.
                            int length = new JSONArray(response).length();
                            for (int i = 0; i <= length; i ++){
                                JSONObject jo = new JSONArray(response).getJSONObject(i);
                                Log.i(TAG,jo.getString("id"));
                                bundle.putString("driver" + i,jo.getString("id"));
                            }

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



