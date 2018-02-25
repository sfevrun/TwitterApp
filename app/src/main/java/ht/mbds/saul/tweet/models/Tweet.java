package ht.mbds.saul.tweet.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import ht.mbds.saul.tweet.DataLayer.MyDatabase;

/**
 * Created by SAUL on 2/23/2018.
 */
@Table(database = MyDatabase.class)
@org.parceler.Parcel(analyze={Tweet.class})
public class Tweet extends BaseModel implements Parcelable {

    @PrimaryKey
    @Column
    private int id;
    //@Column
    //@ForeignKey(saveForeignKeyModel = false)
    //private User user;
    @Column
    private String name;
    @Column
    private int idUser;
    @Column
    private String screenName;
    @Column
    private String profileImageUrl;
    @Column
    private String createdAt;
    @Column
    private String imTweet;
    @Column
    private String body;

    @Column
    private int reply_count;

    @Column
    private int retweet_count;

    @Column
    private  int favorite_count;

    @Column
    private  boolean retweeted;

    @Column
    private boolean favorited;
@Column
private int type;
@Column
private String tweet_media_type;


    public String getTweet_media_type() {
        return tweet_media_type;
    }

    public void setTweet_media_type(String tweet_media_type) {
        this.tweet_media_type = tweet_media_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int uid) {
        this.id = uid;
    }

    //public User getUser() {
   //     return user;
   // }

   // public void setUser(User user) {
    //    this.user = user;
   // }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImTweet() {
        return imTweet;
    }

    public void setImTweet(String imTweet) {
        this.imTweet = imTweet;
    }
    public Tweet(){

    }
    public Tweet(JSONObject jsonObject) throws JSONException {
        this.id=jsonObject.getInt("id");
        this.body=jsonObject.getString("text");
        this.createdAt=jsonObject.getString("created_at");

        this.idUser=jsonObject.getJSONObject("user").getInt("id");
        this.name=jsonObject.getJSONObject("user").getString("name");
        this.screenName=jsonObject.getJSONObject("user").getString("screen_name");
        this.profileImageUrl=jsonObject.getJSONObject("user").getString("profile_image_url");
       this.type=0;

        if(jsonObject.has("reply_count")) {
            this.reply_count = jsonObject.getInt("reply_count");
        }
        else
            this.reply_count = 0;
        if(jsonObject.has("retweet_count")) {
            this.retweet_count = jsonObject.getInt("retweet_count");
        }
        else
            this.retweet_count = 0;


        if(jsonObject.has("favorite_count")) {
            this.favorite_count = jsonObject.getInt("favorite_count");
        }
        else
            this.favorite_count = 0;

        this.retweeted = jsonObject.getBoolean("retweeted");
        this.favorited = jsonObject.getBoolean("favorited");
    //    this.user=User.fromJsonObject(jsonObject.getJSONObject("user"));
      /*  try{
            JSONArray _multimedia=jsonObject.getJSONObject("entities").getJSONArray("media");
            this.imTweet= _multimedia.getJSONObject(0).getString("media_url");
        }catch (JSONException ex){
            ex.printStackTrace();
        }
*/

        if (!jsonObject.isNull("extended_entities")) {
            JSONObject entitiesObject = jsonObject.getJSONObject("extended_entities");
            JSONArray mediaArray = entitiesObject.getJSONArray("media");
            if (mediaArray.length() > 0) {
                JSONObject mediaObject = mediaArray.getJSONObject(0);
                this.imTweet = mediaObject.getString("media_url_https");
                this.tweet_media_type = mediaObject.getString("type");
            }
        }
    }

    public static Tweet fromJSON(JSONObject json) throws JSONException{
        Tweet tweet=new Tweet();
      /*  try{
            JSONArray _multimedia=json.getJSONObject("entities").getJSONArray("media");
            tweet.imTweet= _multimedia.getJSONObject(0).getString("media_url");
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        */
        if (!json.isNull("extended_entities")) {
            JSONObject entitiesObject = json.getJSONObject("extended_entities");
            JSONArray mediaArray = entitiesObject.getJSONArray("media");
            if (mediaArray.length() > 0) {
                JSONObject mediaObject = mediaArray.getJSONObject(0);

                tweet.tweet_media_type = mediaObject.getString("type");

                if(mediaObject.getString("type").equals("video")){
                    tweet.imTweet = mediaArray.getJSONObject(0).getJSONObject("video_info").getJSONArray("variants").getJSONObject(0).getString("url");
                    Log.i("Media Model","Video exists");

                }else{
                    tweet.imTweet = mediaObject.getString("media_url_https");
                }
            }
        }


        tweet.id=json.getInt("id");
        tweet.body=json.getString("text");
        tweet.createdAt=json.getString("created_at");
      //  tweet.user=User.fromJsonObject(json.getJSONObject("user"));

        tweet.idUser=json.getJSONObject("user").getInt("id");
        tweet.name=json.getJSONObject("user").getString("name");
        tweet.screenName=json.getJSONObject("user").getString("screen_name");
        tweet.profileImageUrl=json.getJSONObject("user").getString("profile_image_url");
tweet.type=0;
        if(json.has("reply_count")) {
            tweet.reply_count = json.getInt("reply_count");
        }
        else
            tweet.reply_count = 0;
        if(json.has("retweet_count")) {
            tweet.retweet_count = json.getInt("retweet_count");
        }
        else
            tweet.retweet_count = 0;


        if(json.has("favorite_count")) {
            tweet.favorite_count = json.getInt("favorite_count");
        }
        else
            tweet.favorite_count = 0;

        tweet.retweeted = json.getBoolean("retweeted");
        tweet.favorited = json.getBoolean("favorited");

       tweet.save();
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray array){
        ArrayList<Tweet> results=new ArrayList<>();

        for(int i=0;i<array.length();i++){
            Tweet tweet;//=new Tweet();
            try{
                tweet=new Tweet(array.getJSONObject(i));
                results.add(tweet);
                tweet.save();
            }catch (JSONException e){
                e.printStackTrace();
            }


        }
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Tweet(Parcel in) {
        this.createdAt = in.readString();
        this.id = in.readInt();
        this.body = in.readString();
         this.reply_count = in.readInt();
        this.retweet_count = in.readInt();
        this.favorite_count = in.readInt();
        this.name=in.readString();
        this.idUser=in.readInt();
        this.screenName=in.readString();
        this.profileImageUrl=in.readString();
        this.imTweet=in.readString();
this.type=in.readInt();
        this.retweeted = in.readByte() != 0;
        this.favorited = in.readByte() != 0;

        this.tweet_media_type=in.readString();




    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(createdAt);
        parcel.writeInt(id);
        parcel.writeString(body);

        parcel.writeInt(reply_count);
        parcel.writeInt(retweet_count);
        parcel.writeInt(favorite_count);
        parcel.writeByte((byte) (retweeted ? 1 : 0));
        parcel.writeByte((byte) (favorited ? 1 : 0));


        parcel.writeString(name);
        parcel.writeInt(idUser);
        parcel.writeString(screenName);
        parcel.writeString(profileImageUrl);
        parcel.writeString(imTweet);
        parcel.writeInt(type);
        parcel.writeString(tweet_media_type);

    }


    public static List<Tweet> recentTweet() {
        return new Select().from(Tweet.class).orderBy(Tweet_Table.id, false).limit(300).queryList();
    }
}
