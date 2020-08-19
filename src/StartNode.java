import org.dreambot.api.methods.map.Tile;

import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodContext.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

public class StartNode extends Node {

    private final Tile tile1 = new Tile(2729, 3488, 0);
    private final Tile tile2 = new Tile(2729, 3489, 0);

    public StartNode(SeersRooftop main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return main.STARTING_AREA.contains(main.getLocalPlayer().getTile());
    }

    @Override
    public int execute() {
        main.adjustCamera(SeersRooftop.State.START);
        sleep(20, 75);
        main.clickRandomTile(tile1, tile2);
        main.getGameObjects().closest(main.WALL_ID_STARTING_AREA).interact();
        sleepUntil(() -> main.determineState(main.getLocalPlayer()) !=
                SeersRooftop.State.START, random(4000, 5000));
        return main.sleepTime();
    }
}
