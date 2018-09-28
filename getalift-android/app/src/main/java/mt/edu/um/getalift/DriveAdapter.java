package mt.edu.um.getalift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mt.edu.um.getalift.R;

public class DriveAdapter extends ArrayAdapter<Drive> {

    TextView destinationText ;
    TextView originsText;
    TextView dateText;

    public DriveAdapter( Context context, List<Drive> list) {
        super(context,0, list);
    }


    public View getView(int position,  View convertView,  ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view =  layoutInflater.inflate(R.layout.activity_list,parent,false);

        destinationText = (TextView) view.findViewById(R.id.destination);
        destinationText.setText(getItem(position).getDestination());

        originsText = (TextView) view.findViewById(R.id.origin);
        originsText.setText(getItem(position).getOrigin());

        dateText = (TextView) view.findViewById(R.id.date);
        dateText.setText(getItem(position).getDate());

        return view;

    }
}
