package org.shoukaiseki.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MapHouhou {

            	/**
            	 * 返回排序的key
            	 * @param map
            	 * @return
            	 */
            	public static Set sort(Map map ) {
            		List list = new ArrayList(map.keySet());
            		Collections.sort(list, new Comparator() {
            			public int compare(Object a, Object b) {
            				return a.toString().toLowerCase().compareTo(
            						b.toString().toLowerCase());
            			}
            		});

            		return new TreeSet(list);
            	}

}


