package lab_6.world.creation.musical;

import lab_6.world.creation.Thing;
import lab_6.world.state.DynamicsState;
import lab_6.world.state.FeelState;
import lab_6.world.state.PositionState;

import java.io.Serializable;

public class MusicalInstrument extends Thing implements Serializable {
    MusicalInstrument(String newName){
        super(newName);
        this.dynamicsStateState = DynamicsState.NEUTRAL;
        this.positionState = PositionState.NEUTRAL;
        System.out.println("Показалась " + newName);
    }
    MusicalInstrument(String newName, PositionState newPosition){
        super(newName);
        this.dynamicsStateState = DynamicsState.NEUTRAL;
        this.positionState = newPosition;
        System.out.println(this.positionState + " показалась " + newName);
    }
    public Music play(){
        System.out.println(this.positionState + " " + this.dynamicsStateState + " " + "играет " + this.name);
        return new Music(this.name);
    }
    public Music play(FeelState newState){
        System.out.println(this.positionState + " " + this.dynamicsStateState + " играет " + this.name + ". В её игре присутствует " + newState);
        return new Music(this.name, newState);
    }
}
