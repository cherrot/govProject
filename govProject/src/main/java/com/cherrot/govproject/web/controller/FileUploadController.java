/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.VideoConvertService;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ReportAsSingleViolation;
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
    private Post videoPost;
    @Inject
    private Post videoPostParent;
    @Inject
    private PostService postService;
    @Inject
    private VideoConvertService videoConvertService;
    

    //Defined in servlet-context.xml
    @Named(value="uploadDirResource")
    private FileSystemResource fileSystemResource;

    @RequestMapping("/post/upload")
    public void doUpload(@RequestParam("file")MultipartFile file
        , @RequestParam("postId")int postId, HttpServletResponse response) throws IOException {

        String outputString = "<script>parent.callback('upload file successfully')</script>";
        if (!file.isEmpty()) {
            try {
                file.transferTo(new File(fileSystemResource.getPath() + file.getOriginalFilename()));
                //by lai 2012.6.12
                videoPostParent =  postService.find(postId);
                videoPost.setPostParent(videoPostParent);
                videoPost.setType(Post.PostType.ATTACHMENT);
                videoPost.setMime(file.getContentType());
                videoConvertService.videoConvert(fileSystemResource.getPath(),file.getOriginalFilename());                
                
            } catch (Exception ex) {
                outputString = "<script>parent.callback('upload file failed')</script>";
            } finally {
                response.getWriter().println(outputString);
            }
        } else {
            outputString = "<script>parent.callback('upload file failed')</script>";
            response.getWriter().println(outputString);
        }
    }
}
