import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

public class Roof5Node extends Node {

    public Roof5Node(SeersRooftop main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return main.ROOFTOP_AREA_5.contains(main.getLocalPlayer().getTile());
    }

    @Override
    public int execute() {
        main.adjustCamera(SeersRooftop.State.ROOF_5);
        sleep(50, 125);
        main.pickUpMarkOfGrace(SeersRooftop.State.ROOF_5);
        main.getGameObjects().closest(main.EDGE_ID_AREA_5).interact();
        sleepUntil(() -> main.determineState(main.getLocalPlayer()) !=
                SeersRooftop.State.ROOF_5, random(4000, 5000));
        return main.sleepTime();
    }
}
