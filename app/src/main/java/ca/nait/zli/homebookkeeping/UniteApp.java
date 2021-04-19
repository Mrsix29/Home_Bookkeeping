package ca.nait.zli.homebookkeeping;

import android.app.Application;

import ca.nait.zli.homebookkeeping.db.DBManager;



// Integrate application
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // initiate database
        DBManager.initDB(getApplicationContext());
    }
}
