package com.fs.antitheftsdk.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.fs.antitheftsdk.R
import com.fs.antitheftsdk.base.Constants

/* This class is use trigger alarm inside the application */
internal class AlarmThread internal constructor(var ctx: Context) : Thread() {
    var mp: MediaPlayer? = null
    companion object{
        var increment:Int=0
    }

    override fun run() {
        super.run()
        val filterLogin = IntentFilter(Constants.BROADCAST_ALARM)
        LocalBroadcastManager.getInstance(ctx).registerReceiver(broadcastReceiver, filterLogin)
        var start = false
        try {
            val audio = ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val max = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val setVolFlags =
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE or AudioManager.FLAG_VIBRATE
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, max, setVolFlags)
            audio.setMode(AudioManager.MODE_IN_COMMUNICATION)
           audio.setSpeakerphoneOn(true);
            mp = MediaPlayer.create(ctx, R.raw.siren)
            mp?.setLooping(true)
            mp?.start()
            val mp3Listener: Mp3OnCompletionListener = Mp3OnCompletionListener()
            mp?.setOnCompletionListener(mp3Listener)
            start = true
            var i = 0
            while (i < 600) {
                sleep(500)
                val currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
                if (currentVolume != max) {
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, max, setVolFlags)
                }
                i++
            }

           // sleep(400000)

            mp?.stop()
        } catch (e: Exception) {
            Log.i(Constants.EXCEPTION, e.toString())
        } finally {
            mp?.release()
            mp = null
        }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (mp != null) {
                try {
                    mp?.stop()

                } catch (e: Exception) {
                    Log.i(Constants.EXCEPTION, e.toString())
                } finally {
                    mp?.release()
                    mp = null
                }

            }
        }
    }

    internal inner class Mp3OnCompletionListener : MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer) {
            mp.stop()
            if(increment<120) {
                AlarmThread(ctx).run()
                increment++
            }
        }
    }
}