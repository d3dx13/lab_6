package lab_6.world.exception;

public class SetupHyperParametersException extends Exception {
    @Override
    public String getMessage() {
        return "Не удаётся прочитать входные гиперпараметры.";
    }
}
