package org.siu.saku.api;

import lombok.extern.slf4j.Slf4j;


import org.siu.saku.generator.AbstractGenerator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @Author Siu
 * @Date 2020/3/22 16:23
 * @Version 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/v1/saku")
public class SakuApi {

    @Resource(name = "globalGenerator")
    AbstractGenerator generator;

    @GetMapping("/l2s")
    public String saku(String url) {

        return generator.shorten(url).getSurl();
    }

    @GetMapping("/s2l")
    public String saku1(String surl) {
        return generator.getUrl(surl).getUrl();
    }


}
