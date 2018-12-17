package bottle.distributed.register.imps;

import leeping.HttpUtil;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class HttpResult implements HttpUtil.Callback  {

    String text;

    void bindParam(StringBuffer sb,Map<String,String > map){
        Iterator<Map.Entry<String,String>> it = map.entrySet().iterator();
        Map.Entry<String,String> entry ;

        while (it.hasNext()) {
            entry = it.next();
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length()-1);
    }

    HttpResult accessUrl(String url){
        new HttpUtil.Request(url,this).setReadTimeout(1000).setConnectTimeout(1000).text().execute();
        return this;
    }
    String getRespondContent(){
        return text;
    }

    @Override
    public void onProgress(File file, long progress, long total) {
        //pass
    }

    @Override
    public void onResult(HttpUtil.Response response) {
        this.text = response.getMessage();
    }

    @Override
    public void onError(Exception e) {
        this.text = null;
    }
}
