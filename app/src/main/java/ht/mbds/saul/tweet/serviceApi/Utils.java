package ht.mbds.saul.tweet.serviceApi;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by SAUL on 2/25/2018.
 */

public class Utils {

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = getTwitterReldate(dateMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public static String getTwitterReldate(long date){
        String ret = DateUtils.getRelativeTimeSpanString(date, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        String [] strings = ret.split(" ");
        if(strings.length == 3){
            if(strings[0].equalsIgnoreCase("in")){
                return "0s";
            }
            return strings[0].concat(Character.toString((strings[1].charAt(0))));
        }else{
            return ret;
        }
    }
}
