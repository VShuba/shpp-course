package shuba.practice.state.states;

import shuba.practice.state.ui.MyPlayer;

/**
 * Загальний інтерфейс усіх станів.
 */
public abstract class State {
    MyPlayer player;

    /**
     * Контекст передає себе в конструктор стану, щоб стан міг
     * звертатися до його даних і методів у майбутньому, якщо буде потрібно.
     */
    State(MyPlayer player) {
        this.player = player;
    }

    public abstract String onLock();
    public abstract String onPlay();
    public abstract String onNext();
    public abstract String onPrevious();
}
