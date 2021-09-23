# Interview Questions: Analysis of Algorithms (ungraded)

1. 3-SUM in quadratic time. Design an algorithm for the 3-SUM problem that takes time proportional to $n^2$ in the worst case. You may assume that you can sort the n integers in time proportional to $n^2$ or better.  

![](https://picgoej.oss-cn-beijing.aliyuncs.com/20210922202437.png)

My answer: First，sort the N numbers with insertion sort algorithm, with O(N^2) in worst case. Second, for each pair of numbers a[i] and a[j]，hash map for -(a[i]+a[j])，with O(1*N^2). 

2. Search in a bitonic array. An array is bitonic if it is comprised of an increasing sequence of integers followed immediately by a decreasing sequence of integers. Write a program that, given a bitonic array of nnn distinct integer values, determines whether a given integer is in the array.
![](https://picgoej.oss-cn-beijing.aliyuncs.com/20210922192325.png)

![](https://picgoej.oss-cn-beijing.aliyuncs.com/20210922202603.png)

3. Egg drop. Suppose that you have an nnn-story building (with floors 1 through nnn) and plenty of eggs. An egg breaks if it is dropped from floor TTT or higher and does not break otherwise. Your goal is to devise a strategy to determine the value of  TTT given the following limitations on the number of eggs and tosses:
![](https://picgoej.oss-cn-beijing.aliyuncs.com/20210922192128.png)

![](https://picgoej.oss-cn-beijing.aliyuncs.com/20210922202716.png)

My answer:   
Version 0: Drop an egg floor by floor from floors 1 until egg breaks;  
Version 1: Binary search to n floors. Step 1, drop egg from the middle floor of 1 and n floors, if breaks, drop from the middle floor of 1 and the middle floor of 1 and n floors, otherwise drop from the middle floor of the middle floor of 1 and n floors and n，repeat it until find the floor.
Version 2:Drop from floor 1, 2, 4, 8, ..., 2^(n-1) floors, suppose first broken egg is dropped from floors 2^t, then drops with binary search from floors 2^(t-1) to 2^(n-1) until the floor is found.
Version 3:uhhhh... 