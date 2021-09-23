# Programing AssignmentInterview Questions: Union–Find (ungraded)

Question 1 **Social network connectivity.** 

Hint：直接就是Union-Find

Question 2 **Union-find with specific canonical element.** 

Hint：额外维护一个largest[]数组，用来存储每个component的最大值，并在 union()操作中更新

Question 3 **Successor with delete.**

Hint：将 root(x)置为 -1 以实现把它从原本component 里删除；遍历 x+1, x+2, ..., n-1，找到第一个与 x 在同一个 component 里的 node **（需要在删除 x 完成查找其successor的操作）**