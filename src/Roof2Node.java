import org.dreambot.api.methods.map.Tile;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

public class Roof2Node extends Node {

    private final Tile tile1 = new Tile(2710, 3491, 2);
    private final Tile tile2 = new Tile(2710, 3490, 2);

    public Roof2Node(SeersRooftop main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return main.ROOFTOP_AREA_2.contains(main.getLocalPlayer().getTile());
    }

    @Override
    public int execute() {
        main.adjustCamera(SeersRooftop.State.ROOF_2);
        sleep(50, 125);
        main.pickUpMarkOfGrace(SeersRooftop.State.ROOF_2);
        main.clickRandomTile(tile1, tile2);
        main.getGameObjects().closest(main.TIGHTROPE_ID_AREA_2).interact();
        sleepUntil(() -> main.determineState(main.getLocalPlayer()) !=
                SeersRooftop.State.ROOF_2, random(4000, 5000));
        return main.sleepTime();
    }
}
