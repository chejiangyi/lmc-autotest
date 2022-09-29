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
                <p style="margin-left: 10px"><input type="checkbox" id="openRefresh" >自动刷新报告数据</p>
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
                        <th style="width:3%">节点</th>
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
                        <td style="width: 15%">采样表</td>
                        <td>${model.filter_table}</td>
                    </tr>
                    <tr>
                        <td>涉及接口数</td>
                        <td>${urlcount}</td>
                    </tr>
                    <tr>
                        <td>涉及样本数</td>
                        <td>总样本: ${model.filter_table_lines!},
                            错误样本: ${model.filter_table_error_lines!},
                            <#assign useSamplesCount=model.filter_table_lines-model.filter_table_error_lines />
                            可用样本: ${Html.w2("==",useSamplesCount,0,"<b style='color:red'>"+useSamplesCount+"</b>",useSamplesCount)}
                        </td>
                    </tr>
                    <tr>
                        <td>样本存储</td>
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
                        <td style="width: 15%">最大承载能力</td>
                        <td>在 ${Html.p(maxthroughput.create_time)!} , 节点并发线程总和${maxthroughput.active_threads!} , 节点吞吐量总和共${maxthroughput.throughput!}/s , 错误总和共${maxthroughput.error!}/s。</td>
                    </tr>
                    <tr>
                        <td>最佳承载能力</td>
                        <td>在 ${Html.p(maxthroughputWithNoError.create_time)!} , 节点并发线程总和${maxthroughputWithNoError.active_threads!} , 节点吞吐量总和共${maxthroughputWithNoError.throughput!}/s , 错误总和共${maxthroughputWithNoError.error!}/s。</td>
                    </tr>
                </table>
            </li>
            <#assign nodeWeiduMap={"cpu":"cpu(%)/s","memory":"内存(M)/s","network_read":"网络读(Bytes)/s","network_write":"网络写(Bytes)/s","active_threads":"活跃线程数/s","throughput":"吞吐量/s","error":"错误量/s"}/>
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
                        <option value="">所有节点</option>
                        <#list model.nodes?split(",") as key>
                            <option value="${key}">${key}</option>
                        </#list>
                    </select>
                <button type="button" onclick="loadNodeReportChart()">刷新</button>
                <div class="chartlist">
                 <#list nodeWeiduMap?keys as key>
                    <div id='nodeReport_${key}' class="cnt" style='width:35%;height:250px;'></div>
                 </#list>
                </div>
            </li>
            <li>
                <label>接口性能指标统计</label>
                    <select id="nodes2" name="nodes2" onchange="loadUrlReportChart()">
                        <option value="">所有节点</option>
                        <#list model.nodes?split(",") as key>
                            <option value="${key}">${key}</option>
                        </#list>
                    </select>
                <button type="button" onclick="loadUrlReportChart()">刷新</button>
                    <table id="urlReport">
                        <tr>
                            <th style="width:15%">接口api</th>
                            <th style="width:5%">累计压测次数</th>
                            <th style="width:5%">最大吞吐量/s</th>
                            <th style="width:5%">最大错误数/s</th>
                            <th style="width:5%">错误率(%)/s</th>
                            <th style="width:5%">最小耗时(ms)/s</th>
                            <th style="width:5%">最大耗时(ms)/s</th>
                            <th style="width:5%">平均耗时(ms)/s</th>
    <#--                        <th style="width:5%">99line耗时/s</th>-->
    <#--                        <th style="width:5%">98line耗时/s</th>-->
                            <th style="width:5%">最大网络读(Bytes)/s</th>
                            <th style="width:5%">最大网络写(Bytes)/s</th>
                            <th style="width:5%">操作</th>
                        </tr>
                    </table>
                    <table style="display: none" id="urltemplate">
                        <tr data="">
                            <td>{url}</td>
                            <td>{all_visit_num}</td>
                            <td>{max_throughput}</td>
                            <td>{max_error}</td>
                            <td>{max_error_per}</td>
                            <td>{min_visit_time}</td>
                            <td>{max_visit_time}</td>
                            <td>{avg_visit_time}</td>
    <#--                        <td>{item.url}</td>-->
    <#--                        <td>{item.url}</td>-->
                            <td>{max_network_read}</td>
                            <td>{max_network_write}</td>
                            <td><a href="javascript:loadUrlChart('{url}')">查看曲线趋势</a></td>
                        </tr>
                    </table>
                    <#assign urlWeiduMap={"throughput":"吞吐量/s","error":"错误量/s","network_read":"网络读(Bytes)/s","network_write":"网络写(Bytes)/s","visit_time":"耗时(ms)/s"}/>

                     <div id="urlCharts" style="display: none;padding-top: 20px" >
                         <div class="cnt" style="text-align: center; ">
                             <b style="font-size: 16px">接口:<span id="urlChart">无</span></b>
                             <div class="chartlist">
                                 <#list urlWeiduMap?keys as key>
                                     <div id='urlChart_${key}' style='width:35%;height:250px;'></div>
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
            $.post("/report/nodesReport",
                {
                    "id":${model.id},
                    "weidu": $("#weidu").val(),
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
                                    text: weidu
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
                },
                function (data) {
                    $('#urlReport tr[data]').remove()
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        for(var r of data.data.report){
                            console.log("aaa",r);
                            var html = $("#urltemplate").html().replaceAll("{url}",r.url)
                                .replaceAll("{all_visit_num}",r.all_visit_num).replaceAll("{max_throughput}",r.max_throughput.toFixed(2))
                                .replaceAll("{max_error_per}",errorPer(r.max_throughput,r.max_error)+"%")
                                .replaceAll("{min_visit_time}",r.min_visit_time.toFixed(2))
                                .replaceAll("{max_visit_time}",r.max_visit_time.toFixed(2)).replaceAll("{avg_visit_time}",r.avg_visit_time.toFixed(2))
                                .replaceAll("{max_network_read}",r.max_network_read.toFixed(2)).replaceAll("{max_network_write}",r.max_network_write.toFixed(2))
                                .replaceAll("{max_error}",((r.max_error==null||r.max_error>0)?"<b style='color:red'>"+r.max_error.toFixed(2)+"</b>":r.max_error.toFixed(2)));
                            $("#urlReport").append(html);
                        }
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
                    "node": $("#nodes").val(),
                    "url":urlSelect,
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        $("#urlChart").html(urlSelect);
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
                                    text: weidu
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
            if(data>0){
                return "<b style='color:red'>"+data.toFixed(2)+"</b>";
            }else{
                return data.toFixed(2);
            }
        }
    </script>
</@layout._layout>