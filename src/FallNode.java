import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.sleep;
import static org.dreambot.api.methods.MethodProvider.sleepUntil;

class FallNode extends Node {

    public FallNode(SeersRooftop main) {
        super(main);
    }

    @Override
    public boolean validate() {
        return main.getLocalPlayer().getTile().getZ() == 0;
    }

    @Override
    public int execute() {
        main.adjustCamera(SeersRooftop.State.FALL);
        sleep(50, 125);
        sleepUntil(() -> main
                .getWalking()
                .walk(main.STARTING_AREA.getRandomTile()), random(2000, 3000));
        return main.sleepTime();
    }
}
