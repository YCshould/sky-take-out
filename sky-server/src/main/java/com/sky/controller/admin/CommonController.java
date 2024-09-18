package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result uploadfile(MultipartFile file) throws IOException {
        log.info("上传的图片信息:{}",file);
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();

        //保证文件名唯一使用UUID
        //将最后小数点截取即获取文件后缀名
        int index = originalFilename.lastIndexOf('.');
        String substring = originalFilename.substring(index);
        String newfilename= UUID.randomUUID().toString()+substring;
        //保存在阿里云
        String filepath=aliOssUtil.upload(file.getBytes(),newfilename);

        //保存在本地
        //file.transferTo(new File("D:\\CCC\\"+newfilename));
        //String image=newfilename;
        return  Result.success(filepath);
    }

}
