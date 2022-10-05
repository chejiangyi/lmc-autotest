package com.lmc.autotest.provider.pager;

import com.free.bsf.core.util.StringUtils;
import com.free.bsf.core.util.WebUtils;

/**
 * 分页控件1
 * [后台demo]
 * Pager1 p = new Pager1();
 * p.out();
 * [前台demo]
 * java use; request.getAttribute('pagesize');//pageindex,pagesize,datacount,pagehtml
 * freemarker use: ${pagesize}
 * jvascript need: function pagerfunction(pageindex){ do things....}
 */
public class Pager1 {
	public final static int DEFAULT_PAGESIZE=20;
	public final static int DEFAULT_PAGEINDEX=1;
    private int pageindex;
    private int datacount;

    /**
     * 分页大小 <0 则使用默认20
     */
    public int pagesize=20;
    /**
     * 显示分页数量 <0 则默认为5
     */
    public int showpagecount=5;
    /**
     * 点击界面时的js回调,默认函数名为:pagerfunction,传入pageindex参数。示范 funcation pagerfunction(pageindex){}
     */
    public String jscallbackfunctionname="pagerfunction";

    /**
     *
     * @param pageindex 分页索引 从1开始
     * @param datacount 数据总数
     */
    public Pager1(int pageindex,int datacount)
    {
        this.pageindex = pageindex;
        this.datacount = datacount;
    }

    public Pager1 setPageSize(int pagesize)
    {
        this.pagesize = pagesize;
        return this;
    }

    public Pager1 setShowPageCount(int showpagecount)
    {
        this.showpagecount = showpagecount;
        return this;
    }

    public Pager1 setJsCallBackFuncationName(String jsCallBackFuncationName)
    {
        this.jscallbackfunctionname = this.jscallbackfunctionname;
        return this;
    }



    public void out()
    {
        WebUtils.getRequest().setAttribute("pagehtml", setRequestAttribute().html());
    }
    /**
     * Html
     * @return
     */
    public String html() {
        int pageNo = pageindex;
        int showPages = showpagecount;
        int totalPage = datacount%pagesize==0? (datacount / pagesize):(((int) (datacount / pagesize)) + 1);

        String callFunName = (StringUtils.isEmpty(jscallbackfunctionname)?"pagerfunction":jscallbackfunctionname);

        StringBuffer sb = new StringBuffer();
        sb.append("<div class=\"page_list clearfix\">");
        if (pageNo != 1) {
            sb.append("<a href=\"javascript:" + callFunName + "(" + 1 + ")" + ";\" class=\"top_page\">首页</a>");
            sb.append("<a href=\"javascript:" + callFunName + "(" + (pageNo - 1) + ");\" class=\"page_prev\" > 上一页 </a >");
        }
        int start;
        if (pageNo - (showPages / 2) > 0) {
            start = pageNo - (showPages - 1) / 2;
            if (showPages > totalPage) {
                start = 1;
            }
        } else {
            start = 1;
        }
        int end;
        if (totalPage > showPages) {
            end = (start + showPages - 1);
            if (end > totalPage) {
                start = totalPage - showPages + 1;
                end = totalPage;
            }
        } else {
            end = totalPage;
        }
        for (int page = start; page <= end; page++) {
            if (page == pageNo) {
                sb.append("<a href=\"javascript:" + callFunName + "(" + page + ");\" class=\"current\" >" + page + "</a >");
            } else {
                sb.append("<a href=\"javascript:" + callFunName + "(" + page + ");\" >" + page + "</a >");
            }
        }
        if (pageNo != totalPage) {
            sb.append("<a href = \"javascript:" + callFunName + "(" + (pageNo + 1) + ");\" class=\"page_next\" > 下一页 </a >");
            sb.append("<a href = \"javascript:" + callFunName + "(" + totalPage + ");\" class=\"end_page\" > 尾页 </a >");
        }
        sb.append("</div>");
        return sb.toString();
    }

    public Pager1 setRequestAttribute()
    {
        WebUtils.getRequest().setAttribute("pagesize",pagesize);
        WebUtils.getRequest().setAttribute("pageindex",pageindex);
        WebUtils.getRequest().setAttribute("datacount",datacount);
        return this;
    }
}
