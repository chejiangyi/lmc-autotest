package com.lmc.autotest.task.base;

import lombok.val;

import java.io.File;
import java.util.Date;

public class FileUtils {
    public static void delete(String filename){
        val file = new File(filename);
        if(file.exists()) {
            file.delete();
        }
    }

    public static void clearExpireFile(String dir){
        val dir2 = new File(dir);
        if(dir2.isDirectory()) {
            val files = com.free.bsf.core.util.FileUtils.getAllFiles(dir2);
            for(val file:files){
                //超过三天的文件
                if(new Date().getTime()- new Date(file.lastModified()).getTime()>1000*60*60*24*3){
                    file.delete();
                }
            }
        }
    }
}
