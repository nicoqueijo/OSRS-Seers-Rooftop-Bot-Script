import org.dreambot.api.methods.magic.Normal;

import java.awt.Rectangle;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

public class EndNode extends Node {
    public EndNode(SeersRooftop main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return main.ENDING_AREA.contains(main.getLocalPlayer().getTile());
    }

    @Override
    public int execute() {
        sleep(50, 125);
        main.getMagic().castSpell(Normal.CAMELOT_TELEPORT);
        moveMouseToStartArea();
        main.adjustCamera(SeersRooftop.State.END);
        sleepUntil(() -> main.determineState(main.getLocalPlayer()) !=
                SeersRooftop.State.END, random(4000, 5000));
        return main.sleepTime();
    }

    private void moveMouseToStartArea() {
        Rectangle startArea = new Rectangle(325, 122, 65, 65);
        main.getMouse().move(startArea);
    }
}
