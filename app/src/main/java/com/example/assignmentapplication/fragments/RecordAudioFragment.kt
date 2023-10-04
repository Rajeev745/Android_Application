package com.example.assignmentapplication.fragments

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.assignmentapplication.R
import com.example.assignmentapplication.databinding.FragmentRecordAudioBinding
import java.io.File

class RecordAudioFragment : Fragment() {

    var binding: FragmentRecordAudioBinding? = null
    private var mediaRecorder = MediaRecorder()
    private var isRecording = false
    private var recordedFilePath: String? = null

    private val audioFile: String by lazy {
        "${Environment.getExternalStorageDirectory().absolutePath}/recorded_audio.3gp"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordAudioBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.startRecoding?.setOnClickListener {
            if (!isRecording) {
                // Start recording audio
                startRecording()
            } else {
                // Stop recording audio
                stopRecording()
                isRecording = false
            }
        }

        binding?.playRecoding?.setOnClickListener {
            playRecording()
        }

        binding?.backBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_recordAudioFragment_to_homeFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun startRecording() {
        try {
            mediaRecorder = MediaRecorder()
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            val recordedFile =
                File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                    "recording.mp4"
                )
            recordedFilePath = recordedFile.absolutePath
            mediaRecorder.setOutputFile(recordedFilePath)
            mediaRecorder.prepare()
            mediaRecorder.start()
            isRecording = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        // Stop recording
        mediaRecorder.stop()

        // Release the MediaRecorder
        mediaRecorder.release()
    }

    private fun playRecording() {
        // Create a MediaPlayer object to play the recorded audio
        val mediaPlayer = MediaPlayer()

        // Set the data source for the MediaPlayer
        if (recordedFilePath == null) {
            Toast.makeText(
                requireContext(),
                "Unable to play audio check for permissions",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        mediaPlayer.setDataSource(recordedFilePath)

        // Prepare the MediaPlayer
        mediaPlayer.prepare()

        // Start playing the recorded audio
        mediaPlayer.start()
    }

}