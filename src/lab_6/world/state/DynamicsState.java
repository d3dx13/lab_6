package lab_6.world.state;

public enum DynamicsState {
    FALLING{
        @Override
        public String toString() {
            return "падает";
        }
    },
    STANDING{
        @Override
        public String toString() {
            return "покоится";
        }
    },
    MOVING{
        @Override
        public String toString() {
            return "движется";
        }
    },
    STAYING{
        @Override
        public String toString() {
            return "стоит";
        }
    },
    NEUTRAL{
        @Override
        public String toString() {
            return "...";
        }
    },
    FALLED{
        @Override
        public String toString() {
            return "упал на землю";
        }
    },
    SPIT_OUT{
        @Override
        public String toString() {
            return "выплюнут";
        }
    },
    DANCING{
        @Override
        public String toString() {
            return "танцует";
        }
    }
}
