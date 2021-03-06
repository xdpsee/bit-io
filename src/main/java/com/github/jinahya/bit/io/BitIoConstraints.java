/*
 *  Copyright 2010 Jin Kwon.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.jinahya.bit.io;

import static java.lang.Math.pow;

/**
 * A class for constraints.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoConstraints {

    static int requireValidSize(final boolean unsigned,
                                final int exponent, final int size) {
        if (exponent <= 0) {
            throw new IllegalArgumentException(
                    "exponent(" + exponent + ") <= 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("size(" + size + ") < 1");
        }
        final int maxSize = (int) pow(2, exponent) - (unsigned ? 1 : 0);
        if (size > maxSize) {
            throw new IllegalArgumentException(
                    "size(" + size + ") > " + maxSize + ";unsigned=" + unsigned
                    + ";exponent=" + exponent);
        }
        return size;
    }

    static int requireValidSizeUnsigned8(final int size) {
        if (size < 1) {
            throw new IllegalArgumentException(
                    "unsigned8.size(" + size + ") < 1");
        }
        if (size > 8) {
            throw new IllegalArgumentException(
                    "unsigned8.size(" + size + ") > 8");
        }
        return size;
    }

    static int requireValidSizeUnsigned16(final int size) {
        if (size < 1) {
            throw new IllegalArgumentException(
                    "unsigned16.size(" + size + ") < 1");
        }
        if (size > 16) {
            throw new IllegalArgumentException(
                    "unsigned16.size(" + size + ") > 16");
        }
        return size;
    }

    static int requireValidSizeByte(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, 3, size);
    }

    static int requireValidSizeShort(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, 4, size);
    }

    static int requireValidSizeInt(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, 5, size);
    }

    static int requireValidSizeLong(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, 6, size);
    }

    static int requireValidSizeChar(final int size) {
        return requireValidSizeUnsigned16(size);
    }

    private BitIoConstraints() {
        super();
    }
}
