package lab_6.world;

import lab_6.world.DynamicsState;

public abstract class Existing {
    abstract void setDynamics(DynamicsState newState);
    abstract void setPosition(PositionState newState);
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
