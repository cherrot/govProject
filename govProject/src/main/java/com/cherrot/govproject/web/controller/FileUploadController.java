/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.impl.VideoConverterService;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    /**
     * 用于 qqFileUpload JS插件
     *
     * @param file
     * @param postId
     * @param response
     * @throws IOException
     */
    @RequestMapping("/post/uploadvideo")
    public void doUploadVideo(@RequestParam("qqfile") MultipartFile file
        , @RequestParam("postId")Integer postId
        , HttpServletResponse response) throws IOException {

        if (!file.isEmpty()) {
            Date today = new Date();
            Format formatter = new SimpleDateFormat("yyyy-MM/dd");//按日期分为两层目录：年-月/日
            try {
                File localFile = new File(fileSystemResource.getPath() + formatter.format(today) + "/" + today.getTime() + "-" + file.getOriginalFilename());
                localFile.mkdirs();
                file.transferTo(localFile);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().print("{success: true}");
                //PENDING file.getContentType() 是否返回MIME？
                postService.addAttachment(postId, localFile, file.getContentType());
                videoConvertService.videoConvert(localFile.getAbsolutePath());
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

    @RequestMapping(value = "/post/uploadimage"/*, produces="application/json"*/)
    public void doUploadImage(@RequestParam("imageFile") MultipartFile imageFile
        , @RequestParam(value="postId", required=false)Integer postId /*TODO 实现和Post的关联*/
        , HttpServletResponse response) throws IOException {

        if (!imageFile.isEmpty()) {
            String title = "";   //图片标题
            String url = "";     //图片地址
//            String fileName = "";
            String originalName = imageFile.getOriginalFilename();
            String state = "SUCCESS";

            Pattern reg = Pattern.compile("[.]jpg|png|jpeg|gif$");
            Matcher matcher = reg.matcher(originalName);
            if (!matcher.find()) {
                state = "文件类型不允许！";
            } else {
                Date today = new Date();
                Format formatter = new SimpleDateFormat("yyyy-MM/dd");
                String uri = formatter.format(today) + "/" + today.getTime() + "-" + imageFile.getOriginalFilename();
                url = "/uploads/" + uri;
                title = originalName;
                title = title.replace("&", "&amp;").replace("'", "&qpos;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;");

                try {
                    File localFile = new File(fileSystemResource.getPath() + uri);
                    localFile.mkdirs();
                    imageFile.transferTo(localFile);
                } catch (Exception ex) {
                    Logger.getLogger(FileUploadController.class.getSimpleName()).log(Level.SEVERE, ex.getMessage(), ex);
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
            response.getWriter().print("{'original':'" + originalName + "','url':'" + url + "','title':'" + title + "','state':'" + state + "'}");
//            response.getWriter().print("{ 'url':'" +url +"','title':'" +title +",'state':'" +state +"'}");
//            System.err.println("{'url':'" +url +"','title':'" +title +",'state':'" +state +"'}");
        }
    }
}
