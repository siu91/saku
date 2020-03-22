package org.siu.saku.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;


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
        //  return Long.toString(id, Character.MAX_RADIX);
        return encode(id);
    }


    /**
     * 短链串转id
     *
     * @param sUrl
     * @return
     */
    public static long sUrl2Id(String sUrl) {
        // return Long.parseLong(sUrl, Character.MAX_RADIX);
        return decode(sUrl);
    }


    private static char[] rDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    /**
     * 进制
     */
    private static final int RADIX = 62;

    /**
     * 10进制转64进制
     *
     * @param value
     * @return
     */
    public static String encode(long value) {
        int digitIndex = 0;
        long longPositive = Math.abs(value);
        int radix = RADIX;
        char[] outDigits = new char[65];
        for (digitIndex = 0; digitIndex <= 64; digitIndex++) {
            if (longPositive == 0) {
                break;
            }
            outDigits[outDigits.length - digitIndex - 1] = rDigits[(int) (longPositive % radix)];
            longPositive /= radix;
        }
        return new String(outDigits, outDigits.length - digitIndex, digitIndex);
    }


    /**
     * 64 进制转10进制
     *
     * @param value
     * @return
     */
    public static long decode(String value) {
        int fromBase = RADIX;

        if (!StringUtils.hasText(value)) {
            return 0L;
        }
        String sDigits = new String(rDigits, 0, fromBase);
        long result = 0;
        char[] values = value.toCharArray();
        for (int i = 0; i < values.length; i++) {
            char tmp = values[i];

            if (!sDigits.contains(new String(new char[]{tmp}))) {
                //
            } else {
                try {
                    int index = 0;
                    for (int xx = 0; xx < rDigits.length; xx++) {
                        if (rDigits[xx] == values[value.length() - i - 1]) {
                            index = xx;
                        }
                    }
                    result += (long) Math.pow(fromBase, i) * index;
                } catch (Exception e) {
                    //
                }
            }
        }
        return result;
    }


    public static void main(String[] args) {
        long test2 = MurmurHash.hash("details/43214");
        String t3 = SakuUtil.encode(test2);
        double t4 = SakuUtil.decode(t3);
        if (test2 == t4) {
            System.out.println("------");
        }
        System.out.println("---");

        long tttt3 = SakuUtil.decode("3hcCxy");
        System.out.println("---");
    }


}
