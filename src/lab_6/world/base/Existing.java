package lab_6.world.base;

import lab_6.world.state.DynamicsState;
import lab_6.world.state.PositionState;

public abstract class Existing {
    public abstract void setDynamics(DynamicsState newState);
    public abstract void setPosition(PositionState newState);
    public PositionState getPosition(){
        //Добавить ли сюда сообщение?
        return this.positionState;
    }
    public DynamicsState getDynamics(){
        //Добавить ли сюда сообщение?
        return this.dynamicsStateState;
    }
    public DynamicsState dynamicsStateState;
    public PositionState positionState;
}
