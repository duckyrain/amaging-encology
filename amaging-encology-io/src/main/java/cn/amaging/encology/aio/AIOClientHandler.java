package cn.amaging.encology.aio;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

/**
 * Created by DuQiyu on 2018/11/19 18:04.
 */
public class AIOClientHandler implements CompletionHandler<Void, ByteBuffer> {

    @Override
    public void completed(Void result, ByteBuffer attachment) {

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
