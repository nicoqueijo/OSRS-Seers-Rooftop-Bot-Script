import static org.dreambot.api.methods.Calculations.random;
import static org.dreambot.api.methods.MethodProvider.logInfo;
import static org.dreambot.api.methods.MethodProvider.sleep;

public class BreakNode extends Node {

    public BreakNode(SeersRooftop main) {
        super(main);
    }

    @Override
    public boolean validate() {
        int probabilityOfTakingABreak = random(1000);
        return probabilityOfTakingABreak == 0;
    }

    @Override
    public int execute() {
        logInfo("Taking a break. Breaks taken: " + (++main.breaksTaken));
        sleep(6000, 10000);
        main.getMouse().moveMouseOutsideScreen();
        int oneMinute = 1000 * 60;
        int threeMinutes = 1000 * 60 * 3;
        sleep(oneMinute, threeMinutes);
        return main.sleepTime();
    }
}
