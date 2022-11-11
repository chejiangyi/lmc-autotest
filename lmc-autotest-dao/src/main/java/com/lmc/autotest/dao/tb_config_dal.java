package com.lmc.autotest.dao;


import com.free.bsf.core.db.DbConn;
import com.free.bsf.core.util.ConvertUtils;
import com.lmc.autotest.dao.dal.auto.tb_config_base_dal;
import com.lmc.autotest.dao.model.auto.tb_config_model;
import lombok.val;

import java.util.ArrayList;
import java.util.Map;

/**
 * tb_config 表自动dal映射,不要手工修改
 *
 * @author 车江毅
 * @since 2022-11-11 15:28:07
 * 自动生成: https://gitee.com/makejava/EasyCode/wikis/
 */
public class tb_config_dal extends tb_config_base_dal {
    public boolean edit2(DbConn conn, tb_config_model model) {
        val par = new Object[]{
                /***/
                model.dic_value,
                /***/
                model.dic_key
        };
        int rev = conn.executeSql("update tb_config set dic_value=? where dic_key=?", par);
        return rev == 1;

    }
}


