package com.example.musicclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.MusicInfo;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Declaring Views
    Button startServicebtn, stopServicebtn,getAllSongs;
    LinearLayout control_layout;
    ImageButton stop_song,pause_song;
    ListView lView;
    customAdapter cAdapter = null;
    protected MusicInfo musicInfo = null;
    Intent i;
    public static MediaPlayer mediaPlayer;
    int pauseLength = 0;

    boolean isServiceStarted = false;
    boolean isBound = false;
    int btnstatus;
    int btnstatus2;

    //Constants
    final int START_ALL = 1;
    final int PAUSE_SONG= 2;
    final int PLAY_SONG = 3;
    final String TAG = "MusicClient";
    final String SERVICE_NAME = "musicRes";
    final String SERVICE_PACKAGE = "com.example.musiccentral";

    public static Bitmap song_image;
    private TextView statusTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing views
        startServicebtn = findViewById(R.id.start_button);
        stopServicebtn = findViewById(R.id.stop_button);
        getAllSongs = findViewById(R.id.getallsongs);
        lView = (ListView) findViewById(R.id.listofsongs);
        stop_song = (ImageButton) findViewById(R.id.stop_music);
        pause_song = (ImageButton) findViewById(R.id.pause_music);
        control_layout = (LinearLayout)findViewById(R.id.musiccontrollayout);
        statusTxt = (TextView)findViewById(R.id.active);

        //Create explicit intent to connect to the service
        i = new Intent(SERVICE_NAME);
        i.setPackage(SERVICE_PACKAGE);
        stop_song.setEnabled(false);
        pause_song.setEnabled(false);
        getAllSongs.setEnabled(false);
        stopServicebtn.setEnabled(false);
        control_layout.setVisibility(View.INVISIBLE);
        stop_song.setVisibility(View.INVISIBLE);
        pause_song.setVisibility(View.INVISIBLE);

        startServicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isServiceStarted) {

                    //start the foreground service and bind to the service
                    startForegroundService(i);
                    bindService(i, connection, Context.BIND_AUTO_CREATE);
                    isServiceStarted = true;
                    isBound = true;
                    getAllSongs.setEnabled(true);
                    stopServicebtn.setEnabled(true);
                    stop_song.setEnabled(true);
                    pause_song.setEnabled(true);
                    Log.i(TAG,"Pressed start button");

                }else{
                    Toast.makeText(MainActivity.this, "Service is already running",Toast.LENGTH_SHORT).show();;
                }
            }
        });

        //Stop Service
        stopServicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceStarted) {
                    statusTxt.setText("Status: Service is not active");
                    unbindService(connection);
                    musicInfo = null;
                    isBound = false;
                    isServiceStarted = false;
                    startServicebtn.setEnabled(true);
                    getAllSongs.setEnabled(false);
                    lView.setAdapter(null);
//                    Log.i(TAG,"Pressed stop service button");
                }else{
                    Toast.makeText(MainActivity.this, "Service isn't running ",Toast.LENGTH_SHORT).show();;
                }

            }
        });

        //Get all songs in a list from the Music Service
        getAllSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceStarted) {
                    if(!isBound){
                        bindService(i, connection, Context.BIND_AUTO_CREATE);
                    }
                    else{
                        btnstatus = START_ALL;
                        control_layout.setVisibility(View.VISIBLE);
                        stop_song.setVisibility(View.VISIBLE);
                        pause_song.setVisibility(View.VISIBLE);
                        startServicebtn.setEnabled(false);
                        allSongInfo();
                        Log.i(TAG,"Pressed play service button");
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Service isn't running",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //stop the music playing
        stop_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        try{
                            mediaPlayer.stop();
                            mediaPlayer = null;
                        }catch(Exception ex){
                            Log.e(TAG,"Error: " + ex.getMessage());
                        }
                }
        });

        //pause/resume the music

        pause_song.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                        Log.i(TAG,String.valueOf(mediaPlayer));
                        //To pause music
                        if (btnstatus2 == PAUSE_SONG) {

                            try{
                                Log.i(TAG,"btn status is"+btnstatus);
                                btnstatus2 = PLAY_SONG;
                                pause_song.setImageResource(R.drawable.play);
                                mediaPlayer.pause();
                                pauseLength = mediaPlayer.getCurrentPosition();
                            }catch(Exception ex){
                                Log.e(TAG,"Error: " + ex.getMessage());
                            }
                            //To resume music
                        } else if (btnstatus2 == PLAY_SONG) {
                            try{
                                Log.i(TAG,"btn status is"+btnstatus);
                                Log.i(TAG,"paused");
                                btnstatus2 = PAUSE_SONG;
                                pause_song.setImageResource(R.drawable.pause);
                                mediaPlayer.seekTo(pauseLength);
                                mediaPlayer.start();
                            }catch(Exception ex){
                                Log.e(TAG,"Error: " + ex.getMessage());
                            }
                        }
                        else{
                            Log.i(TAG,"not either"+btnstatus);
                        }
            }
        });

    }

    public void allSongInfo(){
        if(!isBound) {
                bindService(i, connection, Context.BIND_AUTO_CREATE);
        }
        else
        {
            try {

                // get all info from the service
                List<SongInfo> info = musicInfo.getAllInfo();
                int length = info.size();
                String[] song_title = new String[length];
                String[] song_artist = new String[length];
                String[] song_url = new String[length];
                Bitmap[] images = new Bitmap[length];

                Log.i(TAG, "fetching info");

                Log.i(TAG,"music info size"+String.valueOf(info.size()));

                for(int i= 0; i<length;i++){
                    song_artist[i] = info.get(i).getName();
                    song_title[i] = info.get(i).getArtist();
                    images[i] = info.get(i).getImage();
                    song_url[i] = info.get(i).getSongURL();

                }

                //populate the list view
                cAdapter = new customAdapter(MainActivity.this, song_title, song_artist,images);
                lView.setAdapter(cAdapter);
                lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int lvposition, long l) {
//                        Log.i("yes","In this");

                        btnstatus2 = PAUSE_SONG;

                        if(mediaPlayer!=null){
                            mediaPlayer.stop();
                        }
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build());
                        try {
                            // get specific song url from the service
                            String newurl = musicInfo.getSongURL(lvposition);
                            mediaPlayer.setDataSource(newurl);
                            mediaPlayer.prepare();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    Log.i("im prepared","songprepared");

                                    mp.start();

                                }
                            });

                        } catch(Exception ex){
                            Log.e(TAG,"Error: " + ex.getMessage());
                        }

                        try {
                            //get specific song information from the service

                            SongInfo info = musicInfo.getSpecificSongInfo(lvposition);
                            String song_name = info.getName();
                            String song_artist = info.getArtist();
                            song_image = info.getImage();

//                            Log.i("song name is",song_name);
//                            Log.i("song artist is",song_artist);

                            // Start a new Activity to show the details of the music selected

                            Intent i = new Intent(MainActivity.this,SelectedsongActivity.class);
                            i.putExtra("song name", song_name);
                            i.putExtra("song artist",song_artist);
                            startActivity(i);

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBound){
            unbindService(connection);
        }
        stopService(i);
    }


    //Creating service connection
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicInfo = MusicInfo.Stub.asInterface(service);
            isBound = true;
//            Toast.makeText(MainActivity.this, "Service was connected.",Toast.LENGTH_SHORT).show();
            statusTxt.setText("Status: Service is active");
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicInfo = null;
            isBound = false;
//            Log.i(TAG, "Service Disconnected");
        }


    };




}





