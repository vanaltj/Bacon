package org.rhok.bacon.android.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class BaconBluetoothService extends Service {

    private BluetoothAdapter bt;
    private final IBinder binder = new BaconBinder();

    public static final int RQ_BT_ENABLE = 1;

    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }
    
    @Override
    public void onCreate() {
        tryEnableBluetooth();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "In onStartCommand()", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "In onDestroy()", Toast.LENGTH_SHORT).show();
        disableBluetoothIfNeeded();
    }

    private void tryEnableBluetooth() {
        bt = BluetoothAdapter.getDefaultAdapter();
        if (bt != null) {
            if (!bt.isEnabled()) {
                startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }
            
            if (!bt.isEnabled()) {
                // I guess the user doesn't want bluetooth started.  Apply settings accordingly.
            } else {
                // Do tha Thang
            }
        }
    }
    
    private void disableBluetoothIfNeeded() {
        
    }

    public class BaconBinder extends Binder {
        public BaconBluetoothService getService() {
            return BaconBluetoothService.this;
        }
    }
    
    private class AutoBluetoothThread extends Thread {

        @Override
        public void run() {
            
        }
    }
}
