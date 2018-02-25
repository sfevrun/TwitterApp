package ht.mbds.saul.tweet.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ht.mbds.saul.tweet.R;

/**
 * Created by SAUL on 2/23/2018.
 */

public class ViewHolderNoImage extends RecyclerView.ViewHolder{
    //  ImageView imageView;
    TextView tvBody;
    TextView tvTitle;
    ImageView imProfile;
  //  TextView tvScreen_name;
    TextView tvTime;


    public TextView getTvTime() {
        return tvTime;
    }

    public void setTvTime(TextView tvTime) {
        this.tvTime = tvTime;
    }

    public TextView getTvBody() {
        return tvBody;
    }

    public void setTvBody(TextView tvBody) {
        this.tvBody = tvBody;
    }

    public ImageView getImProfile() {
        return imProfile;
    }

    public void setImProfile(ImageView imProfile) {
        this.imProfile = imProfile;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public ViewHolderNoImage(View view) {
        super(view);
        imProfile=(ImageView) view.findViewById(R.id.imProfile);
        tvTitle=(TextView) view.findViewById(R.id.tvTitle);
        tvBody=(TextView) view.findViewById(R.id.tvBody);

        tvTime=(TextView) view.findViewById(R.id.tvTime);
     //   tvScreen_name=(TextView) view.findViewById(R.id.tvScreen_name);


    }
}
