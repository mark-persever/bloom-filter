# Bloom Filter Implementation

![Java CI](https://img.shields.io/badge/Java-21%2B-blue)
[![License](https://img.shields.io/badge/License-Apache%202.0-brightgreen.svg)](https://github.com/mark-persever/bloom-filter/blob/master/LICENSE)

A high-performance Bloom filter implementation in Java with customizable hash functions and optimized bit storage.

## Features

- **Optimized bit storage** using efficient `BitSet` implementation
- **Multiple hash functions** including FNV1a, DJB2, and Jenkins
- **Dynamic hash generation** using double hashing technique
- **Configurable parameters** for expected items and false positive rate
- **Memory-efficient** implementation with minimal dependencies
- **Thread-safe** operations (with proper synchronization)

## Installation

### Maven

Add the following repository and dependency to your `pom.xml`:

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

### Gradle

Add to your `build.gradle`:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.mark-persever:bloom-filter:1.0.0'
}
```

## Usage

### Basic Example

```java
import com.github.markpersever.bloom.BloomFilter;
import com.github.markpersever.bloom.DefaultBloomFilter;

public class BloomFilterExample {
    public static void main(String[] args) {
        // Create a Bloom filter with 10000 expected items and 1% false positive rate
        BloomFilter bloomFilter = new DefaultBloomFilter(10000, 0.01);
        
        // Add items to the filter
        bloomFilter.add("apple");
        bloomFilter.add("banana");
        bloomFilter.add("cherry");
        
        // Check for existence
        System.out.println(bloomFilter.mightContain("banana")); // true
        System.out.println(bloomFilter.mightContain("mango"));  // false (probably)
        
        // Get false positive probability
        System.out.println("False positive probability: " + 
                           bloomFilter.expectedFalsePositiveProbability());
        
        // Clear the filter
        bloomFilter.clear();
    }
}
```

### Advanced Usage

```java
// Create a Bloom filter with custom size and hash functions
BloomFilter customFilter = new DefaultBloomFilter(1024, 3);

// Add multiple items
List<String> fruits = Arrays.asList("apple", "orange", "kiwi", "grape");
fruits.forEach(customFilter::add);

// Batch containment check
List<String> testItems = Arrays.asList("apple", "mango", "orange", "peach");
testItems.forEach(item -> 
    System.out.println(item + ": " + customFilter.mightContain(item)));

// Get filter size
System.out.println("Bit size: " + customFilter.bitSize());
```

## Configuration

The `DefaultBloomFilter` constructor accepts two parameters:

```java
public DefaultBloomFilter(int expectedInsertions, double falsePositiveRate)
```

- `expectedInsertions`: Number of items expected to be added to the filter
- `falsePositiveRate`: Desired false positive probability (0.0-1.0)

Alternatively, you can create a filter with explicit parameters:

```java
public DefaultBloomFilter(int bitSize, int hashCount)
```

- `bitSize`: Size of the bit array (larger size = lower false positives)
- `hashCount`: Number of hash functions to use

## Hash Functions

The implementation includes three high-quality hash functions:

1. **FNV1aHash** - Fowler-Noll-Vo hash function
2. **DJB2Hash** - Daniel J. Bernstein hash function
3. **JenkinsHash** - Jenkins one-at-a-time hash

Additional hash functions are generated using double hashing when more than 3 functions are required.

## Performance

The Bloom filter provides O(k) time complexity for `add` and `mightContain` operations, where k is the number of hash functions.

Memory usage is optimized using a compact `BitSet` implementation that stores bits in `long` arrays.

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

## API Documentation

### BloomFilter Interface

```java
public interface BloomFilter {
    // Add an element to the filter
    void add(String key);
    
    // Check if an element might be in the set
    boolean mightContain(String key);
    
    // Get the expected false positive probability
    double expectedFalsePositiveProbability();
    
    // Clear all elements from the filter
    void clear();
    
    // Get the size of the bit array
    long bitSize();
}
```

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a new feature branch
3. Make your changes with appropriate tests
4. Submit a pull request

Ensure all code changes include tests via JUnit and maintain 100% test coverage.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- Bloom filter concept by Burton Howard Bloom (1970)
- Hash function implementations based on public domain algorithms
- Inspired by Guava's BloomFilter implementation
