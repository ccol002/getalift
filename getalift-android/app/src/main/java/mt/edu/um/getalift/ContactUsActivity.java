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
    private TextView txtSubject;
    private TextView txtMessage;
    private String name;
    private String phoneNumber;
    private String email;

    private Button mSendMessageButton;
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
        txtMessage = findViewById(R.id.edt_contact_message);
        txtSubject = findViewById(R.id.edt_contact_subject);

        //Retrieve info of the current user
        SharedPreferences sh = getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
        JSONObject user = null;
        try {
            user = new JSONObject(sh.getString(getString(R.string.msc_saved_user), null));
            name = user.getString("name");
            //We don't realy need his email because the sendMail() method redirect him to an app in his device to send the mail so,
            // automatically if the app has his email, it will send with this one

            email = user.getString("email");
            phoneNumber =user.getString("");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Animation for loading
        ImageView loading = (ImageView) findViewById(R.id.loading_image);
        animationLoading = (AnimationDrawable) loading.getDrawable();

        //Display the buttons
        mSendMessageButton = (Button) findViewById(R.id.contact_validate_button);
        mClearAll = (Button) findViewById(R.id.contact_clear_button);

        mClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAll();
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
        final String message = ((EditText) findViewById(R.id.edt_contact_message)).getText().toString().trim();
        final String subject = ((EditText) findViewById(R.id.edt_contact_subject)).getText().toString().trim();

        //We check if all the information is correct
        // We check a bunch of things from what the user type.
       if (message.length() > 500){
            Toast.makeText(getApplicationContext(), getString(R.string.error_message_long), Toast.LENGTH_SHORT).show();
        } else if (message.length() < 20){
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

    //Clear all the fields
    public void clearAll(){
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
        intentMail.putExtra(Intent.EXTRA_TEXT, txtMessage.getText() +"\n You can call" + name + " with this phone number :" + phoneNumber);
        startActivity(Intent.createChooser(intentMail, getString(R.string.txt_message_of)+" " + name));

    }
}
