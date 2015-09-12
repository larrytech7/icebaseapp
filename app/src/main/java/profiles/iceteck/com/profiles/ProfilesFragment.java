package profiles.iceteck.com.profiles;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.widgets.Dialog;
import com.iceteck.icebase.JSON_DB_EXTENDED;

import org.json.JSONException;

import adapter.ProfilesAdapter;
import model.ORM;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfilesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilesFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View rootView;
    private RecyclerView profilesListView;
    private ButtonFloat btnFloatAddProfile;

    private ProfilesAdapter profilesAdapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilesFragment.
     */
    public static ProfilesFragment newInstance(String param1, String param2) {
        ProfilesFragment fragment = new ProfilesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfilesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSON_DB_EXTENDED profilesdb = new JSON_DB_EXTENDED(getActivity(), "profiles");
        /*
        System.out.println("read start "+System.currentTimeMillis());
            profilesAdapter = new ProfilesAdapter(getActivity(),
                             getActivity()
                            .getContentResolver()
                            .query(ORM.Person.CONTENT_URI,null,null,null,null));
        System.out.println("read end "+System.currentTimeMillis());
        */
        try {
            System.out.println(System.currentTimeMillis());
            profilesAdapter = new ProfilesAdapter(getActivity(), profilesdb.get(), false);
            System.out.println(System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profiles, container, false);
        btnFloatAddProfile = (ButtonFloat) rootView.findViewById(R.id.buttonFloatAddProfile);
        profilesListView = (RecyclerView)rootView.findViewById(android.R.id.list);
        profilesListView.setHasFixedSize(true);
        profilesListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        profilesListView.setAdapter(null);
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        profilesListView.setAdapter(profilesAdapter);
        btnFloatAddProfile.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonFloatAddProfile:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new ProfileNewFragment())
                        .commit();

                final Dialog dialog = new Dialog(getActivity(),"New Profile", "You will Create a new Profile and publish ");

                dialog.show();
                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ButtonFlat acceptbtn = dialog.getButtonAccept();
                    acceptbtn.setText("OK");

                break;
            default:
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
