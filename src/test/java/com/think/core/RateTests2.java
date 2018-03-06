package com.think.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RateTests2 {
	public static void main(String[] args) {
		// 30,60,80 权重列表
		List<Integer> weightList = Arrays.asList(90, 80, 60);
		List<Scene> mapList = Arrays.asList(new Scene(0), new Scene(1), new Scene(2));

		int weightSum = weightList.stream().mapToInt(m -> m).sum();
		int randomNum = (new Random().nextInt(weightSum));
		System.out.println("生成的的随机数:" + randomNum);
		Scene scene = null;

		for (int i = 0; i < weightList.size(); i++) {
			randomNum -= weightList.get(i);
			System.out.println("随机数递减:" + randomNum);
			if (randomNum < 0) {
				scene = mapList.get(i);
				if (scene == null) {
					scene = mapList.get(0);
				}
				break;
			}
		}
		System.out.println("最终地图位置:" + scene.index);

		List<Integer> list1 = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		List<Integer> list3 = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			int randomValue = (new Random().nextInt(weightSum));
			if (randomValue <= 60) {
				list1.add(randomValue);
			} else if (randomValue > 60 && randomValue <= 80) {
				list2.add(randomValue);
			} else {
				list3.add(randomNum);
			}
		}
		System.out.println(list1.size());
		System.out.println(list2.size());
		System.out.println(list3.size());
	}

	static class Scene {
		public int index;

		public Scene(int index) {
			this.index = index;
		}
	}
}
