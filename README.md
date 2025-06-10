# Bloom Filter Implementation

[![Maven Central](https://img.shields.io/github/v/release/mark-persever/bloom-filter?color=blue&label=Release)](https://github.com/mark-persever/bloom-filter/packages)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


A high-performance, memory-efficient Bloom filter implementation in Java with configurable false-positive rates and automatic parameter optimization.

## Features

- 🚀 **High performance**: Optimized hash functions and bit operations
- 🧠 **Memory efficient**: Compact bit storage using `long[]` arrays
- ⚙️ **Auto-optimized**: Calculates optimal parameters based on expected elements and false-positive rate
- 🔄 **Multiple hash strategies**: Includes FNV-1a, DJB2, and Jenkins hash algorithms
- 🧪 **Test coverage**: Comprehensive unit tests with edge cases
- 📦 **Easy integration**: Simple Maven dependency

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.github.mark-persever</groupId>
    <artifactId>bloom-filter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Repository Configuration

Add GitHub Packages repository to your `pom.xml` or `settings.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub mark-persever Apache Maven Packages</name>
        <url>https://maven.pkg.github.com/mark-persever/bloom-filter</url>
    </repository>
</repositories>
```

## Usage

### Basic Usage

```java
import com.github.markpersever.bloom.BloomFilter;
import com.github.markpersever.bloom.DefaultBloomFilter;

public class Example {
    public static void main(String[] args) {
        // Create filter for 100,000 elements with 0.1% false positive rate
        BloomFilter filter = new DefaultBloomFilter(100_000, 0.001);
        
        // Add elements
        filter.add("https://example.com");
        filter.add("user@domain.com");
        filter.add("7b8a1d9c-3f6e-4a5b-9d2c-1e0f8a7b6c5d");
        
        // Check existence
        System.out.println("Contains 'example.com': " + 
            filter.mightContain("https://example.com"));  // true
        
        System.out.println("Contains 'unknown': " + 
            filter.mightContain("unknown-value"));        // false
        
        // Get expected false positive probability
        System.out.printf("False positive probability: %.6f%n",
            filter.expectedFalsePositiveProbability());
    }
}
```

### Advanced Configuration

```java
// Create with custom bit size and hash functions count
BloomFilter customFilter = new DefaultBloomFilter(10_000_000, 5);

// Add bulk elements
List<String> items = /* large dataset */;
items.forEach(filter::add);

// Clear the filter
filter.clear();

// Get actual bit usage
System.out.println("Bits used: " + filter.bitSize());
```

## Performance Metrics

| Element Count | False Positive Rate | Memory Usage | Hash Functions |
|---------------|---------------------|--------------|----------------|
| 100,000       | 0.01 (1%)           | 114 KB       | 7              |
| 1,000,000     | 0.001 (0.1%)        | 1.67 MB      | 10             |
| 10,000,000    | 0.0001 (0.01%)      | 19.5 MB      | 14             |

## Use Cases

- **URL checker**: Quickly check if URL has been processed
- **Email validator**: Prevent duplicate email processing
- **Security systems**: Fast membership checks for blacklisted tokens
- **Big data processing**: Efficient distinct value approximation
- **Network routers**: Packet filtering and deduplication

## API Documentation

### BloomFilter Interface

```java
public interface BloomFilter {
    /**
     * Add an element to the filter
     * @param key Element to add
     */
    void add(String key);
    
    /**
     * Check if element might be in the filter
     * @param key Element to check
     * @return true if element might exist (with false positive probability),
     *         false if element definitely doesn't exist
     */
    boolean mightContain(String key);
    
    /**
     * Calculate expected false positive probability
     * @return Current expected false positive rate
     */
    double expectedFalsePositiveProbability();
    
    /**
     * Clear all elements from the filter
     */
    void clear();
    
    /**
     * Get total bit size of the filter
     * @return Number of bits in the bit array
     */
    long bitSize();
}
```

## Building from Source

1. Clone the repository:
```bash
git clone https://github.com/mark-persever/bloom-filter.git
cd bloom-filter
```

2. Build with Maven:
```bash
mvn clean package
```

3. Run tests:
```bash
mvn test
```

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a pull request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Bloom filter concept by Burton Howard Bloom (1970)
- Hash function implementations based on public domain algorithms
- Memory optimization techniques from Google Guava library

---
**Project Maintainer**: Mark Persever  
**Contact**: [persever07@gmail.com](mailto:persever07@gmail.com)  
**GitHub**: [https://github.com/mark-persever](https://github.com/mark-persever)
