package lab_6.world;

public class IdMaster {
    static int idCounter = 4096;
    static int create(){
        idCounter++;
        return idCounter;
    }
}
