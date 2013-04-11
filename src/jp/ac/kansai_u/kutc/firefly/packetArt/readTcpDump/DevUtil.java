package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

public class DevUtil {

    private static StringBuilder errBuf;//libpcapからのエラーをここに
    private List<PcapIf> allDevs;
    private ArrayList<DeviceInfo> allDevInfo;
    private boolean gotError;

    public ArrayList getAllDevInfo(){return allDevInfo;}
    public boolean hasError(){return gotError;}
    public StringBuilder getErrBuf(){return errBuf;}

    public DevUtil(StringBuilder errorbuffer) {
        errBuf = errorbuffer;
        allDevs = new ArrayList<PcapIf>();
        allDevInfo = new ArrayList<DeviceInfo>();
        if ( Pcap.findAllDevs(allDevs, errBuf) == Pcap.NOT_OK
            || allDevs.isEmpty() ) {
            System.err.printf("Couldn't read list of devices, error is %s",
             errBuf.toString());
            gotError = true;
        } else {
            for (PcapIf pcapIf : allDevs) {
                allDevInfo.add(new DeviceInfo(pcapIf));
            }
        }
    }

    /**
     * デバイスのIPからデバイス名を取得します。
     * エラーは出しません。
     * @param ip デバイスの持つIPアドレス。
     * @return name デバイス名。該当無しならnull。
    */
    public String getDevNameByIP(String ip) {
        for (DeviceInfo devInfo : allDevInfo) {
            if (ip == devInfo.ipAddr || devInfo.ip6Addr.contains(ip)) {
                return devInfo.name;
            }
        }
        return null;
    }



    public static void main(String[] args) {
        StringBuilder a = new StringBuilder();
        ArrayList<DeviceInfo> ar = (new DevUtil(a)).getAllDevInfo();
        
        for (DeviceInfo dev : ar ) {
            System.out.println(dev.name);
            System.out.println(dev.description);
            System.out.println(dev.macAddr);
            System.out.println(dev.ipAddr);
            System.out.println(dev.ip6Addr);
            System.out.println(dev.loopback);
            System.out.println("----------");
            System.out.println(a);
            System.out.println("----------");
            
        }
    }
}
