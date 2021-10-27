# Programing Assignment: KdTrees

# 成绩

![image-20211027223358997](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20211027223358997.png)



# 分析

## PointSET.java

`PointSET.java` 非常容易完成，specification 中指出了使用 Brute-force implementation，还提示了必须使用 [`SET`](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/SET.html) 或 [`java.util.TreeSet`](https://docs.oracle.com/javase/8/docs/api/java/util/TreeSet.html)。

故我选择使用 TreeSet 来直接实现 PointSET

```Java
	private final TreeSet<Point2D> pointSet;
	...
    public PointSET() {
        pointSet = new TreeSet<>();
    }
```

至于`range()`(查找哪些点在给定长方形内)和`nearest()`(寻找与给定点距离最近的点)，简单地遍历整个树即可实现。

---

## KdTree.java

`KdTree.java` 则暗示我们需要自己定义树的结点 `Node`，需要有的属性：点、点所代表的长方形(Each node corresponds to an axis-aligned rectangle in the unit square, which encloses all of the points in its subtree.)、点所在分割线的方向(垂直或水平)、两个子树的引用。同时，由于后面我们需要经常判断一个点与另一个结点(所对应的点)的位置关系(左/下还是右/上)，所以在 Node 里实现 compareTo() 方法。

由于我们后面遇到的所有问题的解决思路都是递归地在子树中查找，所以 insert、contains等方法都需要重载(overload)。

## insert 方法的实现

整个代码中最为复杂的就是 insert 方法的实现，下面来讲解其中的几个值得一提的点。

### 为什么重载后的 insert() 有四个参数?

一般来说，在树中递归查找只需前两个参数(Point2D p, Node cur)，但每个结点对应的长方形依赖于其父结点的分割线的方向和当前结点 cur 对应的点与其父结点对应的点的相对位置。通过观察代码也能发现在树中递归搜索时是没有用到后两个参数时，只有在构建新结点的长方形时才用到后两个参数。

### correspondRect() 逻辑讲解

结合该该方法上方注释很容易理解

```JAva
     * ____________________|    location > 0    |      location < 0    |
     * parent.p.VERTICAL   | parent.rect.LEFT   |   parent.rect.RIGHT  |
     * parent.p.HORIZONTAL | parent.rect.BOTTOM |   parent.rect.TOP    |
     ...
     * @param location - 1 if cur in left/bottom subtree of parent,
     *                  -1 if cur in right/top subtree
```

此时通过层层递归，我们已经来到了叶子结点下的 null node处(记为当前结点)

若其父结点的分割线是竖直方向的(VERTICAL)，则当前点在父结点左侧或右侧。

​	再比较`当前结点(对应点)`->即要插入的点与父结点(对应点)的位置关系：`cur.compareTo(P)`

​	大于零说明要插入点在叶子结点(父结点)的左侧，插入点对应长方形为叶子结点(父结点)的长方形的左侧。

​	小于零在右侧。

分割线是水平方向的(HORIZONTAL)，也类似。

orz，感觉越讲越混乱，自己在纸上画一下就能懂了

## contains() 方法

整体思路就是看给定点在当前结点对应点的左/下方还是右/上方，然后进去搜索，总体上是逐步找到离给定点最近的点，若恰好相等，证明当前点就是给定点，若直到null node也没相等，证明给定点确实不在set 中。

## draw() 方法

按照什么方法遍历不重要，我在这里选择中序遍历(左子树->当前结点->右子树)

draw 当前结点时先 draw 点，再 draw 它所在的分割线(需根据其竖直或水平选择颜色及确定线两个端点的坐标)

## contains() 方法

检查当前结点对应点是否在矩形内

然后若左/右子结点与给定矩形相交，则进左/右子节点比较左子节点对应点是否在矩形内；若不相交，则左/右子结点一定不在给定矩形中。

## nearest() 方法

使用一个 tmpNearest 来记录比较过程中**当前**最短的距离对应的点。

在课程视频中教授提到，我们根据目标点在当前点的相对位置(左/下，右/上)，去对应的子树搜索，这样得到最近的点的概率更高。

但也有例外，所以我们在检索完当前侧子树后需要判断 当前最短距离 与 给定点到另外一侧子树根结点对应长方形的距离，若前者更小则另外一侧一定没有比当前距离更小的点；若后者更小，则另一侧可能存在距离更小的点，也需要搜索。

