package org.rhok.bacon.android;

import org.rhok.bacon.android.audio.AudibleAlertManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class AlertNeedHelp extends Activity implements OnClickListener {
	private Thread aat = null;
	private AudibleAlertManager aam = null;
	private ToggleButton aac = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        aac = (ToggleButton) findViewById(R.id.togglebutton);
        aac.setOnClickListener(this);
        //MediaPlayer mp = MediaPlayer.create(this, R.raw.emergency_alert_system_attention_signal30s);
        //mp.start();
    }
	@Override
	public void onClick(View v) {
		if (v instanceof ToggleButton) {
			if (((ToggleButton) v).isChecked()) {
				aam = new AudibleAlertManager(getBaseContext(), R.raw.emergency_alert_system_attention_signal30s);
				aat = new Thread(aam);
				aat.start();
			} else {
				if (aat != null) {
					aam.stop();
					aat.interrupt();
					aat = null;
				}
			}
		}
	}
}