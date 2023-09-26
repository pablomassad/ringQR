package com.pp.ringqr

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}