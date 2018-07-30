package mt.edu.um.getalift;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ResultSearchActivity extends AppCompatActivity {

    private List<Ride> myRides = new ArrayList<Ride>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        setTitle(getString(R.string.txt_title_result_search));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateRideList();
        populateListView();
    }

    private void populateRideList() {
        myRides.add(new Ride("University of Malta","Mater Dei Hostipal", new User(4,"toto"),"10/08/2018",3));
        myRides.add(new Ride("University of France","Mater Dei Hostipal",new User(2,"tata"), "11/08/2018",3));
        myRides.add(new Ride("University of Germany","Mater Dei Hostipal",new User(1,"titi"), "12/08/2018",3));
    }

    private void populateListView() {
        ArrayAdapter<Ride> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.ridesListView);
        list.setAdapter(adapter);
    }


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

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.ridesListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Ride clickedRide = myRides.get(position);
                String message = "You clicked position " + position;
                Toast.makeText(ResultSearchActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Ride> {
        public MyListAdapter(){
            super(ResultSearchActivity.this, R.layout.item_drive_view, myRides);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_drive_view, parent, false);
            }

            // Find the car to work with.
            Ride currentRide = myRides.get(position);

            // Fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.usr_icon);
            imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);

            // Name:
            TextView nameText = (TextView) itemView.findViewById(R.id.usr_name);
            nameText.setText(currentRide.getDriver().getUsername());

            // Rate:
            TextView rateText = (TextView) itemView.findViewById(R.id.usr_rate);
            rateText.setText(Float.toString(currentRide.getDriver().getRate()) + "/5");

            // Distance:
            TextView distanceText = (TextView) itemView.findViewById(R.id.usr_distance);
            distanceText.setText(currentRide.getMinWalking() + " mn walk");

            return itemView;
        }
    }
}
