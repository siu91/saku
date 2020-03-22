package org.siu.saku;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siu.saku.generator.AutoIncrGenerator;
import org.siu.saku.generator.HashGenerator;
import org.siu.saku.generator.distributor.AtomicLongDistributor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author Siu
 * @Date 2020/2/18 9:58
 * @Version 0.0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributorTests {

    @Autowired
    AtomicLongDistributor distributor;
    //LongAdderDistributor distributor;

    @Autowired
    AutoIncrGenerator generator;

    @Autowired
    HashGenerator generator1;

    @Test
    public void testhash() {
        generator1.shorten("https://blog.csdn.net/34214321/article/details/43214");

    }

    @Test
    public void testhash1() {
        generator1.getUrl("829mq9zt1pyy");

    }

    @Test
    public void testhash3() throws InterruptedException {

        Map<Long, String> map = new ConcurrentHashMap<>();

        for (int i = 0; i < 50; i++) {
            int finalI = i;
            new Thread() {
                @SneakyThrows
                @Override
                public void run() {
                    System.out.println("thread-" + finalI);
                    for (int i = 0; i < 1000000; i++) {
                        generator1.shorten(UUID.randomUUID().toString());
                    }

                }
            }.start();


        }
        Thread.sleep(2 * 60 * 1000);
    }

    @Test
    public void test0() {
        generator.getUrl("8gv8iw");

    }

    @Test
    public void test() {
        for (int i = 0; i < 1000000; i++) {
            generator.shorten(UUID.randomUUID().toString());
        }

    }

    @Test
    public void test2() throws InterruptedException {

        Map<Long, String> map = new ConcurrentHashMap<>();

        for (int i = 0; i < 50; i++) {
            int finalI = i;
            new Thread() {
                @SneakyThrows
                @Override
                public void run() {
                        System.out.println("thread-" + finalI);
                    for (int i = 0; i < 1000000; i++) {
                        generator.shorten(UUID.randomUUID().toString());
                    }

                }
            }.start();


        }
        Thread.sleep(2 * 60 * 1000);
    }


    @Test
    public void test3() throws InterruptedException {

        Map<Long, String> map = new ConcurrentHashMap<>();

        for (int i = 0; i < 50; i++) {
            int finalI = i;
            new Thread() {
                @SneakyThrows
                @Override
                public void run() {
                    //     System.out.println("thread-" + finalI);
                    for (int i = 0; i < 1000000; i++) {
                        long id = distributor.next();
                        System.out.println("thread-" + finalI + ":" + id);
                        if (map.containsKey(id)) {
                            System.out.println("thread-" + finalI + ":hit-" + id);
                        }
                        map.put(id, "");
                    }

                }
            }.start();


        }
        Thread.sleep(2 * 60 * 1000);
    }


}
