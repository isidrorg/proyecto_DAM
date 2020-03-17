package entities;

import java.io.Serializable;
import java.util.Collection;

public abstract class CommonEntity implements Serializable {

    public abstract Integer getId();

    public abstract void setId(Integer id);

    public abstract String toString();

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
