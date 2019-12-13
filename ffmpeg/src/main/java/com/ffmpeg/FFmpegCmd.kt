package com.ffmpeg

object FFmpegCmd {

    init {
        System.loadLibrary("lib-ffmpeg")
    }

    fun run(cmd: List<String>): Int {
        return run(cmd.size, cmd.toTypedArray())
    }

    private external fun run(cmdLen: Int, cmd: Array<String>): Int
}