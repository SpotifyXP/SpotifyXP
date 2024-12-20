/*
	https://github.com/BlackOverlord666/mslinks
	
	Copyright (c) 2015 Dmitrii Shamrikov

	Licensed under the WTFPL
	You may obtain a copy of the License at
 
	http://www.wtfpl.net/about/
 
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package com.spotifyxp.deps.mslinks.data;

import com.spotifyxp.deps.io.ByteReader;
import com.spotifyxp.deps.io.ByteWriter;
import com.spotifyxp.deps.mslinks.Serializable;

import java.io.IOException;

public class BitSet32 implements Serializable {
    private int d;

    public BitSet32(int n) {
        d = n;
    }

    public BitSet32(ByteReader data) throws IOException {
        d = (int) data.read4bytes();
    }

    protected boolean get(int i) {
        return (d & (1 << i)) != 0;
    }

    protected void set(int i) {
        d = (d & ~(1 << i)) | (1 << i);
    }

    protected void clear(int i) {
        d = d & ~(1 << i);
    }

    public void serialize(ByteWriter bw) throws IOException {
        bw.write4bytes(d);
    }
}
