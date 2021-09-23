# 1.5 Union-Find 并查

## dynamic connectivity 动态连通性

* Union 操作：两个数之间加一个连接；
* Find/connected 查询：判断两个数之间是否有连接（路径）。![image-20210916203348332](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210916203348332.png)

### 相连（is connected to）满足以下条件：

> 自反性（Reflexive）： p和p是相连的；
>
> 对称性（Symmetric）： 如果p和q是相连的，那么q和p也是相连的；
>
> 传递性（Transitive）： 如果p和q相连且q和r相连，那么p和r相连。
>
> 
>
> 作者：El_Nino_
> 链接：https://www.jianshu.com/p/44541a3fe8b3
> 来源：简书
> 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

### 等价类（Connected components）

所有相连的数的集合。（Maximal set of objects that are mutually connected.）

![](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210916204011044.png)



### 操作实现：

* Find/connected 查询：两个数是否在一个等价类；

* Union 操作：将两个数所在的等价类并成一个。

  ![image-20210916210204433](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210916210204433.png)



### API：

- 需要考虑的问题：
  - 数的数量 N 可能很大；
  - 操作的数量 M 可能很大；
  - 查与并操作可能混在一起；
  - 输入：
    - 输入数量N；
    - 成对输入，如果这一对没连接，连接它们并成对打印输出；
      ![image-20210916205852797](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210916205852797.png)

## quick find

### 数据结构![image-20210916231542133](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210916231542133.png)

![image-20210916231846721](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210916231846721.png)

Union 时，谁改？改成什么？

![image-20210916232209060](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210916232209060.png)

教授强调了 union() 函数里，要先取出id[p]的值放到pid中。for 循环里的if条件不能写成 `id[i] == id[p]`。如果这样写的话，在遍历到 p 位置时，id[p]换成了 id[q]，这样的话，p 位置后的 与原来的p在一个component里的元素就无法被更新component编号了。

| algorithm  | 初始化 | Find/connected | Union |
| ---------- | ------ | -------------- | ----- |
| quick-find | N      | 1              | N     |

从上面分析可以看出，Quick-find 很慢。

对 N 对点进行 Union 操作，复杂度为 O(n^2)



## quick union

![image-20210916234123991](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210916234123991.png)

![image-20210916234731487](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210916234731487.png)

![image-20210916234828280](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210916234828280.png)

## Improvement

### Weighted quick-union

* quick-union 的一个弊端就是树可能会长得很畸形，特别高，我们可以尝试**避免形成长得很高的树。**

* 将矮树的根挂在高的树上

  这部分没太理解，看看教授讲得咋样

![image-20210917100035109](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210917100035109.png)

在 Union 操作时，会判断两个节点的树哪个更矮，将矮树挂在高树的根上

![image-20210917101155385](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210917101155385.png)

通过加权，保证任一结点的深度不会超过 lgN (N为结点总数)，进而保证 Find 操作复杂度在 lg(N) => Find主要就是 root()函数； Union 操作在 root() 基础上只需花费常数阶时间

命题的证明：![image-20210917102859069](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210917102859069.png)

假设树T2比T1高，其中树T1包含结点x，当T1合并到T2上时，x的深度会加1。

- 合并后的这个树的结点数量，至少是树T1结点数量的两倍；
- 包含结点 x 的树最多合并 lgN 次(这时就已经把所有结点合并到一棵树上了)。

![image-20210917105317180](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210917105317180.png)

教授说从 1 million 到 1 billion(1000倍增长)处理时间只是从 20 变为 30，lg N 在大规模问题上表现还是挺优秀的。

---

![image-20210917175207959](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210917175207959.png)

> Be careful not to confuse *union-by-size* with *union-by-height*. The former uses the size of the tree (number of nodes) while the latter uses the height of the tree (number of links on longest path from the  root of the tree to a leaf node). Both variants guarantee logarithmic  height.
>
> 大小与高度，二者都能保证对数阶高度。

### path compression

![image-20210917125901893](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210917125901893.png)

> 图片来源：https://www.jianshu.com/p/44541a3fe8b3

从结点9开始，id[9]为6，将id[9]设为其root结点：0；然后是结点6，id[6]为3，将id[6]设为0；然后是结点3，id[3]为1，将id[3]设为0；然后是结点1，id[[1]为0。这样一系列操作，将结点9自身及其先辈结点全部挂到根上

**如果对每一个叶子结点都进行path compression，最后就变成了 quick-find 了。**

**实现**

![image-20210917133240485](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210917133240485.png)

![image-20210917133339007](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210917133339007.png)

以上面那个树为例子，查询结点9的根结点，第一次查询如上图所示。

如果再对结点9查询根结点，会把结点9挂到结点0上。

**在查询根结点的过程中，依次把经过的结点挂到其祖父结点上。如上图，9挂到3上，3挂到0上。**

**查询根结点次数越多，整个树就越来越平整，最终是变成了 根结点和它若干叶子结点，这就是 quick-find 了**

**添加的那行代码实质上是在找根的过程中，一次跨两步，并把层数较深的结点往上提**



### WQUPC(Weighted quick-union with path compression) 算法复杂度分析

命题：对 N 个数，进行M次 union-find 操作，使用WQUPC算法，最多需要访问数组的次数不超过 c(N + M lg\*N)。(c为常数， lg\* N是lg(lg(lg(...lg()...))) 称作 *迭代对数函数*。)

迭代对数函数：使N变为1需要取对数的次数。实际一般把它当做一个不大于5的数。

![image-20210917174503838](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210917174503838.png)

理论上，WQUPC 算法不是线性的；实际中，WQUPC算法是线性的。

事实上/已证明，没有完全线性的算法存在。(对Union-find来说?)

![image-20210917140007592](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210917140007592.png)

## Union-Find Application

![image-20210917180642211](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210917180642211.png)

这个块被涂白，四周原来有几个白块就需要调用几次 union()，可能四周都是黑的(0次)

---

---

# Interview Questions: Union–Find (ungraded)

Question 1 **Social network connectivity.** 

Hint：直接就是Union-Find

Question 2 **Union-find with specific canonical element.** 

Hint：额外维护一个largest[]数组，用来存储每个component的最大值，并在 union()操作中更新

Question 3 **Successor with delete.**

Hint：将 root(x)置为 -1 以实现把它从原本component 里删除；遍历 x+1, x+2, ..., n-1，找到第一个与 x 在同一个 component 里的 node **（需要在删除 x 完成查找其successor的操作）**

---

---

# Programing Assignment：Percolation

这个问题的关键在于如何把这个二维的格子投影成一维的数组以便使用 union-find

很自然的，我们按照从上到下、从左到右的顺序把格子放到一维数组中，通过函数 `xyTo1D(row, col)`可轻易实现。

长度为 n*n 的boolean数组 sign 存储的是该格子是否是 open 的。

长度为 n\*n + 2 的 `WeightedQuickUnionUF`数组前 n\*n 个是每个格子的根结点，**我们把 top 和 bottom 分别看做一个虚拟的结点，放在倒数第二个和倒数第一个位置上**。

其他的就很直观了

# FAQ

## backwash 与 4 个isFull 的 Test无法通过

最后有4个Test死活通不过

![image-20210921235058188](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210921235058188.png)

![image-20210921235129736](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210921235129736.png)

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

