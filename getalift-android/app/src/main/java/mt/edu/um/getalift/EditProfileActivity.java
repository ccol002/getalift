package mt.edu.um.getalift;

import android.app.Activity;
import android.content.Intent;
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

    private Button mValidButton;
    private Button mClearButton;

    private boolean info;

    //Création of the intent recovering the ID of the user
    Intent intent_edit_profile_activity;
    private int userID;

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

        mValidButton = (Button) findViewById(R.id.btn_valid_edit_profile);
        mClearButton = (Button) findViewById(R.id.btn_clear_edit);
        txtTestId = (TextView)findViewById(R.id.txt_test_id);

        // On recupere l'Id
        intent_edit_profile_activity = getIntent();
        if (intent_edit_profile_activity != null) {
            userID = intent_edit_profile_activity.getIntExtra("userId",0);
            txtTestId.setText("L'identifiant du user est : " +userID);
        }

        mValidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillOutTheEditForm();
                if (info ){
                    editDataBase(view);
                }
            }
        });
    }

    //We check if all the information is correct
    private void fillOutTheEditForm() {
        // We retrieve what the user select in the form
        final String name = ((EditText) findViewById(R.id.edt_edit_name)).getText().toString().trim();
        final String phonenumber = ((EditText) findViewById(R.id.edt_edit_phoneNumber)).getText().toString().trim();
        final String email = ((EditText) findViewById(R.id.edt_edit_email)).getText().toString().trim();
       // final String comment = ((EditText) findViewById(R.id.edt_edit_comment)).getText().toString().trim();
        final String username = ((EditText) findViewById(R.id.edt_edit_surname)).getText().toString().trim();

        // We check a bunch of things from what the user type.
        if (name.length() < 6 && name.length() > 0 ) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_username_short), Toast.LENGTH_SHORT).show();
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
        } */else if (username.length() < 10 && username.length() > 0){
            Toast.makeText(getApplicationContext(), getString(R.string.error_subject_short), Toast.LENGTH_SHORT).show();
        } else if (username.length() > 100){
            Toast.makeText(getApplicationContext(), getString(R.string.error_subject_long), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "All information is correct", Toast.LENGTH_SHORT).show();
                info = true;
        }

    }



    /**
     * This method is called when the user click on the "edit all" button.
     * @param view the view of the button clicked.
     */
    public void editDataBase(View view){
        fillOutTheEditForm();
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/users/"+"9";

        // We retrieve what the user taped in the form
        final String name = ((EditText) findViewById(R.id.edt_edit_name)).getText().toString().trim();
        final String surname = ((EditText) findViewById(R.id.edt_edit_surname)).getText().toString().trim();
        final String phonenumber = ((EditText) findViewById(R.id.edt_edit_phoneNumber)).getText().toString().trim();
        final String email = ((EditText) findViewById(R.id.edt_edit_email)).getText().toString().trim();
        //final String comment = ((EditText) findViewById(R.id.edt_edit_comment)).getText().toString().trim();
        final String username = ((EditText) findViewById(R.id.edt_edit_username)).getText().toString().trim();

        final Activity activity = this;


            Toast.makeText(getApplicationContext(), "Edit the information", Toast.LENGTH_LONG).show();


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
                                    Toast.makeText(getApplicationContext(), "Okay ca marche !!", Toast.LENGTH_SHORT).show();
                                    // And we move him back to the login activity.
                                    //NavUtils.navigateUpFromSameTask(activity);
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
                    if (username.length() != 0 ) {
                        params.put("username",username);
                    } else{
                        params.put("username","test1245");
                    }
                    params.put("password","test1245");

                    if (name.length() != 0 ) {
                        params.put("name",name);
                    }else {
                        params.put("name","testname");
                    }
                    if (surname.length() != 0 ) {
                        params.put("surname",surname);
                    }else {
                        params.put("surname","testSurname");
                    }

                    if (email.length() != 0 ) {
                        params.put("email", email);
                    }else {
                        params.put("email","emailtest@hotmail.fr");
                    }

                    if (phonenumber.length() != 0 ) {
                        params.put("mobileNumber",phonenumber);
                    }else {
                        params.put("mobileNumber","047000000");
                    }

                    params.put("isVerified","true");

                    return params;
                }
            };
            queue.add(sr);
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
