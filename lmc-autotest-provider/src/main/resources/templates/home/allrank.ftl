<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","用户指标统计")}
<@layout._layout>

<div class="head">
    <div class="title draggable">
        统计列表
    </div>
</div>
    <div>
        开始时间:<input type="text" class="Wdate" id="Wdate1"  onclick="WdatePicker({skin:'whyGreen',dateFmt: 'yyyy-MM-dd'})"/>
        结束时间:<input type="text" class="Wdate" id="Wdate2"  onclick="WdatePicker({skin:'whyGreen',dateFmt: 'yyyy-MM-dd'})"/>
        <button class="search">搜索</button>
    </div>
    <div class="table">
        <table>
            <thead>
            <tr>
                <th>名称</th>
                <th data-type="productNum" data-sort="">
                    项目任务数
                    <div class="ant-table-column-sorter-inner">
                        <div
                                role="img"
                                aria-label="caret-up"
                                class="anticon anticon-caret-up ant-table-column-sorter-up"
                        >
                            <svg
                                    viewBox="0 0 1024 1024"
                                    focusable="false"
                                    data-icon="caret-up"
                                    width="1em"
                                    height="1em"
                                    fill="currentColor"
                                    aria-hidden="true"
                            >
                                <path
                                        d="M858.9 689L530.5 308.2c-9.4-10.9-27.5-10.9-37 0L165.1 689c-12.2 14.2-1.2 35 18.5 35h656.8c19.7 0 30.7-20.8 18.5-35z"
                                />
                            </svg>
                        </div>
                        <div
                                role="img"
                                aria-label="caret-down"
                                class="anticon anticon-caret-down ant-table-column-sorter-down"
                        >
                            <svg
                                    viewBox="0 0 1024 1024"
                                    focusable="false"
                                    data-icon="caret-down"
                                    width="1em"
                                    height="1em"
                                    fill="currentColor"
                                    aria-hidden="true"
                            >
                                <path
                                        d="M840.4 300H183.6c-19.7 0-30.7 20.8-18.5 35l328.4 380.8c9.4 10.9 27.5 10.9 37 0L858.9 335c12.2-14.2 1.2-35-18.5-35z"
                                />
                            </svg>
                        </div>
                    </div>
                </th>
                <th data-type="codeSubmission" data-sort="">
                    代码提交数
                    <div class="ant-table-column-sorter-inner">
                        <div
                                role="img"
                                aria-label="caret-up"
                                class="anticon anticon-caret-up ant-table-column-sorter-up"
                        >
                            <svg
                                    viewBox="0 0 1024 1024"
                                    focusable="false"
                                    data-icon="caret-up"
                                    width="1em"
                                    height="1em"
                                    fill="currentColor"
                                    aria-hidden="true"
                            >
                                <path
                                        d="M858.9 689L530.5 308.2c-9.4-10.9-27.5-10.9-37 0L165.1 689c-12.2 14.2-1.2 35 18.5 35h656.8c19.7 0 30.7-20.8 18.5-35z"
                                />
                            </svg>
                        </div>
                        <div
                                role="img"
                                aria-label="caret-down"
                                class="anticon anticon-caret-down ant-table-column-sorter-down"
                        >
                            <svg
                                    viewBox="0 0 1024 1024"
                                    focusable="false"
                                    data-icon="caret-down"
                                    width="1em"
                                    height="1em"
                                    fill="currentColor"
                                    aria-hidden="true"
                            >
                                <path
                                        d="M840.4 300H183.6c-19.7 0-30.7 20.8-18.5 35l328.4 380.8c9.4 10.9 27.5 10.9 37 0L858.9 335c12.2-14.2 1.2-35-18.5-35z"
                                />
                            </svg>
                        </div>
                    </div>
                </th>
                <th data-type="bugNum" data-sort="">
                    BUG数
                    <div
                            class="ant-table-column-sorter-inner"
                            style="right: 60px"
                    >
                        <div
                                role="img"
                                aria-label="caret-up"
                                class="anticon anticon-caret-up ant-table-column-sorter-up"
                        >
                            <svg
                                    viewBox="0 0 1024 1024"
                                    focusable="false"
                                    data-icon="caret-up"
                                    width="1em"
                                    height="1em"
                                    fill="currentColor"
                                    aria-hidden="true"
                            >
                                <path
                                        d="M858.9 689L530.5 308.2c-9.4-10.9-27.5-10.9-37 0L165.1 689c-12.2 14.2-1.2 35 18.5 35h656.8c19.7 0 30.7-20.8 18.5-35z"
                                />
                            </svg>
                        </div>
                        <div
                                role="img"
                                aria-label="caret-down"
                                class="anticon anticon-caret-down ant-table-column-sorter-down"
                        >
                            <svg
                                    viewBox="0 0 1024 1024"
                                    focusable="false"
                                    data-icon="caret-down"
                                    width="1em"
                                    height="1em"
                                    fill="currentColor"
                                    aria-hidden="true"
                            >
                                <path
                                        d="M840.4 300H183.6c-19.7 0-30.7 20.8-18.5 35l328.4 380.8c9.4 10.9 27.5 10.9 37 0L858.9 335c12.2-14.2 1.2-35-18.5-35z"
                                />
                            </svg>
                        </div>
                    </div>
                </th>
                <th data-type="findBugNum" data-sort="">
                    发现BUG数
                    <div class="ant-table-column-sorter-inner">
                        <div
                                role="img"
                                aria-label="caret-up"
                                class="anticon anticon-caret-up ant-table-column-sorter-up"
                        >
                            <svg
                                    viewBox="0 0 1024 1024"
                                    focusable="false"
                                    data-icon="caret-up"
                                    width="1em"
                                    height="1em"
                                    fill="currentColor"
                                    aria-hidden="true"
                            >
                                <path
                                        d="M858.9 689L530.5 308.2c-9.4-10.9-27.5-10.9-37 0L165.1 689c-12.2 14.2-1.2 35 18.5 35h656.8c19.7 0 30.7-20.8 18.5-35z"
                                />
                            </svg>
                        </div>
                        <div
                                role="img"
                                aria-label="caret-down"
                                class="anticon anticon-caret-down ant-table-column-sorter-down"
                        >
                            <svg
                                    viewBox="0 0 1024 1024"
                                    focusable="false"
                                    data-icon="caret-down"
                                    width="1em"
                                    height="1em"
                                    fill="currentColor"
                                    aria-hidden="true"
                            >
                                <path
                                        d="M840.4 300H183.6c-19.7 0-30.7 20.8-18.5 35l328.4 380.8c9.4 10.9 27.5 10.9 37 0L858.9 335c12.2-14.2 1.2-35-18.5-35z"
                                />
                            </svg>
                        </div>
                    </div>
                </th>
            </tr>
            </thead>
            <tbody class="resultList">
            <tr>
                <td>芦丁</td>
                <td>2</td>
                <td>1888</td>
                <td>2</td>
                <td>2</td>
            </tr>
            </tbody>
        </table>
    </div>
    <script src="/content/form/lkform.js"></script>
    <script>
        $(function() {
            $(".search").click(function(){
                sortFun();
            })
            Date.prototype.format = function (format) {
                var o = {
                    "M+": this.getMonth() + 1, // month
                    "d+": this.getDate(), // day
                    "h+": this.getHours(), // hour
                    "m+": this.getMinutes(), // minute
                    "s+": this.getSeconds(), // second
                    "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
                    S: this.getMilliseconds(),
                    // millisecond
                };
                if (/(y+)/.test(format))
                    format = format.replace(
                        RegExp.$1,
                        (this.getFullYear() + "").substr(4 - RegExp.$1.length)
                    );
                for (var k in o)
                    if (new RegExp("(" + k + ")").test(format))
                        format = format.replace(
                            RegExp.$1,
                            RegExp.$1.length == 1
                                ? o[k]
                                : ("00" + o[k]).substr(("" + o[k]).length)
                        );
                return format;
            };
            var begin = new Date();
            var end = new Date();
            new Date(begin.setMonth(new Date().getMonth() - 1));
            var begintime = begin.format("yyyy-MM-dd");
            var endtime = end.format("yyyy-MM-dd");
            $("#Wdate1").val(begintime);
            $("#Wdate2").val(endtime);

            var searchList = []; // 搜索字段
            function sortFun() {
                $.ajax({
                    url: "/getAllUserByOrder",
                    type: "post",
                    contentType: "application/json",
                    data: JSON.stringify({paramSortList:searchList,beginTime:$("#Wdate1").val(),endTime:$("#Wdate2").val()}),
                    success: function (res) {
                        if (res.code !== 200) return;
                        $(".resultList").empty();
                        for (let i = 0; i < res.data.length; i++) {
                            var text = $(
                                "<tr><td>" +
                                res.data[i].show_name +
                                "</td><td>" +
                                res.data[i].productNum +
                                "</td><td>" +
                                res.data[i].codeSubmission +
                                "</td><td>" +
                                res.data[i].bugNum +
                                "</td><td>" +
                                res.data[i].findBugNum +
                                "</td></tr>"
                            );
                            $(".resultList").append(text);
                        }
                    },
                    error: function (err) {
                        console.error(err);
                    },
                });
            }
            sortFun();


            $("th:not(:first-child)").click(function () {
                var _this = this;
                var sort = $(this).attr("data-sort");
                var sortType = sort === "" ? "asc" : sort === "asc" ? "desc" : "";
                if (sortType === "desc") {
                    $(this).find(".anticon-caret-down").css({ color: "#1890ff" });
                    $(this).find(".anticon-caret-up").css({ color: "#bfbfbf" });
                } else if (sortType === "asc") {
                    $(this).find(".anticon-caret-down").css({ color: "#bfbfbf" });
                    $(this).find(".anticon-caret-up").css({ color: "#1890ff" });
                } else {
                    $(this).find(".anticon-caret-down").css({ color: "#bfbfbf" });
                    $(this).find(".anticon-caret-up").css({ color: "#bfbfbf" });
                }
                $(this).attr("data-sort", sortType);

                var name = $(this).attr("data-type");
                var newList = [...searchList];
                if (newList.length) {
                    newList.forEach(function (item, i) {
                        if (item.sortValue === name) {
                            searchList.splice(i, 1);
                        }
                    });
                    var flag = searchList.some(function (item) {
                        return item.sortValue === name;
                    });
                    if (!flag) {
                        searchList.push({ sort: sortType, sortValue: name });
                    }
                } else {
                    searchList.push({ sort: sortType, sortValue: name });
                }

                searchList = searchList.filter((item) => item.sort !== "");
                sortFun();
            });
        });


    </script>
</@layout._layout>