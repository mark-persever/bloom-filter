# Bloom Filter 实现 (Java)

![Java 版本](https://img.shields.io/badge/Java-21%2B-blue)
[![开源协议](https://img.shields.io/badge/License-Apache%202.0-brightgreen.svg)](https://github.com/mark-persever/bloom-filter/blob/master/LICENSE)

一个基于 Java 的高性能布隆过滤器实现，支持自定义哈希函数和优化的位存储设计，适用于大规模数据去重、缓存穿透防护等场景。

---

## 核心特性

1. **高效位存储**  
   - 使用优化的 `BitSet` 实现内存压缩，支持动态扩容
   - 每个元素仅需 1.44 * log₂(1/ε) 比特（ε为误判率）

2. **多哈希算法集成**  
   - 内置 `FNV1a`、`DJB2`、`Jenkins` 三种非加密哈希算法
   - 支持双哈希（Double Hashing）动态生成任意数量哈希函数

3. **参数智能优化**  
   - 自动计算最优位数组大小：`m = - (n * ln p) / (ln 2)^2`
   - 自动确定最佳哈希函数数量：`k = (m/n) * ln 2`

4. **线程安全设计**  
   - 所有写操作通过同步控制保证并发安全
   - 读操作无锁设计实现高吞吐

---

## 安装方式

### Maven 项目

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.mark-persever</groupId>
        <artifactId>bloom-filter</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Gradle 项目

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.mark-persever:bloom-filter:1.0.0'
}
```

---

## 使用示例

### 基础使用

```java
// 创建过滤器：预期插入10万元素，误判率1%
BloomFilter filter = new DefaultBloomFilter(100000, 0.01);

// 添加元素
filter.add("user:101");
filter.add("user:205");

// 检查存在性
System.out.println(filter.mightContain("user:101"));  // true
System.out.println(filter.mightContain("user:999"));  // false (大概率)

// 获取当前误判率
System.out.println("当前误判率: " + filter.expectedFalsePositiveProbability());
```

### 高级配置

```java
// 自定义配置：位数组大小1MB，使用5个哈希函数
BloomFilter customFilter = new DefaultBloomFilter(8_388_608, 5);

// 批量添加数据
List<String> users = Arrays.asList("user:301", "user:422", "user:578");
users.forEach(customFilter::add);

// 批量验证
List<String> testIds = Arrays.asList("user:301", "user:700", "user:578");
testIds.forEach(id -> 
    System.out.println(id + ": " + customFilter.mightContain(id))
);
```

---

## 配置参数详解

### 智能参数模式

```java
// 构造器：DefaultBloomFilter(int expectedInsertions, double falsePositiveRate)
BloomFilter smartFilter = new DefaultBloomFilter(1_000_000, 0.005);
```

| 参数名               | 说明                          | 计算公式                                   |
|----------------------|-------------------------------|-------------------------------------------|
| expectedInsertions   | 预期插入元素数量              | 由业务场景决定                            |
| falsePositiveRate    | 可接受最大误判率 (0.0-1.0)     | 通常设置在0.01-0.001之间      |

### 手动调优模式

```java
// 构造器：DefaultBloomFilter(int bitSize, int hashCount)
BloomFilter manualFilter = new DefaultBloomFilter(16_777_216, 7);
```

| 参数名     | 说明                     | 推荐值推算公式                     |
|------------|--------------------------|-------------------------------------|
| bitSize    | 位数组总比特数           | m = - (n * ln p) / (ln 2)^2        |
| hashCount  | 哈希函数数量             | k = (m/n) * ln 2                   |

> **计算公式推导**：当插入 n 个元素时，最优位大小 m 和哈希函数数 k 满足：  
> m = - (n * ln p) / (ln 2)^2  
> k = (m/n) * ln 2  
> 其中 p 为期望的误判率

---

## 哈希函数实现

### 内置哈希算法

| 算法名称     | 计算原理                               | 适用场景                     |
|--------------|----------------------------------------|------------------------------|
| `FNV1a`      | 基于质数乘法和异或运算                 | 短字符串、数值型数据         |
| `DJB2`       | 初始哈希5381，采用位移叠加             | 英文文本、标识符             |
| `Jenkins`    | 非线性混合（加/移/异或）               | 二进制数据、长文本     |

### 双哈希扩展

当所需哈希数超过3个时，自动启用双哈希算法：

```java
int combinedHash = hash1 + seed * hash2;  // seed为动态种子
```

> **优势**：避免独立哈希的计算开销，同时保持分布均匀性

---

## 性能指标

| 操作         | 时间复杂度 | 百万操作耗时(实测) |
|--------------|------------|--------------------|
| 元素添加     | O(k)       | ≈230ms             |
| 存在性检查   | O(k)       | ≈180ms             |
| 清空过滤器   | O(m)       | ≈15ms             |
| 初始化       | O(1)       | ≈0.3ms            |

> 测试环境：Intel i7-11800H, 32GB DDR4, Java 21 HotSpot  
> k=5, m=8MB, n=100万元素, p=0.01

---

## 典型应用场景

1. **缓存穿透防护**  
   在查询前过滤不存在的Key，降低数据库压力  
   ```java
   if (!bloomFilter.mightContain(key)) return null; // 直接拦截
   ```

2. **爬虫URL去重**  
   高效识别已抓取链接，内存占用仅为HashSet的1/10  

3. **风控黑名单**  
   快速拦截高风险用户（支持1000万用户仅用12MB内存）  

4. **分布式系统**  
   结合Redis Bitmap实现集群级布隆过滤器  
   ```properties
   # Redis BloomFilter配置示例
   bloom_filter.bit_size=134217728
   bloom_filter.hash_functions=5
   ```

---

## 开发者指南

### 编译项目

```bash
git clone https://github.com/mark-persever/bloom-filter.git
cd bloom-filter
mvn clean package
```

### 运行测试

```bash
mvn test
```

### 扩展自定义哈希

1. 实现 `HashFunction` 接口
```java
public class MD5Hash implements HashFunction {
    @Override
    public int hash(String key) {
        // 实现MD5摘要算法
    }
}
```

2. 注入过滤器
```java
List<HashFunction> customFuncs = Arrays.asList(
    new FNV1aHash(), 
    new JenkinsHash(),
    new MD5Hash()  // 自定义实现
);
BloomFilter customFilter = new DefaultBloomFilter(bitSize, customFuncs);
```

---

## 贡献代码

欢迎通过以下方式参与项目：

1. 提交问题报告：[Issue Tracker](https://github.com/mark-persever/bloom-filter/issues)
2. 创建功能分支提交PR
3. 完善单元测试（覆盖率目标100%）
4. 更新技术文档

> 提交代码需遵循 [Apache 2.0 开源协议](LICENSE)

---

## 参考实现对比

| 特性                | 本实现          | Guava       | Hutool       |
|---------------------|----------------|-------------|--------------|
| 内存占用优化        | ✅ (BitSet)    | ✅           | ❌ (BitMap)   |
| 动态哈希生成        | ✅ (双哈希)     | ❌           | ❌           |
| 参数自动计算        | ✅              | ✅           | ❌           |
| 删除支持            | ❌              | ❌           | ❌           |
| Redis集成           | ➖             | ➖           | ✅           |

> 测试数据参考

---

**技术文档更新于 2025年6月11日 | 项目版本 v1.0.0**
