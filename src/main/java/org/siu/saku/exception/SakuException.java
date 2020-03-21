package org.siu.saku.exception;

/**
 * 异常
 *
 * @Author Siu
 * @Date 2020/3/19 14:48
 * @Version 0.0.1
 */
public class SakuException extends Exception {

    public SakuException(String message) {
        super("Saku Exception[" + message + "]");
    }
}
