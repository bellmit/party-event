var myChart = echarts.init(document.getElementById('main3'));
                 
                option={
        title : {
            text: '2014全市空气质量',
            subtext: '数据来模拟',
            x:'right',
            y: 'top'
        },
        tooltip : {
            trigger: 'item'
        },
        dataRange: {
            min: 10,
            max: 200,
            color:['red','yellow'],
            text:['高','低'],           // 文本，默认为数值文本
            calculable : true,
            x: 'right',
            y: 'bottom',
            textStyle: {
                color: 'orange'
            }
        },
        grid:{
            x: 50,
            x2: 200,
            y2: 10,
            borderWidth:0
        },
        xAxis : [
            {
                type : 'value',
                position: 'top',
                name: 'pm2.5',
                splitLine: {show:false},
                boundaryGap : [0, 0.01]
            }
        ],
        yAxis : [
            {
                type : 'category',
                splitLine: {show:false},
                axisLine: {show:false},
                axisLabel : {show:false},
                data : ['北碚区','渝北区','江北区','巴南区','南岸区','渝中区','九龙坡区','大渡口区','沙坪坝区']
            }
        ],
        series : [
            {
                type: 'bar',
                itemStyle : {
                    normal : {
                        color : (function (){
                            var zrColor = zrender.tool.color;
                            return zrColor.getLinearGradient(
                                0, 80, 0, 700,
                                [[0, 'purple'],[0.5, 'orangered'],[1, 'orange']]
                            )
                        })(),
                        label : {
                            show : true,
                            position: 'right',
                            formatter:'{b} : {c}'
                        }
                    }
                },
                data:[
                    {name:'北碚区', value:50},
                    {name:'渝北区', value:90},
                    {name:'江北区', value:100},
                    {name:'巴南区', value:110},
                    {name:'南岸区', value:120},
                    {name:'渝中区', value:130},
                    {name:'九龙坡区', value:140},
                    {name:'大渡口区', value:99},
                    {name:'沙坪坝区', value:88}
                ]
            },
            {
                name: '2014全市空气质量',
                type: 'map',
                mapType: '重庆',
                itemSdtyle:{
                    normal:{label:{show:true}}
                },
                mapLocation: {
                    x: 'right',
                    y: 80
                },
                data:[
                    {name:'北碚区', value:50},
                    {name:'渝北区', value:90},
                    {name:'江北区', value:100},
                    {name:'巴南区', value:110},
                    {name:'南岸区', value:120},
                    {name:'渝中区', value:130},
                    {name:'九龙坡区', value:140},
                    {name:'大渡口区', value:99},
                    {name:'沙坪坝区', value:88}
                ]
            }
        ]
    };
        
                // 为echarts对象加载数据 
                myChart.setOption(option); 
                    