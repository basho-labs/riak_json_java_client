package com.basho.riak.json.transports.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractRestClient implements RestClient {
	protected final void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[(1 << 10) * 8];
        int count = 0;
        do {
            out.write(buffer, 0, count);
            out.flush();
            count = in.read(buffer, 0, buffer.length);
        }
        while (count != -1);
	}
}
