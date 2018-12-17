package bottle.distributed.register.imps;

import Ice.Current;
import bottle.distributed.register.reg.FSAddressConfig;
import bottle.distributed.register.reg.FSAddressInfo;
import bottle.distributed.register.reg._IFileServerCenterDisp;

import java.util.ArrayList;
import java.util.List;

/**
 * ice 文件服务器 注册中心 实现
 *
 */
public class FileServerCenterImps extends _IFileServerCenterDisp {

    private final static FileServerCenterProperties properties = new FileServerCenterProperties();

    @Override
    public FSAddressInfo queryFileServerAddress(Current __current) {
        for (FSAddressConfig config : properties.serverList){
            //http访问
            String text = new HttpResult().accessUrl("http://"+config.address+properties.pathPrev+"/online").getRespondContent();
            if (text!=null && text.equals("online")){
                return new FSAddressInfo(true,"http://"+config.address+properties.pathPrev+"/upload",
                        "http://"+config.address);
            }
        }
        return null;
    }

    @Override
    public boolean dynamicRegistrationFileServerInfo(FSAddressConfig[] list, boolean isCovered, Current __current) {
        //暂未实现
        return false;
    }

    @Override
    public boolean dynamicRemoveFileServerInfo(FSAddressConfig[] list, Current __current) {
        //暂未实现
        return false;
    }

}
