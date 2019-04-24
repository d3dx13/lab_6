package lab_6.world.creation;

import lab_6.world.IdMaster;
import lab_6.world.base.*;
import lab_6.world.state.*;

import java.io.Serializable;

public class People extends Existing implements Feeling, Thinking, Affecting, Affected, Serializable {

    People(String newName){
        super();
        this.name = newName;
        this.feelState = FeelState.NEUTRAL;
        this.thinkState = ThinkState.NEUTRAL;
        this.id = IdMaster.create();
    }

    @Override
    public void affectBy(AffectState state) {
        System.out.println(this.toString() + " теперь " + state);
    }

    @Override
    public void affectOn(Affected object, AffectState state) {
        object.affectBy(state);
        System.out.println(this.toString() + " " + state + " " + object);
    }

    @Override
    public void setDynamics(DynamicsState newState) {
        this.dynamicsStateState = newState;
        System.out.println("*" + this.toString() + " сейчас " + newState + "*");
    }

    @Override
    public void setPosition(PositionState newState) {
        this.positionState = newState;
        System.out.println("*" + this.toString() + " сейчас " + newState + "*");
    }

    @Override
    public FeelState feel() {
        System.out.println("*" + this.toString() + " чувствует " + this.feelState + "*");
        return this.feelState;
    }

    @Override
    public FeelState feel(FeelState newState) {
        if (this.feelState == FeelState.NEUTRAL || this.feelState == newState) {
            this.feelState = newState;
            return feel();
        }
        System.out.println("*" + this.toString() + " чувствовал " + this.feelState + ", а теперь чувствует " + newState +"*");
        this.feelState = newState;
        return this.feelState;
    }

    @Override
    public void think() {
        System.out.println("*" + this.toString() + " думает, что " + this.thinkState + "*");
    }

    @Override
    public void think(FeelState newState) {
        System.out.println("*" + this.toString() + " думает, что испытывает " + newState + "*");
    }

    @Override
    public void think(ThinkState newState) {
        this.thinkState = newState;
        System.out.println("*" + this.toString() + " думает, что " + this.thinkState + "*");
    }

    @Override
    public int hashCode() {
        return id;
    }
    @Override
    public String toString() {
        return name;
    }
    @Override
    public boolean equals(Object obj) {
        return (this.id == ((People) obj).id);
    }
    protected int id;
    public ThinkState thinkState;
    public FeelState feelState;
    public String name;
}
