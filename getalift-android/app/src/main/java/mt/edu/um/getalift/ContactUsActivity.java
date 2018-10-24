package mt.edu.um.getalift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

/**  Created by Jean-Louis Thessalene **/

public class ContactUsActivity extends AppCompatActivity {

    //Creating variables to store Layout variables
    private TextView txtName;
    private TextView txtPhoneNumber;
    private TextView txtEmail;
    private TextView txtSubject;
    private TextView txtMessage;

    private Button mSendMessageButton;
    private Button mAutoFillButton;
    private Button mClearAll;

    //Tag for the LOG
    private static final String TAG = "ContactUsActivity";

    //Creation of the intent which recovers the ID of the current user
    Intent intent_profile_activity;
    private int userID;

    //Animation for the loading
    AnimationDrawable animationLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setTitle(getString(R.string.text_title_contact_us));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //We assimilate the variables created above with the Id of the layout
        txtName = findViewById(R.id.edt_contact_name);
        txtPhoneNumber = findViewById(R.id.edt_contact_phoneNumber);
        txtEmail = findViewById(R.id.edt_contact_email);
        txtMessage = findViewById(R.id.edt_contact_message);
        txtSubject = findViewById(R.id.edt_contact_subject);

        // Recovering the ID of the current user
        intent_profile_activity = getIntent();
        if (intent_profile_activity != null) {
            userID = intent_profile_activity.getIntExtra("userId",0);
        }

        //Animation for loading
        ImageView loading = (ImageView) findViewById(R.id.loading_image);
        animationLoading = (AnimationDrawable) loading.getDrawable();

        //Display the buttons
        mSendMessageButton = (Button) findViewById(R.id.contact_validate_button);
        mAutoFillButton = (Button) findViewById(R.id.contact_autofill_button);
        mClearAll = (Button) findViewById(R.id.contact_clear_button);

        //Autofill all the fields with the info of the current user
        mAutoFillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationLoading.start();
                profil();
            }
        });


        mClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAll();
            }
        });

        txtName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //Stop the loading animation when the first field is filled
                    animationLoading.stop();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                                           }
        });


        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillOutTheForm(view);
            }
        });



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


    public void  fillOutTheForm(View view){
        // We retrieve what the user select in the form
        final String name = ((EditText) findViewById(R.id.edt_contact_name)).getText().toString().trim();
        final String phonenumber = ((EditText) findViewById(R.id.edt_contact_phoneNumber)).getText().toString().trim();
        final String email = ((EditText) findViewById(R.id.edt_contact_email)).getText().toString().trim();
        final String message = ((EditText) findViewById(R.id.edt_contact_message)).getText().toString().trim();
        final String subject = ((EditText) findViewById(R.id.edt_contact_subject)).getText().toString().trim();

        //We check if all the information is correct
        // We check a bunch of things from what the user type.
        if (name.length() < 6) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_username_short), Toast.LENGTH_SHORT).show();
        } else if (name.length() > 63){
            Toast.makeText(getApplicationContext(), getString(R.string.error_name_long), Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(), getString(R.string.error_email_valid), Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(phonenumber).matches()){
            Toast.makeText(getApplicationContext(), getString(R.string.error_phone_valid), Toast.LENGTH_SHORT).show();
        } else if (message.length() > 500){
            Toast.makeText(getApplicationContext(), getString(R.string.error_message_long), Toast.LENGTH_SHORT).show();
        } else if (message.length() < 100){
            Toast.makeText(getApplicationContext(), getString(R.string.error_message_short), Toast.LENGTH_SHORT).show();
        } else if (subject.length() < 10){
            Toast.makeText(getApplicationContext(), getString(R.string.error_subject_short), Toast.LENGTH_SHORT).show();
        } else if (subject.length() > 100){
            Toast.makeText(getApplicationContext(), getString(R.string.error_subject_long), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "Sending the email", Toast.LENGTH_LONG).show();
            sendMail();
        }

    }

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

                            // Display the info of the user
                            Log.i("Test",response);
                            txtEmail.setText(jo.getString("email"));
                            txtPhoneNumber.setText(jo.getString("mobileNumber"));
                            txtName.setText(jo.getString("name")+ " " + jo.getString("surname"));

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
                SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename), Context.MODE_PRIVATE);
                params.put("x-access-token", sh.getString("token", null));
                return params;
            }
        };
        queue.add(sr);

    }

    //Clear all the fields
    public void clearAll(){
        txtEmail.setText("");
        txtPhoneNumber.setText("");
        txtName.setText("");
        txtMessage.setText("");
        txtSubject.setText("");
    }

    public void sendMail(){
        //send message to the email of the enterprise
        String emailList [] = {"enterprise@hotmail.fr"}; //TO change with the good address
        Intent intentMail = new Intent(Intent.ACTION_SEND);
        intentMail.setType("message/rfc822");
        intentMail.putExtra(Intent.EXTRA_EMAIL, emailList);
        intentMail.putExtra(Intent.EXTRA_SUBJECT,  txtSubject.getText());
        intentMail.putExtra(Intent.EXTRA_TEXT, txtMessage.getText() +"\n You can call" + txtName.getText()+ " with this phone number :" + txtPhoneNumber.getText());
        startActivity(Intent.createChooser(intentMail, getString(R.string.txt_message_of)+" " + txtName.getText()));

    }
}
