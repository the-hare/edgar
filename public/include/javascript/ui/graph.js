goog.provide('ui.graph');
goog.require('cljs.core');
goog.require('jayq.core');
goog.require('jayq.core');
ui.graph.add_signals = (function add_signals(initial_list,signal_map){
return cljs.core.reduce.call(null,(function (rslt,ech){
var default_entry = (function (eF){
return cljs.core.ObjMap.fromObject(["\uFDD0'type","\uFDD0'data","\uFDD0'color","\uFDD0'fillColor","\uFDD0'width","\uFDD0'style","\uFDD0'states"],{"\uFDD0'type":"flags","\uFDD0'data":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'x","\uFDD0'title","\uFDD0'text"],{"\uFDD0'x":(new cljs.core.Keyword("\uFDD0'x")).call(null,eF),"\uFDD0'title":(new cljs.core.Keyword("\uFDD0'title")).call(null,eF),"\uFDD0'text":(new cljs.core.Keyword("\uFDD0'text")).call(null,eF)})], true),"\uFDD0'color":"#5F86B3","\uFDD0'fillColor":"#5F86B3","\uFDD0'width":16,"\uFDD0'style":cljs.core.ObjMap.fromObject(["\uFDD0'color"],{"\uFDD0'color":"white"}),"\uFDD0'states":cljs.core.ObjMap.fromObject(["\uFDD0'hover"],{"\uFDD0'hover":cljs.core.ObjMap.fromObject(["\uFDD0'fillColor"],{"\uFDD0'fillColor":"#395C84"})})});
});
var G__108880 = cljs.core.first.call(null,ech);
if(cljs.core._EQ_.call(null,"default",G__108880))
{return rslt;
} else
{if(cljs.core._EQ_.call(null,"\uFDD0'obv",G__108880))
{return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.assoc.call(null,default_entry.call(null,eF),"\uFDD0'onSeries","obv-list"));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
} else
{if(cljs.core._EQ_.call(null,"\uFDD0'stochastic-oscillator",G__108880))
{return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.assoc.call(null,default_entry.call(null,eF),"\uFDD0'onSeries","k-list"));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
} else
{if(cljs.core._EQ_.call(null,"\uFDD0'macd",G__108880))
{return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.assoc.call(null,default_entry.call(null,eF),"\uFDD0'onSeries","macd-price-list"));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
} else
{if(cljs.core._EQ_.call(null,"\uFDD0'bollinger-band",G__108880))
{return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.assoc.call(null,default_entry.call(null,eF),"\uFDD0'onSeries","bollinger-list"));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
} else
{if(cljs.core._EQ_.call(null,"\uFDD0'moving-average",G__108880))
{return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.assoc.call(null,default_entry.call(null,eF),"\uFDD0'onSeries","ema-list"));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
} else
{if("\uFDD0'else")
{throw (new Error([cljs.core.str("No matching clause: "),cljs.core.str(cljs.core.first.call(null,ech))].join('')));
} else
{return null;
}
}
}
}
}
}
}
}),initial_list,cljs.core.seq.call(null,signal_map));
});
ui.graph.add_strategies = (function add_strategies(initial_list,strategy_map){
return cljs.core.reduce.call(null,(function (rslt,ech){
var default_entry = (function (eF){
return cljs.core.ObjMap.fromObject(["\uFDD0'type","\uFDD0'data","\uFDD0'color","\uFDD0'fillColor","\uFDD0'width","\uFDD0'style","\uFDD0'states"],{"\uFDD0'type":"flags","\uFDD0'data":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'x","\uFDD0'title","\uFDD0'text"],{"\uFDD0'x":(new cljs.core.Keyword("\uFDD0'x")).call(null,eF),"\uFDD0'title":(new cljs.core.Keyword("\uFDD0'title")).call(null,eF),"\uFDD0'text":(new cljs.core.Keyword("\uFDD0'text")).call(null,eF)})], true),"\uFDD0'color":"#5F86B3","\uFDD0'fillColor":"#5F86B3","\uFDD0'width":16,"\uFDD0'style":cljs.core.ObjMap.fromObject(["\uFDD0'color"],{"\uFDD0'color":"white"}),"\uFDD0'states":cljs.core.ObjMap.fromObject(["\uFDD0'hover"],{"\uFDD0'hover":cljs.core.ObjMap.fromObject(["\uFDD0'fillColor"],{"\uFDD0'fillColor":"#395C84"})})});
});
return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.assoc.call(null,default_entry.call(null,eF),"\uFDD0'onSeries","tick-list"));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
}),initial_list,cljs.core.seq.call(null,strategy_map));
});
ui.graph.build_graph_series_data = (function build_graph_series_data(dataList,signal_map,strategy_map){
var initial_list = cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'type","\uFDD0'color","\uFDD0'marker","\uFDD0'tooltip"],{"\uFDD0'name":"Bollinger Band","\uFDD0'id":"bollinger-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.first.call(null,dataList)),"\uFDD0'type":"arearange","\uFDD0'color":"#629DFF","\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Closing Price","\uFDD0'id":"ticklist","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.second.call(null,dataList)),"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Simple Moving Average","\uFDD0'id":"sma-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,2)),"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Exponential Moving Average","\uFDD0'id":"ema-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,3)),"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"MACD Price","\uFDD0'id":"macd-price-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,4)),"\uFDD0'yAxis":1,"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"MACD Signal","\uFDD0'id":"macd-signal-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,5)),"\uFDD0'yAxis":1,"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'type","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"MACD Histogram","\uFDD0'id":"macd-histogram-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,6)),"\uFDD0'yAxis":2,"\uFDD0'type":"column","\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Stochastic K","\uFDD0'id":"k-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,7)),"\uFDD0'yAxis":3,"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Stochastic D","\uFDD0'id":"d-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,8)),"\uFDD0'yAxis":3,"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'type","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"On Balance Volume","\uFDD0'id":"obv-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,9)),"\uFDD0'yAxis":4,"\uFDD0'type":"column","\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'type","\uFDD0'name","\uFDD0'data","\uFDD0'onSeries","\uFDD0'shape"],{"\uFDD0'type":"flags","\uFDD0'name":"strategies","\uFDD0'data":cljs.core.PersistentVector.EMPTY,"\uFDD0'onSeries":"ticklist","\uFDD0'shape":"squarepin"})], true);
return initial_list;
});
ui.graph.chart_fill = (function chart_fill(selector,dataList,signal_map,strategy_map){
return jayq.core.$.call(null,selector).highcharts("StockChart",cljs.core.clj__GT_js.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'names","\uFDD0'rangeSelector","\uFDD0'title","\uFDD0'chart","\uFDD0'navigator","\uFDD0'yAxis","\uFDD0'series"],{"\uFDD0'names":cljs.core.PersistentVector.fromArray([ui.graph.label,"Bolling Band","Simple Moving Average","Exponential Moving Average"], true),"\uFDD0'rangeSelector":cljs.core.ObjMap.fromObject(["\uFDD0'selected"],{"\uFDD0'selected":11}),"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":ui.graph.label}),"\uFDD0'chart":cljs.core.ObjMap.fromObject(["\uFDD0'zoomType"],{"\uFDD0'zoomType":"x"}),"\uFDD0'navigator":cljs.core.ObjMap.fromObject(["\uFDD0'adaptToUpdatedData"],{"\uFDD0'adaptToUpdatedData":true}),"\uFDD0'yAxis":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'title","\uFDD0'height"],{"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"Technical Analysis"}),"\uFDD0'height":200}),cljs.core.ObjMap.fromObject(["\uFDD0'title","\uFDD0'height","\uFDD0'top","\uFDD0'offset","\uFDD0'lineWidth"],{"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"MACD / Signal"}),"\uFDD0'height":100,"\uFDD0'top":300,"\uFDD0'offset":0,"\uFDD0'lineWidth":2}),cljs.core.ObjMap.fromObject(["\uFDD0'title","\uFDD0'height","\uFDD0'top","\uFDD0'offset","\uFDD0'lineWidth"],{"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"MACD Histog"}),"\uFDD0'height":100,"\uFDD0'top":400,"\uFDD0'offset":0,"\uFDD0'lineWidth":2}),cljs.core.ObjMap.fromObject(["\uFDD0'title","\uFDD0'height","\uFDD0'top","\uFDD0'offset","\uFDD0'lineWidth","\uFDD0'max","\uFDD0'min","\uFDD0'plotLines"],{"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"Stochastic Osc"}),"\uFDD0'height":100,"\uFDD0'top":500,"\uFDD0'offset":0,"\uFDD0'lineWidth":2,"\uFDD0'max":1,"\uFDD0'min":0,"\uFDD0'plotLines":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'value","\uFDD0'color","\uFDD0'width","\uFDD0'dashStyle","\uFDD0'label"],{"\uFDD0'value":0.75,"\uFDD0'color":"red","\uFDD0'width":1,"\uFDD0'dashStyle":"longdash","\uFDD0'label":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"Overbought"})}),cljs.core.ObjMap.fromObject(["\uFDD0'value","\uFDD0'color","\uFDD0'width","\uFDD0'dashStyle","\uFDD0'label"],{"\uFDD0'value":0.25,"\uFDD0'color":"green","\uFDD0'width":1,"\uFDD0'dashStyle":"longdash","\uFDD0'label":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"Oversold"})})], true)}),cljs.core.ObjMap.fromObject(["\uFDD0'title","\uFDD0'height","\uFDD0'top","\uFDD0'offset","\uFDD0'lineWidth"],{"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"OBV"}),"\uFDD0'height":100,"\uFDD0'top":600,"\uFDD0'offset":0,"\uFDD0'lineWidth":2})], true),"\uFDD0'series":ui.graph.build_graph_series_data.call(null,dataList,signal_map,strategy_map)})));
});
ui.graph.chart_increment = (function chart_increment(selector,dataList,strategy_map){
cljs.core.first.call(null,jayq.core.$.call(null,selector).highcharts().series).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.first.call(null,dataList))),true,false);
cljs.core.second.call(null,jayq.core.$.call(null,selector).highcharts().series).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.second.call(null,dataList))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,2).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,2))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,3).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,3))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,4).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,4))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,5).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,5))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,6).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,6))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,7).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,7))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,8).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,8))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,9).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,9))),true,false);
console.log("");
console.log([cljs.core.str("Zzz 1["),cljs.core.str(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.second.call(null,dataList)))),cljs.core.str("]")].join(''));
console.log([cljs.core.str("Zzz 2["),cljs.core.str(cljs.core.ObjMap.fromObject(["\uFDD0'x","\uFDD0'title"],{"\uFDD0'x":(new window.Date(cljs.core.first.call(null,cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.second.call(null,dataList)))))),"\uFDD0'title":"Testing 123"})),cljs.core.str("]")].join(''));
return cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,10).addPoint(cljs.core.ObjMap.fromObject(["\uFDD0'x","\uFDD0'title"],{"\uFDD0'x":(new window.Date(cljs.core.first.call(null,cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.second.call(null,dataList)))))),"\uFDD0'title":"Testing 123"}),true,false);
});
ui.graph.render_stock_graph = (function render_stock_graph(selector,dataList,signal_map,strategy_map,label,increment){
if(cljs.core.not.call(null,increment))
{return ui.graph.chart_fill.call(null,selector,dataList,signal_map,strategy_map);
} else
{return ui.graph.chart_increment.call(null,selector,dataList,strategy_map);
}
});
