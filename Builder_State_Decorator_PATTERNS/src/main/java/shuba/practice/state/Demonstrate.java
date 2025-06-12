package shuba.practice.state;

import shuba.practice.state.ui.MyPlayer;
import shuba.practice.state.ui.UI;

public class Demonstrate {
    public static void main(String[] args) {
        MyPlayer player = new MyPlayer();
        UI ui = new UI(player);
        ui.init();
    }
}
