package lab_6.message;

import java.io.Serializable;

/**
 * Вся информация о коллекции.
 */
public class CollectionInfo implements Serializable {
    public int size;
    public String type;
    public String lastChangeTime;
    @Override
    public String toString()
    {
        return new StringBuffer()
                .append("\n--- Collection info ---")
                .append("\nCollection type: ")
                .append(type)
                .append("\nLast change time: ")
                .append(lastChangeTime == null ? "collection has never been changed" : lastChangeTime)
                .append("\nSize of collection: ")
                .append(size)
                .append("\n-----------------------\n")
                .toString();
    }
}
