package shuba.practice.state.ui;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import shuba.practice.state.states.ReadyState;
import shuba.practice.state.states.State;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPlayer {
    public static final int NUM_OF_SONGS = 7;
    private State state;
    private boolean playing = false;
    private final List<String> playlist = new ArrayList<>();
    private int currentTrack = 0;
    private Player mp3Player;

    public MyPlayer() {
        this.state = new ReadyState(this);
        setPlaying(true);
        for (int i = 1; i <= NUM_OF_SONGS; i++) {
            playlist.add("src/main/resources/lamar/lamar" + i + ".mp3");
        }
    }

    public void changeState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isPlaying() {
        return playing;
    }

    public String startPlayback() {
        playMP3(playlist.get(currentTrack));
        return "Playing " + playlist.get(currentTrack);
    }

    public String nextTrack() {
        stopMP3();
        currentTrack++;
        if (currentTrack > playlist.size() - 1) {
            currentTrack = 0;
        }
        playMP3(playlist.get(currentTrack));
        return "Playing " + playlist.get(currentTrack);
    }

    public String previousTrack() {
        stopMP3();
        currentTrack--;
        if (currentTrack < 0) {
            currentTrack = playlist.size() - 1;
        }
        playMP3(playlist.get(currentTrack));
        return "Playing " + playlist.get(currentTrack);
    }

    public void setCurrentTrackAfterStop() {
        stopMP3();
        this.currentTrack = 0;
    }

    private void playMP3(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            mp3Player = new Player(fileInputStream);
            new Thread(() -> {
                try {
                    mp3Player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    private void stopMP3() {
        if (mp3Player != null) {
            mp3Player.close();
        }
    }
}
