package com.think.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RateTest {

  public static void main(String[] args) {
    // 30,60,80 权重列表
    List<Integer> weightList = Arrays.asList(90, 50, 20);


    List<Integer> list1 = new ArrayList<>();
    List<Integer> list2 = new ArrayList<>();
    List<Integer> list3 = new ArrayList<>();

    int sum = weightList.stream().mapToInt(t -> t).sum();
    int random = (int) (Math.random() * sum);
    System.out.println("随机数:" + random);
    int mapIndex = -1;
    int index = 0;
    boolean flag = true;

    do {
      if (random-- <= 0) {
        break;
      }
      for (int j = 0; j < weightList.size(); j++) {
        if (weightList.get(j) == random) {
          mapIndex = j;
          flag = false;
          break;
        }
      }
      index++;
    } while (index < sum && flag);
    
    System.out.println("地图索引位置:" + mapIndex);
    System.out.println("随机数:" + random);
    
    if (mapIndex == -1) {
      int randomMapIndex = weightList.get(new Random().nextInt(weightList.size()));
      mapIndex = randomMapIndex;
      System.out.println("未找到合适的地图,使用默认随机选举方式获取地图:" + mapIndex);
    }

    for (int i = 0; i < sum; i++) {
      int randomNum = (int)(Math.random() * sum);
      if (randomNum <= 20) {
        list3.add(randomNum);
      } else if (randomNum > 20 && randomNum <= 50) {
        list2.add(randomNum);
      } else {
        list1.add(randomNum);
      }
    }

    System.out.println("第一个数：" + list1.size());
    System.out.println(list1);
    System.out.println("第二个数：" + list2.size());
    System.out.println(list2);
    System.out.println("第三个数：" + list3.size());
    System.out.println(list3);
  }

}
