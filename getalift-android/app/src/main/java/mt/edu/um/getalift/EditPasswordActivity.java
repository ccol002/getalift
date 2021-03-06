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
        setTitle(getString(R.string.txt_edit_pwd));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_view_message = findViewById(R.id.txt_view_edit_pwd);
        btn_valid = findViewById(R.id.btn_valid_edit_pwd);
        txt_new_password = (EditText) findViewById(R.id.edt_new_password);
        txt_old_password = (EditText) findViewById(R.id.edt_old_password);
        txt_view_message.setText(getString(R.string.txt_to_edit_pwd));

        //Because in the back-end the request crypt the password before to edit the database, and in sharedPref, we have the pwd crypted
        //So  retrieve the pwd entered while connexion and I transfer it to this page.
        String passwordNoCrypted = getIntent().getStringExtra("password_no_crypted");
        password = passwordNoCrypted;
        //Retrieve info of the current user
        SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
        JSONObject user = null;
        try {
            user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
            userID = user.getInt("id");
            username = user.getString("username");
           // password = user.getString("password");
            //Log.i("TAG_pwdCrypte", password);
            name = user.getString("name");
            surname = user.getString("surname");
            //We don't realy need his email because the sendMail() method redirect him to an app in his device to send the mail so,
            // automatically if the app has his email, it will send with this one
            email = user.getString("email");
            phoneNumber =user.getInt("mobileNumber");
        } catch (JSONException e) {
            e.printStackTrace();
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

        if (new_password.equals(old_password)){
            Toast.makeText(getApplicationContext(), getString(R.string.error_same_password), Toast.LENGTH_LONG).show();
        }
        else {
            if (new_password.length() < 6 && new_password.length() > 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_password_short), Toast.LENGTH_LONG).show();
            } else if (new_password.length() == 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_password_missing), Toast.LENGTH_LONG).show();
            } else if (new_password.length() > 63) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_password_long), Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getApplicationContext(), "New password okay ! ", Toast.LENGTH_SHORT).show();
                editDataBase(new_password);
            }
        }
    }

    public void checkOldPassword(){
        String oldPassword = txt_old_password.getText().toString();

        //Check if the field for actual password is empty and if not, check it in the database
        if(oldPassword.length() == 0){
            Toast.makeText(getApplicationContext(), getString(R.string.error_actual_password_missing), Toast.LENGTH_LONG).show();
        }
        else {
            login(oldPassword);
        }
    }

    public void login(String actualPassword){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/auth";
        final String password = actualPassword;
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response){
                        // We got a response from our server.
                        try {
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONObject(response);
                            Log.i("TAG_Response", response);
                            if (jo.getBoolean("success")){
                                // If it's OK, we send a temporary message to say that it's okay
                                Log.i("TAG_old pwd_correct", "correct");
                                Toast.makeText(getApplicationContext(), getString(R.string.error_actual_password_correct), Toast.LENGTH_SHORT).show();
                                //If teh actual pwd is correct, we check with our constraints the new one
                                checkPassword();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_actual_password_incorrect), Toast.LENGTH_SHORT).show();
                                Log.i("TAG_pwd_incorrect", "incorrect");
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
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
                headers.put("x-access-token", sh.getString("token", null));
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
