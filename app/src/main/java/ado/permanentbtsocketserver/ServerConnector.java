package ado.permanentbtsocketserver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ado on 12/07/15.
 *
 *
 * - listen for connections with well known UUID from knwon devices
 *
 * Assumptions:
 * 1. devices are already paired
 * 2. Bt is on for both devices
 * 3. they already know the service UUID
 * 4. they both support secure socket (BT2.1+)
 */
public class ServerConnector {

    private static final String TAG = "ServerConnector";

    public static final UUID SERVER_UUID = UUID.fromString("cf5169a6-2897-11e5-b345-feff819cdc9f");

    private final BluetoothAdapter mAdapter;

    private BluetoothServerSocket mServerSocket;

    private boolean mRunning = false;

    public ServerConnector() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void startListening() {
        if(!mRunning) {
            mRunning = true;
            final Thread t = new Thread(new ListenService());
            t.start();
        }
    }

    public void stopListening() {
        if(mRunning) {
            mRunning = false;
            try {
                mServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "ListenService.stopListening() exception closing server socket");
            }
        }
    }

    public boolean isListening() {
        return mRunning;
    }

    private class ListenService implements Runnable {

        @Override
        public void run() {
            //create one if null
            if(mServerSocket == null) {
                try {
                    mServerSocket = mAdapter.listenUsingRfcommWithServiceRecord("BtSocketService", SERVER_UUID);
                } catch(IOException e) {
                    throw new IllegalStateException("can't create a server socket. BT down?");
                }
            }
            //continuos listening
            while(mRunning) {
                try {
                    mServerSocket.accept();
                    Log.d(TAG, "ListenService.run() accepted a new client!");
                } catch (IOException e) {
                    Log.e(TAG, "ListenService.run() accept() call aborted");
                }
            }

        }
    }

}
