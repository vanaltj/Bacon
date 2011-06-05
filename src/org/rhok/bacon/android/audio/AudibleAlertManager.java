package org.rhok.bacon.android.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class AudibleAlertManager implements Runnable, MediaPlayer.OnCompletionListener {
	private MediaPlayer alertPlayer;
	private Context context;
	private int uriID;
	private int maxVol;
	private boolean running;
	// Sound file is 30s.  This will allow 10s break.
	private static final long PLAYBACK_INTERVAL = 40000;

	public AudibleAlertManager(Context context, int uriID) {
		this.context = context;
		this.uriID = uriID;
		this.maxVol = getMaxVolume(context);
		this.running = false;
	}

	@Override
	public void run() {
		this.running = true;
		long startTime = 0;
		long currentTime = 0;
		long elapsedTime = 0;
		while (running) {
			startTime = System.currentTimeMillis();
			if((alertPlayer == null) || !alertPlayer.isPlaying()) {
				initAlertPlayer(uriID, maxVol);
				alertPlayer.start();
			}
			
			while (true) {
				currentTime = System.currentTimeMillis();
				elapsedTime = currentTime - startTime;
				if ((elapsedTime) >= PLAYBACK_INTERVAL) {
					break;
				}
				try {
					Thread.sleep(PLAYBACK_INTERVAL - elapsedTime);
				} catch (InterruptedException ex) {
					if (!running) {
						break;
					}
				}
			}
		}
		
		if((alertPlayer != null) && alertPlayer.isPlaying()) {
			alertPlayer.stop();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mp.release();
		alertPlayer = null;
	}
	
	public void stop() {
		running = false;
	}

	private void initAlertPlayer(int uriID, int maxVolume) {
		if (alertPlayer != null) {
			alertPlayer.stop();
			//onCompletion(alertPlayer);
		}
		alertPlayer = MediaPlayer.create(this.context, uriID);
		alertPlayer.setLooping(false);
		alertPlayer.setOnCompletionListener(this);
	}
	
	private int getMaxVolume(Context context) {
		return ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE))
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}
}
