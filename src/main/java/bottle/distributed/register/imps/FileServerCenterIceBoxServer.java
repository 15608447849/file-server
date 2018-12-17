package bottle.distributed.register.imps;

import Ice.Object;
import bottle.myice.IceBoxServerAbs;

public class FileServerCenterIceBoxServer extends IceBoxServerAbs {
    @Override
    protected Object specificServices(String[] args) {
        return new FileServerCenterImps();
    }
}
