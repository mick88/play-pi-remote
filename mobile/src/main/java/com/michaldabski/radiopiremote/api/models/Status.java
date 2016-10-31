package com.michaldabski.radiopiremote.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by Michal on 31/10/2016.
 */

public class Status {
    public static final String
        STATE_PLAY = "play",
        STATE_STOP = "stop",
        STATE_PAUSE = "pause";

    @SerializedName("volume")
    String volume;
    @SerializedName("repeat")
    Boolean repeat;
    @SerializedName("random")
    Boolean random;
    @SerializedName("single")
    Boolean single;
    @SerializedName("consume")
    Boolean consume;
    @SerializedName("playlist")
    Integer playlist;
    @SerializedName("playlistlength")
    Integer playlistLength;
    @SerializedName("state")
    String state;
    @SerializedName("song")
    Integer song;
    @SerializedName("songid")
    Integer songId;
    @SerializedName("nextsong")
    Integer nextSong;
    @SerializedName("nextsongid")
    Integer nextSongId;
    @SerializedName("time")
    String time;
    @SerializedName("elapsed")
    String elapsed;
    @SerializedName("bitrate")
    Integer bitrate;
    @SerializedName("mixrampdb")
    String mixrampdb;
    @SerializedName("audio")
    String audio;

    /**
     * Gets volume as an integer
     */
    public Integer getVolumeInt() {
        if (this.volume == null) return null;
        return Integer.valueOf(this.volume);
    }

    public String getVolume() {
        return volume;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public Boolean getRandom() {
        return random;
    }

    public Boolean getSingle() {
        return single;
    }

    public Boolean getConsume() {
        return consume;
    }

    public Integer getPlaylist() {
        return playlist;
    }

    public Integer getPlaylistLength() {
        return playlistLength;
    }

    public String getState() {
        return state;
    }

    public Integer getSong() {
        return song;
    }

    public Integer getSongId() {
        return songId;
    }

    public Integer getNextSong() {
        return nextSong;
    }

    public Integer getNextSongId() {
        return nextSongId;
    }

    public String getTime() {
        return time;
    }

    public String getElapsed() {
        return elapsed;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public String getMixrampdb() {
        return mixrampdb;
    }

    public String getAudio() {
        return audio;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setVolumeInt(int volume) {
        this.volume = String.valueOf(volume);
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public void setRandom(Boolean random) {
        this.random = random;
    }

    public void setSingle(Boolean single) {
        this.single = single;
    }

    public void setConsume(Boolean consume) {
        this.consume = consume;
    }

    public void setPlaylist(Integer playlist) {
        this.playlist = playlist;
    }

    public void setPlaylistLength(Integer playlistLength) {
        this.playlistLength = playlistLength;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setSong(Integer song) {
        this.song = song;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public void setNextSong(Integer nextSong) {
        this.nextSong = nextSong;
    }

    public void setNextSongId(Integer nextSongId) {
        this.nextSongId = nextSongId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setElapsed(String elapsed) {
        this.elapsed = elapsed;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public void setMixrampdb(String mixrampdb) {
        this.mixrampdb = mixrampdb;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public boolean isLastItem() {
        return Objects.equals(song, playlistLength);
    }

    public boolean isFirstItem() {
        return song != null && song.equals(0);
    }
}
