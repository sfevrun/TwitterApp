package ht.mbds.saul.tweet.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.scribejava.apis.TwitterApi;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import ht.mbds.saul.tweet.R;
import ht.mbds.saul.tweet.models.Tweet;
import ht.mbds.saul.tweet.models.Tweet_Table;
import ht.mbds.saul.tweet.serviceApi.TwitterApp;
import ht.mbds.saul.tweet.serviceApi.TwitterClient;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * A simple {@link Fragment} subclass.

 */
public class MessageDialogFragment extends DialogFragment implements View.OnClickListener, TextWatcher {

    private static final int TWEET_LENGTH =140;
 private EditText edt_Tweet;
    private TextView tv_rename;
    private TextView tv_profil;
    private TextView tv_screen;
    private ImageView im_profil;
    private Button btOk;
    private ImageButton btClose;
    private Tweet tweet;
    private OnFragmentInteractionListener mListener;

    public MessageDialogFragment() {
        // Required empty public constructor
    }
    public static MessageDialogFragment newInstance(String title) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }
    /**

     */
    // TODO: Rename and change types and number of parameters
    public static MessageDialogFragment newInstance(String param1, String param2) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btOk = (Button) view.findViewById(R.id.btOk);
        btClose = (ImageButton) view.findViewById(R.id.btClose);
        edt_Tweet=(EditText)view.findViewById(R.id.tvTweet);
        tv_rename=(TextView)view.findViewById(R.id.idWacher);
        edt_Tweet.addTextChangedListener(this);
        tv_rename.setText(String.format(Locale.US, "%d", (TWEET_LENGTH)));
        tv_screen=(TextView)view.findViewById(R.id.txSc_name);
        tv_profil=(TextView)view.findViewById(R.id.txName);
        im_profil=(ImageView) view.findViewById(R.id.imProfile);
        TwitterClient client = TwitterApp.getRestClient();
        client.getAuthUser(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", "user: " + response.toString());

                try {

                    tv_profil.setText(response.getString("name"));
                   tv_screen.setText(String.format(Locale.US, "@%s", response.getString("screen_name")));
                    Glide.with(getContext())
                            .load(response.getString("profile_image_url_https"))
                        //   .override(100, 100)
                            .bitmapTransform(new RoundedCornersTransformation(getContext(), 30, 10))
                            .bitmapTransform(new CropCircleTransformation(getContext()))
                            .into(im_profil);



                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        getDialog().setTitle("New Tweet");

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComposerTweet listener = (ComposerTweet) getActivity();
                listener.onSubmitTweet(edt_Tweet.getText().toString());
                // Close the dialog and return back to the parent activity
                dismiss();
                //   return true;
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
                modaleShow();
                dismiss();
            }
        });
        List<Tweet> tweets = SQLite.select()
                .from(Tweet.class)
                .where(Tweet_Table.type.eq(1))
                .queryList();

        if (tweets.size() > 0){
            tweet = tweets.get(0);
            edt_Tweet.setText(tweet.getBody());
        }

        getDialog().setTitle("My Dialog Title");
    }
public void modaleShow(){
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
// Add the buttons
    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            // User clicked OK button
            tweet=new Tweet();
            tweet.setBody(edt_Tweet.getText().toString());
            tweet.setType(1);
            tweet.save();
        }
    });
    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            // User cancelled the dialog
            if(tweet!=null){
                tweet.delete();
            }

        }
    });
    builder.setMessage("Do you want to save your tweet for later");
    AlertDialog dialog = builder.create();
    dialog.show();
}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().setTitle("My Dialog Title");
        return inflater.inflate(R.layout.fragment_message_dialog, container, false);

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**

     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.97),(int) (size.y * 0.60));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        tv_rename.setText(String.format(Locale.US, "%d", (TWEET_LENGTH - s.length())));
    }

    public interface ComposerTweet {
        void onSubmitTweet(String tweet);
    }


}
