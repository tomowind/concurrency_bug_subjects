package benchmarks.atomicity;

public class SimpleObject implements Comparable {
    public int v;

    public SimpleObject() {
    }

    public SimpleObject(int v) {
        this.v = v;
    }
    
    public int get() {
    	return v;
    }
    
    public void set(int v) {
    	this.v = v;
    }

    public int hashCode() {
        return v+1000;
    }

    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null) return false;
        return(this.v == ((SimpleObject)obj).v);
    }

    public int compareTo(Object o) {
        return ((SimpleObject)this).v - ((SimpleObject)o).v;
    }
    
    public String toString() {
    	return this.v+"";
    }
}
