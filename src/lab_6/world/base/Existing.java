package lab_6.world.base;

import lab_6.world.state.DynamicsState;
import lab_6.world.state.PositionState;

import java.io.Serializable;

public abstract class Existing implements Serializable {
    public Existing(){
        this.dynamicsStateState = DynamicsState.NEUTRAL;
        this.positionState = PositionState.NEUTRAL;
    }
    public abstract void setDynamics(DynamicsState newState);
    public abstract void setPosition(PositionState newState);
    public PositionState getPosition(){
        return this.positionState;
    }
    public DynamicsState getDynamics(){
        return this.dynamicsStateState;
    }
    public DynamicsState dynamicsStateState;
    public PositionState positionState;
}
