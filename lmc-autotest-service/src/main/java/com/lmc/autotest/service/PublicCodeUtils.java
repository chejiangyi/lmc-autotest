package com.lmc.autotest.service;

import com.free.bsf.core.db.DbHelper;
import com.free.bsf.core.util.StringUtils;
import com.lmc.autotest.core.Config;
import com.lmc.autotest.dao.tb_config_dal;
import lombok.val;

public class PublicCodeUtils {
    public static String getPublic(){
        //公共代码
        return DbHelper.get(Config.mysqlDataSource(), c -> {
            val code = new tb_config_dal().get(c,"public_code");
            if(code!=null){
                return StringUtils.nullToEmpty(code.dic_value);
            }
            return "";
        });
    }
}
