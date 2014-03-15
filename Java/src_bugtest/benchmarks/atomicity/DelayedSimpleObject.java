package benchmarks.atomicity;

import java.util.Random;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedSimpleObject implements Delayed {
	public int v;
	public static Random rand = new Random();

    public DelayedSimpleObject() {
    }

    public DelayedSimpleObject(int v) {
        this.v = v;
    }
    
    public long getDelay(TimeUnit unit) {
    	return (rand.nextLong()%3);
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
        return(this.v == ((DelayedSimpleObject)obj).v);
    }
    
    public String toString() {
    	return this.v+"";
    }

	public int compareTo(Delayed o) {
		return ((DelayedSimpleObject)this).v - ((DelayedSimpleObject)o).v;
	}
}
