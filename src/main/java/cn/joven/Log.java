package cn.joven;

/**
 * @ClassName Log
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/3/2020 5:52 PM
 * @Version 1.0
 **/
public class Log {
    public Log() {
    }

    public static void info(String msg, Object... param) {
        if (param != null) {
            Object[] var2 = param;
            int var3 = param.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object o = var2[var4];
                String parm = (String)o;
                msg = msg.replaceFirst("\\{}", parm);
            }

            System.out.println("[MMGOHOME] " + msg);
        } else {
            System.out.println("[MMGOHOME] " + msg);
        }

    }

    public static void error(String msg, Object... param) {
        if (param != null) {
            Object[] var2 = param;
            int var3 = param.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object o = var2[var4];
                String parm = (String)o;
                msg = msg.replaceFirst("\\{}", parm);
            }

            System.err.println("[MMGOHOME-ERROR] " + msg);
        } else {
            System.err.println("[MMGOHOME-ERROR " + msg);
        }

    }
}
