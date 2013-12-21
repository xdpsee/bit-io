/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
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


package com.github.jinahya.io.bit;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;


/**
 * A {@link ByteInput} implementation for {@link ReadableByteChannel}s.
 */
public class ChannelInput extends ByteInput<ReadableByteChannel> {


    /**
     * Creates a new instance on top of specified channel.
     *
     * @param source the underlying source channel.
     * @param buffer the buffer to buffering input.
     */
    public ChannelInput(final ReadableByteChannel source,
                        final ByteBuffer buffer) {

        super(source);

        this.buffer = buffer;
    }


    /**
     * Creates a new instance on top of specified channel.
     *
     * @param channel the underlying source channel.
     */
    public ChannelInput(final ReadableByteChannel channel) {
        this(channel, ByteBuffer.allocate(1));
    }


    /**
     * {@inheritDoc}
     * <p/>
     * The {@code readUnsignedByte()} method of {@code ChannelInput} class first
     * tries to replenish {@link #buffer}, if it is drained, from
     * {@link #source} calls {@link ByteBuffer#get()} and returns the result.
     *
     * @return {@inheritDoc }
     *
     * @throws IllegalStateException if {@link #buffer} is currently
     * {@code null} or has zero capacity.
     * @throws IllegalStateException if {@link #source} is currently
     * {@code null}.
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int readUnsignedByte() throws IOException {
        if (buffer == null) {
            throw new IllegalStateException("null buffer");
        }
        if (buffer.capacity() == 0) {
            throw new IllegalStateException("buffer.capacity == 0");
        }
        if (source == null) {
            throw new IllegalStateException("null source");
        }
        if (!buffer.hasRemaining()) {
            buffer.clear(); // position -> zero, limit -> capacity
            while (buffer.position() == 0) {
                if (source.read(buffer) == -1) {
                    return -1;
                }
            }
            assert buffer.position() > 0;
            buffer.flip(); // limit -> position, position -> zero
        }
        return buffer.get();
    }


    /**
     * {@inheritDoc }
     * <p/>
     * The {@code close()} method of {@code ChannelInput} class calls
     * {@link java.nio.channels.Channel#close()} on {@link #source}.
     *
     * @throws IOException {@inheritDoc}
     *
     * @see Channel#close()
     */
    @Override
    public void close() throws IOException {
        if (source != null) {
            source.close();
        }
    }


    public ByteBuffer getBuffer() {
        return buffer;
    }


    public void setBuffer(final ByteBuffer buffer) {
        this.buffer = buffer;
    }


    protected ByteBuffer buffer;


}

