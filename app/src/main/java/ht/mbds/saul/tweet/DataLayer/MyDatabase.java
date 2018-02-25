package ht.mbds.saul.tweet.DataLayer;

/**
 * Created by SAUL on 2/24/2018.
 */
import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String NAME = "TwitterDatabase";

    public static final int VERSION = 1;
}
