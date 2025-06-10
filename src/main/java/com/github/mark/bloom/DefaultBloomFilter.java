package com.github.mark.bloom;

import com.github.mark.bloom.hash.DJB2Hash;
import com.github.mark.bloom.hash.FNV1aHash;
import com.github.mark.bloom.hash.HashFunction;
import com.github.mark.bloom.hash.JenkinsHash;
import com.github.mark.bloom.util.BitSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author mark
 * @version 1.0
 * @since 2025-06-10
 */




public class DefaultBloomFilter implements BloomFilter {
    private final BitSet bitSet;
    private final List<HashFunction> hashFunctions;
    private final int hashCount;

    public DefaultBloomFilter(int expectedInsertions, double falsePositiveRate) {
        // 计算最优位数组大小和哈希函数数量
        long numBits = optimalNumOfBits(expectedInsertions, falsePositiveRate);
        this.hashCount = optimalNumOfHashFunctions(expectedInsertions, numBits);
        this.bitSet = new BitSet((int) numBits);
        this.hashFunctions = createHashFunctions(hashCount);
    }

    public DefaultBloomFilter(int bitSize, int hashCount) {
        this.bitSet = new BitSet(bitSize);
        this.hashCount = hashCount;
        this.hashFunctions = createHashFunctions(hashCount);
    }

    private List<HashFunction> createHashFunctions(int count) {
        List<HashFunction> functions = new ArrayList<>();
        functions.add(new FNV1aHash());
        functions.add(new DJB2Hash());
        functions.add(new JenkinsHash());

        // 使用双重哈希生成更多哈希函数
        for (int i = 3; i < count; i++) {
            final int seed = i;
            functions.add(key -> {
                int h1 = functions.get(0).hash(key);
                int h2 = functions.get(1).hash(key);
                return Math.abs(h1 + seed * h2);
            });
        }
        return Collections.unmodifiableList(functions);
    }

    @Override
    public void add(String key) {
        for (HashFunction f : hashFunctions) {
            int hash = f.hash(key);
            bitSet.set(hash % bitSet.size());
        }
    }

    @Override
    public boolean mightContain(String key) {
        for (HashFunction f : hashFunctions) {
            int hash = f.hash(key);
            if (!bitSet.get(hash % bitSet.size())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double expectedFalsePositiveProbability() {
        return Math.pow(1 - Math.exp(-hashCount * (double) bitCount() / bitSet.size()), hashCount);
    }

    @Override
    public void clear() {
        bitSet.clear();
    }

    @Override
    public long bitSize() {
        return bitSet.size();
    }

    private long bitCount() {
        // 实际实现需要统计置位数量
        return 0; // 简化示例
    }

    // 以下为布隆过滤器参数计算公式
    static long optimalNumOfBits(long n, double p) {
        if (p == 0) p = Double.MIN_VALUE;
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    static int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
}