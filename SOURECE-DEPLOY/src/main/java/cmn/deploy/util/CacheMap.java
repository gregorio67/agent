/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : CacheMap.java
 * @Description : 
 *
 * @Author : LGCNS
 * @Since : 2017. 4. 20.
 *
 * @Copyright (c) 2018 EX All rights reserved.
 *-------------------------------------------------------------
 *              Modification Information
 *-------------------------------------------------------------
 * 날짜            수정자             변경사유 
 *-------------------------------------------------------------
 * 2018. 5. 9.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package cmn.deploy.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CacheMap<K, V> {

	private LinkedHashMap<K,V>   map;
	private int  cacheSize;
	private static final float   hashTableLoadFactor = 0.75f;

	
	public CacheMap(int cacheSize) {
		this.cacheSize = cacheSize;
		int hashTableCapacity = (int)Math.ceil(cacheSize / hashTableLoadFactor) + 1;
		map = new LinkedHashMap<K,V>(hashTableCapacity, hashTableLoadFactor, true) {
		      // (an anonymous inner class)
		      private static final long serialVersionUID = 1;
		      @Override 
		      protected boolean removeEldestEntry (Map.Entry<K,V> eldest) {
		         return size() > CacheMap.this.cacheSize; 
		      }
		};
	}
	
	/**
	* The retrieved entry becomes the MRU (most recently used) entry.
	*/
	public synchronized V get (K key) {
	   return map.get(key); 
	}
	/**
	* If the cache is full, the LRU (least recently used) entry is dropped.
	*/
	public synchronized void put (K key, V value) {
	   map.put (key,value); 
	}
	
	/**
	 * Clear map
	 */
	public synchronized void clear() {
	   map.clear(); 
	}
	
	/**
	 * Use entry 
	 */
	public synchronized int usedEntries() {
	   return map.size(); 
	}
	
	public synchronized Collection<Map.Entry<K,V>> getAll() {
	   return new ArrayList<Map.Entry<K,V>>(map.entrySet()); 
	}
	
	
	public synchronized void remove(String key) {
		map.remove(key);
	}
	
	public static void main(String[] args) {
		CacheMap<String, String> cacheMap = new CacheMap<String, String>(2);
		cacheMap.put("A", "1");
		cacheMap.put("B", "1");
		
		cacheMap.put("C", "1");
		
		cacheMap.get("C");
		
		cacheMap.put("D", "1");
		
		for (Map.Entry<String,String> e : cacheMap.getAll())
		       System.out.println (e.getKey() + " : " + e.getValue()); 
		
	}

}
