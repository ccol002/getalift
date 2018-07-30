package mt.edu.um.getalift;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle(R.string.txt_title_signin);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_signin);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * This method is called when the user click on the "create account" button.
     * @param view the view of the button clicked.
     */
    public void signIn(View view){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/users";

        // We retrieve what the user select in the form
        final String username = ((EditText) findViewById(R.id.edt_signin_username)).getText().toString().trim();
        final String password = ((EditText) findViewById(R.id.edt_signin_password)).getText().toString().trim();
        final String email = ((EditText) findViewById(R.id.edt_signin_email)).getText().toString().trim();
        final String firstname = ((EditText) findViewById(R.id.edt_signin_firstname)).getText().toString().trim();
        final String lastname = ((EditText) findViewById(R.id.edt_signin_lastname)).getText().toString().trim();
        final String phonenumber = ((EditText) findViewById(R.id.edt_signin_phonenumber)).getText().toString().trim();

        final Activity activity = this;

        // We check a bunch of things from what the user type.
        if (username.length() < 5) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_username_short), Toast.LENGTH_SHORT).show();
        } else if (password.length() < 5) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_password_short), Toast.LENGTH_SHORT).show();
        } else if (username.length() > 63){
            Toast.makeText(getApplicationContext(), getString(R.string.error_username_long), Toast.LENGTH_SHORT).show();
        } else if (password.length() > 63){
            Toast.makeText(getApplicationContext(), getString(R.string.error_password_long), Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(), getString(R.string.error_email_valid), Toast.LENGTH_SHORT).show();
        } else if (firstname.length() == 0){
            Toast.makeText(getApplicationContext(), getString(R.string.error_firstname_notset), Toast.LENGTH_SHORT).show();
        } else if (lastname.length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_lastname_notset), Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(phonenumber).matches()){
            Toast.makeText(getApplicationContext(), getString(R.string.error_phone_valid), Toast.LENGTH_SHORT).show();
        } else {
            // If everything is correct,
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
                    params.put("username",username);
                    params.put("password",password);
                    params.put("email",email);
                    params.put("name",firstname);
                    params.put("surname",lastname);
                    params.put("mobileNumber",phonenumber);
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
