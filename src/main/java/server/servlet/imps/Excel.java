package server.servlet.imps;

import bottle.ftc.tools.StringUtil;
import server.entity.ExcelResult;
import server.entity.WebProperties;
import server.servlet.beans.ExcelReaderOperation;
import server.entity.Result;
import server.servlet.iface.Mservlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static server.servlet.iface.Mservlet.RESULT_CODE.*;

public class Excel extends Mservlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        ExcelResult result = new ExcelResult();
        String path = req.getHeader("excel-path");
        if (!StringUtil.isEntry(path)){
            path = checkDirPath(path);
            path = WebProperties.get().rootPath + path;
            try {
                List<Map<String,String>> list = new ExcelReaderOperation(path).start();
                result.data = list;
                result.info(SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                result.info(EXCEPTION);
            }
        }else{
            result.info(PARAM_ERROR);
        }
        writeJson(resp,result);
    }
}
