package properties.abs;

import bottle.ftc.tools.Log;
import properties.annotations.PropertiesFilePath;
import properties.annotations.PropertiesName;
import properties.infs.FieldConvert;
import properties.infs.baseImp.*;
import server.LunchServer;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Properties;

/**
 * 自动读取属性文件
 * 子类继承,属性使用注解 PropertiesName
 */
@PropertiesFilePath("/application.properties")
public abstract class ApplicationPropertiesBase {

    private static final HashMap<String, FieldConvert> baseType = new HashMap<>();

    static {
        baseType.put("class java.lang.String",new StringConvertImp());
        baseType.put("boolean",new BooleanConvertImp());
        baseType.put("int",new IntConvertImp());
        baseType.put("float",new FloatConvertImp());
        baseType.put("double",new DoubleConvertImp());
        baseType.put("long",new LongConvertImp());
    }

    private static final Properties properties = new Properties();

    public ApplicationPropertiesBase() {
        try {
            String filePath = getPropertiesFilePath();
            InputStream in = readPathProperties(filePath);
            properties.clear();
            properties.load(in);
            in.close();
            autoReadPropertiesMapToField();
            initialization();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream readPathProperties(String filePath) throws FileNotFoundException {
        //优先从外部配置文件获取
        String dirPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
        File file = new File(dirPath+"/resources"+filePath);
        try {
            Log.i("配置文件: "+ file.getCanonicalPath() +" 是否存在: "+file.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.exists()){
            return new FileInputStream(file);
        }else{
            return getClass().getResourceAsStream( filePath );
        }
    }

    public static String getPropertiesPath(String fileName) {
        File f = new File("./resources/"+fileName);
        if (f.exists()) {
            try {
                return f.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ApplicationPropertiesBase.class.getResource("/"+fileName).toString();
    }


    private String getPropertiesFilePath() {
        PropertiesFilePath annotation = this.getClass().getAnnotation(PropertiesFilePath.class);
        return annotation.value();
    }

    protected void autoReadPropertiesMapToField() throws Exception{
        Class clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();//某个类的所有声明的字段，即包括public、private和protected ,但是不包括父类的申明字段

        for (Field field : fields){
            PropertiesName name = field.getAnnotation(PropertiesName.class);
            if(null != name){
                boolean canAccess = field.isAccessible();//如果成员为私有，暂时让私有成员运行被访问和修改
                if(!canAccess){
                    field.setAccessible(true);
                }
                String key = name.value();
                String value = properties.getProperty(key);
                if (value==null || value.length()==0) throw new IllegalAccessException("找不到属性名:"+key+"\n全部属性:"+properties);
                //获取属性类型
                String type = field.getGenericType().toString();
                if(baseType.containsKey(type)){
                    baseType.get(type).setValue(this,field,value);
                }
                field.setAccessible(canAccess);//改回原来的访问方式
            }
        }
    }

    protected abstract void initialization();
}
