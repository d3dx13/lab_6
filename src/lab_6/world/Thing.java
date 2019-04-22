package lab_6.world;

import lab_6.world.Existing;

public class Thing extends Existing implements Affected{

    Thing(String newName){
        this.name = newName;
        this.id = IdMaster.create();
    }

    @Override
    public void affectBy(AffectState state) {
        switch (state){
            case WATCH_OUT:
                System.out.println("Кто-то космотрел из " + this.toString());
        }
    }

    @Override
    void setDynamics(DynamicsState newState) {
        this.dynamicsStateState = newState;
        System.out.println("*" + this.toString() + " теперь " + newState + "*");
    }

    @Override
    void setPosition(PositionState newState) {
        this.positionState = newState;
        System.out.println("*" + this.toString() + " теперь " + newState + "*");
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
        return (this.id == ((Thing) obj).id);
    }
    protected int id;
    public String name;
}
