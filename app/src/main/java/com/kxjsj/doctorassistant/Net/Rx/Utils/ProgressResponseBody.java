package com.kxjsj.doctorassistant.Net.Rx.Utils;

import com.kxjsj.doctorassistant.Net.Rx.BaseObserver;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by vange on 2017/9/13.
 */

public class ProgressResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private BufferedSource bufferedSource;
    private BaseObserver<ResponseBody> downObserve;

    public ProgressResponseBody(ResponseBody responseBody, BaseObserver<ResponseBody> downObserve) {
        this.responseBody = responseBody;
        this.downObserve = downObserve;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long bytesReaded = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead; //实时发送当前已读取的字节和总字节
                downObserve.onProgress(null,bytesRead, contentLength());
                return bytesRead;
            }
        };
    }
}

