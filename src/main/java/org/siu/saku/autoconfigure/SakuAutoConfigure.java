package org.siu.saku.autoconfigure;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.siu.saku.generator.distributor.Distributor;
import org.siu.saku.generator.distributor.LongAdderDistributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Siu
 * @Date 2020/3/21 14:00
 * @Version 0.0.1
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class SakuAutoConfigure {


    @Bean
    @ConditionalOnMissingBean
    public Distributor distributor() {
        return new LongAdderDistributor();
    }

}
