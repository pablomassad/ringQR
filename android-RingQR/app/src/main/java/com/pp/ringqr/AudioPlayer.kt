package com.pp.ringqr

import java.io.File

interface AudioPlayer {
    fun playFile(file:  File)
    fun stop ()
}