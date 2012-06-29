/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author LaiWenGen
 */
@Service
public class VideoConverterService {

    public int videoConvert(final String absoluteFilename) {
        Thread videoConvertThread = new Thread() {
            @Override
            public void run() {
                try {
                    String cmd = "ffmpeg -i " + absoluteFilename + " -s 459x370 " + absoluteFilename + ".flv";
                    Process exec = Runtime.getRuntime().exec(cmd);
                    Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "{0}.flv 创建成功", absoluteFilename);
                    try {
                        int waitFor = exec.waitFor();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(VideoConverterService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(VideoConverterService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        videoConvertThread.start();
        return 0;
    }
}
