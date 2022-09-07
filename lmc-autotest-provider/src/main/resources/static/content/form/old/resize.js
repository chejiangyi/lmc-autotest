function bindResize(el){
    //初始化参数
    var els = el.style,
        //鼠标的 X 和 Y 轴坐标
        x = y = 0;
    //邪恶的食指
    $(el).mousedown(function(e){
        //按下元素后，计算当前鼠标与对象计算后的坐标
        x = e.clientX - el.offsetWidth,
            y = e.clientY - el.offsetHeight;
        //在支持 setCapture 做些东东
        el.setCapture ? (
            //捕捉焦点
            el.setCapture(),
                //设置事件
                el.onmousemove = function(ev){
                    mouseMove(ev || event)
                },
                el.onmouseup = mouseUp
        ) : (
            //绑定事件
            $(document).bind("mousemove",mouseMove).bind("mouseup",mouseUp)
        )
        //防止默认事件发生
        e.preventDefault()
    });
    //移动事件
    function mouseMove(e){
        //宇宙超级无敌运算中...
        els.width = e.clientX - x + 'px',
            els.height = e.clientY - y + 'px'
    }
    //停止事件
    function mouseUp(){
        //在支持 releaseCapture 做些东东
        el.releaseCapture ? (
            //释放焦点
            el.releaseCapture(),
                //移除事件
                el.onmousemove = el.onmouseup = null
        ) : (
            //卸载事件
            $(document).unbind("mousemove", mouseMove).unbind("mouseup", mouseUp)
        )
    }
}