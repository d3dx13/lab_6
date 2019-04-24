package lab_6.message;

import java.io.Serializable;

public class CollectionInfo implements Serializable {
    public int size;
    public String type;
    public String lastChangeTime;
    @Override
    public String toString()
    {
        return "Collection type:"+type+"\nLast change time:"+lastChangeTime+"\nSize of collection:"+size;
    }
}
