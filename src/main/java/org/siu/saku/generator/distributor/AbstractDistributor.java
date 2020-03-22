package org.siu.saku.generator.distributor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.siu.saku.jooq.tables.SakuPreRegister;
import org.siu.saku.model.IdSection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * 派发ID
 * 使用 LongAdder 会冲突，可能逻辑有问题，但是换成AtomicLong 50个线程下并没有冲突
 *
 * @Author Siu
 * @Date 2020/3/21 15:19
 * @Version 0.0.1
 */
@Component
@Slf4j
public abstract class AbstractDistributor implements Distributor {

    /**
     * 注册时申请每一段ID的大小，默认1000
     */
    @Setter
    protected int sectionSize = 1000;

    protected final DSLContext dsl;


    public AbstractDistributor(DSLContext dsl) {
        this.dsl = dsl;
    }

    /**
     * 派发ID
     *
     * @return
     */
    @Override
    public long next() {
        return getNext();
    }


    /**
     * 由子类实现ID 派发
     *
     * @return
     */
    public abstract long getNext();

    /**
     * 注册新的ID号段
     */
    public abstract void register();


    /**
     * 获取下一个号段
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdSection getNextIdSection() {
        IdSection idSection = new IdSection();
        boolean success = true;
        long nextStart = -1;
        try {
            // 获取当前最大的ID
            Object currentMax = dsl.select(SakuPreRegister.SAKU_PRE_REGISTER.END_NO.max()).from(SakuPreRegister.SAKU_PRE_REGISTER).fetch().getValue(0, 0);
            nextStart = currentMax == null ? 0 : (Long) currentMax;
            // 注册新的ID号段
            dsl.insertInto(SakuPreRegister.SAKU_PRE_REGISTER,
                    SakuPreRegister.SAKU_PRE_REGISTER.START_NO, SakuPreRegister.SAKU_PRE_REGISTER.END_NO, SakuPreRegister.SAKU_PRE_REGISTER.CREATE_TIME)
                    .values(nextStart, nextStart + this.sectionSize, new Timestamp(System.currentTimeMillis())).execute();
        } catch (Exception e) {
            success = false;
            log.error("CanNotRegisterNewStartIdError");
        }

        return idSection.setStart(nextStart).setEnd(nextStart + sectionSize).setSuccess(success);

    }


}
