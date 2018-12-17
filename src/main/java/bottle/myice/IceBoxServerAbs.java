package bottle.myice;

import Ice.Communicator;
import Ice.Logger;
import Ice.Object;
import Ice.ObjectAdapter;
import IceBox.Service;

import java.util.Arrays;

public abstract class IceBoxServerAbs implements Service {

    //服务名
    private String _serverName;
    private ObjectAdapter _adapter;
    private Logger logger;

    @Override
    public void start(String name, Communicator communicator, String[] args) {
        logger = communicator.getLogger();
        logger.print("准备启动服务:" + name +" 参数集:"+ Arrays.toString(args));
        //创建objectAdapter 和service同名
        _serverName = name;
        _adapter = communicator.createObjectAdapter(_serverName);
        //创建servant并激活
        Ice.Object object = specificServices(args);
        _adapter.add(object,communicator.stringToIdentity(_serverName));
        _adapter.activate();
        logger.print("成功启动服务:" + _serverName);
    }

    protected abstract Object specificServices(String[] args);

    @Override
    public void stop() {
        _adapter.destroy();
        logger.print("销毁服务:" + _serverName);
    }
}
