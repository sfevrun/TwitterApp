package ht.mbds.saul.tweet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.Locale;

import ht.mbds.saul.tweet.R;
import ht.mbds.saul.tweet.models.Tweet;
import ht.mbds.saul.tweet.serviceApi.Utils;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class DetailTweetActivity extends AppCompatActivity {
    TextView tvBody;
    TextView tvTitle;
    ImageView imProfile;
    ImageView imTweet;
    //  TextView tvScreen_name;
    TextView tvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);

        imProfile=(ImageView) findViewById(R.id.imProfile);
        imTweet=(ImageView)findViewById(R.id.imTweet);
        tvTitle=(TextView) findViewById(R.id.tvTitle);
        tvBody=(TextView) findViewById(R.id.tvBody);

        tvTime=(TextView) findViewById(R.id.tvTime);

        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        tvTitle.setText(tweet.getName()+'\n'+String.format(Locale.US, "@%s", tweet.getScreenName()));
        tvTime.setText(Utils.getRelativeTimeAgo(tweet.getCreatedAt()));
      tvBody.setText(tweet.getBody());
        String _thumball=tweet.getProfileImageUrl();
     //   tvBody.setText(_thumball);
            Glide.with(getContext())
                    .load(_thumball)
                    //.override(500, 500)
                    .bitmapTransform(new RoundedCornersTransformation(getContext(), 30, 10))
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(imProfile);


    }
}
