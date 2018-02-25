package ht.mbds.saul.tweet.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import ht.mbds.saul.tweet.R;
import ht.mbds.saul.tweet.models.Tweet;
//import jp.wasabeef.glide.transformations.BlurTransformation;
import ht.mbds.saul.tweet.serviceApi.Utils;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
//import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

//import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
//import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by SAUL on 2/23/2018.
 */

public class TweetAdapter  extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Tweet> tweets;
    Context context;
    ImageView imProfile;
    private final int WITH_IMAGE = 0,WITH_VIDEO=1, WITHOUT_IMAGE = 2;

    public TweetAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }
    public TweetAdapter( List<Tweet> tweets) {

        this.tweets = tweets;
    }
    public Context getContext() {
        return context;
    }
    public void setClicklistener(OnItemClickListenerInterface clicklistener) {
        this.clicklistener = clicklistener;
    }

    private OnItemClickListenerInterface clicklistener;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case WITH_IMAGE:
                View v1 = inflater.inflate(R.layout.item_tweet_image, parent, false);
                viewHolder = new ViewHolderImage(v1);
                break;
            case WITH_VIDEO:
                View v2 = inflater.inflate(R.layout.item_tweet_video, parent, false);
                viewHolder = new ViewHolderVideo(v2);
                break;

            default:
                View v3= inflater.inflate(R.layout.item_tweet_no_image, parent, false);
                viewHolder = new ViewHolderNoImage(v3);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        switch (holder.getItemViewType()) {
            case WITH_IMAGE:
                ViewHolderImage viewHolderImage = (ViewHolderImage) holder;
                viewHolderImage.getTvTitle().setText(tweet.getName()+'\n'+String.format(Locale.US, "@%s", tweet.getScreenName()));
                viewHolderImage.getTvBody().setText(tweet.getBody());

           //     viewHolderImage.getTvScreen_name().setText(String.format(Locale.US, "@%s", tweet.getScreenName()));
                viewHolderImage.getTvTime().setText(Utils.getRelativeTimeAgo(tweet.getCreatedAt()));

                String _thumball=tweet.getProfileImageUrl();
                 imProfile=viewHolderImage.imProfile;
                ImageView imTweet=viewHolderImage.imTweet;
                if(!TextUtils.isEmpty(_thumball)){

                    Glide.with(getContext())
                            .load(_thumball)
                            //.override(500, 500)
                            .bitmapTransform(new RoundedCornersTransformation(getContext(), 30, 10))
                            .bitmapTransform(new CropCircleTransformation(getContext()))
                            .into(imProfile);

                }
                String _thumballmedia=tweet.getImTweet();

                if(!TextUtils.isEmpty(_thumballmedia)){

                    Glide.with(getContext())
                            .load(_thumballmedia)
                            //.override(500, 500)
                            .bitmapTransform(new RoundedCornersTransformation(getContext(), 15, 10))
                          //  .bitmapTransform(new CropCircleTransformation(getContext()))
                            .into(imTweet);
                }


                break;

            case WITH_VIDEO:
                ViewHolderVideo viewHolderVideo = (ViewHolderVideo) holder;
                viewHolderVideo.getTvTitle().setText(tweet.getName()+'\n'+String.format(Locale.US, "@%s", tweet.getScreenName()));
                viewHolderVideo.getTvBody().setText(tweet.getBody());

                //     viewHolderImage.getTvScreen_name().setText(String.format(Locale.US, "@%s", tweet.getScreenName()));
                viewHolderVideo.getTvTime().setText(Utils.getRelativeTimeAgo(tweet.getCreatedAt()));

                String _thumballProfil=tweet.getProfileImageUrl();
                imProfile=viewHolderVideo.imProfile;


                if(!TextUtils.isEmpty(_thumballProfil)){

                    Glide.with(getContext())
                            .load(_thumballProfil)
                            //.override(500, 500)
                            .bitmapTransform(new RoundedCornersTransformation(getContext(), 30, 10))
                            .bitmapTransform(new CropCircleTransformation(getContext()))
                            .into(imProfile);

                }
                String _thumballVideo=tweet.getImTweet();



                final VideoView mVideoView =viewHolderVideo.mVideoView;
                mVideoView.setVideoPath(_thumballVideo);
                MediaController mediaController = new MediaController(getContext());
                mediaController.setAnchorView(mVideoView);
                mVideoView.setMediaController(mediaController);
                mVideoView.requestFocus();
                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                       mVideoView.start();
                    }
                });




                break;


            case WITHOUT_IMAGE:
                ViewHolderNoImage viewHolderNoImage = (ViewHolderNoImage) holder;
                viewHolderNoImage.getTvTitle().setText(tweet.getName()+'\n'+String.format(Locale.US, "@%s", tweet.getScreenName()));
                viewHolderNoImage.getTvBody().setText(tweet.getBody());

            //    viewHolderNoImage.getTvScreen_name().setText(String.format(Locale.US, "@%s", tweet.getScreenName()));
                viewHolderNoImage.getTvTime().setText(Utils.getRelativeTimeAgo(tweet.getCreatedAt()));


                ImageView noImProfile=viewHolderNoImage.imProfile;
                String _thumballProf=tweet.getProfileImageUrl();
                if(!TextUtils.isEmpty(_thumballProf)){

                    Glide.with(getContext())
                            .load(_thumballProf)
                            //.override(500, 500)
                            .bitmapTransform(new RoundedCornersTransformation(getContext(), 30, 10))
                            .bitmapTransform(new CropCircleTransformation(getContext()))
                            .into(noImProfile);

                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
      //  return super.getItemViewType(position);
        if (getTweet(position).getImTweet()!=null)
        {
            if(getTweet(position).getTweet_media_type().equals("video"))
                return WITH_VIDEO;
            else
                return WITH_IMAGE;

        }

        else {
            return WITHOUT_IMAGE;
        }


    }
    @Override
    public int getItemCount() {
        return tweets.size();
    }
    public Tweet getTweet(int position) {
        return tweets.get(position);
    }
}
