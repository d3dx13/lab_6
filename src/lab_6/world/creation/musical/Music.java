package lab_6.world.creation.musical;

import lab_6.world.base.Affected;
import lab_6.world.base.Affecting;
import lab_6.world.base.Feeling;
import lab_6.world.IdMaster;
import lab_6.world.state.AffectState;
import lab_6.world.state.FeelState;

import java.io.Serializable;

public class Music implements Affecting, Feeling, Serializable {
    public Music(String instrumentName){
        this.instrument = instrumentName;
        this.feelState = FeelState.NEUTRAL;
        this.id = IdMaster.create();
        System.out.println("*" + this.instrument + " что-то играет" + "*");
    }

    public Music(String instrumentName, FeelState newState){
        this.instrument = instrumentName;
        this.feelState = newState;
        this.id = IdMaster.create();
        System.out.println("*" + this.toString() + " заставляет чувствовать " + this.feelState + "*");
    }

    @Override
    public void affectOn(Affected object, AffectState state) {
        object.affectBy(state);
        System.out.println(this.toString() + " заставляет " + object + " " + state + " что-то");
    }

    @Override
    public FeelState feel() {
        System.out.println("Слушатели чувствуют " + this.feelState);
        return this.feelState;
    }

    @Override
    public FeelState feel(FeelState newState) {
        if (newState == this.feelState)
            return this.feel();
        System.out.println("Слушатели чувствуют " + this.feelState + ", переходящее в " + newState);
        this.feelState = newState;
        return newState;
    }

    @Override
    public int hashCode() {
        return id;
    }
    @Override
    public String toString() {
        return "музыка, которую играет " + instrument;
    }
    @Override
    public boolean equals(Object obj) {
        return (this.id == ((Music) obj).id);
    }
    protected int id;

    protected String instrument;
    protected FeelState feelState;
}
