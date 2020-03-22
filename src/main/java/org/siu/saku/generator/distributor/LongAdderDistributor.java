package org.siu.saku.generator.distributor;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.siu.saku.model.IdSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 派发ID
 * 使用 LongAdder 会冲突，可能逻辑有问题，但是换成AtomicLong 50个线程下并没有冲突
 *
 * @Author Siu
 * @Date 2020/3/21 15:19
 * @Version 0.0.1
 */
@Component(value = "longAdderDistributor")
@Slf4j
public class LongAdderDistributor extends AbstractDistributor {

    /**
     * 自增ID派发器
     */
    private final LongAdder adder = new LongAdder();
    private final AtomicLong currentEnd = new AtomicLong(0);

    public LongAdderDistributor() {
        super();
    }


    @Override
    public long getNext() {
        if (this.adder.sum() >= this.currentEnd.get()) {
            register();
        }
        this.adder.increment();
        return this.adder.sum();
    }


    /**
     * 往数据库中注册一个号段
     * 并设置给 distributor
     */
    @Override
    public void register() {
        synchronized (this.adder) {
            IdSection idSection = getNextIdSection();
            if (idSection.isSuccess()) {
                this.adder.add(idSection.getStart() - this.currentEnd.get());
                this.currentEnd.set(idSection.getEnd() - 1);
                log.info("注册ID号段[{}-{}]", idSection.getStart(), idSection.getEnd());
            }
        }
    }


    @Override
    public long current() {
        return this.adder.sum();
    }
}
