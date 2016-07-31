package com.thanhnv.ailatrieuphu;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by thanh on 7/19/2016.
 */
public class SoundManager{
    private Context mContext;
    private MediaPlayer mediaPlayerBG, mediaPlayer;


    public SoundManager(Context context) {
        mContext = context;
    }

    public void playSoundBG(int idSoundPlayer){
        if(mediaPlayerBG != null) {
            mediaPlayerBG.release();
        }

        mediaPlayerBG = MediaPlayer.create(mContext, idSoundPlayer);
        mediaPlayerBG.setLooping(true);
        mediaPlayerBG.start();
    }

    public void playSound(int idSound, MediaPlayer.OnCompletionListener event){
        destroySound();
        mediaPlayer = MediaPlayer.create(mContext, idSound);
        mediaPlayer.setOnCompletionListener(event);
        mediaPlayer.start();
    }

    public void destroyBG() {
        mediaPlayerBG.release();
        mediaPlayerBG = null;
    }

    public void destroySound(){
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
