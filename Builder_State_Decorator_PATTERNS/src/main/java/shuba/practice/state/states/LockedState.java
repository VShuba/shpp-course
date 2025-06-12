package shuba.practice.state.states;


import shuba.practice.state.ui.MyPlayer;

/**
 * Конкретні стани реалізують методи абстрактного стану по-своєму.
 */
public class LockedState extends State {

    private static final String LOCKED_MASSAGE = "Locked...";

    LockedState(MyPlayer player) {
        super(player);
        player.setPlaying(false);
    }

    @Override
    public String onLock() {
        if (player.isPlaying()) {
            player.changeState(new ReadyState(player));
            return "Stop playing";
        } else {
            return LOCKED_MASSAGE;
        }
    }

    @Override
    public String onPlay() {
        player.changeState(new ReadyState(player));
        return "Ready";
    }

    @Override
    public String onNext() {
        return LOCKED_MASSAGE;
    }

    @Override
    public String onPrevious() {
        return LOCKED_MASSAGE;
    }
}
