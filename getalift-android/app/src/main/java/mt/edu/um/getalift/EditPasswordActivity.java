package mt.edu.um.getalift;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        txt_view_message = findViewById(R.id.txt_view_edit_pwd);
        txt_new_password = findViewById(R.id.edt_new_password);
        txt_old_password = findViewById(R.id.edt_old_password);
        btn_valid = findViewById(R.id.btn_valid_edit_pwd);

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
                String oldPassword = txt_old_password.getText().toString().trim();
                Log.i("TAG_OldPassword", "Clic , old pwd : "+oldPassword);
                Toast.makeText(getApplicationContext(), "Clic sur valider", Toast.LENGTH_SHORT).show();
                checkOldPassword();
            }
        });
    }

    public void checkPassword(){
        final String password = txt_new_password.getText().toString().trim();

        if (password.length() < 6){
            Toast.makeText(getApplicationContext(), getString(R.string.error_password_short), Toast.LENGTH_SHORT).show();
        } else if (password.length() > 63){
            Toast.makeText(getApplicationContext(), getString(R.string.error_password_long), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Nouveau mot de passe ok", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkOldPassword(){
        final String oldPassword = txt_old_password.getText().toString().trim();
        Log.i("TAG_checkOldPassword", "Check old pxd");
    Log.i("TAG_oldPassword", "Old password : "+ oldPassword);
        //Check if the field for old password is empty and if not, check it in the database
        if(oldPassword.length() == 0){
            Toast.makeText(getApplicationContext(), getString(R.string.error_old_password_missing), Toast.LENGTH_SHORT).show();
            Log.i("TAG_oldPassword", "Old password null ");
        }
        else {
            //Toast.makeText(getApplicationContext(), getString(R.string.error_old_password_incorrect), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Old password correct !", Toast.LENGTH_SHORT).show();
            Log.i("TAG_oldPassword", "Old password pas null !");
        }
    }
}
