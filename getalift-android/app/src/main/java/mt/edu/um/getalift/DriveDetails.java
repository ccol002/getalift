package mt.edu.um.getalift;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class DriveDetails extends AppCompatActivity {

    Intent intentDetails;
    private TextView txtTest;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intentDetails= getIntent();
        if (intentDetails != null) {
            userID = intentDetails.getIntExtra("userId",0);
        }

        txtTest = findViewById(R.id.textView);
        txtTest.setText(String.valueOf(userID));



    }


}
