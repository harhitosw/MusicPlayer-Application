package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Playsong extends AppCompatActivity {
TextView textView;
ImageView previous,next,play;
ArrayList<File> songs;
MediaPlayer mediaplayer;
String Gaanekanaam;
int position;
SeekBar seekBar;
Thread UpdateSeek;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.stop();
        mediaplayer.release();
        UpdateSeek.interrupt();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong2);
        textView=findViewById(R.id.textView);
        play = findViewById(R.id.pause);
        previous=findViewById(R.id.previous);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList)bundle.getParcelableArrayList("songlist");
        Gaanekanaam=intent.getStringExtra("CurrentSong");
        textView.setText(Gaanekanaam);
        position=intent.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaplayer=MediaPlayer.create(this,uri);
        mediaplayer.start();
        seekBar.setMax(mediaplayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               mediaplayer.seekTo(seekBar.getProgress());
            }
        });
        UpdateSeek=new Thread(){
            @Override
            public void run() {
               int Currpose=0;
               try{
                   while(Currpose<mediaplayer.getDuration()){
                       Currpose=mediaplayer.getCurrentPosition();
                       seekBar.setProgress(Currpose);
                       sleep(800);
                   }

               }
               catch(Exception e){
                   e.printStackTrace();
               }


            }
        };
     UpdateSeek.start();
     play.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if(mediaplayer.isPlaying()){
                 play.setImageResource(R.drawable.play);
                 mediaplayer.pause();
             }
             else{
                 play.setImageResource(R.drawable.pause);
                 mediaplayer.start();
             }
         }
     });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              mediaplayer.stop();
              mediaplayer.release();
              if(position!=songs.size()-1){
                  position=position-1;
              }
              else{
                  position=songs.size()-1;
              }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaplayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaplayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaplayer.getDuration());
                Gaanekanaam=songs.get(position).getName().toString();
                textView.setText(Gaanekanaam);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaplayer.stop();
                mediaplayer.release();
                if(position!=songs.size()-1){
                    position=position+1;
                }
                else{
                    position=0;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaplayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaplayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaplayer.getDuration());
                Gaanekanaam=songs.get(position).getName().toString();
                textView.setText(Gaanekanaam);
            }
        });



        
    }
}