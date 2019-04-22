package lab_6.world.state;

import java.io.Serializable;

public enum AffectState implements Serializable {
    CREATE{
        @Override
        public String toString() {
            return "создать";
        }
    },
    THROW{
        @Override
        public String toString() {
            return "кинуть";
        }
    },
    CATCH{
        @Override
        public String toString() {
            return "поймать";
        }
    },
    MOVE{
        @Override
        public String toString() {
            return "подвинуть";
        }
    },
    NEUTRAL{
        @Override
        public String toString() {
            return "";
        }
    },
    WATCH_OUT{
        @Override
        public String toString() {
            return "посмотрел из";
        }
    },
    DISAPPEAR_INTO{
        @Override
        public String toString() {
            return "исчезнул в";
        }
    },
    RUN_ON {
        @Override
        public String toString() {
            return "бежал по";
        }
    }
}
