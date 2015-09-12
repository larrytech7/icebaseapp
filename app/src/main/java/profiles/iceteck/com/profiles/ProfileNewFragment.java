package profiles.iceteck.com.profiles;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.BitmapCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.iceteck.icebase.JSON_DB_EXTENDED;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.ProfilesAdapter;
import model.ORM;

public class ProfileNewFragment extends Fragment implements Spinner.OnItemSelectedListener, View.OnClickListener{

    private View root;
    private ButtonFlat cancel;
    private ButtonRectangle create;
    private EditText nametext, countrytext, backgroundtext, longitudetext, latitudetext,citytext;
    private Spinner countrySpinner;
    private ImageView profileimg;
    private String country;
    private Bitmap bitmap;

    public static ProfileNewFragment newInstance(String param1, String param2) {
        ProfileNewFragment fragment = new ProfileNewFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ProfileNewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.createprofilelayout, container, false);

        countrySpinner = (Spinner) root.findViewById(R.id.countrySpinner);
        countrySpinner.setOnItemSelectedListener(this);
        profileimg = (ImageView) root.findViewById(R.id.profileimg);
        profileimg.setOnClickListener(this);
        nametext = (EditText) root.findViewById(R.id.profileNameEditText);
        citytext = (EditText) root.findViewById(R.id.profilecity);
        backgroundtext = (EditText) root.findViewById(R.id.profDescriptionEdititext);
        longitudetext = (EditText) root.findViewById(R.id.proflocationLongitude);
        latitudetext = (EditText) root.findViewById(R.id.proflocationlatitude);

        //button setup
        create = (ButtonRectangle) root.findViewById(R.id.buttonCreate);
        cancel = (ButtonFlat) root.findViewById(R.id.buttonflatReturn);
        create.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView countrytext = (TextView) view;
        country = countrytext.getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonCreate: //cave the profile
                if(saveProfile()) {
                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                    getFragmentManager()
                            .beginTransaction()
                            .detach(this)
                            .add(R.id.container, ProfilesFragment.newInstance("", ""))
                            .commit();
                }else
                    Toast.makeText(getActivity(), "Failed to save, Please verify your profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonflatReturn: //return to parent fragment
                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .add(R.id.container, ProfilesFragment.newInstance("", ""))
                        .commit();
                break;
            case R.id.profileimg:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Picture:"),
                        100);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = data.getData();
        String selectedImagePath = getPath(selectedImageUri);
        bitmap = getPreview(selectedImagePath);
        profileimg.setImageBitmap(bitmap);
    }


    private String getPath(Uri uri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(proj[0]);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private Bitmap getPreview(String fileName) {
        File image = new File(fileName);

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
            return null;
        }
        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                : bounds.outWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / 256; //default = 64
        return BitmapFactory.decodeFile(image.getPath(), opts);
    }

    private boolean saveProfile(){
        JSON_DB_EXTENDED jsondb = new JSON_DB_EXTENDED(getActivity(), ProfilesAdapter.PROFILES_DB);
        //String name =
        List<Object> person = new ArrayList<>();
        person.add(nametext.getText().toString());
        person.add(country);
        person.add(backgroundtext.getText().toString());
        person.add(citytext.getText().toString());
        person.add(latitudetext.getText().toString());
        person.add(longitudetext.getText().toString());
        person.add(SimpleDateFormat.getDateInstance().format(new Date()));
/*
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
*/
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byte[] bytes = byteBuffer.array();

        person.add(bytes);

/*
        ContentValues contentValues = new ContentValues();
        ContentValues aContentValues = new ContentValues();
        //insert person's address
        aContentValues.put(ORM.Address.COLUMN_COUNTRY, country);
        aContentValues.put(ORM.Address.COLUMN_CITY, citytext.getText().toString());
        aContentValues.put(ORM.Address.COLUMN_LATITUDE, Float.parseFloat(latitudetext.getText().toString()));
        aContentValues.put(ORM.Address.COLUMN_LONGITUDE, Float.parseFloat(longitudetext.getText().toString()));

        //insert person
        contentValues.put(ORM.Person.COLUMN_NAME, nametext.getText().toString());
        contentValues.put(ORM.Person.COLUMN_PORTFOLIO, backgroundtext.getText().toString());
        contentValues.put(ORM.Person.COLUMN_IMAGE, byteArray);
        contentValues.put(ORM.Person.COLUMN_DATE, SimpleDateFormat.getDateInstance().format(new Date()));
        contentValues.put(ORM.Person.COLUMN_ADDRESS_ID, ORM.Person.COLUM_ID );
        System.out.println("write start "+System.currentTimeMillis());
        getActivity().getContentResolver().insert(ORM.Address.CONTENT_URI, aContentValues);
        getActivity().getContentResolver().insert(ORM.Person.CONTENT_URI, contentValues);
        System.out.println("write end "+System.currentTimeMillis());
        */
        System.out.println(System.currentTimeMillis());
//        int insert = jsondb.insert((ArrayList<Object>) person);
        System.out.println(System.currentTimeMillis());

        return jsondb.insert((ArrayList<Object>) person) == 1 ? true:false;
    }
}
