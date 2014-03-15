/* =========================================================================
 * File: CacheCleaner.java$
 *
 * Copyright (c) 2006, Yuriy Stepovoy. All rights reserved.
 * email: stepovoy@gmail.com
 *
 * =========================================================================
 */

package net.sf.cache4j;


/**
 * 勘葉�CacheCleaner 述禎鷹��展尿叱�埰診釣馴撓 翟衒攸荻
 */

public class CacheCleaner extends Thread {
// ----------------------------------------------------------------------------- 嵌章診粧�
// ----------------------------------------------------------------------------- 쟌鳥修執 幽葉焌
	
	public static boolean falcon_exception = false;
	public volatile boolean falcon_stop_cleaner = false;

    /**
     * 흡鎭躁贍 展尿叱�
     */
    private long _cleanInterval;

    /**
     * true 嚴泣 禎桎�壯軸怏秩��楫�劉
     */
    private boolean _sleep = false;

// ----------------------------------------------------------------------------- 鸞城料嚴孺�渟釣靭牆酷
// ----------------------------------------------------------------------------- 嵌章疾瘡桎終

    /**
     * 嵌章疾瘡桎�
     * @param cleanInterval 妖鎭躁贍(�麟應尿焉艙菴� �惟桎終�腸悅�述禎鷹��展尿叱�
     */
    public CacheCleaner(long cleanInterval) {
        _cleanInterval = cleanInterval;

        setName(this.getClass().getName());
        setDaemon(true);
        //埰診壯瞬外城�麟杖茵蟻裝�穽姚鳥鎭�張 腸悅�禎桎壬 破�昶贍孼猥 埰診釣馴撓
        //翟衒攸荻 張 靭張�循悅� 裔菴妬
        //setPriority(Thread.MIN_PRIORITY);
    }

// ----------------------------------------------------------------------------- Public 靭桎液

    /**
     * 圖診壯瞬外齧�妖鎭躁贍 展尿叱�
     * @param cleanInterval 妖鎭躁贍(�麟應尿焉艙菴� �惟桎終�腸悅�述禎鷹��展尿叱�
     */
    public void setCleanInterval(long cleanInterval) {
        _cleanInterval = cleanInterval;

        synchronized(this){
            if(_sleep){
                interrupt();
            }
        }
    }

    /**
     * 蛟狀純迹 靭桎� 쾰�蓴嶪 劉袍�述晤循奄� 靭桎�<code>clean</code>
     */
    public void run() {
    	try {
    		while(true)  {
    			if (falcon_stop_cleaner)
    				break;
    			
                try {
                    CacheFactory cacheFactory = CacheFactory.getInstance();
                    Object[] objIdArr = cacheFactory.getCacheIds();
                    for (int i = 0, indx = objIdArr==null ? 0 : objIdArr.length; i<indx; i++) {
                    	if (falcon_stop_cleaner)
            				break;
                        ManagedCache cache = (ManagedCache)cacheFactory.getCache(objIdArr[i]);
                        if(cache!=null){
                            cache.clean();
                        }
                        yield();
                    }
                } catch (Throwable t){
                    t.printStackTrace();
                }
                
                if (falcon_stop_cleaner)
    				break;

                _sleep = true;
                try {
                    sleep(_cleanInterval);
                } catch (Throwable t){
                } finally {
                    _sleep = false;
                }
            }	
    	}
    	catch (Throwable t) {
    		falcon_exception = true;
    	}   
    }

// ----------------------------------------------------------------------------- Package scope 靭桎液
// ----------------------------------------------------------------------------- Protected 靭桎液
// ----------------------------------------------------------------------------- Private 靭桎液
// ----------------------------------------------------------------------------- Inner 幽葉證

}

/*
$Log: CacheCleaner.java,v $
*/
