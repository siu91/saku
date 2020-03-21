package org.siu.saku.exception;

/**
 * 未找到主数据源异常
 *
 * @Author Siu
 * @Date 2020/3/19 14:51
 * @Version 0.0.1
 */
public class CanNotRegisterNewStartIdError extends SakuException {

    public CanNotRegisterNewStartIdError() {
        super(CanNotRegisterNewStartIdError.class.getSimpleName().toUpperCase());
    }

    public CanNotRegisterNewStartIdError(String msg) {
        super(CanNotRegisterNewStartIdError.class.getSimpleName().toUpperCase() + "-[" + msg + "]");
    }
}
