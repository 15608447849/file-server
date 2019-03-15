package launch;

import Ice.Communicator;
import Ice.Identity;
import Ice.Object;
import Ice.ObjectAdapter;
import bottle.distributed.register.imps.FileServerCenterImps;

import leeping.HttpResult;
import leeping.IceIo;
import server.servlet.beans.ExcelReaderOperation;


import java.io.*;
import java.util.List;
import java.util.Map;

public class Main {
    /*
    public static void main(String[] args) {
//        startServer(args);
//        startServerByGrid(args);

//        testFileUpload();
        testExcel();
    }*/

    private static void testFileUpload() {
        try {
            File f = new File("D:\\ftcServer\\c\\33.jpg");
            FileInputStream fileInputStream = new FileInputStream(f);
            String  test =
                    new HttpResult()
                            .addStream(fileInputStream,null,"A.jpg")
                            .fileUpload("http://192.168.1.144:8888/upload")
                            .getRespondContent();
            System.out.println(test);
            while (true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    private static void testExcel(){
        try {
            String filepath = "C:\\Users\\user\\Desktop\\test\\p.xlsx";
            List<Map<String,String>> list = new ExcelReaderOperation(filepath).start();

            //遍历解析出来的list
            for (Map<String,String> map : list) {
                for (Map.Entry<String,String> entry : map.entrySet()) {
                    System.out.print(entry.getKey()+":"+entry.getValue() +" ");
                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
