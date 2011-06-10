package org.rhok.bacon.android;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class AlertNeedHelp extends Activity implements OnClickListener {
	
	private ToggleButton aac = null;
	private MediaPlayer mp;
	boolean active;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        active = false;
        setContentView(R.layout.main);
        aac = (ToggleButton) findViewById(R.id.togglebutton);
        aac.setOnClickListener(this);
        mp = MediaPlayer.create(this, R.raw.emergency_alert_system_attention_signal40s_loop);
		mp.setLooping(true);
    }
	@Override
	public void onClick(View v) {
		if (v.equals(aac)) { // Audible Alert toggle control.
			if (((ToggleButton) v).isChecked()) {
				this.mp.start();
			} else {
				active = false;
				this.mp.stop();
				try {
					this.mp.prepare();
				} catch (IllegalStateException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		}
	}
}