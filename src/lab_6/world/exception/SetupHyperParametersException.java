package lab_6.world.exception;

import java.io.Serializable;

public class SetupHyperParametersException extends Exception implements Serializable {
    @Override
    public String getMessage() {
        return "Не удаётся прочитать входные гиперпараметры.";
    }
}
