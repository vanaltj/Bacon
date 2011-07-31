package org.rhok.bacon.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Settings extends Activity implements OnClickListener {

    private Button bc = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        bc = (Button) findViewById(R.id.settings_back_button);
        bc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(bc)) {
            finish();
        }
    }
}
