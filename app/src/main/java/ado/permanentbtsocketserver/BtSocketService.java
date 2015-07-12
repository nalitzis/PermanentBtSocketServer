package ado.permanentbtsocketserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Ado on 12/07/15.
 */
public class BtSocketService extends Service {

    private static final String TAG = "BtSocketService";

    private ServerConnector mServerConnector;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate () {
        super.onCreate();
        mServerConnector = new ServerConnector();
    }

    @Override
    public void onDestroy () {
        mServerConnector.stopListening();
        Log.w(TAG, "onDestroy() called, BtSocket will be closed!!");
        super.onDestroy();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        if(!mServerConnector.isListening()) {
            mServerConnector.startListening();
        }
        Log.w(TAG, "onStartCommand() called, BtSocket will listen...");
        return START_STICKY;
    }

}
