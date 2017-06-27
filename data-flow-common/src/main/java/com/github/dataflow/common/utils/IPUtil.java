package com.github.dataflow.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * @author kevin
 * @date 2017-05-30 1:33 AM.
 */
public class IPUtil {
    private static final String  LOCALHOST        = "127.0.0.1";
    private static final String  ANYHOST          = "0.0.0.0";
    private static final Pattern IP_PATTERN       = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    // 查询本地成本很高的
    private static       String  localHostAddress = null;

    public synchronized static String getLocalIp() {
        if (localHostAddress != null) {
            return localHostAddress;
        }

        try {
            InetAddress localHost = InetAddress.getLocalHost();
            if (isValidAddress(localHost)) {
                localHostAddress = localHost.getHostAddress();
                return localHostAddress;
            }

            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                while (addressEnumeration.hasMoreElements()) {
                    InetAddress inetAddress = addressEnumeration.nextElement();
                    if (isValidAddress(inetAddress)) {
                        localHostAddress = inetAddress.getHostAddress();
                        return localHostAddress;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new UnsupportedOperationException("could not get the localhost address of this server.");
    }

    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null
                && !ANYHOST.equals(name)
                && !LOCALHOST.equals(name)
                && IP_PATTERN.matcher(name).matches());
    }

    public static int valueOf(String ipStr) {
        String[] strArr = ipStr.split("\\.");
        int num = 0;
        num |= Integer.valueOf(strArr[0]) << 24;
        num |= Integer.valueOf(strArr[1]) << 16;
        num |= Integer.valueOf(strArr[2]) << 8;
        num |= Integer.valueOf(strArr[3]) << 0;
        return num;
    }

    public static String toString(int num) {
        StringBuilder sb = new StringBuilder();
        sb.append(num >>> 24 & 0x00FF).append(".");
        sb.append(num >>> 16 & 0x00FF).append(".");
        sb.append(num >>> 8 & 0x00FF).append(".");
        sb.append(num >>> 0 & 0x00FF);
        return sb.toString();
    }

}
