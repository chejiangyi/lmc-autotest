<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","压测报告")}
<@layout._layout>
    <script src="https://cdn.jsdelivr.net/npm/echarts/dist/echarts.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/echarts-gl/dist/echarts-gl.min.js"></script>
    <style>
        .mydetail p{
            display: inline;
        }
        .mydetail table{
            margin-left: 200px;
            width: 80%;
        }
        .mydetail table td{
            text-align: left;
        }
        .mydetail .cnt{
            margin-left: 200px;
        }
        .mydetail .chartlist{
            display:flex;
            flex-wrap:wrap;
        }
         .current{
            color: #0ABD0A;
        }
         .avg{
            color: #ba8b00;
        }
         .min{
            color: #8b4902;
        }
         .max{
            color: #6f42c1;
        }
         .sum{
            color: #0000FF;
        }
         .redS{
             color: red;
         }
         .time1{
             color:#FF9D0A;
         }
         .line90{
             color: #15B6DF;
         }
         .line95{
             color: #0070a9;
         }
         .line98{
             color: #0489C7;
         }
    </style>
    <div class="head">
        <div class="title">
            压测报告
        </div>
    </div>
    <div>
        <ul class="mydetail">
            <li>
                <label>报告名</label>
                <p><b>${model.report_name}</b></p>
            </li>
            <li>
                <label>任务</label>
                <p><a href="/task/edit?id=${model.task_id}">${model.task_name}</a></p>
            </li>
            <li>
                <label>参与节点</label>
                <p>${model.nodes}</p>
                <table>
                    <tr>
                        <th style="width:30%">节点</th>
                        <th style="width:15%">cpu核心数</th>
                        <th style="width:20%">内存(M)</th>
                        <th style="width:15%">压测线程数</th>
                    </tr>
                    <#list nodeinfos as item>
                        <tr data-id="${item.node}">
                            <td>${item.node}</td>
                            <td>${item.cpu}</td>
                            <td>${item.memory}</td>
                            <td>${item.threads}</td>
                        </tr>
                    </#list>
                </table>
            </li>
            <li>
                <label>采样数据</label>
                <table>
                    <tr>
                        <td style="width: 15%">采样表(文件)涉及样本数${Html.help("节点本地生成的临时样本压测文件名称,一般会自动删除!")}</td>
                        <td>${model.filter_table}</td>
                    </tr>
                    <tr>
                        <td>涉及接口数</td>
                        <td>${urlcount}</td>
                    </tr>
                    <tr>
                        <td>涉及样本数${Html.help("筛选样本集合,过滤率错误样本数,最后才是真正的可用有效的压测样本！")}</td>
                        <td>总样本: ${model.filter_table_lines!},
                            错误样本: ${model.filter_table_error_lines!},
                            <#assign useSamplesCount=model.filter_table_lines-model.filter_table_error_lines />
                            可用样本: ${Html.w2("==",useSamplesCount,0,"<b style='color:red'>"+useSamplesCount+"</b>",useSamplesCount)}
                        </td>
                    </tr>
                    <tr>
                        <td>样本存储${Html.help("样本存储的存储引擎,目前仅支持mysql,未来支持es等大容量存储")}</td>
                        <td>${model.filter_store}</td>
                    </tr>
                </table>
            </li>
            <li>
                <label>报告时间</label>
                <table>
                    <tr>
                        <td style="width: 15%">开始时间</td><td>${Html.p(model.begin_time)}</td>
                    </tr>
                    <tr>
                        <td>结束时间</td><td>${Html.p(model.end_time)}</td>
                    </tr>
                    <tr>
                        <td >性能压测耗时</td>
                        <td>${Utils.subTime(model.begin_time,model.end_time)}分钟</td>
                    </tr>
                </table>

            </li>
            <li>
                <label>压测结论</label>
                <table>
                    <tr>
                        <td style="width: 15%">最大承载能力${Html.help("某个心跳周期内所有节点的吞吐量总和最大,则判定为最大承载能力,相应的并发数为最优参考")}</td>
                        <td>在 ${Html.p(maxthroughput.create_time)!} , 节点并发线程总和${maxthroughput.active_threads!} , 节点吞吐量总和共${maxthroughput.throughput!}/s , 错误总和共${maxthroughput.error!}/s。</td>
                    </tr>
                    <tr>
                        <td>最佳承载能力${Html.help("某个心跳周期内所有节点的吞吐量总和最大且无任何错误,则判定为最佳承载能力,相应的并发数为最优参考")}</td>
                        <td>在 ${Html.p(maxthroughputWithNoError.create_time)!} , 节点并发线程总和${maxthroughputWithNoError.active_threads!} , 节点吞吐量总和共${maxthroughputWithNoError.throughput!}/s , 错误总和共${maxthroughputWithNoError.error!}/s。</td>
                    </tr>
                </table>
            </li>
            <li>
                <label>自动刷新图表</label>
                <p style="margin-left: 10px"><input type="checkbox" id="openRefresh" >(自动刷新以下报表数据)</p>
            </li>
            <#assign nodeWeiduMap={"cpu":"cpu(%)/s","memory":"内存(M)/s","network_read":"网络读(Bytes)/s","network_write":"网络写(Bytes)/s","active_threads":"活跃线程数/s","throughput":"吞吐量/s","error":"错误数/s"}/>
            <li>
                <label>单一维度多节点对比</label>
                <select id="weidu" name="weidu" onchange="loadNodesReportChart()">
                        <#list nodeWeiduMap?keys as key>
                            <option value="${key}">${nodeWeiduMap[key]}</option>
                        </#list>
                </select>
                <button type="button" onclick="loadNodesReportChart()">刷新</button>
                <div id='nodesReport' class="cnt" style='width:80%;height:450px;'></div>
            </li>
            <li>
                <label>多维度单节点对比</label>
                    <select id="nodes" name="nodes" onchange="loadNodeReportChart()">
                        <option value="">所有节点(相加)</option>
                        <#list model.nodes?split(",") as key>
                            <option value="${key}">${key}</option>
                        </#list>
                    </select>
                ${Html.help("以节点为维度,可指定单个节点测算,也可以将所有节点的值累加得到总吞吐量和错误数")}
                <button type="button" onclick="loadNodeReportChart()">刷新</button>
                <div class="chartlist">
                 <#list nodeWeiduMap?keys as key>
                    <div id='nodeReport_${key}' title="${nodeWeiduMap[key]}" class="cnt" style='width:35%;height:250px;'></div>
                 </#list>
                </div>
            </li>
            <li>
                <label>接口性能指标统计</label>
                <select id="nodes2" name="nodes2" onchange="loadUrlReportChart()">
                    <option value="">不区分节点</option>
                    <#list model.nodes?split(",") as key>
                        <option value="${key}">${key}</option>
                    </#list>
                </select>
                ${Html.help("以url为维度,可指定单个节点测算,也可以不指定节点测算某个url的统计值,这些统计值不会累加!")}
                <#assign orderMap={"url":"接口api","throughput":"现吞吐量","error":"现错误数","visit_time":"现耗时","sum_throughput":"总吞吐量","sum_error":"总错误数","sum_visit_time":"总耗时","sum_visit_num_per":"占比","jiuling_visit_time":"90Line耗时","jiuwu_visit_time":"95Line耗时","jiuba_visit_time":"98Line耗时"}/>
                降序
                <select id="urlOrder" name="urlOrder" onchange="loadUrlReportChart()">
                    <#list orderMap?keys as key>
                        <option value="${key}">${orderMap[key]}</option>
                    </#list>
                </select>
                <input id="current" type="checkbox" checked="checked" onchange="loadUrlReportChart()"/><p class="current">现在值</p>
                <input id="avg" type="checkbox" onchange="loadUrlReportChart()"/><p class="avg">平均值</p>
                <input id="min" type="checkbox"  onchange="loadUrlReportChart()"/><p class="min">最小值</p>
                <input id="max" type="checkbox"  onchange="loadUrlReportChart()"/><p class="max">最大值</p>
                <input id="sum" type="checkbox" checked="checked" onchange="loadUrlReportChart()"/><p class="sum">总和值</p>${Html.help("现在值:表示当前时间最后获取的值;平均值:表示当前时间及之前历史获取的平均值;最小值:表示当前时间及之前历史获取的最大值;最大值:表示当前时间及之前历史获取的最大值;总和值:表示当前时间持续累计的总和")}

                <#assign heartbeart=Utils.heartBeat() />
                <button type="button" onclick="loadUrlReportChart()">刷新</button>
                    <table id="urlReport">
                        <tr>
                            <th style="width:15%">接口api</th>
                            <th style="width:5%">信息${Html.help("(心跳周期)每隔"+heartbeart+"秒内,[占比]:累计的压测次数/所有压测请求次数,[时间]:现在值的更新时间")}</th>
                            <th style="width:5%">压测次数${Html.help("(心跳周期)每隔"+heartbeart+"秒内,累计的压测次数")}</th>
                            <th style="width:5%">吞吐量/s${Html.help("(心跳周期)每隔"+heartbeart+"秒内,平均的每秒吞吐量次数")}</th>
                            <th style="width:5%">错误数/s${Html.help("(心跳周期)每隔"+heartbeart+"秒内,平均的每秒错误次数,大于0则标红")}</th>
                            <th style="width:5%">错误率(%)/s${Html.help("(心跳周期)每隔"+heartbeart+"秒内,平均的每秒错误次数/(平均的每秒吞吐量+平均的每秒错误次数),大于0则标红")}</th>
    <#--                        <th style="width:5%">99line耗时/s</th>-->
    <#--                        <th style="width:5%">98line耗时/s</th>-->
                            <th style="width:5%">网络读(B)/s${Html.help("(心跳周期)每隔"+heartbeart+"秒内,平均的每秒网络读 单位:字节")}</th>
                            <th style="width:5%">网络写(B)/s${Html.help("(心跳周期)每隔"+heartbeart+"秒内,平均的每秒网络写 单位:字节")}</th>
                            <th style="width:5%">耗时(ms)/s${Html.help("(心跳周期)每隔"+heartbeart+"秒内,平均的每秒耗时,大于300ms则标红")}</th>
                            <th style="width:5%">耗时Line(ms)/s${Html.help("(心跳周期)每隔"+heartbeart+"秒内,平均的每秒耗时排序集合的的90,98,95百分位,大于300ms则标红")}</th>
                            <th style="width:3%">操作</th>
                        </tr>
                    </table>
                    <table style="display: none" id="urltemplate">
                        <tr data="">
                            <td style="word-break: break-all" title="{url}">{url}</td>
                            <td><div class="zhanbi">占:{sum_visit_num_per}</div>
                                <div class="createtime">时:{create_time}</div>
                            </td>
                            <td>
                                <div class="current">{visit_num}</div>
                                <div class="avg">{avg_visit_num}</div>
                                <div class="min">{min_visit_num}</div>
                                <div class="max">{max_visit_num}</div>
                                <div class="sum">{sum_visit_num}</div>
                            </td>
                            <td>
                                <div class="current">{throughput}</div>
                                <div class="avg">{avg_throughput}</div>
                                <div class="min">{min_throughput}</div>
                                <div class="max">{max_throughput}</div>
                                <div class="sum">{sum_throughput}</div>
                            </td>
                            <td>
                                <div class="current">{error}</div>
                                <div class="avg">{avg_error}</div>
                                <div class="min">{min_error}</div>
                                <div class="max">{max_error}</div>
                                <div class="sum">{sum_error}</div>
                            </td>
                            <td>
                                <div class="current">{error_per}</div>
                                <div class="avg">/</div>
                                <div class="min">/</div>
                                <div class="max">/</div>
                                <div class="sum">{sum_error_per}</div>
                            </td>
                            <td>
                                <div class="current">{network_read}</div>
                                <div class="avg">{avg_network_read}</div>
                                <div class="min">{min_network_read}</div>
                                <div class="max">{max_network_read}</div>
                                <div class="sum">{sum_network_read}</div>
                            </td>
                            <td>
                                <div class="current">{network_write}</div>
                                <div class="avg">{avg_network_write}</div>
                                <div class="min">{min_network_write}</div>
                                <div class="max">{max_network_write}</div>
                                <div class="sum">{sum_network_write}</div>
                            </td>
                            <td>
                                <div class="current">{visit_time}</div>
                                <div class="avg">{avg_visit_time}</div>
                                <div class="min">{min_visit_time}</div>
                                <div class="max">{max_visit_time}</div>
                                <div class="sum">{sum_visit_time}</div>
                            </td>
                            <td>
                                <div class="line90">{jiuling_visit_time}</div>
                                <div class="line95">{jiuwu_visit_time}</div>
                                <div class="line98">{jiuba_visit_time}</div>
                            </td>
                            <td><a href="javascript:loadUrlChart('{url}')">趋势图</a></td>
                        </tr>
                    </table>
                    <#assign urlWeiduMap={"throughput":"吞吐量/s","error":"错误数/s","network_read":"网络读(Bytes)/s","network_write":"网络写(Bytes)/s","visit_time":"耗时(ms)/s"}/>

                     <div id="urlCharts" style="display: none;padding-top: 20px" >
                         <div class="cnt" style="text-align: left; ">
                             <p >
                                 <b style="font-size: 16px"><span style="color: blue">[节点]</span><span id="urlChartNode">无</span></b>
                                 <b style="font-size: 16px"><span style="color: blue">[接口]</span><span id="urlChartUrl">无</span></b>
                             </p>
                             <div class="chartlist">
                                 <#list urlWeiduMap?keys as key>
                                     <div id='urlChart_${key}' title="${urlWeiduMap[key]}" style='width:35%;height:250px;'></div>
                                 </#list>
                             </div>
                         </div>
                    </div>
            </li>

        </ul>
    </div>

    <script type="text/javascript">
        function loadNodesReportChart(){
            var nodes = "${model.nodes}".split(',');
            var weidu = $("#weidu").val();
            $.post("/report/nodesReport",
                {
                    "id":${model.id},
                    "weidu": weidu,
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        var  option = {
                            title: {
                                text: ''
                            },
                            tooltip: {
                                trigger: 'axis'
                            },
                            legend: {
                                data: nodes
                            },
                            grid: {
                                left: '3%',
                                right: '4%',
                                bottom: '3%',
                                containLabel: true
                            },
                            toolbox: {
                                feature: {
                                    saveAsImage: {}
                                }
                            },
                            xAxis: {
                                type: 'time',
                                boundaryGap: false
                            },
                            yAxis: {
                                type: 'value'
                            },
                            series: [
                            ]
                        };
                        for(var key in data.data.nodes) {
                            option.series.push({
                                name: key,
                                type: 'line',
                                stack: 'Total',
                                data: data.data.nodes[key]
                            });
                        }
                        var myChart  = echarts.init(document.getElementById('nodesReport'));
                        myChart.setOption(option);
                        console.log("loadNodesReportChart",option);
                    }
                }, "json");
        }
        function loadNodeReportChart(){
            var nodeWeidus = [];
            <#list nodeWeiduMap?keys as key>
            nodeWeidus.push("${key}");
            </#list>
            $.post("/report/nodeReport",
                {
                    "id":${model.id},
                    "node": $("#nodes").val(),
                    "weidu": $("#weidu").val(),
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        for(var weidu of nodeWeidus) {
                            var  option = {
                                tooltip: {
                                    trigger: 'axis',
                                    position: function (pt) {
                                        return [pt[0], '10%'];
                                    }
                                },
                                title: {
                                    left: 'center',
                                    text: $('#nodeReport_'+weidu).attr('title')
                                },
                                toolbox: {
                                    feature: {
                                        dataZoom: {
                                            yAxisIndex: 'none'
                                        },
                                        restore: {},
                                        saveAsImage: {}
                                    }
                                },
                                xAxis: {
                                    type: 'time',
                                    boundaryGap: false
                                },
                                yAxis: {
                                    type: 'value',
                                    boundaryGap: [0, '100%']
                                },
                                series: [
                                    {
                                        name: weidu,
                                        type: 'line',
                                        smooth: true,
                                        symbol: 'none',
                                        areaStyle: {},
                                        data: data.data.node[weidu]
                                    }
                                ]
                            };
                            var myChart = echarts.init(document.getElementById('nodeReport_'+weidu));
                            myChart.setOption(option);
                            console.log("loadNodeReportChart-"+weidu,option);
                        }
                    }
                }, "json");
        }
        function loadUrlReportChart(){
            $.post("/report/urlReport",
                {
                    "id":${model.id},
                    "node": $("#nodes2").val(),
                    "order":$("#urlOrder").val(),
                },
                function (data) {
                    $('#urlReport tr[data]').remove()
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        for(var r of data.data.report){
                            console.log("aaa2",r);
                            var html = $("#urltemplate").html();

                            for(let key of Object.keys(r)){
                                //console.log("pp",key);
                                html =html.replaceAll("{"+key+"}",toNumber(key,r[key]));
                            }
                            html = html.replaceAll("{error_per}", toNumber("error_per",errorPer( r["throughput"],r["error"])));
                            html = html.replaceAll("{sum_error_per}", toNumber("sum_error_per",errorPer( r["sum_throughput"],r["sum_error"])));
                            $("#urlReport").append(html);
                        }
                        $("#urlReport .current").each(function (){
                            $(this).html("现:"+$(this).html());$(this).attr("title","当前值");if(!$("#current").is(':checked')){$(this).hide();}
                        })
                        $("#urlReport .avg").each(function (){
                            $(this).html("均:"+$(this).html());$(this).attr("title","平均值");if(!$("#avg").is(':checked')){$(this).hide();}
                        })
                        $("#urlReport .min").each(function (){
                            $(this).html("小:"+$(this).html());$(this).attr("title","最小值");if(!$("#min").is(':checked')){$(this).hide();}
                        })
                        $("#urlReport .max").each(function (){
                            $(this).html("大:"+$(this).html());$(this).attr("title","最大值");if(!$("#max").is(':checked')){$(this).hide();}
                        })
                        $("#urlReport .sum").each(function (){
                            $(this).html("总:"+$(this).html());$(this).attr("title","总和值");if(!$("#sum").is(':checked')){$(this).hide();}
                        })
                        $("#urlReport .line90").each(function (){
                            $(this).html("90L:"+$(this).html());$(this).attr("title","90百分位,90line");if(!$("#sum").is(':checked')){$(this).hide();}
                        })
                        $("#urlReport .line95").each(function (){
                            $(this).html("95L:"+$(this).html());$(this).attr("title","95百分位,95line");if(!$("#sum").is(':checked')){$(this).hide();}
                        })
                        $("#urlReport .line98").each(function (){
                            $(this).html("98L:"+$(this).html());$(this).attr("title","98百分位,98line");if(!$("#sum").is(':checked')){$(this).hide();}
                        })
                        $('#urlReport tr[data]').show();
                    }
                }, "json");
        }
        var urlSelect="";
        function loadUrlChart(url){
            if(url!=null&&url != undefined){
                urlSelect=url;
            }
            if(urlSelect==""){
                return;
            }
            var urlWeidus = [];
            <#list urlWeiduMap?keys as key>
            urlWeidus.push("${key}");
            </#list>
            $.post("/report/urlChart",
                {
                    "id":${model.id},
                    "node": $("#nodes2").val(),
                    "url":urlSelect,
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        $("#urlChartUrl").html(urlSelect);
                        $("#urlChartNode").html($("#nodes2").find("option:selected").text());
                        $("#urlCharts").show();
                        for(var weidu of urlWeidus){
                            var  option = {
                                tooltip: {
                                    trigger: 'axis',
                                    position: function (pt) {
                                        return [pt[0], '10%'];
                                    }
                                },
                                title: {
                                    left: 'center',
                                    text: $('#urlChart_'+weidu).attr('title')
                                },
                                toolbox: {
                                    feature: {
                                        dataZoom: {
                                            yAxisIndex: 'none'
                                        },
                                        restore: {},
                                        saveAsImage: {}
                                    }
                                },
                                xAxis: {
                                    type: 'time',
                                    boundaryGap: false
                                },
                                yAxis: {
                                    type: 'value',
                                    boundaryGap: [0, '100%']
                                },
                                series: [
                                    {
                                        name: weidu,
                                        type: 'line',
                                        smooth: true,
                                        symbol: 'none',
                                        areaStyle: {},
                                        data: data.data.chart[weidu]
                                    }
                                ]
                            };
                            var myChart = echarts.init(document.getElementById('urlChart_'+weidu));
                            myChart.setOption(option);
                        }
                    }
                }, "json");
        }
        $(function (){
            refresh();
            setInterval("autoRefresh()",5000);
        });

        function autoRefresh(){
            if($("#openRefresh").is(":checked")) {
                refresh();
            }
        }

        function refresh(){
            loadNodesReportChart();
            loadNodeReportChart();
            loadUrlReportChart();
            loadUrlChart();
        }
        function errorPer(throughput, error){
            var count = throughput+error;
            var data = 0;
            if(count==0)
                data= 0;
            else
                data= error/count;
            return data;
        }
        function toNumber(key,value){
            var v2=value;
            if(key=='create_time'){
                return showDate(v2);
            }
            if(key.indexOf('_per')>-1){
                var v3 = (v2*100).toFixed(2);
                var pv3="≈"+v3+"%";
                if(key.indexOf('error')>-1&&v3>0){
                    return "<b class='redS'>"+pv3+"</b>";
                }
                return pv3;
            }
            if(String(value).indexOf(".")>-1){
                try {
                    v2 = value.toFixed(2);
                }catch (e){}
            }
            if(key.indexOf('error')>-1&&v2>0){
                return "<b class='redS'>"+v2+"</b>";
            }
            if(key.indexOf('visit_time')>-1&&key.indexOf('sum')<0&&v2>300){
                return "<b class='redS'>"+v2+"</b>";
            }

            return v2;
        }

        function showDate(date){
            var myDate= new Date(Date.parse(date.replace(/-/g, "/")));
            console.log("时间",date,myDate);
            var myTime = "";
            var timespan = new Date().getTime()-myDate.getTime();
            if(timespan<15*1000){
                myTime= (timespan/1000).toFixed(0)+"秒内";
            }
            else if(timespan<60*1000){
                myTime= "1分钟内";
            }else {
                myTime = myDate.format("hh:mm:ss");
            }
            return '<span class="time1" title="'+myDate.format("yyyy-MM-dd hh:mm:ss")+'">'+myTime+'</span>';
        }
        // function showStr(str){
        //     if(str==null||str==""){
        //         return str;
        //     }
        //     if(str.length()>100) {
        //         return str.substr(0, 100)+"...";
        //     }
        //     return str;
        // }
    </script>
</@layout._layout>