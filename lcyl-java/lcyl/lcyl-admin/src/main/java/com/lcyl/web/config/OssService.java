package com.lcyl.web.config;



import java.io.InputStream;


public interface OssService {
    String upload(InputStream in, String filename);

    /**
     * 【新增】合同PDF专用上传
     */
    String uploadContractPdf(InputStream in, String filename);


}
