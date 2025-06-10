# Bloom Filter 实现

[![Maven Central](https://img.shields.io/github/v/release/mark-persever/bloom-filter?color=blue&label=Release)](https://github.com/mark-persever/bloom-filter/packages)
[![许可证](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


一个高性能、内存高效的 Bloom Filter Java 实现，具有可配置的误判率和自动参数优化功能。

## 功能特性

- 🚀 **高性能**：优化的哈希函数和位操作
- 🧠 **内存高效**：使用 `long[]` 数组实现紧凑的位存储
- ⚙️ **自动优化**：根据预期元素数量和误判率自动计算最优参数
- 🔄 **多种哈希策略**：包含 FNV-1a、DJB2 和 Jenkins 哈希算法
- 🧪 **完整测试**：覆盖各种边界情况的单元测试
- 📦 **易集成**：简单的 Maven 依赖

## 安装

在 `pom.xml` 中添加以下依赖：

```xml
<dependency>
    <groupId>com.github.mark-persever</groupId>
    <artifactId>bloom-filter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 仓库配置

在 `pom.xml` 或 `settings.xml` 中添加 GitHub Packages 仓库：

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub mark-persever Apache Maven Packages</name>
        <url>https://maven.pkg.github.com/mark-persever/bloom-filter</url>
    </repository>
</repositories>
```

## 使用指南

### 基础用法

```java
import com.github.markpersever.bloom.BloomFilter;
import com.github.markpersever.bloom.DefaultBloomFilter;

public class Example {
    public static void main(String[] args) {
        // 创建过滤器：预期10万元素，0.1%误判率
        BloomFilter filter = new DefaultBloomFilter(100_000, 0.001);
        
        // 添加元素
        filter.add("https://example.com");
        filter.add("user@domain.com");
        filter.add("7b8a1d9c-3f6e-4a5b-9d2c-1e0f8a7b6c5d");
        
        // 检查元素是否存在
        System.out.println("包含 'example.com': " + 
            filter.mightContain("https://example.com"));  // true
        
        System.out.println("包含 'unknown': " + 
            filter.mightContain("unknown-value"));        // false
        
        // 获取预期误判率
        System.out.printf("误判概率: %.6f%n",
            filter.expectedFalsePositiveProbability());
    }
}
```

### 高级配置

```java
// 自定义位数组大小和哈希函数数量
BloomFilter customFilter = new DefaultBloomFilter(10_000_000, 5);

// 批量添加元素
List<String> items = /* 大数据集 */;
items.forEach(filter::add);

// 清空过滤器
filter.clear();

// 获取实际位使用量
System.out.println("使用的位数: " + filter.bitSize());
```

## 性能指标

| 元素数量 | 误判率 | 内存占用 | 哈希函数数量 |
|----------|--------|----------|--------------|
| 100,000  | 0.01 (1%) | 114 KB   | 7            |
| 1,000,000| 0.001 (0.1%) | 1.67 MB | 10           |
| 10,000,000| 0.0001 (0.01%) | 19.5 MB | 14          |

## 应用场景

- **URL 检查器**：快速检查 URL 是否已处理
- **邮件验证**：防止重复处理邮件
- **安全系统**：黑名单令牌的快速成员检查
- **大数据处理**：高效的去重近似计算
- **网络路由器**：数据包过滤和去重

## API 文档

### BloomFilter 接口

```java
public interface BloomFilter {
    /**
     * 添加元素到过滤器
     * @param key 要添加的元素
     */
    void add(String key);
    
    /**
     * 检查元素是否可能在过滤器中
     * @param key 要检查的元素
     * @return true 表示元素可能存在（有一定误判概率），
     *         false 表示元素绝对不存在
     */
    boolean mightContain(String key);
    
    /**
     * 计算预期误判概率
     * @return 当前预期误判率
     */
    double expectedFalsePositiveProbability();
    
    /**
     * 清空过滤器中的所有元素
     */
    void clear();
    
    /**
     * 获取过滤器的总位数
     * @return 位数组中的位数
     */
    long bitSize();
}
```

## 从源码构建

1. 克隆仓库：
```bash
git clone https://github.com/mark-persever/bloom-filter.git
cd bloom-filter
```

2. 使用 Maven 构建：
```bash
mvn clean package
```

3. 运行测试：
```bash
mvn test
```

## 贡献指南

欢迎贡献！请按以下步骤操作：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/你的特性`)
3. 提交更改 (`git commit -am '添加新特性'`)
4. 推送到分支 (`git push origin feature/你的特性`)
5. 提交 Pull Request

## 许可证

本项目采用 Apache 2.0 许可证 - 详见 [LICENSE](LICENSE) 文件。

## 致谢

- Bloom Filter 概念由 Burton Howard Bloom (1970) 提出
- 哈希函数实现基于公共领域算法
- 内存优化技术参考 Google Guava 库

---
**项目维护者**: Mark Persever  
**联系方式**: [persever07@gmail.com](mailto:persever07@gmail.com)  
**GitHub**: [https://github.com/mark-persever](https://github.com/mark-persever)
