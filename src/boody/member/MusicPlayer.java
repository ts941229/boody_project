package boody.member;

import java.util.ArrayList;

import boody.vo.MusicVO;
import javazoom.jl.player.MP3Player;

public class MusicPlayer {
    private ArrayList<MusicVO> playlist = new ArrayList<MusicVO>();
    private MP3Player mp3Player;
    private int index;

    public MusicPlayer(ArrayList<MusicVO> playlist) {
        this.playlist = playlist;
        this.mp3Player = new MP3Player();
    }

    public ArrayList<MusicVO> getPlaylist() {
        return playlist;
    }

    public void indexNum(int index) {
        this.index = index;
    }

    public MusicVO play1() {
        MusicVO m = playlist.get(0);
        if(mp3Player.isPlaying()) {
            mp3Player.stop();
        }
        mp3Player.play(m.getPath());

        return m;
    }
    
    public MusicVO play2() {
        MusicVO m = playlist.get(1);
        if(mp3Player.isPlaying()) {
            mp3Player.stop();
        }
        mp3Player.play(m.getPath());

        return m;
    }
    
    public MusicVO play3() {
        MusicVO m = playlist.get(2);
        if(mp3Player.isPlaying()) {
            mp3Player.stop();
        }
        mp3Player.play(m.getPath());

        return m;
    }

    public void stop() {
        if(mp3Player.isPlaying()) {
            mp3Player.stop();
        }
    }

}