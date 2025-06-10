package com.github.mark.bloom.hash;

/**
 * @author mark
 * @version 1.0
 * @since 2025-06-10
 */

public class DJB2Hash implements HashFunction {
    @Override
    public int hash(String key) {
        int hash = 5381;
        for (char c : key.toCharArray()) {
            hash = ((hash << 5) + hash) + c;
        }
        return hash & Integer.MAX_VALUE; // 确保非负
    }
}