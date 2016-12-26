package com.shineyang.pullword_lib.parser;

import com.shineyang.pullword_lib.expection.SegmentException;
import com.shineyang.pullword_lib.service.CallBack;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by ShineYang on 2016/12/24.
 */

public abstract class AsyncParser<T> implements Parser<T> {

    private Executor mExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void parse(final String text, final CallBack<T> callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.finish(parseSync(text));
                } catch (SegmentException e) {
                    e.printStackTrace();
                    callback.error(e);
                }
            }
        });
    }

    public T parseSync(String text) throws SegmentException {
        throw new SegmentException("Not yet implemented");
    }
}
