package com.example.musicclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.TwoStatePreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ServiceConnection;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.musicapp.MusicInfo;
import com.example.musicclient.MainActivity;

import java.util.ArrayList;

public class customAdapter extends BaseAdapter {
    Context context;
    private final String [] songtitle;
    private final String [] songartist;
    private final Bitmap[] songimage;
    private Boolean isBound;

    public customAdapter(Context context, String [] songtitle, String [] songartist, Bitmap[] songimage){


        this.context = context;
        this.songtitle = songtitle;
        this.songartist = songartist;
        this.songimage = songimage;

    }

    @Override
    public int getCount() {
        return songtitle.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position,  View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.song_list, parent, false);
            viewHolder.song_title = (TextView) convertView.findViewById(R.id.song_title);
            viewHolder.song_artist = (TextView) convertView.findViewById(R.id.song_artist);
//            viewHolder.specific_info = (RadioButton) convertView.findViewById(R.id.specificinfo);
            viewHolder.song_image = (ImageView)convertView.findViewById(R.id.thumbnail);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.song_title.setText(songtitle[position]);
        viewHolder.song_artist.setText(songartist[position]);
        viewHolder.song_image.setImageBitmap(songimage[position]);

        return convertView;
    }

    private static class ViewHolder {

        TextView song_title;
        TextView song_artist;
        ImageView song_image;
    }



}
