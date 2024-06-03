# Keywords-Aware Top-k Routes Identification in Road Networks

赵浩宇, Java, 2022-2024. 


## 使用方法

```java
// 43,25,14,28,19,26,48,47时间，43,25,5,18,19,26,48,47剪枝效率
int[] POI_Type = {43, 25};
// 想取的结果数
int k = 20;
double alpha = 0.5;
int num = 1; //循环次数
```

在 `main/KATR.java` 中，修改如上参数。 POI_Type 的数量对应论文中的 `u`。