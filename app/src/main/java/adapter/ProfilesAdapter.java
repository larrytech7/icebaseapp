package adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import model.ORM;
import profiles.iceteck.com.profiles.ProfilesDetailActivity;
import profiles.iceteck.com.profiles.R;

/**
 * Created by root on 9/11/15.
 */
public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ProfilesViewHolder> {

    public static String PROFILES_DB = "profiles";
    private Context context;
    private List profilesDataSet;
    private boolean dataIsJson;
    private Cursor cursor;

    public ProfilesAdapter(Context ctx, List data, boolean json){
        context  = ctx;
        profilesDataSet = data;
        dataIsJson = json;
    }

    public ProfilesAdapter(Context ctx, Cursor cursor){
        context = ctx;
        this.cursor = cursor;
    }

    /**
     * Called when RecyclerView needs a new {@link android.support.v7.widget.RecyclerView.ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * . Since it will be re-used to display different
     * items in the data set, it is a good idea to cache references to sub views of the View to
     * avoid unnecessary  calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     */
    @Override
    public ProfilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       return new ProfilesViewHolder(
               LayoutInflater.from(context)
                       .inflate(R.layout.item_profile_layout, parent, false)
                );
    }

    @Override
    public void onBindViewHolder(ProfilesViewHolder profilesViewHolder, int position) {

        ArrayList<Object> profile = (ArrayList<Object>) profilesDataSet.get(position);
/*
        if(cursor.moveToNext())
        {
        profilesViewHolder.profileNameText.setText(cursor.getString(cursor.getColumnIndex(ORM.Person.COLUMN_NAME)));
        profilesViewHolder.profileCountryText.setText("");
        profilesViewHolder.profileViewsText.setText("Viewed 1M+ times");
        byte[] imgbyte = cursor.getBlob(cursor.getColumnIndex(ORM.Person.COLUMN_IMAGE));
        profilesViewHolder.profileView.setImageBitmap(BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length));
    }
*/
        final String name = (String) profile.get(0);
        profilesViewHolder.profileNameText.setText(name);
        profilesViewHolder.profileCountryText.setText((CharSequence) profile.get(1));
        profilesViewHolder.profileViewsText.setText("Viewed 1M+ times");
        byte[] imgbyte = profile.get(7).toString().getBytes();

        profilesViewHolder.profileView.setImageBitmap(BitmapFactory.decodeByteArray(imgbyte,0,imgbyte.length));

        profilesViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, ProfilesDetailActivity.class);
                detailIntent.putExtra("id", name);
                context.startActivity(detailIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        //return this.cursor.getCount();
        return profilesDataSet.size();
    }

    public class ProfilesViewHolder extends RecyclerView.ViewHolder {

        public final ImageView profileView;
        public final TextView profileNameText, profileCountryText, profileViewsText;// dateAdd;
        public final ImageButton btnlikeProfile, btnCommentProfile;
        public final CardView cardView;

        public ProfilesViewHolder(View itemView) {
            super(itemView);
            profileView = (ImageView) itemView.findViewById(R.id.profileImageView);
            profileNameText = (TextView) itemView.findViewById(R.id.profileName);
            profileCountryText = (TextView) itemView.findViewById(R.id.profileCountry);
            profileViewsText = (TextView) itemView.findViewById(R.id.profileViews);
            btnlikeProfile = (ImageButton) itemView.findViewById(R.id.btnLikeProfile);
            btnCommentProfile = (ImageButton) itemView.findViewById(R.id.btnCommentProfile);
            cardView = (CardView) itemView.getRootView();
        }
    }
}
