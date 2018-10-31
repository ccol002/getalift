package mt.edu.um.getalift;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

/**  Created by Jean-Louis Thessalène   31-10-2018 **/
public class EditPasswordActivity extends AppCompatActivity {

    //Creation of the intent recovering the ID of the user
    Intent intent_edit_profile_activity;
    private int userID;
    private String username ;
    private String name;
    private String email;
    private String surname;
    private String password;
    private int phoneNumber;

    private TextView txt_view_message;

    private EditText txt_new_password;
    private EditText txt_old_password;
    private Button btn_valid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        setTitle("Edit password ");

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_view_message = findViewById(R.id.txt_view_edit_pwd);
        btn_valid = findViewById(R.id.btn_valid_edit_pwd);

        txt_new_password = (EditText) findViewById(R.id.edt_new_password);
        txt_old_password = (EditText) findViewById(R.id.edt_old_password);
        txt_view_message.setText("If you want to edit your password, enter your old one");
        //Recover the intent
        intent_edit_profile_activity = getIntent();

        //Recover user's info already saved in his profile to fill out the parameters that it doesn't want to change
        if (intent_edit_profile_activity != null) {
            userID = intent_edit_profile_activity.getIntExtra("userId",0);
            username = intent_edit_profile_activity.getStringExtra("username");
            name = intent_edit_profile_activity.getStringExtra("name");
            email = intent_edit_profile_activity.getStringExtra("email");
            surname = intent_edit_profile_activity.getStringExtra("surname");
            password = intent_edit_profile_activity.getStringExtra("password");
            phoneNumber = intent_edit_profile_activity.getIntExtra("mobileNumber", 0000000000);
            Log.i("TAG_Old_pwd_Recup", "Password recupéré :" + password);
        }

        btn_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOldPassword();
            }
        });
    }

    public void checkPassword(){
        String new_password = txt_new_password.getText().toString();
        String old_password = txt_old_password.getText().toString();
        Log.i("TAG_new_Password", "New password : " + new_password);

        if (new_password.equals(old_password)){
            Toast.makeText(getApplicationContext(), "Your actual password and your new one are the same", Toast.LENGTH_LONG).show();
        }
        else {
            if (new_password.length() < 6 && new_password.length() > 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_password_short), Toast.LENGTH_LONG).show();
                //Log.i("TAG_new_Password", "New password too short");
            } else if (new_password.length() == 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_password_missing), Toast.LENGTH_LONG).show();
                //Log.i("TAG_new_Password", "New password null");
            } else if (new_password.length() > 63) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_password_long), Toast.LENGTH_LONG).show();
                //Log.i("TAG_new_Password", "New password too long");
            } else {
                Toast.makeText(getApplicationContext(), "New password okay ! ", Toast.LENGTH_SHORT).show();
                editDataBase(new_password);
            }
        }
    }

    public void checkOldPassword(){
        Log.i("TAG_checkOldPassword", "Check old pxd");
        String oldPassword = txt_old_password.getText().toString();
   // Log.i("TAG_oldPassword", "Old password : "+ oldPassword);
        //Check if the field for old password is empty and if not, check it in the database
        if(oldPassword.length() == 0){
            Toast.makeText(getApplicationContext(), getString(R.string.error_old_password_missing), Toast.LENGTH_LONG).show();
            Log.i("TAG_oldPassword", "Old password null ");
        }
        else {
            //Toast.makeText(getApplicationContext(), "Old password pas null !", Toast.LENGTH_LONG).show();
            Log.i("TAG_oldPassword", "Old password pas null !");
            login(oldPassword);
        }
    }


    public void login(String oldPassword){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/auth";
        final String password = oldPassword;
        Log.i("TAG_old pwd", oldPassword);
        Log.i("TAG_old pwd_param", password);
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response){
                        // We got a response from our server.
                        try {
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONObject(response);

                            if (jo.getBoolean("success")){
                                // If it's OK, we send a temporary message to say that it's okay
                                Log.i("TAG_old pwd_correct", "correct");
                                Toast.makeText(getApplicationContext(), getString(R.string.error_old_password_correct), Toast.LENGTH_SHORT).show();
                                checkPassword();

                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_old_password_incorrect), Toast.LENGTH_SHORT).show();
                                Log.i("TAG_old pwd_incorrect", "incorrect");
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

    //To update the database with the new password
    public void editDataBase(String new_password) {
        final String new_pwd = new_password;
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL + "/api/users/" + userID;
        final Activity activity = this;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response.toString());
                        Toast.makeText(getBaseContext(),getString(R.string.txt_new_pwd_edited), Toast.LENGTH_LONG).show();
                        NavUtils.navigateUpFromSameTask(activity);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(getBaseContext(),"Error : unfortunately your password not has been updated! \n try later", Toast.LENGTH_SHORT).show();

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
            //Parameters for the SQL request, we need all the information
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                    params.put("username",username);
                    params.put("name",name);
                    params.put("surname",surname);
                    params.put("email",email);
                //New Password
                    params.put("password", new_pwd);
                    params.put("mobileNumber",Integer.toString(phoneNumber));
                    params.put("isVerified", "0");
                return params;
            }
        };
        queue.add(putRequest);
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
}
