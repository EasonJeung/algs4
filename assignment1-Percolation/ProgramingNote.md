# Programing Assignment: Percolation

![image-20210922090833311](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210922090833311.png)

只有 94 分，orz

这个问题的关键在于如何把这个二维的格子投影成一维的数组以便使用 union-find

很自然的，我们按照从上到下、从左到右的顺序把格子放到一维数组中，通过函数 `xyTo1D(row, col)`可轻易实现。

长度为 n*n 的boolean数组 sign 存储的是该格子是否是 open 的。

长度为 n\*n + 2 的 `WeightedQuickUnionUF`数组前 n\*n 个是每个格子的根结点，**我们把 top 和 bottom 分别看做一个虚拟的结点，放在倒数第二个和倒数第一个位置上**。

其他的就很直观了

# FAQ

## backwash 与 4 个isFull 的 Test无法通过

最后4个Test（Test 18 ~ 21）死活通不过

```
Test 18: check for backwash with predetermined sites
  * filename = input20.txt
    - isFull() returns wrong value after 231 sites opened
    - student   isFull(18, 1) = true
    - reference isFull(18, 1) = false

  * filename = input10.txt
    - isFull() returns wrong value after 56 sites opened
    - student   isFull(9, 1) = true
    - reference isFull(9, 1) = false

  * filename = input50.txt
    - isFull() returns wrong value after 1412 sites opened
    - student   isFull(22, 28) = true
    - reference isFull(22, 28) = false

  * filename = jerry47.txt
    - isFull() returns wrong value after 1076 sites opened
    - student   isFull(11, 47) = true
    - reference isFull(11, 47) = false

  * filename = sedgewick60.txt
    - isFull() returns wrong value after 1577 sites opened
    - student   isFull(21, 59) = true
    - reference isFull(21, 59) = false

  * filename = wayne98.txt
    - isFull() returns wrong value after 3851 sites opened
    - student   isFull(69, 9) = true
    - reference isFull(69, 9) = false

==> FAILED

Test 19: check for backwash with predetermined sites that have
         multiple percolating paths
  * filename = input3.txt
    - isFull() returns wrong value after 4 sites opened
    - student   isFull(3, 1) = true
    - reference isFull(3, 1) = false

  * filename = input4.txt
    - isFull() returns wrong value after 7 sites opened
    - student   isFull(4, 4) = true
    - reference isFull(4, 4) = false

  * filename = input7.txt
    - isFull() returns wrong value after 12 sites opened
    - student   isFull(6, 1) = true
    - reference isFull(6, 1) = false

==> FAILED

Test 20: call all methods in random order until all sites are open
         (these inputs are prone to backwash)
  * n = 3
    - isFull() returns wrong value after 6 sites opened
    - student   isFull(2, 1) = true
    - reference isFull(2, 1) = false

    - failed on trial 13 of 40

  * n = 5
    - isFull() returns wrong value after 17 sites opened
    - student   isFull(5, 5) = true
    - reference isFull(5, 5) = false

    - failed on trial 2 of 20

  * n = 7
    - isFull() returns wrong value after 29 sites opened
    - student   isFull(7, 1) = true
    - reference isFull(7, 1) = false

    - failed on trial 1 of 10

  * n = 10
    - isFull() returns wrong value after 66 sites opened
    - student   isFull(6, 1) = true
    - reference isFull(6, 1) = false

    - failed on trial 1 of 5

  * n = 20
    - isFull() returns wrong value after 219 sites opened
    - student   isFull(14, 20) = true
    - reference isFull(14, 20) = false

    - failed on trial 1 of 2

  * n = 50
    - isFull() returns wrong value after 1543 sites opened
    - student   isFull(28, 24) = true
    - reference isFull(28, 24) = false

    - failed on trial 1 of 1

==> FAILED
```

看了一下，都是说这块格子不是与顶部连通的，但我的结果是连通的。

~~猜测了一下，大概还是我的 `open(row, col)`写的有点问题，但我捉不到虫啊 orz~~

## 2021.09.22 Update: 

### bug location：

![image-20210922084322643](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210922084322643.png)

之所以会出现"这块格子不是与顶部连通的，但我的结果是连通的。"，是最后 top 和 bottom 连通了，所以导致只与 bottom 连通的底部块也被算作与顶部连通。

如上图右侧图下方那两个涂蓝的区域，它只与 bottom 连通，但top 和 bottom 连通后导致这两区域也算作与 top 连通，即isFull()判定其为true。

### Solution：

fuck，I have no idea...

FYI, check [Princeton Spring COS226 assignment percolation chicklist](https://www.cs.princeton.edu/courses/archive/spring19/cos226/assignments/percolation/checklist.php)

