package lab_6.world;

import lab_6.world.FeelState;

public interface Thinking {
    public void think();
    public void think(FeelState newState);
    public void think(ThinkState newState);
    public ThinkState state = null;
}
