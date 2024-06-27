package com.example.ab.ui.slideshow;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ab.R;
import com.example.ab.databinding.FragmentSlideshowBinding;

import java.util.concurrent.TimeUnit;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private MediaPlayer mediaPlayer;
    private ImageButton btnPlayPause;
    private SeekBar seekBar;
    private Handler seekBarHandler;
    private Runnable updateSeekBar;

    private boolean isPlaying = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize views
        btnPlayPause = binding.btnPlayPause;
        seekBar = binding.seekBar;

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.audio);
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        // Initialize seek bar max duration
        seekBar.setMax(mediaPlayer.getDuration());

        // Set initial state of the button and seek bar
        updatePlayPauseButton();

        // Set click listener for play/pause button
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pausePlayback();
                } else {
                    startPlayback();
                }
            }
        });

        // SeekBar listener for tracking progress
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Handler and Runnable for updating seek bar progress
        seekBarHandler = new Handler();
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    seekBarHandler.postDelayed(this, 100); // Update seek bar every 100ms
                }
            }
        };

        return root;
    }

    private void startPlayback() {
        mediaPlayer.start();
        isPlaying = true;
        updatePlayPauseButton();
        seekBarHandler.postDelayed(updateSeekBar, 0); // Start updating seek bar
    }

    private void pausePlayback() {
        mediaPlayer.pause();
        isPlaying = false;
        updatePlayPauseButton();
        seekBarHandler.removeCallbacks(updateSeekBar); // Stop updating seek bar
    }

    private void updatePlayPauseButton() {
        if (isPlaying) {
            btnPlayPause.setImageResource(R.drawable.ic_pause); // Change to pause icon
        } else {
            btnPlayPause.setImageResource(R.drawable.ic_play); // Change to play icon
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        // Release the MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Remove callbacks and messages from Handler to prevent memory leaks
        seekBarHandler.removeCallbacks(updateSeekBar);
    }
}