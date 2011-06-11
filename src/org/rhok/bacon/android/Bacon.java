package org.rhok.bacon.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Bacon extends Activity implements OnClickListener {

	Button aac, sac, cc;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bacon);
        
     // Buttons!
        aac = (Button) findViewById(R.id.bacon_alert_button);
        sac = (Button) findViewById(R.id.bacon_search_button);
        cc = (Button) findViewById(R.id.bacon_exit_button);
        
        aac.setOnClickListener(this);
        sac.setOnClickListener(this);
        cc.setOnClickListener(this);

    }

	@Override
	public void onClick(View v) {
		if (v.equals(findViewById(R.id.bacon_alert_button))) { // Enter FindMe Activity
			Intent fm = new Intent(this, FindMe.class);
			startActivity(fm);
		} else if (v.equals(findViewById(R.id.bacon_search_button))) { // Enter Search Activity
			
		} else if (v.equals(findViewById(R.id.bacon_exit_button))) { // Done.
			finish();
		}
		
	}

}
