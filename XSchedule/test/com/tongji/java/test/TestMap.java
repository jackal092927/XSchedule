package com.tongji.java.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestMap {

	@Test
	public void Test(){
		List<Map> mapList = new ArrayList<Map>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("name", "TJ"+(i+1));
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("number", (i+1));
			mapList.add(map1);
			mapList.add(map2);
		}
		for (Map map : mapList) {
			System.out.println(map.get("name")+": "+(map.get("number")));
		}
		
		
	}
}
