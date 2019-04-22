package lab_6.world.base;

import lab_6.world.state.FeelState;
import lab_6.world.state.ThinkState;

public interface Thinking {
    public void think();
    public void think(FeelState newState);
    public void think(ThinkState newState);
    public ThinkState state = null;
}
