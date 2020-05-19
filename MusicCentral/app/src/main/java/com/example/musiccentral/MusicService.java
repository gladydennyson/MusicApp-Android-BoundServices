package com.example.musiccentral;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.musicapp.MusicInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicService extends Service {

	final String TAG = "Music Service";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		//Log.i(TAG,"Start Command was called");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			startMyOwnForeground();
		else
			startForeground(1, new Notification());

		return START_NOT_STICKY;
	}

	private void startMyOwnForeground(){
		String NOTIFICATION_CHANNEL_ID = "com.example.musiccentral";
		String channelName = "My Music Service";
		NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);

		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		assert manager != null;
		manager.createNotificationChannel(chan);

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
		Notification notification = notificationBuilder.setOngoing(true)
				.setContentTitle("App is running in background")
				.setPriority(NotificationManager.IMPORTANCE_MIN)
				.setCategory(Notification.CATEGORY_SERVICE)
				.build();
		//start the foreground, helps if disconnects
		startForeground(2, notification);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent arg0) {
		//Log.i(TAG,"Service has been binded");
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		//Log.i(TAG,"Service has been Un-binded");
		return super.onUnbind(intent);
	}


	private final MusicInfo.Stub binder = new MusicInfo.Stub() {


		List<SongInfo> informstionList = Collections.synchronizedList(new ArrayList<SongInfo>());

		@Override
		public synchronized List<SongInfo> getAllInfo() throws RemoteException {

				Bitmap[] image = {BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.imaginedragons),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.honeybee),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.chandelie),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.sunsetlover),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.unstoppable),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.seeyouagain),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.havana),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.moveyourdream)
				};
				int length = getResources().getStringArray(R.array.songTitle).length;
				informstionList.clear();
				for (int i=0; i<length; i++){
					SongInfo info = new SongInfo(getResources().getStringArray(R.array.songTitle)[i],
							getResources().getStringArray(R.array.songArtist)[i], getResources().getStringArray(R.array.songURL)[i],image[i]);
					informstionList.add(info);
					Log.i(TAG, info.name);
				}
				Log.i(TAG, String.valueOf(informstionList.size()));
				return informstionList;

		}
		public synchronized SongInfo getSpecificSongInfo(int selectedSong) throws RemoteException {


				Bitmap[] image = {BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.imaginedragons),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.honeybee),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.chandelie),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.sunsetlover),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.unstoppable),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.seeyouagain),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.havana),
						BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.moveyourdream)
				};
				SongInfo songInfo = new SongInfo(getResources().getStringArray(R.array.songTitle)[selectedSong],
						getResources().getStringArray(R.array.songArtist)[selectedSong], getResources().getStringArray(R.array.songURL)[selectedSong],image[selectedSong]);
				return songInfo;


		}

		public synchronized String getSongURL(int selectedSong) throws RemoteException{

			String url = getResources().getStringArray(R.array.songURL)[selectedSong];
			return url;
		}
	};


}
