import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.items.GroundItem;

import java.awt.Color;
import java.awt.Graphics;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.dreambot.api.methods.Calculations.random;

@ScriptManifest(
        author = "Nico",
        description = "Does laps around Seers Rooftop",
        category = Category.AGILITY,
        version = 1.0,
        name = "Seers Rooftop"
)
public class SeersRooftop extends AbstractScript {

    public enum State {
        START, ROOF_1, ROOF_2, ROOF_3, ROOF_4, ROOF_5, END, TRANSITION, FALL
    }

    private final int MARK_OF_GRACE_ID = 11849;

    public final int WALL_ID_STARTING_AREA = 14927;
    public final int GAP_ID_AREA_1 = 14928;
    public final int TIGHTROPE_ID_AREA_2 = 14932;
    public final int GAP_ID_AREA_3 = 14929;
    public final int GAP_ID_AREA_4 = 14930;
    public final int EDGE_ID_AREA_5 = 14931;

    public final Area STARTING_AREA = new Area(new Tile(2731, 3489, 0), new Tile(2724, 3481, 0));
    public final Area ROOFTOP_AREA_1 = new Area(new Tile(2730, 3497, 3), new Tile(2721, 3490, 3));
    public final Area ROOFTOP_AREA_2 = new Area(new Tile(2713, 3495, 2), new Tile(2705, 3488, 2));
    public final Area ROOFTOP_AREA_3 = new Area(new Tile(2715, 3481, 2), new Tile(2710, 3477, 2));
    public final Area ROOFTOP_AREA_4 = new Area(new Tile(2715, 3475, 3), new Tile(2700, 3470, 3));
    public final Area ROOFTOP_AREA_5 = new Area(new Tile(2702, 3465, 2), new Tile(2698, 3460, 2));
    public final Area ENDING_AREA = new Area(new Tile(2708, 3465, 0), new Tile(2704, 3461, 0));

    /**
     * -Small chance I double/triple click tele.
     * -Small chance I double/triple click gap object.
     */

    public void pickUpMarkOfGrace(State playerState) {
        List<GroundItem> marksOfGrace = getGroundItems().all(groundItem -> {
            State itemState = determineState(groundItem);
            int itemId = groundItem.getID();
            return (itemId == MARK_OF_GRACE_ID && itemState == playerState);
        });
        if (marksOfGrace != null && !marksOfGrace.isEmpty()) {
            int marksOfGraceCount = getInventory().count(MARK_OF_GRACE_ID);
            log("Picking up Mark of Grace.");
            marksOfGrace.get(0).interact("Take");
            sleepUntil(() -> getInventory().count(MARK_OF_GRACE_ID) == (marksOfGraceCount + 1), random(3000, 4000));
            sleep(200, 300);
        }
    }

    public void clickRandomTile(Tile tile1, Tile tile2) {
        int randomTile = random(1);
        int randomClick = random(3);
        if (randomTile == 0) {
            getMouse().move(tile1);
        } else {
            getMouse().move(tile2);
        }
        for (int i = 0; i <= randomClick; i++) {
            getMouse().click();
            sleep(80, 120);
        }
        sleep(1000, 2000);
    }

    public void adjustCamera(State state) {
        int yaw = getCamera().getYaw();
        int pitch = getCamera().getPitch();
        int MIN_PITCH = 128;
        int MID_PITCH = 255;
        int MAX_PITCH = 383;
        int NORTH_CAMERA_YAW = 0;
        int NORTH_WEST_YAW = 250;
        int WEST_CAMERA_YAW = 500;
        int SOUTH_WEST_YAW = 750;
        int SOUTH_YAW = 1000;
        int SOUTH_EAST_YAW = 1250;
        int EAST_YAW = 1500;
        int NORTH_EAST_YAW = 1750;
        int MAX_CAMERA_YAW = 2000;
        switch (state) {
            case END:
            case FALL: {
                yaw = random(NORTH_CAMERA_YAW, NORTH_WEST_YAW);
                pitch = random(MID_PITCH, MAX_PITCH);
                break;
            }
            case ROOF_1: {
                yaw = random(NORTH_WEST_YAW, WEST_CAMERA_YAW);
                pitch = random(MIN_PITCH, MID_PITCH);
                break;
            }
            case ROOF_2:
            case ROOF_4: {
                yaw = random(SOUTH_WEST_YAW, SOUTH_YAW);
                pitch = random(MIN_PITCH, MID_PITCH);
                break;
            }
            case ROOF_3: {
                yaw = random(SOUTH_YAW, SOUTH_EAST_YAW);
                pitch = random(MIN_PITCH, MID_PITCH);
                break;
            }
        }
        getCamera().rotateTo(yaw, pitch);
    }

