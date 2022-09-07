<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>排行榜</title>
    <script src="/scripts/jquery-1.8.2.min.js" type="text/javascript"></script>
    <script src="/scripts/jquery-ui-1.8.24.js" type="text/javascript"></script>
    <link href="/content/css/rank.css" rel="stylesheet"/>
</head>
<body>
<#--<iframe allow="autoplay" style="display:none" src="/mp3"></iframe>-->
<audio src="/mp3"  autoplay="autoplay" controls="controls" preload="auto"></audio>
<div class="music img_ar_paused">
    <img
            class="music_bg"
            src="//p2.music.126.net/aG5zqxkBRfLiV7A8W0iwgA==/109951166702962263.jpg?param=90y90"
    />
    <img class="pause" src="/content/images/pause.svg" alt="" />
    <img class="play" src="/content/images/play.svg" alt="" />
</div>
<div class="ranking-content">
    <#if loginUser??>
        <!-- 登录状态 -->
        <div class="flex input-item-right">
            <div class="input-container">
                <div
                        class="select-selector"
                        id="channel"
                        data-active="false"
                        data-select-value="">
                    <div class="default-name">
                        <img class="avator" src="/content/images/avator.svg" alt="" />
                        <span class="selector-title">${loginUser.username}</span>
                    </div>
                    <span class="selector-icon">
              <svg
                      viewBox="64 64 896 896"
                      focusable="false"
                      data-icon="down"
                      width="1em"
                      height="1em"
                      fill="#fff"
                      aria-hidden="true"
              >
                <path
                        d="M884 256h-75c-5.1 0-9.9 2.5-12.9 6.6L512 654.2 227.9 262.6c-3-4.1-7.8-6.6-12.9-6.6h-75c-6.5 0-10.3 7.4-6.5 12.7l352.6 486.1c12.8 17.6 39 17.6 51.7 0l352.6-486.1c3.9-5.3.1-12.7-6.4-12.7z"
                ></path>
              </svg>
            </span>
                    <ul class="selector-option-list">
                        <li class="selector-my" data-id="">个人指标</li>
                        <li class="selector-option">退出登录</li>
                    </ul>
                </div>
            </div>
        </div>
        <#if loginUser.admin == 1>
            <a href="/allrank" class="adminPage">管理员界面</a>
        </#if>
    <#else>
        <a href="/login.ftl" class="login">登录</a>

    </#if>
    <div class="ranking-bg"></div>
    <!-- 排行榜内容 -->
    <ul class="ranking-item">
        <li>
            <div class="ranking-left-img">
                <img src="/content/images/image-1.png" />
            </div>
            <div>
                <div class="ranking-title">测试任务排行榜</div>
                <div class="ranking-table">
                    <div class="ranking-header">
                        <div>排名</div>
                        <div>江湖人称</div>
                        <div>发现BUG数</div>
                    </div>
                     <#if testTaskRank??&&(testTaskRank?size>0)>
                          <#list testTaskRank as testTask>
                              <#if testTask_index < 3>
                                        <div class="ranking-table-item"  data-id="${testTask.id}">
                                            <div class="table-img">
                                                <img src="/content/images/top${testTask_index+1}.png" />
                                            </div>
                                            <div class="table-name">${testTask.show_name}</div>
                                            <div class="table-bug-num">${testTask.num}</div>
                                        </div>


                              </#if>
                         </#list>
                    </#if>
                </div>
            </div>
            <div class="ranking-angle-mark">神 捕</div>
        </li>
        <li>
            <div class="ranking-left-img">
                <img src="/content/images/image-2.png" />
            </div>
            <div>
                <div class="ranking-title">项目任务排行榜</div>
                <div class="ranking-table">
                    <div class="ranking-header">
                        <div>排名</div>
                        <div>江湖人称</div>
                        <div>项目任务数</div>
                    </div>
                    <#if projectTaskRank??&&(projectTaskRank?size>0)>
                        <#list projectTaskRank as projectTask>
                          <#if projectTask_index < 3>
                            <div class="ranking-table-item"  data-id="${projectTask.id}">
                                <div class="table-img">
                                    <img src="/content/images/top${projectTask_index+1}.png" />
                                </div>
                                <div class="table-name">${projectTask.show_name}</div>
                                <div class="table-bug-num">${projectTask.num}</div>
                            </div>
                          </#if>
                        </#list>
                    </#if>
                </div>
            </div>
            <div class="ranking-angle-mark">战 神</div>
        </li>
        <li>
            <div class="ranking-left-img">
                <img src="/content/images/image-3.png" />
            </div>
            <div>
                <div class="ranking-title">代码提交排行榜</div>
                <div class="ranking-table">
                    <div class="ranking-header">
                        <div>排名</div>
                        <div>江湖人称</div>
                        <div>代码提交行数</div>
                    </div>
                    <#if codeCommitRank??&&(codeCommitRank?size>0)>
                        <#list codeCommitRank as codeCommit>
                            <#if codeCommit_index < 3>
                            <div class="ranking-table-item"  data-id="${codeCommit.id}">
                                <div class="table-img">
                                    <img src="/content/images/top${codeCommit_index+1}.png" />
                                </div>
                                <div class="table-name">${codeCommit.show_name}</div>
                                <div class="table-bug-num">${codeCommit.num}</div>
                            </div>
                            </#if>
                        </#list>
                    </#if>
                </div>
            </div>
            <div class="ranking-angle-mark">码 神</div>
        </li>
        <li>
            <div class="ranking-left-img">
                <img src="/content/images/image-4.png" />
            </div>
            <div>
                <div class="ranking-title">BUG数排行榜</div>
                <div class="ranking-table">
                    <div class="ranking-header">
                        <div>排名</div>
                        <div>江湖人称</div>
                        <div>BUG数</div>
                    </div>
                    <#if bugCountRank??&&(bugCountRank?size>0)>
                        <#list bugCountRank as bugCount>
                           <#if bugCount_index < 3>
                            <div class="ranking-table-item" data-id="${bugCount.id}">
                                <div class="table-img">
                                    <img src="/content/images/top${bugCount_index+1}.png" />
                                </div>
                                <div class="table-name">${bugCount.show_name}</div>
                                <div class="table-bug-num">${bugCount.num}</div>
                            </div>
                           </#if>
                        </#list>
                    </#if>
                </div>
            </div>
        </li>
    </ul>
