/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.service.VideoConvertService;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author LaiWenGen
 */
@Service
public class VideoConvertServiceImpl implements VideoConvertService{

    @Override
    public int videoConvert(final String filepath,final String filename) {
        //throw new UnsupportedOperationException("Not supported yet.");
        Thread videoConvertThread = new Thread(){
            @Override
            public void run(){           
                try {
                    
                    String cmd = "ffmpeg.exe -i " +  filepath + filename + " -s 459x370 " + filename + " .flv";
                    Process exec = Runtime.getRuntime().exec(cmd);
                    try {
                        int waitFor = exec.waitFor();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(VideoConvertServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(VideoConvertServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        videoConvertThread.start();
        return 0;
    }
    
}
