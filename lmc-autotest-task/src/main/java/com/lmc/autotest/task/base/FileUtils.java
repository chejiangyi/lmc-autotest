package com.lmc.autotest.task.base;

import com.free.bsf.core.base.BsfException;
import lombok.val;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Date;

public class FileUtils {
    public static String getSampleFile(Integer taskId,String tranId){
        return tranId+"-"+taskId+".sample";
    }
    public static void delete(String filename){
        val file = new File(filename);
        if(file.exists()) {
            file.delete();
        }
    }

    public static void clearExpireFile(String dir){
        val dir2 = new File(dir);
        if(dir2.isDirectory()) {
            val files = dir2.listFiles();
            for(val file:files){
                if(!file.isDirectory()) {
                    if(file.getName().toLowerCase().endsWith(".temp")
                            ||file.getName().toLowerCase().endsWith(".sample")) {
                        //超过三天的文件
                        if (new Date().getTime() - new Date(file.lastModified()).getTime() > 1000 * 60 * 60 * 24 * 3) {
                            file.delete();
                        }
                    }
                }
            }
        }
    }

    /**
     * 统计文件行数
     * @param filePath 文件路径
     * @return 文件行数
     */
    public static int fileCount(String filePath){
        int lines =0;//文件行数
        try {
            File file = new File(filePath);
            if(file.exists()){
                long fileLength = file.length();
                LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
                lineNumberReader.skip(fileLength);
                lines = lineNumberReader.getLineNumber();
                lineNumberReader.close();
            }else {
            }
        }catch(IOException e) {
            throw new BsfException(e);
        }
        return lines;
    }
}
