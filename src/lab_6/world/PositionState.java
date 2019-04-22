package lab_6.world;

public enum PositionState {
    NOWHERE{
        @Override
        public String toString() {
            return "нигде";
        }
    },
    NEUTRAL{
        @Override
        public String toString() {
            return "где-то";
        }
    },
    IN_WINDOW{
        @Override
        public String toString() {
            return "в окне";
        }
    },
    IN_HARP{
        @Override
        public String toString() {
            return "на арфе";
        }
    },
    IN_AIR{
        @Override
        public String toString() {
            return "в воздухе";
        }
    }
}
