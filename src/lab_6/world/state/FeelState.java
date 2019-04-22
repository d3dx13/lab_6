package lab_6.world.state;

import java.io.Serializable;

public enum FeelState implements Serializable {
    HAPPY{
        @Override
        public String toString() {
            return "счастье";
        }
    },
    NEUTRAL{
        @Override
        public String toString() {
            return "...";
        }
    },
    SAD{
        @Override
        public String toString() {
            return "грусть";
        }
    },
    CONFUSED{
        @Override
        public String toString() {
            return "c растерянностью";
        }
    },
    SURPRISED{
        @Override
        public String toString() {
            return "удивление";
        }
    },
    WANT_TO_DANCE{
        @Override
        public String toString() {
            return "желание пуститься в пляс";
        }
    },
    SOFT{
        @Override
        public String toString() {
            return "нежность";
        }
    },
    CONFIDENTLY{
        @Override
        public String toString() {
            return "с уверенностью";
        }
    }
}
