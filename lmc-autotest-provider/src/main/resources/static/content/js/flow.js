var paint = go.GraphObject.make;
var myDiagram = paint(go.Diagram, "myDiagramDiv",
    {
        initialContentAlignment: go.Spot.TopCenter, // 居中显示内容
        "undoManager.isEnabled": false // 打开 Ctrl-Z 和 Ctrl-Y 撤销重做功能
    });

// 我们早先定义的节点模板
//myDiagram.nodeTemplate =
var mantemplate=  paint(go.Node, "Horizontal",
    { background: "#44CCFF" },
    new go.Binding('position', 'loc', go.Point.parse),
    paint(go.TextBlock, "",
        { margin: 12, stroke: "white", font: "bold 16px sans-serif" },
        new go.Binding("text", "name")));
var starttemplate=  paint(go.Node, "Horizontal",
    { background: "#33CC66" },
    new go.Binding('position', 'loc', go.Point.parse),
    paint(go.TextBlock, "",
        { margin: 12, stroke: "#000000", font: "bold 16px sans-serif" },
        new go.Binding("text", "name")));
var autotemplate=  paint(go.Node, "Horizontal",
    { background: "#FFCC00" },
    new go.Binding('position', 'loc', go.Point.parse),
    paint(go.TextBlock, "",
        { margin: 12, stroke: "#000000", font: "bold 16px sans-serif" },
        new go.Binding("text", "name")));
myDiagram.nodeTemplateMap.add('', mantemplate);
myDiagram.nodeTemplateMap.add('starttemplate', starttemplate);
myDiagram.nodeTemplateMap.add('autotemplate', autotemplate);

myDiagram.linkTemplate =
    paint(go.Link,
        // 默认的路由 go.Link.Normal
        // 默认角度值 0
        { routing: go.Link.Normal, corner: 5 },
        paint(go.Shape, { strokeWidth: 2, stroke: "#555" }), // 线的宽度和笔画的颜色

        // 如果我们要显示箭头，就应该定义一个有箭头的形状
        paint(go.Shape, { toArrow: "Standard", stroke: null }),
        paint(go.TextBlock, new go.Binding('text', 'text'))
    );