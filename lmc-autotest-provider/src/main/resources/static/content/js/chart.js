//$(function () {
    Highcharts.setOptions({
        lang: {
            shortMonths:['0','1', '2', '3', '4', '5', '6',  '7', 
    '8', '9', '10', '11', '12']
        }
    });
    var plotOptions1={
        area: {
            fillColor: {
                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                stops: [
                    [0, Highcharts.getOptions().colors[0]],
                    [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                ]
            },
            lineWidth: 1,
            marker: {
                enabled: false
            },
            shadow: false,
            states: {
                hover: {
                    lineWidth: 1
                }
            },
            threshold: null
        }
    };
    //alert(1);
//});	
