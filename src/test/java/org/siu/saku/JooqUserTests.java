package org.siu.saku;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siu.saku.generator.preregister.Distributor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author Siu
 * @Date 2020/2/18 9:58
 * @Version 0.0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JooqUserTests {

    @Autowired
    Distributor distributor;

    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            System.out.println(distributor.distributor());
        }

    }

}
