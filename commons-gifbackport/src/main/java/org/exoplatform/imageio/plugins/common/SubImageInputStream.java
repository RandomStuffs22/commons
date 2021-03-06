/*
 * Copyright 2000-2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package org.exoplatform.imageio.plugins.common;

import java.io.IOException;
import javax.imageio.stream.ImageInputStreamImpl;
import javax.imageio.stream.ImageInputStream;

/**
 * version: openjdk-7-ea-src-b35-11_sep_2008
 */
public final class SubImageInputStream extends ImageInputStreamImpl {

    ImageInputStream stream;
    long startingPos;
    int startingLength;
    int length;

    public SubImageInputStream(ImageInputStream stream, int length)
        throws IOException {
        this.stream = stream;
        this.startingPos = stream.getStreamPosition();
        this.startingLength = this.length = length;
    }

    public int read() throws IOException {
        if (length == 0) { // Local EOF
            return -1;
        } else {
            --length;
            return stream.read();
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (length == 0) { // Local EOF
            return -1;
        }

        len = Math.min(len, length);
        int bytes = stream.read(b, off, len);
        length -= bytes;
        return bytes;
    }

    public long length() {
        return startingLength;
    }

    public void seek(long pos) throws IOException {
        stream.seek(pos - startingPos);
        streamPos = pos;
    }

    protected void finalize() throws Throwable {
        // Empty finalizer (for improved performance; no need to call
        // super.finalize() in this case)
    }
}
