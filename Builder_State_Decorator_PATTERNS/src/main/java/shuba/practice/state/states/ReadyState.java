package shuba.practice.state.states;

import shuba.practice.state.ui.MyPlayer;

/**
 * Конкретні стани реалізують методи абстрактного стану по-своєму.
 * Вони також можуть переводити контекст в інші стани.
 */
public class ReadyState extends State {

    private static final String LOCKED_MASSAGE = "Locked...";
    public ReadyState(MyPlayer player) {
        super(player);
    }

    @Override
    public String onLock() {
        player.changeState(new LockedState(player));
        return LOCKED_MASSAGE;
    }

    @Override
    public String onPlay() {
        String action = player.startPlayback();
        player.changeState(new PlayingState(player));
        return action;
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
