package org.siu.saku.autoconfigure;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author Siu
 * @Date 2020/3/21 14:02
 * @Version 0.0.1
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = SakuProperties.PREFIX)
public class SakuProperties {
    public static final String PREFIX = "saku";
    /**
     * 短链生成类型，默认 hash，可选hash,dbincr(数据库自增)
     */
    private String type = "hash";

}
