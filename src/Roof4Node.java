import org.dreambot.api.methods.map.Tile;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

public class Roof4Node extends Node {

    private final Tile tile1 = new Tile(2703, 3470, 3);
    private final Tile tile2 = new Tile(2702, 3470, 3);

    public Roof4Node(SeersRooftop main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return main.ROOFTOP_AREA_4.contains(main.getLocalPlayer().getTile());
    }

    @Override
    public int execute() {
        main.adjustCamera(SeersRooftop.State.ROOF_4);
        sleep(50, 125);
        main.pickUpMarkOfGrace(SeersRooftop.State.ROOF_4);
        main.clickRandomTile(tile1, tile2);
        main.getGameObjects().closest(main.GAP_ID_AREA_4).interact();
        sleepUntil(() -> main.determineState(main.getLocalPlayer()) !=
                SeersRooftop.State.ROOF_4, random(4000, 5000));
        return main.sleepTime();
    }
}
