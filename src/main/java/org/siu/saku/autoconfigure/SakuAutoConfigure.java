package org.siu.saku.autoconfigure;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.siu.saku.generator.AbstractGenerator;
import org.siu.saku.generator.AutoIncrGenerator;
import org.siu.saku.generator.Generator;
import org.siu.saku.generator.HashGenerator;
import org.siu.saku.generator.distributor.Distributor;
import org.siu.saku.generator.distributor.LongAdderDistributor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(SakuProperties.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(prefix = SakuProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class SakuAutoConfigure {
    private final SakuProperties properties;


    @Bean(name = "globalGenerator")
    @ConditionalOnMissingBean
    public AbstractGenerator generator(DSLContext dsl) {
        if ("dbincr".equals(this.properties.getType())) {
            Distributor distributor = new LongAdderDistributor(dsl);
            return new AutoIncrGenerator(distributor);
        }

        return new HashGenerator();
    }


}