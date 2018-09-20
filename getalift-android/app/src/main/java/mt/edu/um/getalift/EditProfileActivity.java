package mt.edu.um.getalift;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    // Creation des variables pour stocker celles du Layout
    private TextView txtName;
    private TextView txtUsername;
    private TextView txtPhoneNumber;
    private TextView txtEmail;
    private TextView txtComment;

    private Button mValidButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle(getString(R.string.text_title_edit_profile));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // On assimile les variables créées plus haut avec les Id des layout
        txtName = (TextView) findViewById(R.id.edt_edit_name);
        txtPhoneNumber = (TextView) findViewById(R.id.edt_edit_phoneNumber);
        txtEmail = (TextView) findViewById(R.id.edt_edit_email);
        txtComment = (TextView) findViewById(R.id.edt_edit_comment);
        txtUsername = (TextView) findViewById(R.id.edt_edit_username);
        mValidButton = (Button) findViewById(R.id.btn_valid_edit_profile);

        mValidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillOutTheEditForm();
            }
        });
    }

    //We check if all the information is correct
    private void fillOutTheEditForm() {
        // We retrieve what the user select in the form
        final String name = ((EditText) findViewById(R.id.edt_edit_name)).getText().toString().trim();
        final String phonenumber = ((EditText) findViewById(R.id.edt_edit_phoneNumber)).getText().toString().trim();
        final String email = ((EditText) findViewById(R.id.edt_edit_email)).getText().toString().trim();
        final String comment = ((EditText) findViewById(R.id.edt_edit_comment)).getText().toString().trim();
        final String username = ((EditText) findViewById(R.id.edt_edit_username)).getText().toString().trim();

        // We check a bunch of things from what the user type.
        if (name.length() < 6 ) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_username_short), Toast.LENGTH_SHORT).show();
        } else if (name.length() > 63){
            Toast.makeText(getApplicationContext(), getString(R.string.error_name_long), Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(), getString(R.string.error_email_valid), Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(phonenumber).matches()){
            Toast.makeText(getApplicationContext(), getString(R.string.error_phone_valid), Toast.LENGTH_SHORT).show();
        } else if (comment.length() > 500){
            Toast.makeText(getApplicationContext(), getString(R.string.error_comment_long), Toast.LENGTH_SHORT).show();
        } else if (comment.length() < 20){
            Toast.makeText(getApplicationContext(), getString(R.string.error_comment_short), Toast.LENGTH_SHORT).show();
        } else if (username.length() < 10){
            Toast.makeText(getApplicationContext(), getString(R.string.error_subject_short), Toast.LENGTH_SHORT).show();
        } else if (username.length() > 100){
            Toast.makeText(getApplicationContext(), getString(R.string.error_subject_long), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();

        }

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
