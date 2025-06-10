package com.github.mark.bloom;

/**
 * @author mark
 * @version 1.0
 * @since 2025-06-10
 */



public interface BloomFilter {
    void add(String key);
    boolean mightContain(String key);
    double expectedFalsePositiveProbability();
    void clear();
    long bitSize();
}