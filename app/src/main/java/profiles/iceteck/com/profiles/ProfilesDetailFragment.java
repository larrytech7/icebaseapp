package profiles.iceteck.com.profiles;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.iceteck.icebase.JSON_DB_EXTENDED;

import org.json.JSONException;

import java.util.ArrayList;

import adapter.ProfilesAdapter;

public class ProfilesDetailFragment extends Fragment implements View.OnClickListener {

    private static final String P_ID = "profileid"; //profile id to fetch
    private JSON_DB_EXTENDED json_db_extended;
    private String id;

    public static ProfilesDetailFragment newInstance(String param1) {
        ProfilesDetailFragment fragment = new ProfilesDetailFragment();
        Bundle args = new Bundle();
        args.putString(P_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfilesDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        id = getArguments().getString(P_ID);
        View rootview = inflater.inflate(R.layout.fragment_profiles_detail, container, false);
        json_db_extended = new JSON_DB_EXTENDED(getActivity(), ProfilesAdapter.PROFILES_DB);

        try {
            ArrayList<Object> mainprofile = json_db_extended.get(id);
            ((TextView)rootview.findViewById(R.id.profileName)).setText((CharSequence) mainprofile.get(0));
            ((TextView)rootview.findViewById(R.id.profileCountry)).setText((CharSequence) mainprofile.get(1));
            ((TextView)rootview.findViewById(R.id.profileDateCreated)).setText((CharSequence) mainprofile.get(6));
            ((TextView)rootview.findViewById(R.id.profileHistory)).setText((CharSequence) mainprofile.get(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rootview.findViewById(R.id.deletebutton).setOnClickListener(this);
        Log.i("Argument Parameter", ""+id);
        return rootview;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.deletebutton:
                try {
                    json_db_extended.delete(id);
                    getActivity().finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
