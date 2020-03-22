package org.siu.saku.util;

import lombok.experimental.UtilityClass;



/**
 * @Author Siu
 * @Date 2020/3/22 17:08
 * @Version 0.0.1
 */
@UtilityClass
public class SakuUtil {

    /**
     * id 转短链串
     *
     * @param id
     * @return
     */
    public static String id2SUrl(long id) {
        return Long.toString(id, Character.MAX_RADIX);
    }


    /**
     * 短链串转id
     *
     * @param sUrl
     * @return
     */
    public static long sUrl2Id(String sUrl) {
        return Long.parseLong(sUrl, Character.MAX_RADIX);
    }



}
