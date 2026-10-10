package com.lcyl.web.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OssServiceImpl implements OssService {
    @Autowired
    private OSS ossClient;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    @Value("${aliyun.oss.dir.prefix}")
    private String dirPrefix;
    @Value("${aliyun.oss.dir.datePattern}")
    private String dirDatePattern;
    @Override
    public String upload(InputStream in, String filename) {
        final SimpleDateFormat sdf = new SimpleDateFormat(dirDatePattern);
        final String dirDatePath = sdf.format(new Date ());
        String dir = dirPrefix + dirDatePath;
        String key = dir + "/" + filename;
        //ossClient.putObject(bucketName, key, in);
        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                key, in);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setObjectAcl( CannedAccessControlList.PublicRead);
        ossClient.putObject(putObjectRequest);
        //返回文件上传成功后的访问路径 https://bucketName+"."+endpoint+"/"+key
        String uploadUrl = "https://" + bucketName + "." + endpoint + "/" + key;
        return uploadUrl;
    }
    public String upload1(InputStream in, String filename) {
        final SimpleDateFormat sdf = new SimpleDateFormat(dirDatePattern);
        final String dirDatePath = sdf.format(new Date());
        // 目录固定：lcyl/contract/20260325
        String dir = "lcyl/contract/" + dirDatePath;
        // 直接用原始文件名，不修改！
        String key = dir + "/" + filename;

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, in);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setObjectAcl(CannedAccessControlList.PublicRead);
        ossClient.putObject(putObjectRequest);

        String uploadUrl = "https://" + bucketName + "." + endpoint + "/" + key;
        return uploadUrl;
    }

    // ====================== 【合同PDF上传】 ======================
    @Override
    public String uploadContractPdf(InputStream in, String filename) {
        // 直接传 原文件名！不做任何修改
        return upload1(in, filename);
    }

}
