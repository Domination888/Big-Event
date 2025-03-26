package com.domination.cotroller;

import com.domination.pojo.Result;
import com.domination.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        //确保文件名字唯一，防止文件覆盖
        String filename = UUID.randomUUID().toString()+ originalFilename.substring(originalFilename.lastIndexOf("."));
//        file.transferTo(new File("/Volumes/meng/files/" + filename));
        String url = AliOssUtil.uploadFile(filename, file.getInputStream());
        return Result.success(url);

    }
}
