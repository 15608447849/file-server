package bottle.distributed.register.imps;

import Ice.Logger;
import bottle.distributed.register.reg.FSAddressConfig;
import properties.abs.ApplicationPropertiesBase;
import properties.annotations.PropertiesFilePath;
import properties.annotations.PropertiesName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@PropertiesFilePath("/fileServerRegister.properties")
public class FileServerCenterProperties extends ApplicationPropertiesBase {

    public static ArrayList<FSAddressConfig> serverList = new ArrayList<>();

    public static void sortAddress(){
        serverList.sort(Comparator.comparingInt(o -> o.priority));
    }

    @PropertiesName("file.server.address.list")
    private String listStr;

    @Override
    protected void initialization() {
        String[] infos = listStr.split(";");
        for (String info : infos){
            String[] temp = info.split("#");
            FSAddressConfig config = new FSAddressConfig();
            config.address = temp[0];
            config.priority = Integer.parseInt(temp[1]);
            serverList.add(config);
        }
        sortAddress();
        System.out.println("\n\n已注册的文件服务器地址:" + listStr+"\n\n");
    }

}
