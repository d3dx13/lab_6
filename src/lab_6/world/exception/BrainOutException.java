package lab_6.world.exception;

import java.io.Serializable;

public class BrainOutException extends RuntimeException implements Serializable {
    @Override
    public String getMessage() {
        return "\n...\n...\nЗаметил, задумался и завис\n...";
    }
}
