package server.entity;

import bottle.ftc.tools.FileUtil;
import properties.abs.ApplicationPropertiesBase;
import properties.annotations.PropertiesFilePath;
import properties.annotations.PropertiesName;

@PropertiesFilePath("/web.properties")
public class WebProperties extends ApplicationPropertiesBase {

    private WebProperties(){}

    private static class Holder{
        private static WebProperties INSTANCE = new WebProperties();
    }

    public static WebProperties get(){
        return Holder.INSTANCE;
    }

    @PropertiesName("web.ip")
    public String webIp;
    @PropertiesName("web.port")
    public int webPort;
    @PropertiesName("web.file.directory")
    public String rootPath;

    public String tempPath;
    @Override
    protected void initialization() {
        if (!FileUtil.checkDir(rootPath)){
            throw new IllegalStateException("启动失败,无效的主目录路径:"+rootPath);
        }
        tempPath = rootPath+FileUtil.SEPARATOR+"temporary";
        if (!FileUtil.checkDir(tempPath)){
            throw new IllegalStateException("启动失败,无效的主目录路径:"+tempPath);
        }
    }


}