</div>

<!-- 个人指标弹框 -->
<div class="modal-mask">
    <div class="ranking-modal-mask"></div>
    <div class="modal-content">
        <div class="modal-header">
            <span>个人指标</span>
            <img class="modal-close" src="/content/images/close.svg" alt="关闭" />
        </div>
        <ul class="modal-item">
            <li>
                <img src="/content/images/productNum.png" alt="" />
                <span>项目任务数</span>
                <div class="productNum">0</div>
            </li>
            <li>
                <img src="/content/images/codeSubmission.png" alt="" />
                <span>代码提交数</span>
                <div class="codeSubmission">0</div>
            </li>
            <li>
                <img src="/content/images/bugNum.png" alt="" />
                <span>BUG数</span>
                <div class="bugNum">0</div>
            </li>
            <li>
                <img src="/content/images/viewBugNum.png" alt="" />
                <span>发现BUG数</span>
                <div class="viewBugNum">0</div>
            </li>
        </ul>
    </div>
</div>

</body>

<script>
    // 下拉选择框
    $(function () {
        // 个人指标弹框
        function getUser(id){
            $("body").addClass("body-mask");
            $(".modal-mask").show();
            $.ajax({
                url: "/getUserNumById",
                type: "post",
                contentType: "application/json",
                data: JSON.stringify({id:id}),
                headers: {"token":sessionStorage.getItem("token")},
                success: function (res) {
                    if(res.code === 200) {
                        $(".productNum").text(res.data.productNum)
                        $(".codeSubmission").text(res.data.codeSubmission)
                        $(".bugNum").text(res.data.bugNum)
                        $(".viewBugNum").text(res.data.viewBugNum)
                    }
                },
                error: function (err) {
                    console.error(err);
                },
            });
        }

        // 个人指标弹框
        function getCurrentUser(){
            $("body").addClass("body-mask");
            $(".modal-mask").show();
            $.ajax({
                url: "/getCurrentUserById",
                type: "post",
                contentType: "application/json",
                data: {},
                headers: {"token":sessionStorage.getItem("token")},
                success: function (res) {
                    if(res.code === 200) {
                        $(".productNum").text(res.data.productNum)
                        $(".codeSubmission").text(res.data.codeSubmission)
                        $(".bugNum").text(res.data.bugNum)
                        $(".viewBugNum").text(res.data.viewBugNum)
                    }
                },
                error: function (err) {
                    console.error(err);
                },
            });
        }

        // $(document).on("click", ".ranking-table-item", function (e) {
        //    getUser($(this).attr("data-id"))
        // });

        // 个人
        $(document).on("click", ".selector-my", function (e) {
            getCurrentUser()
        });
        function modalHide() {
            $(".modal-mask").hide();
            $(".modal-mask-all").hide();
            $("body").removeClass("body-mask");
        }

        $(".ranking-modal-mask").click(function () {
            modalHide();
        });

        $(".modal-close").click(function () {
            modalHide();
        });

        //设置状态
        function setSelectorStatus(target, isActive) {
            if (isActive === "false") {
                isActive = "true";
                $(target).addClass("active");
            } else {
                isActive = "false";
                $(target).removeClass("active");
            }
            $(target).attr("data-active", isActive);
        }

        //关闭所有下拉框
        function closeAllSelector() {
            $(".select-selector").each(function () {
                if ($(this).attr("data-active") === "true") {
                    setSelectorStatus(this, "true");
                }
            });
        }

        //点击自己关闭下拉框
        $(document).on("click", ".select-selector", function (e) {
            var _this = this;
            $(".select-selector").each(function () {
                if (_this !== this) {
                    setSelectorStatus(this, "true");
                }
            });

            var isActive = $(this).attr("data-active");
            setSelectorStatus(this, isActive);
            e.stopPropagation();
            return false;
        });

        //点击其他地方 关闭下拉框
        $(document).click(function () {
            closeAllSelector();
        });

        // 退出登录
        $(".selector-option").click(function () {
            $.ajax({
                url: "/login/loginOut",
                type: "post",
                contentType: "application/json",
                data: null,
                headers: {"token":sessionStorage.getItem("token")},
                success: function (res) {
                    console.log(res);
                    if (res.code === 200) {
                        sessionStorage.removeItem("token");
                        window.location.reload();
                    }
                },
                error: function (err) {
                    console.error(err);
                },
            });
        });
        var player = false;
        $(document).click(function () {
            player = !player;
            if (player) {
                $("audio")[0].play();
                $(".pause").show();
                $(".play").hide();
                $(".music").removeClass("img_ar_paused");
            } else {
                $("audio")[0].pause();
                $(".pause").hide();
                $(".play").show();
                $(".music").addClass("img_ar_paused");
            }
        });

    });
</script>
</html>
