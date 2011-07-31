package org.rhok.bacon.android;

import org.rhok.bacon.android.services.BaconBluetoothService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Bacon extends Activity implements OnClickListener {
    
    private Animation slideLeftIn, slideLeftOut, slideRightIn, slideRightOut;
    private ViewFlipper vf;
    private BaconTouchListener tl;

    private ServiceConnection svcConn = new BaconServiceConnection();
    private BaconBluetoothService bt = null;
    private boolean isBound = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBaseContext().bindService(new Intent(this.getApplicationContext(), BaconBluetoothService.class),
                svcConn, Context.BIND_AUTO_CREATE);
        setContentView(R.layout.bacon);
        
        listenToButtons();

        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        
        vf = (ViewFlipper)findViewById(R.id.bacon_viewflipper);
        vf.setDisplayedChild(1);
        tl = new BaconTouchListener();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(findViewById(R.id.bacon_alert_button))) { // Enter FindMe
                                                               // Activity
            Intent fm = new Intent(this, FindMe.class);
            startActivity(fm);
        } else if (v.equals(findViewById(R.id.bacon_exit_button))) {
            finish();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (tl.getGestureDetector().onTouchEvent(event)) {
            return true;
        } else {
            return false;
        }
    }

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(getApplicationContext(), BaconBluetoothService.class), 
                svcConn, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    void doUnbindService() {
        if (isBound) {
            // Detach our existing connection.
            unbindService(svcConn);
            isBound = false;
        }
    }
    
    private void listenToButtons() {
        Button b = (Button) findViewById(R.id.bacon_alert_button);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.bacon_exit_button);
        b.setOnClickListener(this);
    }

    private class BaconServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className, IBinder service) {
            bt = ((BaconBluetoothService.BaconBinder) service).getService();
            Toast.makeText(getApplication().getBaseContext(), R.string.bluetooth_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            bt = null;
            Toast.makeText(getApplication().getBaseContext(), R.string.bluetooth_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class BaconTouchListener implements View.OnTouchListener {

        private GestureDetector gd;

        public BaconTouchListener() {
            gd = new GestureDetector(new BaconSwipeGestureListener());
        }
        
        public GestureDetector getGestureDetector() {
            return gd;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (gd.onTouchEvent(event)) {
                return true;
            }
            return false;
        }
        
        private class BaconSwipeGestureListener extends SimpleOnGestureListener {
            private static final int SWIPE_MIN_DISTANCE = 120;
            private static final int SWIPE_MAX_OFF_AXIS = 250;
            private static final int SWIPE_THRESHOLD_VELOCITY = 200;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e1.getY() - e2.getY()) >= SWIPE_MAX_OFF_AXIS) {
                    return super.onFling(e1, e2, velocityX, velocityY);
                } else if (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && // -> swipe
                            vf.getDisplayedChild() < vf.getChildCount() - 1) {
                        vf.setInAnimation(slideLeftIn);
                        vf.setOutAnimation(slideLeftOut);
                        vf.showNext();
                        return true;
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && // <- swipe
                            vf.getDisplayedChild() > 0) {
                        vf.setInAnimation(slideRightIn);
                        vf.setOutAnimation(slideRightOut);
                        vf.showPrevious();
                        return true;
                    }
                }
                return false;
            }
        }
    }
}
