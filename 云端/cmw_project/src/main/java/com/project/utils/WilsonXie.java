package com.project.utils;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by xieyanhao on 17/2/28.
 */
public class WilsonXie {

    public static void main(String[] args) {
       /* int[] arrs = {49, 38, 65, 97, 76, 13, 27, 11};
        bubbleSort(arrs);
        System.out.println(Arrays.toString(arrs));*/

        random();
    }

    public static void random() {

        int[] arr = {49, 38, 65, 97, 76, 13, 27, 11};

        int temp = 0;
        int size = arr.length;

        for (int i = 0; i < size -1; i++) {
            for (int j = 0; j <  size-1; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }

        for (int i = 0;i < 4; i ++) {
            System.out.print(arr[i] + ",");
        }
    }

    /**
     * 冒泡排序
     * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。
     * 针对所有的元素重复以上的步骤，除了最后一个。
     * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
     *
     * @param numbers 需要排序的整型数组
     */
    public static void bubbleSort(int[] numbers) {

        int temp = 0;
        int size = numbers.length;
        for (int i = 0; i < size - 1; i++) {

            for (int j = 0; j < size - 1 - i; j++) {
                if (numbers[j] > numbers[j + 1]) {
                    temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
            }
        }

    }


    public static int getMiddle(int[] numbers, int low, int high) {
        int temp = numbers[low]; //数组的第一个作为中轴
        while (low < high) {
            while (low < high && numbers[high] > temp) {
                high--;
            }
            numbers[low] = numbers[high];//比中轴小的记录移到低端
            while (low < high && numbers[low] < temp) {
                low++;
            }
            numbers[high] = numbers[low]; //比中轴大的记录移到高端
        }
        numbers[low] = temp; //中轴记录到尾
        return low; // 返回中轴的位置
    }











}
