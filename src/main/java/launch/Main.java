package launch;

import Ice.Communicator;
import Ice.Identity;
import Ice.Object;
import Ice.ObjectAdapter;
import bottle.distributed.register.imps.FileServerCenterImps;

import leeping.IceIo;


import java.nio.file.Paths;

public class Main {
//    public static void main(String[] args) {
//        startServer(args);
//        startServerByGrid(args);

//    }

    private static void startServerByGrid(String[] args) {
        try {
            IceIo.get().init("WLQ","192.168.1.240",7061);
            ClientFileServerRegisterServer server = new ClientFileServerRegisterServer();
            server.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startServer(String[] args) {
        int status = 0;
        Communicator communicator = null;
        try{
            communicator = Ice.Util.initialize();
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("FileServerRegisterAdapter","default -p 8888");
            Object servant = new FileServerCenterImps();
            Identity identity = Ice.Util.stringToIdentity("FileServerRegister");
            adapter.add(servant,identity);
            adapter.activate();
            communicator.waitForShutdown();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (communicator!=null) communicator.destroy();
        }
        System.exit(status);
    }
}
