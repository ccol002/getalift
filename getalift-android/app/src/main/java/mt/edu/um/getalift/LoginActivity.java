package mt.edu.um.getalift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is linked to the Login Activity.
 * It handles everything that is linked to the login action.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * This method override the onCreate method of his parent.
     * It's a basic method in Android development.
     * It's launched when the activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_login);
        setSupportActionBar(toolbar);
        setTitle(R.string.txt_title_login);

        TextView txt_username = (TextView)findViewById(R.id.edt_username);
        txt_username.setText("dodo");
        TextView txt_pw = (TextView)findViewById(R.id.edt_password);
        txt_pw.setText("dodo");
    }

    /**
     * This method is called when the user click on the "login" button.
     * @param view the current view of the activity.
     */
    public void login(View view){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/auth";

        // We retrieve the user credentials
        final String name = ((EditText) findViewById(R.id.edt_username)).getText().toString().trim();
        final String password = ((EditText) findViewById(R.id.edt_password)).getText().toString().trim();
        /*
        Log.d("LOGIN","name : "+name);
        Log.d("LOGIN","pwd : "+password);

        if (true){
            // If it's OK, we store the token in a SharedPreferences file.
            Context context = getApplicationContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    getString(R.string.msc_shared_pref_filename),
                    Context.MODE_PRIVATE
            );

            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(
                    getString(R.string.msc_key_saved_token),
                    "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJkb2RvIiwicGFzc3dvcmQiOiIkMmIkMTAkTGhNLnVCZ1YyL2JkYW9nbHpRUkNVZS5XL2Z0QTdnUG5mdEp2NC5JWFlGeGtCamplNVhVOHEiLCJuYW1lIjoiZG9kbyIsInN1cm5hbWUiOiJkb2RvIiwiZW1haWwiOiJkb2RvQGdtYWlsLmNvbSIsIm1vYmlsZU51bWJlciI6IjA2MDYwNjA2MDYiLCJpc1ZlcmlmaWVkIjowfQ.kWqjMDwA6iwcNDXEYYzgHHnMwnCOwBHBX9aDHHi3gKo"
            );
            edit.putString(
                    getString(R.string.msc_saved_user),
                    "blablabla"
            );
            edit.apply();

            // ... and we go to the main menu.
            Intent intent = new Intent(getBaseContext(), HomeMapActivity.class);
            finish();
            startActivity(intent);
        }
        */
        // We create the Request. It's a StringRequest, and not directly a JSONObjectRequest because
        // it looks like it's more stable.
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response){
                        // We got a response from our server.
                        try {
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONObject(response);

                            if (jo.getBoolean("success")){
                                // If it's OK, we store the token in a SharedPreferences file.
                                Context context = getApplicationContext();
                                SharedPreferences sharedPreferences = context.getSharedPreferences(
                                    getString(R.string.msc_shared_pref_filename),
                                    Context.MODE_PRIVATE
                                );

                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putString(
                                        getString(R.string.msc_key_saved_token),
                                        jo.getString("token")
                                );

                                edit.putString(
                                        getString(R.string.msc_saved_user),
                                        jo.getJSONObject("user").toString()
                                );
                                edit.apply();

                                // ... and we go to the main menu.
                                Intent intent = new Intent(getBaseContext(), HomeMapActivity.class);
                                finish();
                                startActivity(intent);
                            } else {
                                Context context = getApplicationContext();
                                CharSequence text = jo.getString("message");
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
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
                params.put("username",name);
                params.put("password",password);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(sr);
    }

    /**
     * This method is called when the user click on the "sign in" button.
     * @param view the current view of the activity
     */
    public void signin(View view){
        Intent intent = new Intent(getBaseContext(), SignInActivity.class);
        startActivity(intent);
    }
}
