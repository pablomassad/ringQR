package com.pp.ringqr

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import android.widget.Toast
import androidx.core.app.NotificationCompat

private lateinit var mediaPlayer: MediaPlayer

class AudioRecorderService: Service() {
    private var audioFile:File?=null
    private lateinit var audioManager: AudioManager
    private val recorder = MediaRecorder()
    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
    private lateinit var handler: Handler
    private var isRecording = false

    private val binder = LocalBinder()
    inner class LocalBinder : Binder() {
        fun getService(): AudioRecorderService {
            return this@AudioRecorderService
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        Log.v("PPP AudioRecSrv", "onBind")
        return binder
    }
    override fun onCreate() {
        super.onCreate()
        Log.v("PPP AudioRecSrv", "onCreate")
        Toast.makeText(applicationContext, "start foreground", Toast.LENGTH_SHORT).show()
//        val notification = NotificationCompat.Builder(this, "audioRecSrv")
//            .setContentTitle("Audio Recorder")
//            .setContentText("Recon voice...")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .build()
//        startForeground(11, notification)

//        val serviceIntent = Intent(applicationContext, AudioRecorderService::class.java)
//        startForegroundService(serviceIntent)

//        val intent = Intent(this, AudioRecorderService::class.java)
//        val serviceIntent = Intent(this, AudioRecorderService::class.java)
//        startForegroundService(serviceIntent)
//        Log.v("PPP Foreground starting....", topicname)

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v("PPP AudioRecSrv", "onStartCommand")
//        val thread = Thread(MyRunRec())
//        thread.start()
        return START_STICKY
    }
    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
        Log.v("PPP AudioRecSrv", "onUnbind")
    }
    override fun onDestroy() {
        Log.v("PPP AudioRecSrv", "onDestroy")
        Toast.makeText(applicationContext, "stop service", Toast.LENGTH_SHORT).show()
//        recorder.stop()
//        recorder.release()
//        applicationContext.stopService(this)
        super.onDestroy()
    }

    fun startRecon(){
//        // Create the handler
//        handler = Handler(Looper.getMainLooper())
//        // Create the timer task
//        timerTask = object : Runnable {
//            override fun run() {
//                if (recorder.isSpeechDetected()) {
//                    // Handle speech
//                }
//                handler.postDelayed(this, 100)
//            }
//        }
//        handler.postDelayed(timerTask, 100)


//        // Create an AudioRecord instance
//        val minBufferSize = AudioRecord.getMinBufferSize(
//            RECORDER_SAMPLE_RATE,
//            RECORDER_CHANNELS,
//            RECORDER_AUDIO_FORMAT
//        )
//        recorder = AudioRecord(
//            RECORDER_AUDIO_SOURCE,
//            RECORDER_SAMPLE_RATE,
//            RECORDER_CHANNELS,
//            RECORDER_AUDIO_FORMAT,
//            minBufferSize
//        )

//                File(filesDir, "audio.mp3 ").also {
//                    recorder.start(it)
//                    audioFile = it
//                    Log.v("PPP recording", "ok")
//                }

        Log.v("PPP AudioRecSrv", "startRecon")
        Toast.makeText(applicationContext, "startRecon", Toast.LENGTH_SHORT).show()
        handler = Handler()
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                Log.v("PPP onResults", "....")
//                if (results != null && results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
//                    val recognizedText = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)!!.first()
//                    Log.v("PPP onResults", recognizedText)
//                }
            }
            override fun onPartialResults(partialResults: Bundle?) {
                Log.v("PPP onPartialResults", partialResults.toString())
            }
            override fun onError(error: Int) {
                Log.v("PPP onError", error.toString())
            }
            override fun onEvent(eventType: Int, params: Bundle?) {
                Log.v("PPP onEvent", eventType.toString())
            }
            override fun onBeginningOfSpeech() {
                Toast.makeText(applicationContext, "onBeginningOfSpeech", Toast.LENGTH_SHORT).show()
                beep(1f)
//                File(filesDir, "audio.mp3 ").also {
//                    audioFile = it
////                        recorder.start(it)
//                }
                // Start recording audio
                if (!isRecording) {
                    Log.v("PPP begin record", "onBeginningOfSpeech")
//                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
//                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
//                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//                    recorder.setOutputFile(FileOutputStream(audioFile).fd)
//                    recorder.prepare()
//                    recorder.start()
                    isRecording = true
                }
            }
            override fun onEndOfSpeech() {
                beep(0.3f)
                Log.v("PPP onEndOfSpeech", isRecording.toString())

                if (isRecording) {
                    handler.postDelayed({
                        val isSpeaking = audioManager.isMicrophoneMute
                        Toast.makeText(applicationContext, "isSpeaking: $isSpeaking", Toast.LENGTH_SHORT).show()
                        Log.v("PPP isSpeaking", isSpeaking.toString())
                        if (isSpeaking) {
                            Log.v("PPP restart recon", "onEndOfSpeech")
                            //recorder.start()
                        } else {
//                            recorder.stop()
//                            recorder.release()
                            isRecording = false
                            Log.v("PPP ready to FB", "onEndOfSpeech")
//                    saveToFB(audioFile)
                        }
                    }, 5000)
                }
            }
            override fun onReadyForSpeech(params: Bundle?) {
                Log.v("PPP onReadyForSpeech", "ok")
            }
            override fun onRmsChanged(rmsdB: Float) {
                Log.v("PPP onRmsChanged", "ok")
            }
            override fun onBufferReceived(buffer: ByteArray?) {
                Log.v("PPP onBufferReceived", "ok")
            }
        })
        speechRecognizer.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH))
    }
    fun stopRecon(){
        Log.v("PPP AudioRecSrv", "stopRecon")
        Toast.makeText(applicationContext, "stopRecon", Toast.LENGTH_SHORT).show()
        speechRecognizer.stopListening()
    }
    private fun saveToFB(auFile:File?){
        Log.v("PPP stopRec", "stop")
        val firebaseStorage = FirebaseStorage.getInstance()
        val storageReference = firebaseStorage.getReference("audio")

        //val filePath = audioFile?.absolutePath
        //Log.v("PPP sto path", filePath.toString())
        //storageReference.putFile(Uri.parse(filePath))
        //val au = File(filesDir, "audio.mp3 ")
        auFile?.let {
            storageReference.putFile(it.toUri())
                .addOnCompleteListener{task->
                    if (task.isSuccessful) {
                        Log.v("PPP sto OK", "storage")
                    } else {
                        Log.v("PPP sto Error", task.exception?.stackTraceToString().toString())
                    }
                }
        }
    }
    private fun beep(vol: Float){
        mediaPlayer = MediaPlayer.create(this, R.raw.msg)
        mediaPlayer.setVolume(vol,vol)
        mediaPlayer.start()
    }


//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        // Start recording audio
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//        recorder.setOutputFile("/sdcard/audio_recording.aac")
//        recorder.prepare()
//        recorder.start()
//
//        // Schedule a timer to stop recording audio after 30 seconds
//        val timer = Timer()
//        timer.schedule(object : TimerTask() {
//            override fun run() {
//                // Stop recording audio
//                recorder.stop()
//                recorder.release()
//            }
//        }, 30000)
//        return Service.START_NOT_STICKY
//    }
}


