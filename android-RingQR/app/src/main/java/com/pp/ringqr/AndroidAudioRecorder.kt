package com.pp.ringqr

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream

class AndroidAudioRecorder(
    private val context: Context
):AudioRecorder{
    private var recorder:MediaRecorder? = null

    private fun createRecorder():MediaRecorder{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            MediaRecorder(context)
        }
        else{
            MediaRecorder()
        }
    }

    override fun start(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)
            setAudioChannels(1) // Mono audio recording
            setAudioSamplingRate(44100) // Adjust the sampling rate as needed
            setAudioEncodingBitRate(96000) // Adjust the bit rate as needed

            prepare()
            start()
            recorder = this
        }
    }
    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }
}