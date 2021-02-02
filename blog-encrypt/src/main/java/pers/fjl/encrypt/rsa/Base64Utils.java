package pers.fjl.encrypt.rsa;


import java.io.*;

public class Base64Utils {
    /** */
    /**
     * 28.     * 文件读取缓冲区大小
     * 29.
     */
    private static final int CACHE_SIZE = 1024;

    /** */
    /**
     * 33.     * <p>
     * 34.     * BASE64字符串解码为二进制数据
     * 35.     * </p>
     * 36.     *
     * 37.     * @param base64
     * 38.     * @return
     * 39.     * @throws Exception
     * 40.
     */
    public static byte[] decode(String base64) throws Exception {
        return mBase64.decode(base64.getBytes());
    }

    /** */
    /**
     * 46.     * <p>
     * 47.     * 二进制数据编码为BASE64字符串
     * 48.     * </p>
     * 49.     *
     * 50.     * @param bytes
     * 51.     * @return
     * 52.     * @throws Exception
     * 53.
     */
    public static String encode(byte[] bytes) throws Exception {
        return new String(mBase64.encode(bytes));
    }

    /** */
    /**
     * 59.     * <p>
     * 60.     * 将文件编码为BASE64字符串
     * 61.     * </p>
     * 62.     * <p>
     * 63.     * 大文件慎用，可能会导致内存溢出
     * 64.     * </p>
     * 65.     *
     * 66.     * @param filePath 文件绝对路径
     * 67.     * @return
     * 68.     * @throws Exception
     * 69.
     */
    public static String encodeFile(String filePath) throws Exception {
        byte[] bytes = fileToByte(filePath);
        return encode(bytes);
    }

    /** */
    /**
     * 76.     * <p>
     * 77.     * BASE64字符串转回文件
     * 78.     * </p>
     * 79.     *
     * 80.     * @param filePath 文件绝对路径
     * 81.     * @param base64 编码字符串
     * 82.     * @throws Exception
     * 83.
     */
    public static void decodeToFile(String filePath, String base64) throws Exception {
        byte[] bytes = decode(base64);
        byteArrayToFile(bytes, filePath);
    }

    /** */
    /**
     * <p>
     * 文件转换为二进制数组
     * </p>
     *
     * @param filePath 文件路径
     * @return
     * @throws Exception
     */
    public static byte[] fileToByte(String filePath) throws Exception {
        byte[] data = new byte[0];
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
            out.close();
            in.close();
            data = out.toByteArray();
        }
        return data;
    }

    /** */
    /**
     * 118.     * <p>
     * 119.     * 二进制数据写文件
     * 120.     * </p>
     * 121.     *
     * 122.     * @param bytes 二进制数据
     * 123.     * @param filePath 文件生成目录
     * 124.
     */
    public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
        InputStream in = new ByteArrayInputStream(bytes);
        File destFile = new File(filePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        byte[] cache = new byte[CACHE_SIZE];
        int nRead = 0;
        while ((nRead = in.read(cache)) != -1) {
            out.write(cache, 0, nRead);
            out.flush();
        }
        out.close();
        in.close();
    }
    //新增-----------------------------------------

    private static char[] base64EncodeChars = new char[]{'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/',};

    private static byte[] base64DecodeChars = new byte[]{-1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
            60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
            -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
            38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
            -1, -1};

    /**
     * 解密
     *
     * @param data
     * @return
     */
    public static byte[] decodeStr(byte[] data) {
//		    byte[] data = str.getBytes();
        int len = data.length;
        ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
        int i = 0;
        int b1, b2, b3, b4;

        while (i < len) {
            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1) {
                break;
            }

            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1) {
                break;
            }
            buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

            do {
                b3 = data[i++];
                if (b3 == 61) {
                    return buf.toByteArray();
                }
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1) {
                break;
            }
            buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

            do {
                b4 = data[i++];
                if (b4 == 61) {
                    return buf.toByteArray();
                }
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1) {
                break;
            }
            buf.write((int) (((b3 & 0x03) << 6) | b4));
        }
        return buf.toByteArray();
    }


}

