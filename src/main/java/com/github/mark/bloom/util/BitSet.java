package com.github.mark.bloom.util;

import java.util.Arrays;

/**
 * @author mark
 * @version 1.0
 * @since 2025-06-10
 */

public class BitSet {
    private final long[] bits;
    private final int size;

    public BitSet(int size) {
        this.size = size;
        this.bits = new long[(size + 63) >>> 6];
    }

    public void set(int index) {
        bits[index >>> 6] |= (1L << (index & 63));
    }

    public boolean get(int index) {
        return (bits[index >>> 6] & (1L << (index & 63))) != 0;
    }

    public void clear() {
        Arrays.fill(bits, 0);
    }

    public int size() {
        return size;
    }
}