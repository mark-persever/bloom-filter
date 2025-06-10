package com.github.mark.bloom.hash;

/**
 * @author mark
 * @version 1.0
 * @since 2025-06-10
 */


@FunctionalInterface
public interface HashFunction {
    int hash(String key);
}