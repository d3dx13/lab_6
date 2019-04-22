package lab_6.world.base;

import lab_6.world.state.FeelState;

public interface Feeling {
    public FeelState feel();
    public FeelState feel(FeelState newState);
}
