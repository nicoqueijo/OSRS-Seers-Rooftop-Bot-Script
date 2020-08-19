import org.dreambot.api.methods.map.Tile;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

public class Roof1Node extends Node {

    private final Tile tile1 = new Tile(2722, 3492, 3);
    private final Tile tile2 = new Tile(2722, 3493, 3);

    public Roof1Node(SeersRooftop main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return main.ROOFTOP_AREA_1.contains(main.getLocalPlayer().getTile()) &&
                !main.getLocalPlayer().isAnimating();
    }

    @Override
    public int execute() {
        main.adjustCamera(SeersRooftop.State.ROOF_1);
        sleep(50, 125);
        main.pickUpMarkOfGrace(SeersRooftop.State.ROOF_1);
        main.clickRandomTile(tile1, tile2);
        main.getGameObjects().closest(main.GAP_ID_AREA_1).interact();
        sleepUntil(() -> main.determineState(main.getLocalPlayer()) !=
                SeersRooftop.State.ROOF_1, random(4000, 5000));
        return main.sleepTime();
    }
}
