package com.example.musicclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedsongActivity extends AppCompatActivity {
    TextView displaytxt;
    private TextView songName,songArtist;
    private ImageView songImage;
    private Button button;
    private int PAUSE = 1;
    private int btn_status = PAUSE;

    private int RESUME = 2;
    private ImageButton stop,pause;
    private int pause_length;
    private String TAG = "Selected song activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_song);
        songName = (TextView)findViewById(R.id.songName);
        songArtist = (TextView)findViewById(R.id.songArtist);
        songImage = (ImageView)findViewById(R.id.songImage);
        Intent intent = getIntent();
        String song_name = intent.getStringExtra("song name");
        String song_artist = intent.getStringExtra("song artist");

        // Display song name, artist name and picture
        songName.setText(song_name);
        songArtist.setText(song_artist);
        songImage.setImageBitmap(MainActivity.song_image);


    }


}
