package org.siu.saku.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Siu
 * @Date 2020/3/22 10:23
 * @Version 0.0.1
 */
@Data
@Accessors(chain = true)
public class IdSection {
    long start = -1;
    long end = -1;
    boolean success;
}