    public State determineState(Entity entity) {
        Tile tile = entity.getTile();
        if (STARTING_AREA.contains(tile)) {
            return State.START;
        } else if (ROOFTOP_AREA_1.contains(tile)) {
            return State.ROOF_1;
        } else if (ROOFTOP_AREA_2.contains(tile)) {
            return State.ROOF_2;
        } else if (ROOFTOP_AREA_3.contains(tile)) {
            return State.ROOF_3;
        } else if (ROOFTOP_AREA_4.contains(tile)) {
            return State.ROOF_4;
        } else if (ROOFTOP_AREA_5.contains(tile)) {
            return State.ROOF_5;
        } else if (ENDING_AREA.contains(tile)) {
            return State.END;
        } else if (tile.getZ() != 0) {
            return State.TRANSITION;
        } else {
            return State.FALL;
        }
    }

    private Timer timer = new Timer();
    private long timeToStop = (long) 60 * 1000 * 147;
    private List<Node> nodes = new ArrayList<>();
    public int breaksTaken = 0;
    private int beginningXp;

    private Node lastNodeExecuted = null;
    private int timesExecutedSameNode = 0;

    @Override
    public void onStart() {
        super.onStart();
        beginningXp = getSkills().getExperience(Skill.AGILITY);
        nodes.add(new BreakNode(this));
        nodes.add(new StartNode(this));
        nodes.add(new Roof1Node(this));
        nodes.add(new Roof2Node(this));
        nodes.add(new Roof3Node(this));
        nodes.add(new Roof4Node(this));
        nodes.add(new Roof5Node(this));
        nodes.add(new EndNode(this));
    }

    @Override
    public void onPaint(Graphics graphics) {
        super.onPaint(graphics);
        int currentXp = getSkills().getExperience(Skill.AGILITY);
        int xpGrained = currentXp - beginningXp;
        graphics.setColor(Color.BLACK);
        graphics.drawString("Seers Rooftop", 330, 365);
        graphics.drawString("Time running: " + timer.formatTime(), 330, 383);
        graphics.drawString("Total XP gained: " + formatInt(xpGrained), 330, 401);
        graphics.drawString("Hourly XP rate: " + formatInt(timer.getHourlyRate(xpGrained)), 330, 419);
    }

    @Override
    public void onExit() {
        super.onExit();
        int currentXp = getSkills().getExperience(Skill.AGILITY);
        int xpGrained = currentXp - beginningXp;
        logInfo("Time ran: " + timer.formatTime());
        logInfo("Total XP gained: " + formatInt(xpGrained));
        logInfo("Hourly rate: " + formatInt(timer.getHourlyRate(xpGrained)));
    }

    @Override
    public int onLoop() {
        for (Node node : nodes) {
            if (node.validate()) {
                log("Executing: " + node.getClass().getSimpleName());
                checkInfiniteLoop(node);
                return node.execute();
            }
            if (timer.elapsed() > timeToStop) {
                stop();
            }
        }
        getWalking().walk(STARTING_AREA.getRandomTile());
        return sleepTime();
    }

    public int sleepTime() {
        return random(150, 200);
    }

    private void checkInfiniteLoop(Node node) {
        if (node == lastNodeExecuted) {
            timesExecutedSameNode++;
        } else {
            timesExecutedSameNode = 0;
        }
        if (timesExecutedSameNode > 5) {
            logInfo("Stopped after " + timer.formatTime() + ". " + node.getClass().getSimpleName() + " in infinite loop.");
            stop();
        }
        lastNodeExecuted = node;
    }

    private String formatInt(int num) {
        return NumberFormat.getNumberInstance(Locale.US).format(num);
    }
}
