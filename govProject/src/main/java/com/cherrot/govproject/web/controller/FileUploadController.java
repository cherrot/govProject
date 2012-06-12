/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import java.io.File;
import java.io.IOException;
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

    //Defined in servlet-context.xml
    @Inject
    @Named("uploadDir")
    private FileSystemResource fileSystemResource;

    @RequestMapping("/post/upload")
    public void doUpload(@RequestParam("file")MultipartFile file
        , @RequestParam("postId")int postId, HttpServletResponse response) throws IOException {

        String outputString = "<script>parent.callback('upload file successfully')</script>";
        if (!file.isEmpty()) {
            try {
                File newFile = new File(fileSystemResource.getPath() + "/" + file.getOriginalFilename());
                System.err.println(newFile.getAbsolutePath());
                file.transferTo(newFile);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
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
