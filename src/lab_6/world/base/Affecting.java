package lab_6.world.base;

import lab_6.world.state.AffectState;

public interface Affecting {
    public void affectOn(Affected object, AffectState state);
}
