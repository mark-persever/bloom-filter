package com.github.mark.bloom.hash;

/**
 * @author mark
 * @version 1.0
 * @since 2025-06-10
 */

public class JenkinsHash implements HashFunction {
    @Override
    public int hash(String key) {
        int hash = 0;
        for (char c : key.toCharArray()) {
            hash += c;
            hash += (hash << 10);
            hash ^= (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);
        return hash & Integer.MAX_VALUE;
    }
}