# Observations

**计算时间**，把问题规模翻倍以计算系数值

由于硬件、系统、软件等因素的影响，很难得到精确的测量结果。

# Mathematical

Total running time: sum of **cost × frequency** for all operations.

![image-20210922113006151](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210922113006151.png)

新手错误：滥用字符串拼接。(以为这是个常数时间操作，实际上是与字符串长度正相关的)

Simplification:

1. **cost model.** Use some basic operation as a proxy for running time.

   开销最大的基本操作或者执行次数最多的用开销最大频率最高的操作来代表执行时间。

   一般，我们假设实际的运行时间就是常数乘以这个操作的执行时间。

2. **tilde notation.** 

   丢掉低阶项

 ![](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210922115121303.png)

<img src="https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210922115204574.png"/>

❗ **不能看到一层 for 循环，就乘以个 n 倍！**

![image-20210922120643084](https://picgoej.oss-cn-beijing.aliyuncs.com/image-20210922120643084.png)

蓝色部分是 n-1 + n -2 + ... + 1 = $\frac{1}{2} n(n-1)$

绿色部分每次数组访问是一个常数量，总共是 3\*lgn

所以总的是 $\sim \frac{3}{2} n^3$

# Order-of-Growth Classification
<img src="https://picgoej.oss-cn-beijing.aliyuncs.com/20210922130132.png"/>
介绍了二分查找（Binary search）
<img src="https://picgoej.oss-cn-beijing.aliyuncs.com/20210922132203.png"/>
对式子 T(N) <= T([N/2]) + 1 的理解：
对一长度为 N 的序列，寻找其中某个元素，首先对其二分（1 次比较来检查中间元素和被查找的元素是否相等），继续查找，无论是左侧子序列还是右侧子序列的长度均不会超过[N/2]，故 小于等于号
<img src="https://picgoej.oss-cn-beijing.aliyuncs.com/20210922135556.png"/>

3-sum 算法的优化
1. 插入排序(insertion sort) $N^2$
2. 二分查找(binary search) $N^2lgN$  
所以优化后的 1 + 2 = $N^2lgN$

# theory of algorithms
输入的好坏决定了算法表现的优劣
- 最好情况：代价下界
- 最坏情况：代价上界
- 平均情况：期望代价

输入模型对算法的影响
我们对输入的具体性质是无法掌控的，这种情况下我们对算法的代价的分析有两种方式：
1. 对最坏情况下的性能的保证
2. 随机化算法。例如通过随机打乱输入，快速排序能极大概率保证其性能是对数级别的，而不是最坏情形下的平方级别。

## 对算法的讨论
目标：
1. 确定问题的难度
2. 找到最优算法

方法：
1. 在分析中忽略细节，将分析做到只差一个常数倍数的精度
2. 讨论最坏情况

最优算法
1. 性能保证：对任何输入，性能差距只需乘一个常数系数
2. 没有表现更好的算法了
<img src="https://picgoej.oss-cn-beijing.aliyuncs.com/20210922155603.png"/>
- Big Theta：用来区分算法。  
- Big Oh：算法代价上界，最坏。比如O(N^2)就表示当N增长时，运行时间小于某个常数乘以N^2。  
- Big Omega：代价下界。Ω(N^2)表示当N增长时运行时间比某个常数乘以N^2大。

**如何证明某个算法是最优的？**  
类似于数学中的夹逼定理，以查找一组数字中是否有为零的项这个问题为例。采用暴力算法，很容易证明总代价不超过某个常数乘以 N，即上界为O(N)，而我们必须检查每一项，所以下界为Ω(N)，除去常数，上下界是一样的，即证明了暴力算法是最优解，其时间代价是Θ(N)  
在更复杂的问题中，确定上下界是很困难的。例如 3-sum 问题中，改进后的上界为 O(N^2lgN)。我们能知道每一项都需要被检验，否则可能会漏掉一个三数字和为零的情况，所以下界是Ω(N)。上界和下界之间存在间隔，所以这是个开放问题，我们不知道最优算法是什么，我们甚至不知道是否存在一个运行时间小于O(N^2)的算法，我们也不知道有没有比线性阶更高的下界。

算法设计的过程  
- 提出一个算法
- 如果有gap
   - 则降低上界(发现一个新算法)
   - 或提升下界(更困难)

Caveats/附加说明
- 或许只盯着最坏情况有些过度悲观了，实际工程中，最坏情况我们有些时候并不过度关注。
- 在做更细致地对比性能差异时，常数因子的差异的就不太精确了，即我们需要比常数因子级误差更准确的分析。很多研究都把O()分析的结果当做运行时间的近似模型，它应该是算法的上界
![](https://picgoej.oss-cn-beijing.aliyuncs.com/20210922175606.png)
![](https://picgoej.oss-cn-beijing.aliyuncs.com/20210922175902.png)

# memory
![](https://picgoej.oss-cn-beijing.aliyuncs.com/20210922180355.png)
![](https://picgoej.oss-cn-beijing.aliyuncs.com/20210922180712.png)
![](https://picgoej.oss-cn-beijing.aliyuncs.com/20210922180856.png)

# Summary
- 实验分析
   - 通过实验得到执行时间
   - 利用幂法则(power law)形成对运行时间的假说
   - 利用模型**预测(具体时间)**
- 数学分析
   - 分析算法的基本操作的次数
   - 使用波浪记号(tilde notation)简化分析
   - 模型能让我们**解释算法表现**
- 科学方式
  - 数学模型与具体系统无关，即使这个机器还没有建成
  - 实验模型在验证数学模型时不可缺少，我们可以用它预测具体时间