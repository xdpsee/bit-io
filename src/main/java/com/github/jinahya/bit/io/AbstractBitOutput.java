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

import com.github.jinahya.bit.io.codec.BitEncoder;
import java.io.IOException;

/**
 * An abstract class partially implementing {@link BitInput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class AbstractBitOutput implements BitOutput, ByteOutput {

    /**
     * Consumes given byte value via {@link #write(int)} while incrementing
     * {@code count}.
     *
     * @param value the byte value
     *
     * @throws IOException if an I/O error occurs.
     */
    protected void octet(final int value) throws IOException {

        write(value & 0xFF);

        count++;
    }

    /**
     * Writes an unsigned value whose size is max {@code 8}.
     *
     * @param size the number of lower bits to write; between {@code 1}
     * (inclusive) and {@code 8} (inclusive).
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     */
    protected void unsigned8(final int size, int value) throws IOException {

        BitIoConstraints.requireValidSize(false, 3, size);

        if (size == 8 && index == 0) {
            octet(value);
            return;
        }

        final int required = size - (8 - index);
        if (required > 0) {
            unsigned8(size - required, value >> required);
            unsigned8(required, value);
            return;
        }

        for (int i = index + size - 1; i >= index; i--) {
            flags[i] = (value & 0x01) == 0x01;
            value >>= 1;
        }
        index += size;

        if (index == 8) {
            int octet = 0x00;
            for (int i = 0; i < 8; i++) {
                octet <<= 1;
                octet |= (flags[i] ? 0x01 : 0x00);
            }
            octet(octet);
            index = 0;
        }
    }

    /**
     * Writes an unsigned value whose size is max {@code 16}.
     *
     * @param size the number of lower bits to write; between {@code 1}
     * (inclusive) and {@code 16} (inclusive).
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs
     */
    protected void unsigned16(final int size, final int value)
            throws IOException {

        BitIoConstraints.requireValidSize(false, 4, size);

        final int quotient = size / 8;
        final int remainder = size % 8;

        if (remainder > 0) {
            unsigned8(remainder, value >> (quotient * 8));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            unsigned8(8, value >> (8 * i));
        }
    }

    @Override
    public void writeBoolean(final boolean value) throws IOException {

        writeInt(true, 1, value ? 1 : 0);
    }

    @Override
    public void writeByte(final boolean unsigned, final int size,
            final byte value)
            throws IOException {

        BitIoConstraints.requireValidSize(unsigned, 3, size);

        writeInt(unsigned, size, value);
    }

    @Deprecated
    @Override
    public void writeUnsignedByte(final int size, final byte value)
            throws IOException {

        writeByte(true, size, value);
    }

    @Deprecated
    @Override
    public void writeByte(final int size, final byte value) throws IOException {

        writeByte(false, size, value);
    }

    @Override
    public void writeShort(final boolean unsigned, final int size,
            final short value)
            throws IOException {

        BitIoConstraints.requireValidSize(unsigned, 4, size);

        writeInt(unsigned, size, value);
    }

    @Deprecated
    @Override
    public void writeUnsignedShort(final int size, final short value)
            throws IOException {

        writeShort(true, size, value);
    }

    @Deprecated
    @Override
    public void writeShort(final int size, final short value)
            throws IOException {

        writeShort(false, size, value);
    }

    @Override
    public void writeInt(final boolean unsigned, final int size,
            final int value)
            throws IOException {

        BitIoConstraints.requireValidSize(unsigned, 5, size);

        if (!unsigned) {
            final int usize = size - 1;
            writeInt(true, 1, value >> usize);
            if (usize > 0) {
                writeInt(true, usize, value);
            }
            return;
        }

        final int quotient = size / 16;
        final int remainder = size % 16;
        if (remainder > 0) {
            unsigned16(remainder, value >> (quotient * 16));
        }
        for (int i = quotient - 1; i >= 0; i--) {
            unsigned16(16, value >> (16 * i));
        }
    }

    @Deprecated
    @Override
    public void writeUnsignedInt(final int size, final int value)
            throws IOException {

        writeInt(true, size, value);
    }

    @Deprecated
    @Override
    public void writeInt(final int size, final int value) throws IOException {

        writeInt(false, size, value);
    }

    @Override
    public void writeLong(final boolean unsigned, final int size,
            final long value)
            throws IOException {

        BitIoConstraints.requireValidSize(unsigned, 6, size);

        if (!unsigned) {
            final int usize = size - 1;
            writeLong(true, 1, value >> usize);
            if (usize > 0) {
                writeLong(true, usize, value);
            }
            return;
        }

        final int quotient = size / 31;
        final int remainder = size % 31;
        if (remainder > 0) {
            writeInt(true, remainder, (int) (value >> (quotient * 31)));
        }
        for (int i = quotient - 1; i >= 0; i--) {
            writeInt(true, 31, (int) (value >> (i * 31)));
        }
    }

    @Deprecated
    @Override
    public void writeUnsignedLong(final int size, final long value)
            throws IOException {

        writeLong(true, size, value);
    }

    @Deprecated
    @Override
    public void writeLong(final int size, final long value) throws IOException {

        writeLong(false, size, value);
    }

    @Override
    public void writeChar(final int size, final char value) throws IOException {

        BitIoConstraints.requireValidSize(true, 4, size);

        writeInt(true, size, value);
    }

    @Override
    public void writeFloat(final float value) throws IOException {

        writeInt(false, 32, Float.floatToRawIntBits(value));
    }

    @Override
    public void writeDouble(final double value) throws IOException {

        writeLong(false, 64, Double.doubleToRawLongBits(value));
    }

    @Override
    public <T extends BitWritable> void writeObject(final T value)
            throws IOException {

        if (value == null) {
            throw new NullPointerException("null value");
        }

        value.write(this);
    }

    @Override
    public <T extends BitWritable> void writeObject(final boolean nullable,
            final T value)
            throws IOException {

        if (!nullable && value == null) {
            throw new NullPointerException("null value");
        }

        if (nullable) {
            writeBoolean(value != null);
        }

        if (!nullable || value != null) {
            //value.write(this);
            AbstractBitOutput.this.writeObject(value);
        }
    }

    @Override
    public <T> void writeObject(final BitEncoder<? super T> encoder,
            final T value)
            throws IOException {

        if (encoder == null) {
            throw new NullPointerException("null encoder");
        }

        encoder.encode(this, value);
    }

    @Override
    public long align(final int bytes) throws IOException {

        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }

        long bits = 0; // number of bits to be padded

        // pad remained bits into current octet
        if (index > 0) {
            bits += (8 - index);
            unsigned8((int) bits, 0x00); // count incremented
        }

        final long remainder = count % bytes;
        long octets = (remainder > 0 ? bytes : 0) - remainder;
        for (; octets > 0; octets--) {
            unsigned8(8, 0x00);
            bits += 8;
        }

        return bits;
    }

    @Override
    public long getCount() {

        return count;
    }

    @Override
    public int getIndex() {

        return index;
    }

    /**
     * bit flags.
     */
    private final boolean[] flags = new boolean[8];

    /**
     * bit index to write.
     */
    private int index = 0;

    /**
     * number of bytes written so far.
     */
    private long count = 0L;

}
