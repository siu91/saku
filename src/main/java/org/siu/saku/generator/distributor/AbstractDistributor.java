package org.siu.saku.generator.distributor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.siu.saku.jooq.tables.SakuPreRegister;
import org.siu.saku.model.IdSection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * 派发ID
 * 使用 LongAdder 会冲突，可能逻辑有问题，但是换成AtomicLong 50个线程下并没有冲突
 *
 * @Author Siu
 * @Date 2020/3/21 15:19
 * @Version 0.0.1
 */
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
     * <p>
     * 先取号段id（自增，从1开始自增）
     * start = currentSeq * sectionSize - sectionSize + 1;
     * end = currentSeq * sectionSize;
     *
     * mysql：
     * SELECT AUTO_INCREMENT FROM information_schema.`TABLES` WHERE Table_Schema='数据库名' AND table_name = '对应数据库下表名' LIMIT 1;
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdSection getNextIdSection() {
        IdSection idSection = new IdSection();
        boolean success = true;
        long start = -1, end = -1;
        try {
            // 获取号段id
            Long currentSeq = (Long) dsl.selectFrom("nextval('saku_pre_register_seq'::regclass)").fetch().getValue(0, 0);
            start = currentSeq * sectionSize - sectionSize + 1;
            end = currentSeq * sectionSize;

            // 注册新的ID号段
            dsl.insertInto(SakuPreRegister.SAKU_PRE_REGISTER,
                    SakuPreRegister.SAKU_PRE_REGISTER.ID, SakuPreRegister.SAKU_PRE_REGISTER.START_NO, SakuPreRegister.SAKU_PRE_REGISTER.END_NO, SakuPreRegister.SAKU_PRE_REGISTER.CREATE_TIME)
                    .values(currentSeq, start, end, new Timestamp(System.currentTimeMillis())).execute();

        } catch (Exception e) {
            success = false;
            log.error("CanNotRegisterNewStartIdError");
        }

        return idSection.setStart(start).setEnd(end).setSuccess(success);

    }




}
