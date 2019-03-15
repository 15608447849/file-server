package server.servlet.imps;

import bottle.ftc.tools.FileUtil;
import bottle.ftc.tools.Log;
import bottle.ftc.tools.StringUtil;
import org.omg.PortableInterceptor.SUCCESSFUL;
import server.entity.ErgodicResult;
import server.entity.ExcelResult;
import server.entity.WebProperties;
import server.servlet.beans.FileErgodicOperation;
import server.servlet.iface.Mservlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static server.servlet.iface.Mservlet.RESULT_CODE.EXCEPTION;
import static server.servlet.iface.Mservlet.RESULT_CODE.SUCCESS;

public class FileErgodic extends Mservlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        ErgodicResult result = new ErgodicResult();
        String path = req.getHeader("specify-path");
        String sub = req.getHeader("ergodic-sub");
        boolean isSub = true;
        if (!StringUtil.isEntry(sub)){
            try{
                isSub = Boolean.parseBoolean(sub);
            }catch (Exception ignored){}
        }
        if (StringUtil.isEntry(path)) path = FileUtil.SEPARATOR;
        try {
            path = WebProperties.get().rootPath + checkDirPath(path);
            result.data = new FileErgodicOperation(path,isSub).start();
            result.info(SUCCESS);
        } catch (Exception e) {
//            e.printStackTrace();
            result.data = new ArrayList<>();
            result.info(EXCEPTION,e.toString());
        }
        writeJson(resp,result);
    }
}
