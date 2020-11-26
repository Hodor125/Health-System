package com.ahu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.util.UUID;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/23
 * @description ：
 * @version: 1.0
 */
public class TestQiNiu {
//    @Test
    public void upload(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "ZMyM7XRU0B-KOx6CQ_vUmVnMkqqV1E2NOv-f39Rh";
        String secretKey = "Vem1fsUGZJ2DLFa6yg2MXVv7p_EcvHw0bUKLrcVF";
        String bucket = "health-system-hodor007";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "D:\\java\\健康医疗项目\\day54_health\\资源\\图片资源\\e373b2eb-0e50-4e95-a09b-03f2c1ee1d351.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名(文件名)
        String uid = UUID.randomUUID().toString();
//        String key = "health_01";
        String key = uid + ".jpg";

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

//    @Test
    public void delete(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
//...其他参数参考类注释

        String accessKey = "ZMyM7XRU0B-KOx6CQ_vUmVnMkqqV1E2NOv-f39Rh";
        String secretKey = "Vem1fsUGZJ2DLFa6yg2MXVv7p_EcvHw0bUKLrcVF";
        String bucket = "health-system-hodor007";
        String key = "Fh7hcXM1hNqiujDzM3RUUCqcrvi4";

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

//    @Test
    public void testLastIndex() {
        String url = "ansdf.jpg";
        int i = url.lastIndexOf('.');
        String substring = url.substring(i);
        System.out.println(substring);
    }
}
