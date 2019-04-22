package lab_6.world;

public class BrainOutException extends RuntimeException{
    @Override
    public String getMessage() {
        return "\n...\n...\nЗаметил, задумался и завис\n...";
    }
}
