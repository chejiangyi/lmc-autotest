<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","demo示范")}
<@layout._layout>
    <style>
        #sortable { list-style-type: none; margin: 0; padding: 0; width: 60%; }
        #sortable li { margin: 0 3px 3px 3px; padding: 0.4em; padding-left: 1.5em; font-size: 1.4em; height: 18px; }
        #sortable li span { position: absolute; margin-left: -1.3em; }
    </style>
<#--    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>-->
<#--    <script src="https://code.jquery.com/ui/1.13.1/jquery-ui.js"></script>-->
<#--    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.7.1/jquery.contextMenu.min.css">-->
<#--    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.7.1/jquery.contextMenu.min.js"></script>-->
<#--    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.7.1/jquery.ui.position.js"></script>-->
    <script>
        $( function() {
            $( "#sortable" ).sortable({
                stop: function( event, ui ) {
                    let order = ui.item.css('order');
                    console.log(ui.item);
                    //console.log(ui.sender);
                    console.log(order);
                    ui.item.parent().children().each(function (i){
                        $(this).css('order',(i+1)+"");
                        console.log("index:"+(i+1)+" "+$(this).text()+" "+$(this).css('order'));
                    });
                }});
            $("#sortable li").resizable();
            $("#sortable li").contextmenu();
            // style="flex: 0 0 100%"

            // $( "#sortable2" ).sortable();
            // $("#sortable2 div").resizable();
            //初始化菜单

            $.contextMenu({
                selector: '#sortable li',
                callback: function(key, options) {
                    console.log(key);
                    console.log(options);
                    console.log($(this).attr("key"));
                    console.log("点击了：" + key);
                },
                items: {
                    "edit": {name: "编辑", icon: "edit"},
                    "cut": {name: "剪切", icon: "cut"}
                }

            });
        } );
    </script>
    <div class="head">
        <div class="title draggable">
            排序
        </div>
    </div>
    <div>
        盒子布局一
        <ul id="sortable" style="display: flex; flex-wrap: wrap;">
            <li key="1" class="ui-state-default" style="order:1"><span class="ui-icon ui-icon-arrowthick-2-n-s" ></span>Item 1</li>
            <li key="2" class="ui-state-default" style="order:2;width: 100px;"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 2</li>
            <li key="3" class="ui-state-default" style="order:3;width: 300px;"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 3</li>
            <li class="ui-state-default" style="order:4;width: 500px;display: flex;flex-wrap: wrap;justify-content:center"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span><span>Item 4</span></li>
            <li class="ui-state-default" style="order:5;"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 5</li>
            <li class="ui-state-default" style="order:6;"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 6</li>
            <li class="ui-state-default" style="order:7;"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span>Item 7</li>
        </ul>
    </div>
<#--    <div>-->
<#--        盒子布局二-->
<#--        <div id="sortable2" style="display: flex; flex-wrap: wrap;">-->
<#--            <div>Item 1</div>-->
<#--            <div style="width: 100px">Item 2</div>-->
<#--            <div style="width: 300px">Item 3</div>-->
<#--            <div style="width: 500px">Item 4</div>-->
<#--            <div>Item 5</div>-->
<#--            <div>Item 6</div>-->
<#--            <div>Item 7</div>-->
<#--        </div>-->
<#--    </div>-->
</@layout._layout>