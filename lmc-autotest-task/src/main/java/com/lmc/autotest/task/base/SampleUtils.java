package com.lmc.autotest.task.base;

import com.free.bsf.core.base.BsfException;
import com.free.bsf.core.base.Callable;
import com.free.bsf.core.util.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class SampleUtils {
    private static Charset charset=Charset.forName("utf-8");
    public static void readline(String fileName, Callable.Action1<String> action1){
        try {
            File file = new File(fileName);
            try (FileInputStream fis = new FileInputStream(file)) {
                try (InputStreamReader isr = new InputStreamReader(fis, charset)) {
                    try (BufferedReader br = new BufferedReader(isr)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            action1.invoke(SampleUtils.encode(line,false));
                        }
                    }
                }
            }
        }catch (Exception exp){
            throw new BsfException("读取采样文件出错",exp);
        }
    }

    public static void writeline(String fileName, String line){
        try {
            FileUtils.appendAllText(fileName,SampleUtils.encode(line,true));
        }catch (Exception exp){
            throw new BsfException("写入采样文件出错",exp);
        }
    }

    private static String encode(String line,boolean encode){
        if(encode){
            return line.replace("\r","[换行]").replace("\n","[回车]");
        }else{
            return line.replace("[换行]","\r").replace("[回车]","\n");
        }
    }
}
