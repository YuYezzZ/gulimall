package com.yuye.gulimall.thirdparty.controller;

import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.yuye.gulimall.common.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Auther: yuye
 * @Date: 2022/6/5 - 06 - 05 - 9:18
 * @Description: com.yuye.gulimall.thirdparty.controller
 * @version: 1.0
 */
@RestController()
@RequestMapping("/thirdparty/qn")
public class QnController {
    @Value("${spring.cloud.qiniu.accessKey}")
    private String accessKey;
    @Value("${spring.cloud.qiniu.secretKey}")
    private String secretKey;
    @Value("${spring.cloud.qiniu.bucket}")
    private String bucket;
    @Value("${spring.cloud.qiniu.host}")
    private String host;




    /**
     * 上传logo
     */
    @RequestMapping("/upload")
    //@RequiresPermissions("product:brand:delete")
    public R upload(){
        //构造一个带指定 Region 对象的配置类，指定存储区域，和存储空间选择的区域一致
        Configuration cfg = new Configuration(Region.huanan());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传

//默认不指定key的情况下，以文件内容的hash值作为文件名
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
        HashMap<String, String> data = new HashMap<>();
        data.put("token",upToken);
        data.put("host",host);
        return R.ok().put("data",data);
    }

}
