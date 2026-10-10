package com.lcyl.web.controller.system;

import com.lcyl.common.utils.Result;
import com.lcyl.web.config.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@RestController
public class FileController {

    @Autowired
    private OssService ossService;
    // 生成新的文件名
    private String createNewFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String suffix =
                originalFilename.substring(originalFilename.lastIndexOf('.'));
        String fileName =  UUID.randomUUID()+suffix;
        return fileName;
    }
    /**
     * 图片上传
     */
    @PostMapping("upload")
    public Result<String> upload(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("请选择文件！");
            }
            InputStream in = file.getInputStream();
            String newFileName = createNewFileName(file);
            String uploadUrl = ossService.upload(in, newFileName);
            Result r = Result.success(uploadUrl);
            System.out.println("r = " + r);
            return r;
        }catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传文件失败");

        }
    }

//    @PostMapping("/multipleUpload")
//    public Result multipleUpload(MultipartFile[] files) throws IOException {
//        // 判断是否选择文件
//        if (files.length == 0) {
//            return Result.error("请选择文件");
//        }
//        List<String> filePaths = new ArrayList<> ();
//        for (MultipartFile file : files) {
//            InputStream in = file.getInputStream();
//            String newFileName = createNewFileName(file);
//            String uploadUrl = ossService.upload(in, newFileName);
//            filePaths.add(uploadUrl); // 添加到集合中
//        }
//        return Result.success(filePaths);
//    }

    /**
     * 【新增】合同PDF专用上传
     */
    @PostMapping("/uploadContractPdf")
    public Result<String> uploadContractPdf(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("请选择PDF文件！");
            }

            String originalFilename = file.getOriginalFilename();
            // 兼容 Java 8 的空判断
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                return Result.error("文件名不能为空");
            }

            // 兼容大小写 .pdf 判断
            if (!originalFilename.toLowerCase().endsWith(".pdf")) {
                return Result.error("仅支持上传PDF格式文件");
            }

            InputStream in = file.getInputStream();
            String uploadUrl = ossService.uploadContractPdf(in, originalFilename);
            return Result.success(uploadUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("PDF上传失败：" + e.getMessage());
        }
    }


}