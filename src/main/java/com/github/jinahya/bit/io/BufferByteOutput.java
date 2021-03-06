/*
 * Copyright 2013 Jin Kwon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jinahya.bit.io;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A {@link ByteOutput} uses an instance of {@link ByteBuffer} as its
 * {@link #target}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteInput
 */
public class BufferByteOutput extends AbstractByteOutput<ByteBuffer> {

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance with given {@code ByteBufer}.
     *
     * @param buffer the {@code ByteBuffer} to which bytes are written;
     * {@code null} if it's supposed to be lazily initialized and set.
     */
    public BufferByteOutput(final ByteBuffer buffer) {
        super(buffer);
    }

    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc} The {@code write(int)} method of {@code BufferByteOutput}
     * class invokes {@link ByteBuffer#put(byte)} on {@link #target} with given
     * {@code value}. Override this method if the {@link #target} is supposed to
     * be lazily initialized or adjusted.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see #target
     * @see ByteBuffer#put(byte)
     */
    @Override
    public void write(final int value) throws IOException {
        target.put((byte) value);
    }

    // ------------------------------------------------------------------ target
    @Override
    public BufferByteOutput target(final ByteBuffer target) {
        return (BufferByteOutput) super.target(target);
    }
}
