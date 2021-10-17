# Programing Assignment: Slider Puzzle

# 成绩

![image-20211017205552689](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20211017205552689.png)

# 一些分析

在 specification 中，已经大致描述了 A* 算法 的搜索过程：从初态开始，每一步都选择 priority 最小的局面(board)。

在衡量 priority 时，给出了两个 priority function，虽然 specification 中说两个任意选一个，但 Manhattan priority function 的效果更好。实际上若两个局面 Manhattan priority 相同，可以比较 Hamming priority，这也是提升算法表现的一个方向。



## equals(Object v) 的重写(override)

这个方法第一想法是传入一个 Board 类型的参数，但 specification 中特意强调了

> The `equals()` method is inherited from `java.lang.Object`, so it must obey all of Java’s requirements.

关于 equal() 方法的重写，可以阅读这篇博客：[说说如何重写Java的equals方法](https://hellofrank.github.io/2019/09/21/%E8%AF%B4%E8%AF%B4%E5%A6%82%E4%BD%95%E9%87%8D%E5%86%99Java%E7%9A%84equals%E6%96%B9%E6%B3%95/)

```java
    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == null) return false; 
        if (y == this) return true;	
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return Arrays.deepEquals(this.blocks, that.blocks);
    }
```

所以 equal() 的重写有个大概模板：

1. 判断是否为 null
2. 判断是否为这个对象的引用
3. 判断类型是否相同
4. 类型转换
5. 值相等( JVM 会帮助我们把值相等的元素指向同一个内存地址，从而实现 `==` 的判断值相等功能)
   Object.equals(Object o1, Object o2) 判断的是 o1 与 o2 的值相等/逻辑相等。



## toString() 使用StringBuilder 而不是 String 的 +=

```java
    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension).append("\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                sb.append(String.format("%2d ", blocks[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
```

使用 String 新建一个字符串 s，然后在后面 `s += a[i][j]`，这样相当于频繁地 new 一个新字符串，把旧的拷贝进去后再加上新增的，很容易就把时间提升到平方级别。而 StringBuilder 相当于一个可变字符串，使用 append() 方法往后面添加新增字符，时间只是线性级别的。



## 不可解局面导致的爆栈

几个 unsolvable boards 会由于调用 MinPQ 过多而导致爆栈抛出异常。

```
Test 5a: call isSolvable() with file inputs
  * puzzle01.txt
  * puzzle03.txt
  * puzzle04.txt
  * puzzle17.txt
  * puzzle3x3-unsolvable1.txt
    - initial board =
        3
         1  2  3 
         4  6  5 
         7  8  0 


    
        ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        OperationCountLimitExceededException
        Number of calls to methods in MinPQ exceeds limit: 10000000
        ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

==> FAILED

Test 5b: call isSolvable() on random n-by-n boards
  * 100 random 2-by-2 boards
==> passed

Test 6: check moves() on unsolvable puzzles
  * puzzle2x2-unsolvable1.txt
    - initial board =
        2
         1  0 
         2  3 


    
        ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        OperationCountLimitExceededException
        Number of calls to methods in MinPQ exceeds limit: 10000000
        ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

==> FAILED

Test 7: check solution() on unsolvable puzzles
  * puzzle2x2-unsolvable1.txt

    
        ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        OperationCountLimitExceededException
        Number of calls to methods in MinPQ exceeds limit: 10000000
        ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

==> FAILED

Test 8a: check that Solver is immutable by testing whether methods
         return the same value, regardless of order in which called
  * puzzle3x3-00.txt
  * puzzle3x3-01.txt
  * puzzle3x3-05.txt
  * puzzle3x3-10.txt
  * random 2-by-2 solvable boards
==> passed

Test 8b: check that Solver is immutable by testing whether methods
         return the same value, regardless of order in which called
  * puzzle3x3-unsolvable1.txt

    
        ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        OperationCountLimitExceededException
        Number of calls to methods in MinPQ exceeds limit: 10000000
        ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    - sequence of Solver operations was:
          Solver solver = new Solver(initial);

==> FAILED

```

在 A* 算法的实现中，我并没考虑 unsolvable 的情形，如果输入是不可解的，则程序会陷入死循环导致爆栈。

在 Board 类中，需要我们实现一个 twin() 的API，我起初完全无法理解这个 API 到底有啥用，后来也是看了[别人的博客](https://littlewhite-hai.github.io/2018/06/11/%E5%AF%BB%E6%89%BE%E6%8B%BC%E5%9B%BE%E6%B8%B8%E6%88%8F%E6%9C%80%E4%BC%98%E8%A7%A3/#more)和[Programing Assignment Checklist](https://www.cs.princeton.edu/courses/archive/spring11/cos226/checklist/8puzzle.html)，才意识到这个滑块游戏的一个性质：

> A twin is obatined by swapping two (adjacent) blocks (the blank does not count) **in the same row**. 

**一个有解的局面(board)的twin是无解的，反之，若一个局面的twin是有解的，则这个局面本身无解**

就给我一个启发：如果我们同步地解输入 board 的 twin，在求解过程中 twin 先被解出来，则证明原 board 是不可解的。

(一个问题：如果我们选的这个 twin 也是不可解的，那不就无了... 还是说我们能证明 twin 的等价性?)

网上冲浪，发现滑块问题是否可解有很多讨论，如13年的[这篇问题](https://math.stackexchange.com/questions/293527/how-to-check-if-a-8-puzzle-is-solvable)，里面的答案提到了*逆元(inversion)*的概念。

假设有如下 board：(来自 puzzle3x3-unsolvable1.txt)

```
 1  2  3
 4  6  5
 7  8  0
```

把它写成一行：`1 2 3 4 6 5 7 8` (忽略 0)，大数在小数前面就称它俩构成一组逆元，如(6,5)就构成一组逆元。

有如下 board：(来自 puzzle3x3-05.txt)

```
 1  0  2 
 4  6  3 
 7  5  8 
```

`1 2 4 6 3 7 5 8` 里有(4,3),(6,3),(6,5),(7,5) 四组逆元。

不加证明地给出：**逆元组数为偶数的局面是可解的，为奇数的则不可解**

PS：其实到这里我们直接写个 countInversions() 的方法直接判断 board 是否可解就行了，但这和题意(用解 twin 来判断)不太符合。

再回到 twin 这里，交换两个相邻的数，不会影响它们与另外 $N^2-1-2$ 个数的逆元关系，它俩若原来是一对逆元，则交换后逆元数减一，反之则加一。

这就把我上面的担心解除了，**不可解局面的twin一定是可解的**(其中的数学原理我完全不清楚...)

所以我们同时求解 board 和 board.twin() ，求解过程一定会结束(两个必有一个可解)，判断board是否解出结果。


