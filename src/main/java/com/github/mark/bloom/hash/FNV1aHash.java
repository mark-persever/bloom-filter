package com.github.mark.bloom.hash;

/**
 * @author mark
 * @version 1.0
 * @since 2025-06-10
 */

public class FNV1aHash implements HashFunction {
    private static final int FNV_PRIME = 16777619;
    private static final int FNV_OFFSET = 0x811C9DC5;

    @Override
    public int hash(String key) {
        int hash = FNV_OFFSET;
        for (char c : key.toCharArray()) {
            hash ^= c;
            hash *= FNV_PRIME;
        }
        return hash & Integer.MAX_VALUE;
    }
}