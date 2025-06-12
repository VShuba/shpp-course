package shuba.practice.state.states;


import shuba.practice.state.ui.MyPlayer;

/**
 * Конкретні стани реалізують методи абстрактного стану по-своєму.
 */
public class PlayingState extends State {

    PlayingState(MyPlayer player) {
        super(player);
    }

    @Override
    public String onLock() {
        player.changeState(new LockedState(player));
        player.setCurrentTrackAfterStop();
        return "Stop playing";
    }

    @Override
    public String onPlay() {
        player.changeState(new ReadyState(player));
        //player.setCurrentTrackAfterStop();
        return "Paused...";
    }

    @Override
    public String onNext() {
        return player.nextTrack();
    }

    @Override
    public String onPrevious() {
        return player.previousTrack();
    }
}
