package lab_6.world.creation.musical;

import lab_6.world.state.FeelState;
import lab_6.world.state.PositionState;

import java.io.Serializable;

public class StringedMusicalInstrument extends MusicalInstrument implements Serializable {
    StringedMusicalInstrument(String newName) {
        super(newName);
        StringsInit();
    }
    StringedMusicalInstrument(String newName, PositionState newPosition) {
        super(newName, newPosition);
        StringsInit();
    }
    private void StringsInit(){
        StringBuilder temp = new StringBuilder(" струну");
        strings [0] = new Strings ("первую" + temp);
        strings [1] = new Strings ("вторую" + temp);
        strings [2] = new Strings ("третью" + temp);
        strings [3] = new Strings ("четвёртую" + temp);
        strings [4] = new Strings ("пятую" + temp);
        strings [5] = new Strings ("шестую" + temp);
        strings [6] = new Strings ("седьмую" + temp);
    }
    public void toFingerTheStrings() {
        for (int i = 0; i < 7; i++)
            strings[i].touch();
    }
    public void toFingerTheStrings(FeelState newState) {
        for (int i = 0; i < 7; i++)
            strings[i].touch(newState);
    }
    public class Strings implements Serializable{
        Strings(String newName){
            this.name = newName;
        }
        public void touch(){
            System.out.println("Кто-то трогает " + this.name + " пальцами.");
        }
        public void touch(FeelState newState){
            System.out.println("Кто-то трогает " + this.name + " c " + newState + " пальцами.");
        }
        String name;
    }
    Strings strings [] = new Strings [7];
}