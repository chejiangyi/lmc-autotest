<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","压测报告")}
<@layout._layout>
    <script src="https://cdn.jsdelivr.net/npm/echarts/dist/echarts.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/echarts-gl/dist/echarts-gl.min.js"></script>
    <style>
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
                <p>${model?report_name}</p>
            </li>
            <li>
                <label>任务</label>
                <p><a href="/task/edit?id=${model?task_id}">${model?task_name}</a></p>
            </li>
            <li>
                <label>参与节点</label>
                <p>${model?nodes}</p>
                <table class="mytable" width="500">
                    <tr>
                        <th style="width:3%">节点</th>
                        <th style="width:15%">cpu核心数</th>
                        <th style="width:20%">内存</th>
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
                <p>
                    <label>采样表</label>${model?filter_table}
                    <label>涉及接口数</label>${urlcount}
                    <label>涉及样本数</label>${model?filter_table_lines}-${model?filter_table_error_lines}=${model?filter_table_lines-model?filter_table_error_lines}
                    <label>样本存储</label>${model?filter_store}
                </p>
            </li>
            <li>
                <label>报告时间</label>
                <p>
                    <label>开始时间</label>${Html.p(model?begin_time)}
                    <label>结束时间</label>${Html.p(model?end_time)}
                </p>
            </li>
            <#assign nodeWeiduMap={"cpu":"cpu/s","memory":"内存/s","network_read":"网络读/s","network_write":"网络写/s","active_threads":"活跃线程数/s","throughput":"吞吐量/s","error":"错误量/s"}/>
            <li>
                <label>单一维度多节点对比</label>
                <p>
                    <select id="weidu" name="weidu" onselect="loadNodesReportChart()">
                        <#list nodeWeiduMap?keys as key>
                            <option value="${key}">${nodeWeiduMap[key]}</option>
                        </#list>
                    </select>
                    <div id='nodesReport' style='width:50%;height:450px;float: left;'></div>
                </p>
            </li>
            <li>
                <label>多维度单节点对比</label>
                <p>
                    <select id="nodes" name="nodes" onselect="loadNodeReportChart()">
                        <option value="">所有节点</option>
                        <#list model?nodes?split(",") as key>
                            <option value="${key}">${key}</option>
                        </#list>
                    </select>
                 <#list nodeWeiduMap?keys as key>
                    <div id='nodeReport_${key}' style='width:20%;height:250px;float: left;'></div>
                 </#list>
                </p>
            </li>
            <li>
                <label>接口性能指标统计</label>
                <p>
                    <select id="nodes2" name="nodes2">
                        <option value="">所有节点</option>
                        <#list model?nodes?split(",") as key>
                            <option value="${key}">${key}</option>
                        </#list>
                    </select>
                    <table id="urlReport" class="mytable" width="500">
                        <tr>
                            <th style="width:15%">接口api</th>
                            <th style="width:5%">压测次数</th>
                            <th style="width:5%">最大吞吐量/s</th>
                            <th style="width:5%">最大错误数/s</th>
                            <th style="width:5%">最小耗时/s</th>
                            <th style="width:5%">最大耗时/s</th>
                            <th style="width:5%">平均耗时/s</th>
    <#--                        <th style="width:5%">99line耗时/s</th>-->
    <#--                        <th style="width:5%">98line耗时/s</th>-->
                            <th style="width:5%">最大网络读/s</th>
                            <th style="width:5%">最大网络写/s</th>
                            <th style="width:5%">操作</th>
                        </tr>
                    </table>
                    <table style="display: none" id="urltemplate">
                        <tr data="">
                            <td>{url}</td>
                            <td>{all_visit_num}</td>
                            <td>{max_throughput}</td>
                            <td>{max_error}</td>
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
                    <#assign urlWeiduMap={"throughput":"吞吐量/s","error":"错误量/s","network_read":"网络读/s","network_write":"网络写/s","visit_time":"耗时/s"}/>
                    <label id="urlChart">无</label>
                    <#list urlWeiduMap?keys as key>
                        <div id='urlChart_${key}' style='width:20%;height:250px;float: left;'></div>
                    </#list>
                </p>
            </li>
            <li>
                <label>压测结论</label>
                <p>
                    <label>性能压测耗时:${(model.begin_time.getTime()-model.end_time.getTime())/1000/60}分钟</label>
                    <label>最大承载能力:${Html.p(maxthroughput.create_time)},节点并发线程总和${maxthroughput.active_threads},节点吞吐量总和共${maxthroughput.throughput},错误总和共${maxthroughput.error}。</label>
                    <label>最佳承载能力:${Html.p(maxthroughputWithNoError.create_time)},节点并发线程总和${maxthroughputWithNoError.active_threads},节点吞吐量总和共${maxthroughputWithNoError.throughput},错误总和共${maxthroughputWithNoError.error}。</label>
                </p>
            </li>
        </ul>
    </div>

    <script type="text/javascript">
        function loadNodesReportChart(){
            $.post("/report/nodesReport",
                {
                    "weidu": $("#weidu").val(),
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        var  option = {
                            title: {
                                text: '单一维度多节点对比'
                            },
                            tooltip: {
                                trigger: 'axis'
                            },
                            legend: {
                                data: ${model?nodes?split(",")}
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
                    "node": $("#node").val(),
                    "weidu": $("#weidu").val(),
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        for(var weidu in nodeWeidus) {
                            var  option = {
                                tooltip: {
                                    trigger: 'axis',
                                    position: function (pt) {
                                        return [pt[0], '10%'];
                                    }
                                },
                                title: {
                                    left: 'center',
                                    text: '多维度单节点'
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
                                dataZoom: [
                                    {
                                        type: 'inside',
                                        start: 0,
                                        end: 20
                                    },
                                    {
                                        start: 0,
                                        end: 20
                                    }
                                ],
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
                        }
                    }
                }, "json");
        }
        function loadUrlReportChart(){
            $.post("/report/urlReport",
                {
                    "node": $("#node").val(),
                },
                function (data) {
                    $('#urlReport tr[data]').remove()
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        for(var r in data.data.report){
                            var html = $("#urltemplate").html().replaceAll("{url}","r.url")
                                .replaceAll("{all_visit_num}","r.all_visit_num").replaceAll("{max_throughput}","r.max_throughput")
                                .replaceAll("{max_error}","r.max_error").replaceAll("{min_visit_time}","r.min_visit_time")
                                .replaceAll("{max_visit_time}","r.max_visit_time").replaceAll("{avg_visit_time}","r.avg_visit_time")
                                .replaceAll("{max_network_read}","r.max_network_read").replaceAll("{max_network_write}","r.max_network_write");
                            $("#urlReport").append(html);
                        }
                        $('#urlReport tr[data]').show();
                    }
                }, "json");
        }
        function loadUrlChart(url){
            var urlWeidus = [];
            <#list urlWeiduMap?keys as key>
            urlWeidus.push("${key}");
            </#list>
            $.post("/report/urlChart",
                {
                    "node": $("#node").val(),
                    "url":url,
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        $("#urlChart").html(url);
                        for(var weidu in urlWeidus){
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
                                dataZoom: [
                                    {
                                        type: 'inside',
                                        start: 0,
                                        end: 20
                                    },
                                    {
                                        start: 0,
                                        end: 20
                                    }
                                ],
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
            loadNodesReportChart();
            loadNodeReportChart();
            loadUrlReportChart();
            //loadUrlChart();
        });
    </script>
</@layout._layout>