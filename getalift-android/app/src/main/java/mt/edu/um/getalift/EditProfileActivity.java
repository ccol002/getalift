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
import android.util.Patterns;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**  Created by Jean-Louis Thessalene **/

public class EditProfileActivity extends AppCompatActivity {

    // Creation des variables pour stocker celles du Layout
    private TextView txtName;
    private TextView txtSurname;
    private TextView txtUsername;
    private TextView txtPhoneNumber;
    private TextView txtEmail;
    private TextView txtComment;
    private TextView txtTestId;
    private TextView txtPassword;

    private Button mValidButton;
    private Button mClearButton;

    private boolean info;

    //Création of the intent recovering the ID of the user
    Intent intent_edit_profile_activity;
    private int userID;
    private String username ;
    private String name;
    private String email;
    private String surname;
    private String password;
    private int mobileNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle(getString(R.string.text_title_edit_profile));

        info = false;
        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // On assimile les variables créées plus haut avec les Id des layout
        txtName = (TextView) findViewById(R.id.edt_edit_name);
        txtPhoneNumber = (TextView) findViewById(R.id.edt_edit_phoneNumber);
        txtEmail = (TextView) findViewById(R.id.edt_edit_email);
        txtSurname = (TextView) findViewById(R.id.edt_edit_surname);
        txtUsername = (TextView) findViewById(R.id.edt_edit_username);
        txtPassword = (TextView) findViewById(R.id.edt_edit_password);

        mValidButton = (Button) findViewById(R.id.btn_valid_edit_profile);
        mClearButton = (Button) findViewById(R.id.btn_clear_edit);
        txtTestId = (TextView)findViewById(R.id.txt_test_id);

        // On recupere l'Id
        intent_edit_profile_activity = getIntent();
        if (intent_edit_profile_activity != null) {
            userID = intent_edit_profile_activity.getIntExtra("userId",0);
           /* username = intent_edit_profile_activity.getStringExtra("username");
            name = intent_edit_profile_activity.getStringExtra("name");
            surname = intent_edit_profile_activity.getStringExtra("surname");
            password = intent_edit_profile_activity.getStringExtra("password");
            email = intent_edit_profile_activity.getStringExtra("email");
            mobileNumber = intent_edit_profile_activity.getIntExtra("mobileNumber",0);*/
            txtTestId.setText("L'identifiant du user est : " +userID);

        }

        mValidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillOutTheEditForm();
                if (info ){
                    profil();
                   editDataBase(view);
                }
            }
        });
    }

    //We check if all the information is correct
    private void fillOutTheEditForm() {
        // We retrieve what the user select in the form
        final String name = ((EditText) findViewById(R.id.edt_edit_name)).getText().toString().trim();
        final String surname = ((EditText) findViewById(R.id.edt_edit_surname)).getText().toString().trim();
        final String phonenumber = ((EditText) findViewById(R.id.edt_edit_phoneNumber)).getText().toString().trim();
        final String email = ((EditText) findViewById(R.id.edt_edit_email)).getText().toString().trim();
       // final String comment = ((EditText) findViewById(R.id.edt_edit_comment)).getText().toString().trim();
        final String username = ((EditText) findViewById(R.id.edt_edit_username)).getText().toString().trim();
        final String password = ((EditText) findViewById(R.id.edt_edit_password)).getText().toString().trim();

        // We check a bunch of things from what the user type.
        if (name.length() < 6 && name.length() > 0 ) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_name_short), Toast.LENGTH_SHORT).show();
        } else if (name.length() > 63){
            Toast.makeText(getApplicationContext(), getString(R.string.error_name_long), Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.length()>1){
            Toast.makeText(getApplicationContext(), getString(R.string.error_email_valid), Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(phonenumber).matches() && phonenumber.length()>1){
            Toast.makeText(getApplicationContext(), getString(R.string.error_phone_valid), Toast.LENGTH_SHORT).show();
        }/* else if (comment.length() > 500){
            Toast.makeText(getApplicationContext(), getString(R.string.error_comment_long), Toast.LENGTH_SHORT).show();
        } else if (comment.length() < 20 && comment.length()>0){
            Toast.makeText(getApplicationContext(), getString(R.string.error_comment_short), Toast.LENGTH_SHORT).show();
        } */else if (username.length() < 6 && username.length() > 0){
            Toast.makeText(getApplicationContext(), getString(R.string.error_username_short), Toast.LENGTH_SHORT).show();
        } else if (username.length() > 100){
            Toast.makeText(getApplicationContext(), getString(R.string.error_username_long), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "All information is correct", Toast.LENGTH_SHORT).show();
                info = true;
        }

    }


    //Recover all the information of the user
    public void profil(){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/users/" + Integer.toString(userID);

        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {
                            // We create a JSONObject from the server response.
                            JSONObject jo = new JSONArray(response).getJSONObject(0);

                            // On affiche les données de l'utilisateur
                            Log.i("Test",response);
                            if (email.length() == 0) {
                                txtEmail.setText(jo.getString("email"));
                                Toast.makeText(getBaseContext(), txtEmail.toString(), Toast.LENGTH_SHORT).show();}
                            if (Integer.toString(mobileNumber).length() == 0){
                                txtPhoneNumber.setText(jo.getString("mobileNumber"));
                                Toast.makeText(getBaseContext(), txtPhoneNumber.toString(), Toast.LENGTH_SHORT).show();}
                            if (name.length() == 0){
                                txtName.setText(jo.getString("name"));
                                Toast.makeText(getBaseContext(), txtName.toString(), Toast.LENGTH_SHORT).show();}
                            if (surname.length() == 0){
                                txtSurname.setText(jo.getString("surname"));
                                Toast.makeText(getBaseContext(), txtSurname.toString(), Toast.LENGTH_SHORT).show();}
                            if (username.length() == 0){
                                txtUsername.setText(jo.getString("username"));
                                Toast.makeText(getBaseContext(), txtSurname.toString(), Toast.LENGTH_SHORT).show();}
                            if (password.length() == 0){
                                txtPassword.setText(jo.getString("password"));
                                Toast.makeText(getBaseContext(), txtPassword.toString(), Toast.LENGTH_SHORT).show();}


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.get.request.edit", error.toString());
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

   public void clearAll(){
        //clear all the text zone
   }

    /**
     * This method is called when the user click on the "edit all" button.
     * @param view the view of the button clicked.
     */
    public void editDataBase(View view) {
        fillOutTheEditForm();
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL + "/api/users/" + userID;


        // We retrieve what the user taped in the form
        final String edt_name = ((EditText) findViewById(R.id.edt_edit_name)).getText().toString().trim();
        final String edt_surname = ((EditText) findViewById(R.id.edt_edit_surname)).getText().toString().trim();
        final String edt_phonenumber = ((EditText) findViewById(R.id.edt_edit_phoneNumber)).getText().toString().trim();
        final String edt_email = ((EditText) findViewById(R.id.edt_edit_email)).getText().toString().trim();
        final String edt_username = ((EditText) findViewById(R.id.edt_edit_username)).getText().toString().trim();
        final String edt_password = ((EditText) findViewById(R.id.edt_edit_password)).getText().toString().trim();
        //final String comment = ((EditText) findViewById(R.id.edt_edit_comment)).getText().toString().trim();


        //Recover user's info already saved in his profile to fill out the parameters that it doesn't want to change
        if (intent_edit_profile_activity != null) {
            final String username = intent_edit_profile_activity.getStringExtra("username");
            String name = intent_edit_profile_activity.getStringExtra("name");
            String surrname = intent_edit_profile_activity.getStringExtra("surname");
            String password = intent_edit_profile_activity.getStringExtra("password");
            String email = intent_edit_profile_activity.getStringExtra("email");
            String mobileNumber = intent_edit_profile_activity.getStringExtra("mobileNumber");

        }
        final Activity activity = this;


        Toast.makeText(getApplicationContext(), "Edit the information", Toast.LENGTH_SHORT).show();

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response.toString());
                        Toast.makeText(getBaseContext(),"ça marche bien !", Toast.LENGTH_SHORT).show();
;                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(getBaseContext(),"ça marche PAS !", Toast.LENGTH_SHORT).show();

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if(edt_username.length() != 0){
                    params.put("username",edt_username);
                }else {
                    params.put("username",txtUsername.toString());
                }

                if(edt_name.length() != 0){
                    params.put("name",edt_name);
                }else {
                    params.put("name",txtName.toString());
                }

                if(edt_surname.length() != 0){
                    params.put("surname",edt_surname);
                }else {
                    params.put("surname",txtSurname.toString());
                }

                if(edt_email.length() != 0){
                    params.put("email",edt_email);
                }else {
                    params.put("email","dodo22@hotmail.fr");
                }
                //Password
                if(edt_password.length() != 0){
                    params.put("password",edt_password);
                }else {
                    params.put("password", "dodo22");
                }

                if(edt_phonenumber.length() != 0){
                    params.put("mobileNumber",edt_phonenumber);
                }else{
                    params.put("mobileNumber","00000000");

                }
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
