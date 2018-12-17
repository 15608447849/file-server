package launch;

import bottle.distributed.register.reg.FSAddressInfo;
import bottle.distributed.register.reg.IFileServerCenterPrx;
import leeping.IceServerAbs;

public class ClientFileServerRegisterServer extends IceServerAbs<IFileServerCenterPrx> {
    public void test() throws Exception{
        FSAddressInfo info = getProxy().queryFileServerAddress();
        System.out.println("info - "+ info.isValid +" "+ info.uploadHttpUrl+" "+info.downloadHttpUrlPrefix);

    }
}
