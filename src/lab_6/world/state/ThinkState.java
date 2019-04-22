package lab_6.world.state;

import java.io.Serializable;

public enum ThinkState implements Serializable {
    NOTICE{
        @Override
        public String toString() {
            return "заметил что-то";
        }
    },
    NEUTRAL{
        @Override
        public String toString() {
            return "...";
        }
    },
    WATCH{
        @Override
        public String toString() {
            return "смотрит";
        }
    },
    PONDER{
        @Override
        public String toString() {
            return "размышляет";
        }
    },
    UNDERSTAND{
        @Override
        public String toString() {
            return "понял";
        }
    },
    DOES_NOT_WANT_TO_LEAVE{
        @Override
        public String toString() {
            return "не хочет уходить";
        }
    },
    LISTEN{
        @Override
        public String toString() {
            return "слушает";
        }
    },
    DEPRIVED_OF_ATTENTION{
        @Override
        public String toString() {
            return "лишён внимания";
        }
    },
    GONE{
        @Override
        public String toString() {
            return "ушёл";
        }
    }
}
