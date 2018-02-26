package ht.mbds.saul.tweet.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ht.mbds.saul.tweet.R;
import ht.mbds.saul.tweet.Utils.ItemClickRecycle;
import ht.mbds.saul.tweet.adapters.TweetAdapter;
import ht.mbds.saul.tweet.models.Tweet;
import ht.mbds.saul.tweet.serviceApi.EndlessRecyclerViewScrollListener;
import ht.mbds.saul.tweet.serviceApi.TwitterApp;
import ht.mbds.saul.tweet.serviceApi.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements MessageDialogFragment.ComposerTweet {
    TwitterClient client;
    TweetAdapter adapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweet;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;

    private EndlessRecyclerViewScrollListener scrollListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client= TwitterApp.getRestClient();
        rvTweet=(RecyclerView)findViewById(R.id.rcTweet);
        tweets=new ArrayList<>();
        adapter=new TweetAdapter(this,tweets);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        rvTweet.setLayoutManager(layoutManager);

        rvTweet.addItemDecoration( new DividerItemDecoration(this, layoutManager.getOrientation()));
     //   adapter.setClicklistener(this);
        adapter.notifyDataSetChanged();
        rvTweet.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                populate(page);
                Log.d("DEBUG", "" + totalItemsCount + " Page : " + page);
            }
        };
        rvTweet.addOnScrollListener(scrollListener);



        populate(0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fl_buttom);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              //          .setAction("Action", null).show();
            }
        });


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populate(0);
            }
        });


        ItemClickRecycle.addTo(rvTweet).setOnItemClickListener(new ItemClickRecycle.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
              //  Intent detail = new Intent(TimelineActivity.this,DetailTweetActivity.class);
              //  startActivity(detail);
                Tweet twt = tweets.get(position);
                Intent intent = new Intent(TimelineActivity.this, DetailTweetActivity.class);
                intent.putExtra("tweet", Parcels.wrap(twt));
              //  intent.putExtra("tweet", twt);
                 startActivity(intent);
                //     Toast.makeText(getApplicationContext(),"You have selected pst3", Toast.LENGTH_LONG).show();


            }
        });


    }
    public  void populate(int page){
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();
        client.getTimeLime(page,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                pd.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());

                pd.dismiss();
                for (int i=0;i<response.length();i++){
                    try{
                        Tweet tw=Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tw);
                        adapter.notifyItemInserted(tweets.size()-1);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pd.dismiss();
                Log.d("DEBUG", responseString);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                pd.dismiss();
                // super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //  openDialog();
      //  showEditDialog();
            //return true;
        }

        return super.onOptionsItemSelected(item);

    }
    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        MessageDialogFragment dialogFragment = MessageDialogFragment.newInstance("Some Title");
        dialogFragment.show(fm, "fragment_message_dialoge");
    }

    @Override
    public void onSubmitTweet(final String tweet) {

        TwitterClient client = TwitterApp.getRestClient();
        Toast.makeText(this,tweet.toString(),Toast.LENGTH_LONG).show();
        client.postTweet(tweet.toString(), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", "tweet: " + response.toString());
                try {

                    final Tweet mTweet = new Tweet(response);

                    TimelineActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tweets.add(0, mTweet);
                            adapter.notifyItemInserted(0);
                            rvTweet.scrollToPosition(0);

                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "tweet: " + errorResponse.toString());
            //    super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        });
    }


}
