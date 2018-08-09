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
import android.view.View;
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

public class ProfileActivity extends AppCompatActivity {

    private TextView txtUser;
    private TextView txtName;
    private TextView txtPhone;
    private TextView txtEmail;
    private TextView txtNote;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(getString(R.string.txt_title_profile));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = findViewById(R.id.textFirst_Last);
        txtPhone = findViewById(R.id.textPhoneNumber);
        txtEmail = findViewById(R.id.textEmail);
        txtNote = findViewById(R.id.textNote);

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

    public void profil(View view){
        //We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);

        //We get the URL of the server
        String url = ConnectionManager.SERVER_URL+"/api/users/" + "3";
        //For now I choose 3 and after I will find a way to recover the user's id


        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response){
                        // We got a response from our server.
                        try {
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONObject(response);

                            if (jo.getBoolean("success")){
                                txtName.setText("Alexandre DONY");


                                // If it's OK, we store the token in a SharedPreferences file.
                                //Context context = getApplicationContext();
                                //SharedPreferences sharedPreferences = context.getSharedPreferences(
                                //        getString(R.string.msc_shared_pref_filename),
                                //        Context.MODE_PRIVATE
                                //);

                                //SharedPreferences.Editor edit = sharedPreferences.edit();
                                //edit.putString(
                                //        getString(R.string.msc_key_saved_token),
                                //        jo.getString("token")
                                //);
                                //edit.putString(
                                //        getString(R.string.msc_saved_user),
                                //        jo.getJSONObject("user").toString()
                                //);
                                //edit.apply();

                                // ... and we go to the main menu.
                                //Intent intent = new Intent(getBaseContext(), HomeMapActivity.class);
                                //finish();
                                //startActivity(intent);
                            } else {
                                //Context context = getApplicationContext();
                                //CharSequence text = jo.getString("message");
                                //int duration = Toast.LENGTH_SHORT;

                                //Toast toast = Toast.makeText(context, text, duration);
                                //toast.show();
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
        );
    }

}
