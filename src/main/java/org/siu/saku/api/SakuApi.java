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

    @GetMapping("/s/{url}")
    public String saku(@PathVariable String url) {

        return generator.shorten(url).getSurl();
    }

    @GetMapping("/l/{surl}")
    public String saku1(@PathVariable String surl) {
        return generator.getUrl(surl).getUrl();
    }


}
