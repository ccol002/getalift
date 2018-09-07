package mt.edu.um.getalift;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HelpActivity extends AppCompatActivity {

    private ImageButton mFaqButton;
    private ImageButton mContactUs;
    private ImageButton mAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle(getString(R.string.txt_title_help));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAbout = (ImageButton) findViewById(R.id.image_about);
        mContactUs = (ImageButton) findViewById(R.id.image_contact_us);
        mFaqButton = (ImageButton) findViewById(R.id.image_faq_button);

        // En cliquant sur l'image FAQ l'utilisateur sera redirige vers la page Faq
        mFaqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadNewActivity = new Intent(HelpActivity.this, PageFaqActivity.class);
                startActivity(intentLoadNewActivity);
            }
        });

        // En cliquant sur l'image 'contact us' l'utilisateur sera redirige vers la page de prise de contact
        mContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadNewActivity = new Intent(HelpActivity.this, ContactUsActivity.class);
                startActivity(intentLoadNewActivity);
            }
        });
    }





    // Retourner a la page d'accueil en cliquant sur retour
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
