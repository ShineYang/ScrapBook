package com.shineyang.pullword_lib.expection;

/**
 * Created by ShineYang on 2016/12/24.
 */

public class SegmentException extends Exception {

    public SegmentException(String message) {
        super(message);
    }

    public SegmentException(Throwable cause) {
        super(cause);
    }
}
