/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.impl.VideoConverterService;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
public class FileUploadController {

    @Inject
    private PostService postService;
    @Inject
    private VideoConverterService videoConvertService;


    //Defined in servlet-context.xml
    @Inject
    @Named("uploadDir")
    private FileSystemResource fileSystemResource;

    @RequestMapping("/post/upload")
    public void doUpload(@RequestParam("qqfile")MultipartFile file
        , @RequestParam("postId")int postId, HttpServletResponse response) throws IOException {

        if (!file.isEmpty()) {
            try {
                File newFile = new File(fileSystemResource.getPath() + "/" + file.getOriginalFilename());
                Logger.getLogger(HomeController.class.getSimpleName()).log(Level.INFO, "{0} is created for post {1}", new Object[]{newFile.getAbsolutePath(), postId});
                file.transferTo(newFile);
                //by lai 2012.6.12 视频处理
                postService.testVideo();
                videoConvertService.videoConvert(newFile.getAbsolutePath());

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().print("{success: true}");
            } catch (Exception ex) {
                Logger.getLogger(FileUploadController.class.getSimpleName()).log(Level.SEVERE, ex.getMessage(), ex);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().print("{success: false}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{success: false}");
        }
    }
}
