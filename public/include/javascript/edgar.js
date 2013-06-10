goog.provide('edgar');
goog.require('cljs.core');
goog.require('jayq.core');
goog.require('cljs.reader');
goog.require('jayq.core');
goog.require('jayq.core');
edgar.build_graph_series_data = (function build_graph_series_data(dataList,signal_map){
var initial_list = cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'data","\uFDD0'type","\uFDD0'color","\uFDD0'marker","\uFDD0'tooltip"],{"\uFDD0'name":"Bollinger Band","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.first.call(null,dataList)),"\uFDD0'type":"arearange","\uFDD0'color":"#629DFF","\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":edgar.label,"\uFDD0'id":"tick-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.second.call(null,dataList)),"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Simple Moving Average","\uFDD0'id":"sma-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,2)),"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Exponential Moving Average","\uFDD0'id":"ema-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,3)),"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"MACD Price","\uFDD0'id":"macd-price-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,4)),"\uFDD0'yAxis":1,"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"MACD Signal","\uFDD0'id":"macd-signal-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,5)),"\uFDD0'yAxis":1,"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'type","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"MACD Histogram","\uFDD0'id":"macd-histogram-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,6)),"\uFDD0'yAxis":2,"\uFDD0'type":"column","\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Stochastic K","\uFDD0'id":"k-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,7)),"\uFDD0'yAxis":3,"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Stochastic D","\uFDD0'id":"d-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,8)),"\uFDD0'yAxis":3,"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'id","\uFDD0'data","\uFDD0'yAxis","\uFDD0'type","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"On Balance Volume","\uFDD0'id":"obv-list","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,9)),"\uFDD0'yAxis":4,"\uFDD0'type":"column","\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})})], true);
var with_signals = cljs.core.reduce.call(null,(function (rslt,ech){
if(cljs.core._EQ_.call(null,"\uFDD0'moving-average",cljs.core.first.call(null,ech)))
{return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.ObjMap.fromObject(["\uFDD0'type","\uFDD0'data","\uFDD0'color","\uFDD0'fillColor","\uFDD0'onSeries","\uFDD0'width","\uFDD0'style","\uFDD0'states"],{"\uFDD0'type":"flags","\uFDD0'data":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'x","\uFDD0'title","\uFDD0'text"],{"\uFDD0'x":(new cljs.core.Keyword("\uFDD0'x")).call(null,eF),"\uFDD0'title":(new cljs.core.Keyword("\uFDD0'title")).call(null,eF),"\uFDD0'text":(new cljs.core.Keyword("\uFDD0'text")).call(null,eF)})], true),"\uFDD0'color":"#5F86B3","\uFDD0'fillColor":"#5F86B3","\uFDD0'onSeries":"ema-list","\uFDD0'width":16,"\uFDD0'style":cljs.core.ObjMap.fromObject(["\uFDD0'color"],{"\uFDD0'color":"white"}),"\uFDD0'states":cljs.core.ObjMap.fromObject(["\uFDD0'hover"],{"\uFDD0'hover":cljs.core.ObjMap.fromObject(["\uFDD0'fillColor"],{"\uFDD0'fillColor":"#395C84"})})}));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
} else
{return null;
}
}),initial_list,cljs.core.seq.call(null,signal_map));
return with_signals;
});
edgar.render_stock_graph = (function render_stock_graph(selector,dataList,signal_map,label,increment){
if(cljs.core.not.call(null,increment))
{return jayq.core.$.call(null,selector).highcharts("StockChart",cljs.core.clj__GT_js.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'names","\uFDD0'rangeSelector","\uFDD0'title","\uFDD0'chart","\uFDD0'navigator","\uFDD0'yAxis","\uFDD0'series"],{"\uFDD0'names":cljs.core.PersistentVector.fromArray([label,"Bolling Band","Simple Moving Average","Exponential Moving Average"], true),"\uFDD0'rangeSelector":cljs.core.ObjMap.fromObject(["\uFDD0'selected"],{"\uFDD0'selected":7}),"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":label}),"\uFDD0'chart":cljs.core.ObjMap.fromObject(["\uFDD0'zoomType"],{"\uFDD0'zoomType":"x"}),"\uFDD0'navigator":cljs.core.ObjMap.fromObject(["\uFDD0'adaptToUpdatedData"],{"\uFDD0'adaptToUpdatedData":true}),"\uFDD0'yAxis":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'title","\uFDD0'height"],{"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"Technical Analysis"}),"\uFDD0'height":200}),cljs.core.ObjMap.fromObject(["\uFDD0'title","\uFDD0'height","\uFDD0'top","\uFDD0'offset","\uFDD0'lineWidth"],{"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"MACD / Signal"}),"\uFDD0'height":100,"\uFDD0'top":300,"\uFDD0'offset":0,"\uFDD0'lineWidth":2}),cljs.core.ObjMap.fromObject(["\uFDD0'title","\uFDD0'height","\uFDD0'top","\uFDD0'offset","\uFDD0'lineWidth"],{"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"MACD Histog"}),"\uFDD0'height":100,"\uFDD0'top":400,"\uFDD0'offset":0,"\uFDD0'lineWidth":2}),cljs.core.ObjMap.fromObject(["\uFDD0'title","\uFDD0'height","\uFDD0'top","\uFDD0'offset","\uFDD0'lineWidth","\uFDD0'max","\uFDD0'min","\uFDD0'plotLines"],{"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"Stochastic Osc"}),"\uFDD0'height":100,"\uFDD0'top":500,"\uFDD0'offset":0,"\uFDD0'lineWidth":2,"\uFDD0'max":1,"\uFDD0'min":0,"\uFDD0'plotLines":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'value","\uFDD0'color","\uFDD0'width","\uFDD0'dashStyle","\uFDD0'label"],{"\uFDD0'value":0.75,"\uFDD0'color":"red","\uFDD0'width":1,"\uFDD0'dashStyle":"longdash","\uFDD0'label":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"Overbought"})}),cljs.core.ObjMap.fromObject(["\uFDD0'value","\uFDD0'color","\uFDD0'width","\uFDD0'dashStyle","\uFDD0'label"],{"\uFDD0'value":0.25,"\uFDD0'color":"green","\uFDD0'width":1,"\uFDD0'dashStyle":"longdash","\uFDD0'label":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"Oversold"})})], true)}),cljs.core.ObjMap.fromObject(["\uFDD0'title","\uFDD0'height","\uFDD0'top","\uFDD0'offset","\uFDD0'lineWidth"],{"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":"OBV"}),"\uFDD0'height":100,"\uFDD0'top":600,"\uFDD0'offset":0,"\uFDD0'lineWidth":2})], true),"\uFDD0'series":edgar.build_graph_series_data.call(null,dataList,signal_map)})));
} else
{cljs.core.first.call(null,jayq.core.$.call(null,selector).highcharts().series).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.first.call(null,dataList))),true,false);
cljs.core.second.call(null,jayq.core.$.call(null,selector).highcharts().series).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.second.call(null,dataList))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,2).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,2))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,3).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,3))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,4).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,4))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,5).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,5))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,6).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,6))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,7).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,7))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,8).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,8))),true,false);
return cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,9).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,9))),true,false);
}
});
edgar.populate_multiselect = (function populate_multiselect(selector,options){
var G__59610 = (new cljs.core.Keyword("\uFDD0'bind")).call(null,jayq.core.deferred_m);
var G__59611 = (new cljs.core.Keyword("\uFDD0'return")).call(null,jayq.core.deferred_m);
var G__59612 = (new cljs.core.Keyword("\uFDD0'zero")).call(null,jayq.core.deferred_m);
return G__59610.call(null,$.ajax.call(null,"/list-filtered-input"),(function (filtered_input){
return G__59611.call(null,(function (){var multiselect = jayq.core.$.call(null,selector);
cljs.core.reduce.call(null,(function (rslt,inp){
var option_value = cljs.core.second.call(null,inp);
var option_label = cljs.core.nth.call(null,inp,2);
var price_difference = cljs.core.first.call(null,inp).toFixed(2);
return multiselect.append([cljs.core.str("<option value='"),cljs.core.str(option_value),cljs.core.str("'>"),cljs.core.str(option_label),cljs.core.str(" ("),cljs.core.str(price_difference),cljs.core.str(")</option>")].join(''));
}),null,cljs.core.into_array.call(null,cljs.reader.read_string.call(null,filtered_input)));
return jayq.core.$.call(null,selector).multiselect(cljs.core.clj__GT_js.call(null,cljs.core.merge.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'enableFiltering"],{"\uFDD0'enableFiltering":true}),options)));
})());
}));
});
edgar.parse_result_data = (function parse_result_data(result_data){
return cljs.core.ObjMap.fromObject(["\uFDD0'local-list","\uFDD0'macd-price-list","\uFDD0'stock-name","\uFDD0'sma-list","\uFDD0'macd-histogram-list","\uFDD0'ema-list","\uFDD0'stochastic-d","\uFDD0'bollinger-band","\uFDD0'obv","\uFDD0'stochastic-k","\uFDD0'macd-signal-list","\uFDD0'signals"],{"\uFDD0'local-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt(cljs.core.first.call(null,ech)),window.parseFloat(cljs.core.second.call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.into_array.call(null,(new cljs.core.Keyword("\uFDD0'stock-list")).call(null,result_data)))),"\uFDD0'macd-price-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'last-trade-macd")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'macd")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'stock-name":(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,result_data),"\uFDD0'sma-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt(cljs.core.first.call(null,ech)),window.parseFloat(cljs.core.second.call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,(function (p1__59604_SHARP_){
return (cljs.core.first.call(null,p1__59604_SHARP_) == null);
}),cljs.core.into_array.call(null,(new cljs.core.Keyword("\uFDD0'sma-list")).call(null,result_data))))),"\uFDD0'macd-histogram-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'histogram")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'macd")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'ema-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt(cljs.core.first.call(null,ech)),window.parseFloat(cljs.core.second.call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,(function (p1__59605_SHARP_){
return (cljs.core.first.call(null,p1__59605_SHARP_) == null);
}),cljs.core.into_array.call(null,(new cljs.core.Keyword("\uFDD0'ema-list")).call(null,result_data))))),"\uFDD0'stochastic-d":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'D")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'stochastic-oscillator")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'bollinger-band":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'lower-band")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'upper-band")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'obv":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseInt((new cljs.core.Keyword("\uFDD0'obv")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'obv")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'stochastic-k":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'K")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'stochastic-oscillator")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'macd-signal-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'ema-signal")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'macd")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'signals":cljs.core.ObjMap.fromObject(["\uFDD0'moving-average"],{"\uFDD0'moving-average":cljs.core.map.call(null,(function (p1__59606_SHARP_){
return cljs.core.first.call(null,p1__59606_SHARP_);
}),cljs.core.remove.call(null,cljs.core.empty_QMARK_,cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.map.call(null,(function (inp){
return cljs.core.ObjMap.fromObject(["\uFDD0'x","\uFDD0'title","\uFDD0'text"],{"\uFDD0'x":window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),"\uFDD0'title":(new cljs.core.Keyword("\uFDD0'signal")).call(null,inp),"\uFDD0'text":[cljs.core.str("Why: "),cljs.core.str((new cljs.core.Keyword("\uFDD0'why")).call(null,inp))].join('')});
}),(new cljs.core.Keyword("\uFDD0'signals")).call(null,ech)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'moving-average")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data)))))))})});
});
edgar.populate_multiselect.call(null,".multiselect-live",cljs.core.ObjMap.fromObject(["\uFDD0'onChange"],{"\uFDD0'onChange":(function (element,checked){
if(cljs.core.truth_(checked))
{return $.post.call(null,[cljs.core.str("/get-streaming-stock-data?stock-selection="),cljs.core.str(element.val()),cljs.core.str("&stock-name="),cljs.core.str(element.text())].join(''),(function (data){
return console.log([cljs.core.str("POST:: get-streaming-stock-data > data["),cljs.core.str(data),cljs.core.str("]")].join(''));
}));
} else
{return null;
}
})}));
jayq.core.$.call(null,"#freeform-live").click((function (eventObj){
var input_val = jayq.core.$.call(null,"#freeform-live-input").val();
console.log("... here[",eventObj,"] / input[",input_val,"]");
if(!(cljs.core.empty_QMARK_.call(null,input_val)))
{return $.post.call(null,[cljs.core.str("/get-streaming-stock-data?stock-selection="),cljs.core.str(input_val),cljs.core.str("&stock-name="),cljs.core.str(input_val)].join(''),(function (data){
return console.log([cljs.core.str("POST:: get-streaming-stock-data > data["),cljs.core.str(data),cljs.core.str("]")].join(''));
}));
} else
{return null;
}
}));
edgar.populate_multiselect.call(null,".multiselect-historical",cljs.core.ObjMap.fromObject(["\uFDD0'onChange"],{"\uFDD0'onChange":(function (element,checked){
if(cljs.core.truth_(checked))
{return $.ajax.call(null,"/get-historical-data",cljs.core.clj__GT_js.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'data","\uFDD0'complete"],{"\uFDD0'data":cljs.core.ObjMap.fromObject(["\uFDD0'stock-selection","\uFDD0'time-duration","\uFDD0'time-interval"],{"\uFDD0'stock-selection":element.val(),"\uFDD0'time-duration":"300 S","\uFDD0'time-interval":"1 secs"}),"\uFDD0'complete":(function (jqXHR,status){
console.log([cljs.core.str(".multiselect-historical > jqXHR["),cljs.core.str(jqXHR),cljs.core.str("] / status["),cljs.core.str(status),cljs.core.str("]")].join(''));
var result_data = cljs.reader.read_string.call(null,jqXHR.responseText);
var parsed_result_map = edgar.parse_result_data.call(null,result_data);
var increment_QMARK_ = false;
return edgar.render_stock_graph.call(null,"#historical-stock-graph",cljs.core.PersistentVector.fromArray([(new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'local-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'sma-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'ema-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-price-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-signal-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-histogram-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stochastic-k")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stochastic-d")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'obv")).call(null,parsed_result_map)], true),(new cljs.core.Keyword("\uFDD0'signals")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,parsed_result_map),increment_QMARK_);
})})));
} else
{return null;
}
})}));
edgar.livesource = (new window.EventSource("/get-streaming-stock-data"));
edgar.livesource.addEventListener("stream-live",(function (e){
var result_data = cljs.reader.read_string.call(null,e.data);
var parsed_result_map = edgar.parse_result_data.call(null,result_data);
var increment_QMARK_ = (function (){var and__3949__auto__ = !((jayq.core.$.call(null,"#live-stock-graph").highcharts("StockChart") == null));
if(and__3949__auto__)
{return cljs.core._EQ_.call(null,(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,parsed_result_map),jayq.core.$.call(null,"#live-stock-graph").highcharts("StockChart").title.text);
} else
{return and__3949__auto__;
}
})();
return edgar.render_stock_graph.call(null,"#live-stock-graph",cljs.core.PersistentVector.fromArray([(new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'local-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'sma-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'ema-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-price-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-signal-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-histogram-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stochastic-k")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stochastic-d")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'obv")).call(null,parsed_result_map)], true),(new cljs.core.Keyword("\uFDD0'signals")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,parsed_result_map),increment_QMARK_);
}));
