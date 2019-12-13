package com.android.ffmpeg

import android.app.ProgressDialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import com.ffmpeg.FFmpegCmd
import java.io.File

class MainActivity : AppCompatActivity() {

    private var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VideoHelper.copyFilesFromRaw(
            this@MainActivity,
            R.raw.input,
            "input.mp4",
            getExternalFilesDir("input")?.absolutePath
        )

        pd = ProgressDialog(this@MainActivity)
        pd?.setTitle("Processing")
        pd?.setCancelable(true)
        pd?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        pd?.setIndeterminate(true)

        val videoInput = getExternalFilesDir("input")?.absolutePath + File.separator + "input.mp4"

        bt_get_video_frame_picture.setOnClickListener {
            showProgress()

            AsyncTask.execute {
                val output =
                    getExternalFilesDir("output")?.absolutePath + File.separator + "frame.png"
                var cmd = "ffmpeg -i %s -y -f image2 -ss 8 -t 0.001 -s 1280x720 %s"

                cmd = String.format(cmd, videoInput, output)
                FFmpegCmd.run(cmd.split(" "))

                runOnUiThread {
                    dismissProgress()
                    Glide.with(this@MainActivity)
                        .load(output)
                        .into(iv_frame)
                }
            }
        }

        bt_capturing_a_section_of_video_into_gif.setOnClickListener {
            showProgress()

            AsyncTask.execute {
                val output = getExternalFilesDir("output")?.absolutePath + File.separator + "1.gif"
                var cmd = "ffmpeg -i %s -vframes 30 -y -f gif %s"

                cmd = String.format(cmd, videoInput, output)
                FFmpegCmd.run(cmd.split(" "))

                runOnUiThread {
                    dismissProgress()
                    Glide.with(this@MainActivity)
                        .load(output)
                        .into(iv_gif)
                }
            }
        }
    }

    private fun showProgress() {
        if (pd?.isShowing == false) {
            pd?.show()
        }
    }

    private fun dismissProgress() {
        if (pd?.isShowing == true) {
            pd?.dismiss()
        }
    }
}
