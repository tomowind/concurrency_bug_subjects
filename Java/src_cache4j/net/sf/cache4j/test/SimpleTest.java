package net.sf.cache4j.test;

import java.util.Random;

import net.sf.cache4j.CacheCleaner;
import net.sf.cache4j.CacheConfig;
import net.sf.cache4j.CacheException;
import net.sf.cache4j.CacheFactory;
import net.sf.cache4j.impl.BlockingCache;
import net.sf.cache4j.impl.CacheConfigImpl;

public class SimpleTest {
    
	public static void afterEachMethod() throws Exception {
        CacheFactory cf = CacheFactory.getInstance();
        Object[] cacheIds = cf.getCacheIds();
        for (int i = 0, indx = cacheIds==null ? 0 : cacheIds.length; i <indx; i++) {
            cf.removeCache(cacheIds[i]);
        }
    }
	
    public static boolean test_PUT_GET() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, 0, 0, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        Object key = "key";
        Object value = "value";
        cache.put(key, value);

        return cache.get(key)!=null ? true : false;
    }

    public static boolean test_PUT_OBJ_PUT_NULL_GET() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, 0, 0, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        Object key = "key";
        Object value = "value";

        cache.put(key, value);
        cache.put(key, null);

        return cache.get(key)==null ? true : false;
    }

    public static boolean test_PUT_REMOVE_GET() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, 0, 0, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        Object key = "key";
        Object value = "value";

        cache.put(key, value);
        cache.remove(key);

        return cache.get(key)==null ? true : false;
    }

    public static boolean test_PUT_CLEAR_GET() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, 0, 0, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        Object key = "key";
        Object value = "value";

        cache.put(key, value);
        cache.clear();

        return cache.get(key)==null ? true : false;
    }

    public static boolean test_MAXSIZE() throws Exception {
        BlockingCache cache = new BlockingCache();
        int maxSize = 100;
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, 0, maxSize, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        for (int i = 0, maxi = maxSize*2; i <maxi; i++) {
            cache.put(new Long(i), new Long(i));
        }

        return cache.size()==maxSize ? true : false;
    }

    public static boolean test_MAXMEMORYSIZE() throws Exception {
        BlockingCache cache = new BlockingCache();
        int maxMemorySize = 1000;
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, maxMemorySize, 0, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        for (int i = 0; i <1000; i++) {
            cache.put(new Long(i), new Long(i));
        }

        return cache.getCacheInfo().getMemorySize()<=maxMemorySize ? true : false;
    }

    public static boolean test_MAXSIZE_AND_MAXMEMORYSIZE() throws Exception {
        BlockingCache cache = new BlockingCache();
        int maxMemorySize = 1000;
        int maxSize = 1000;
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, maxMemorySize, maxSize, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        for (int i = 0; i <maxSize*2; i++) {
            cache.put(new Long(i), new Long(i));
        }

        return cache.getCacheInfo().getMemorySize()<=maxMemorySize && cache.size()<=maxSize ? true : false;
    }

    public static boolean test_TTL() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 1, 0, 0, 0, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        Object key = "key";
        Object value = "value";

        cache.put(key, value);
        Thread.sleep(30);

        return cache.get(key)==null ? true : false;
    }

    public static boolean test_IDLE() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 1, 0, 0, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        Object key = "key";
        Object value = "value";

        cache.put(key, value);
        Thread.sleep(30);

        return cache.get(key)==null ? true : false;
    }

    public static boolean test_CACHE_CLEANER_TTL() throws Exception {
        CacheFactory cf = CacheFactory.getInstance();
        long ttl = 100;
        long cleanInterval = ttl*2;
        long sleep = ttl*3;

        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, ttl, 0, 0, 0, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        for (int i = 0; i <1000; i++) {
            cache.put(new Long(i), new Long(i));
        }

        cf.addCache(cache);
        cf.setCleanInterval(cleanInterval);
        Thread.sleep(sleep);

        return cache.size()==0 ? true : false;
    }

    public static boolean test_CACHE_CLEANER_IDLE() throws Exception {
        CacheFactory cf = CacheFactory.getInstance();
        long idle = 100;
        long cleanInterval = idle*2;
        long sleep = idle*3;

        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, idle, 0, 0, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        for (int i = 0; i <1000; i++) {
            cache.put(new Long(i), new Long(i));
        }

        cf.addCache(cache);
        cf.setCleanInterval(cleanInterval);
        Thread.sleep(sleep);

        return cache.size()==0 ? true : false;
    }

    public static boolean test_EVICTION_ALGORITHM_LRU() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, 0, 2, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        Object o1 = "o1";
        Object o2 = "o2";
        Object o3 = "o3";

        cache.put(o1, o1);
        Thread.sleep(50);
        cache.put(o2, o2);
        Thread.sleep(50);
        cache.get(o1); //update access time

        cache.put(o3, o3);

        return cache.get(o1)!=null &&
               cache.get(o3)!=null &&
               cache.get(o2)==null ? true : false;
    }

    public static boolean test_EVICTION_ALGORITHM_LFU() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, 0, 2, null, "lfu", "strong");
        cache.setCacheConfig(cacheConfig);

        Object o1 = "o1";
        Object o2 = "o2";
        Object o3 = "o3";

        cache.put(o1, o1);
        cache.get(o1); //update frequency count
        Thread.sleep(50);
        cache.put(o2, o2);

        cache.put(o3, o3);

        return cache.get(o1)!=null &&
               cache.get(o3)!=null &&
               cache.get(o2)==null ? true : false;
    }

    public static boolean test_EVICTION_ALGORITHM_FIFO() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, 0, 2, null, "fifo", "strong");
        cache.setCacheConfig(cacheConfig);

        Object o1 = "o1";
        Object o2 = "o2";
        Object o3 = "o3";

        cache.put(o1, o1); //create time 1
        Thread.sleep(50);
        cache.put(o2, o2); //create time 2
        Thread.sleep(50);
        cache.put(o3, o3); //create time 3

        return cache.get(o2)!=null &&
               cache.get(o3)!=null &&
               cache.get(o1)==null ? true : false;
    }

    public static boolean test_REFERENCE_STRONG() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, 0, 0, null, "lru", "strong");
        cache.setCacheConfig(cacheConfig);

        int i = 0;
        try {
            for (; i <10; i++) {
                cache.put(new Integer(i), new Long[2048*2048]);
            }
        } catch (OutOfMemoryError o){
            //int size = cache.size();
            cache.clear();
            return true; //return size==i;
        }

        return false;
    }

    public static boolean test_REFERENCE_SOFT() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, 0, 0, null, "lru", "soft");
        cache.setCacheConfig(cacheConfig);

        int i = 0;
        try {
            for (; i <10; i++) {
                cache.put(new Integer(i), new Long[2048*2048]);
            }
        } catch (OutOfMemoryError o){
            cache.clear();
            return false;
        }

        return cache.size()==i;
    }

    public static boolean test_BLOCKING() throws Exception {
        BlockingCache cache = new BlockingCache();
        CacheConfig cacheConfig = new CacheConfigImpl("cacheId", null, 0, 0, 0, 0, null, "lru", "soft");
        cache.setCacheConfig(cacheConfig);

        cache.get("1");
        int exceptCount = 0;

        try {
            cache.get("2");
        } catch (CacheException o){
            exceptCount++; //+
        }

        try {
            cache.put("3", "3");
        } catch (CacheException o){
            exceptCount++; //+
        }

        try {
            cache.remove("4");
        } catch (CacheException o){
            exceptCount++; //+
        }

        try {
            cache.put("1", "1"); 
        } catch (CacheException o){
            exceptCount++;
        }

        return exceptCount==3;
    }

    public static void main(String[] args) throws Exception {
    	CacheCleaner.falcon_exception = false;
    	CacheFactory.newInstance();
    	
    	for (int i=0; i<2; i++) {    		
    		int select = (new Random()).nextInt(25);
    		if (select>17 && select%2==0) select = 9;
    		else if (select>17 && select%2!=0) select = 10;
    		
    		switch(select) {
    		case 0: 
    			test_PUT_GET();
    			break;
    		case 1:
    			test_PUT_OBJ_PUT_NULL_GET();
    			break;
    		case 2:
    			test_PUT_REMOVE_GET();
    			break;
    		case 3:
    			test_PUT_CLEAR_GET();
    			break;
    		case 4:
    			test_MAXSIZE();
    			break;
    		case 5:
    			test_MAXMEMORYSIZE();
    			break;
    		case 6:
    			test_MAXSIZE_AND_MAXMEMORYSIZE();
    			break;
    		case 7:
    			test_TTL();
    			break;
    		case 8:
    			test_IDLE();
    			break;
    		case 9:
    			test_CACHE_CLEANER_TTL();
    			break;
    		case 10:
    			test_CACHE_CLEANER_IDLE();
    			break;
    		case 11:
    			test_EVICTION_ALGORITHM_LRU();
    			break;
    		case 12:
    			test_EVICTION_ALGORITHM_LFU();
    			break;
    		case 13:
    			test_EVICTION_ALGORITHM_FIFO();
    			break;
    		case 14:
    			test_REFERENCE_STRONG();
    			break;
    		case 15:
    			test_REFERENCE_SOFT();
    			break;
    		case 16:
    			test_BLOCKING();
    			break;
    		}
    		afterEachMethod();
    	}
    	CacheFactory.join();
    	if (CacheCleaner.falcon_exception)
    		throw new RuntimeException("Atomicity bug found");
    }

}
