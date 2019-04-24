package lab_6.message;

import java.io.Serializable;

public class CollectionInfo implements Serializable {
    public int size;
    public String type;
    public String lastChangeTime;
    @Override
    public String toString()
    {
        if(type==null) {
            return new StringBuffer().append("\n--- Collection info ---")
                .append("\nCollection has null")
                .append("\n-----------------------\n")
                .toString();
        }
        if(lastChangeTime==0){
            return new StringBuffer().append("\n--- Collection info ---")
                .append("\nCollection has never been changed")
                .append("\n-----------------------\n")
                .toString();
        }

        if(type!=null&&lastChangeTime!=0) {
            return new StringBuffer()
                    .append("\n--- Collection info ---")
                    .append("\nCollection type: ")
                    .append(type)
                    .append("\nLast change time: ")
                    .append(lastChangeTime)
                    .append("\nSize of collection: ")
                    .append(size)
                    .append("\n-----------------------\n")
                    .toString();
        }
    }
}
