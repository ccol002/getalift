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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        String response = getIntent().getStringExtra("JSON_RESULT");
        try {
            JSONArray res = new JSONArray(response);
            int user_id;
            String user_name;
            int route_id;
            Double startLat;
            Double startLng;
            Double endLat;
            Double endLng;
            int minWalking;
            for(int i=0;i<res.length();i++){

                JSONObject tmp = res.getJSONObject(i);
                JSONArray routePoints = tmp.getJSONArray("routePoints");

                //Get the first route point of the route (startingPoint)
                startLat = routePoints.getJSONObject(0).getJSONObject("point").getDouble("x");
                startLng = routePoints.getJSONObject(0).getJSONObject("point").getDouble("y");

                //Get the last route point of the route (endingPoint)
                endLat = routePoints.getJSONObject(routePoints.length()-1).getJSONObject("point").getDouble("x");
                endLng = routePoints.getJSONObject(routePoints.length()-1).getJSONObject("point").getDouble("y");

                route_id = tmp.getInt("id");
                user_id = tmp.getInt("user_id");
                user_name = tmp.getString("user_name");

                minWalking = (int)tmp.getInt("totalDistance")/60;

                myRides.add(new Ride(startLat,startLng,endLat,endLng,route_id,user_id,user_name,"2018-12-31",minWalking));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
            nameText.setText(currentRide.getUser_name());

            // Rate:
            TextView rateText = (TextView) itemView.findViewById(R.id.usr_rate);
            rateText.setText("3.4/5");

            // Distance:
            TextView distanceText = (TextView) itemView.findViewById(R.id.usr_distance);
            distanceText.setText(currentRide.getMinWalking() + " mn walk");

            return itemView;
        }
    }
}
