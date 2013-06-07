goog.provide('edgar');
goog.require('cljs.core');
goog.require('jayq.core');
goog.require('cljs.reader');
goog.require('jayq.core');
goog.require('jayq.core');
edgar.render_stock_graph = (function render_stock_graph(selector,dataList,label,increment){
if(cljs.core.not.call(null,increment))
{return jayq.core.$.call(null,selector).highcharts("StockChart",cljs.core.clj__GT_js.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'names","\uFDD0'rangeSelector","\uFDD0'title","\uFDD0'chart","\uFDD0'navigator","\uFDD0'series"],{"\uFDD0'names":cljs.core.PersistentVector.fromArray([label,"Simple Moving Average","Exponential Moving Average","Bolling Band"], true),"\uFDD0'rangeSelector":cljs.core.ObjMap.fromObject(["\uFDD0'selected"],{"\uFDD0'selected":4}),"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":label}),"\uFDD0'chart":cljs.core.ObjMap.fromObject(["\uFDD0'zoomType"],{"\uFDD0'zoomType":"x"}),"\uFDD0'navigator":cljs.core.ObjMap.fromObject(["\uFDD0'adaptToUpdatedData"],{"\uFDD0'adaptToUpdatedData":true}),"\uFDD0'series":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'data","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":label,"\uFDD0'data":cljs.core.reverse.call(null,cljs.core.first.call(null,dataList)),"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'data","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Simple Moving Average","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.second.call(null,dataList)),"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'data","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Exponential Moving Average","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,2)),"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})}),cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'data","\uFDD0'type","\uFDD0'color","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":"Bollinger Band","\uFDD0'data":cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,3)),"\uFDD0'type":"arearange","\uFDD0'color":"#B0C4DE","\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":false,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})})], true)})));
} else
{cljs.core.first.call(null,jayq.core.$.call(null,selector).highcharts().series).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.first.call(null,dataList))),true,false);
cljs.core.second.call(null,jayq.core.$.call(null,selector).highcharts().series).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.second.call(null,dataList))),true,false);
cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,2).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,2))),true,false);
return cljs.core.nth.call(null,jayq.core.$.call(null,selector).highcharts().series,3).addPoint(cljs.core.last.call(null,cljs.core.reverse.call(null,cljs.core.nth.call(null,dataList,3))),true,false);
}
});
edgar.tick_list = cljs.core.clj__GT_js.call(null,cljs.core.PersistentVector.fromArray([cljs.core.PersistentVector.fromArray([1368215573010,203.98], true),cljs.core.PersistentVector.fromArray([1368215576331,203.99], true),cljs.core.PersistentVector.fromArray([1368215576857,203.99], true),cljs.core.PersistentVector.fromArray([1368215577765,203.99], true),cljs.core.PersistentVector.fromArray([1368215578769,204.0], true),cljs.core.PersistentVector.fromArray([1368215579272,204.01], true),cljs.core.PersistentVector.fromArray([1368215579517,204.02], true),cljs.core.PersistentVector.fromArray([1368215581769,204.02], true),cljs.core.PersistentVector.fromArray([1368215583602,204.01], true),cljs.core.PersistentVector.fromArray([1368215585650,204.02], true),cljs.core.PersistentVector.fromArray([1368215586060,204.02], true),cljs.core.PersistentVector.fromArray([1368215587029,204.01], true),cljs.core.PersistentVector.fromArray([1368215588318,204.02], true),cljs.core.PersistentVector.fromArray([1368215589335,204.01], true),cljs.core.PersistentVector.fromArray([1368215589536,204.01], true),cljs.core.PersistentVector.fromArray([1368215589846,204.0], true),cljs.core.PersistentVector.fromArray([1368215591079,203.99], true),cljs.core.PersistentVector.fromArray([1368215591789,203.99], true),cljs.core.PersistentVector.fromArray([1368215592104,203.98], true),cljs.core.PersistentVector.fromArray([1368215592615,203.98], true),cljs.core.PersistentVector.fromArray([1368215592758,203.99], true),cljs.core.PersistentVector.fromArray([1368215594039,203.97], true),cljs.core.PersistentVector.fromArray([1368215597119,203.98], true),cljs.core.PersistentVector.fromArray([1368215597632,203.97], true),cljs.core.PersistentVector.fromArray([1368215599396,203.97], true),cljs.core.PersistentVector.fromArray([1368215603876,203.96], true),cljs.core.PersistentVector.fromArray([1368215606059,203.96], true),cljs.core.PersistentVector.fromArray([1368215610316,203.95], true),cljs.core.PersistentVector.fromArray([1368215610634,203.95], true),cljs.core.PersistentVector.fromArray([1368215610813,203.93], true),cljs.core.PersistentVector.fromArray([1368215612886,203.95], true),cljs.core.PersistentVector.fromArray([1368215615858,203.94], true),cljs.core.PersistentVector.fromArray([1368215618621,203.94], true),cljs.core.PersistentVector.fromArray([1368215619138,203.96], true),cljs.core.PersistentVector.fromArray([1368215623846,203.94], true),cljs.core.PersistentVector.fromArray([1368215632669,203.94], true),cljs.core.PersistentVector.fromArray([1368215634709,203.92], true),cljs.core.PersistentVector.fromArray([1368215636587,203.93], true),cljs.core.PersistentVector.fromArray([1368215636952,203.94], true),cljs.core.PersistentVector.fromArray([1368215638328,203.93], true)], true));
edgar.populate_multiselect = (function populate_multiselect(selector,options){
var G__14744 = (new cljs.core.Keyword("\uFDD0'bind")).call(null,jayq.core.deferred_m);
var G__14745 = (new cljs.core.Keyword("\uFDD0'return")).call(null,jayq.core.deferred_m);
var G__14746 = (new cljs.core.Keyword("\uFDD0'zero")).call(null,jayq.core.deferred_m);
return G__14744.call(null,$.ajax.call(null,"/list-filtered-input"),(function (filtered_input){
return G__14745.call(null,(function (){var multiselect = jayq.core.$.call(null,selector);
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
return cljs.core.ObjMap.fromObject(["\uFDD0'local-list","\uFDD0'sma-list","\uFDD0'ema-list","\uFDD0'bollinger-band","\uFDD0'stock-name"],{"\uFDD0'local-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt(cljs.core.first.call(null,ech)),window.parseFloat(cljs.core.second.call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.into_array.call(null,(new cljs.core.Keyword("\uFDD0'stock-list")).call(null,result_data)))),"\uFDD0'sma-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt(cljs.core.first.call(null,ech)),window.parseFloat(cljs.core.second.call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,(function (p1__14739_SHARP_){
return (cljs.core.first.call(null,p1__14739_SHARP_) == null);
}),cljs.core.into_array.call(null,(new cljs.core.Keyword("\uFDD0'sma-list")).call(null,result_data))))),"\uFDD0'ema-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt(cljs.core.first.call(null,ech)),window.parseFloat(cljs.core.second.call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,(function (p1__14740_SHARP_){
return (cljs.core.first.call(null,p1__14740_SHARP_) == null);
}),cljs.core.into_array.call(null,(new cljs.core.Keyword("\uFDD0'ema-list")).call(null,result_data))))),"\uFDD0'bollinger-band":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'lower-band")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'upper-band")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'stock-name":(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,result_data)});
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
edgar.populate_multiselect.call(null,".multiselect-historical",cljs.core.ObjMap.fromObject(["\uFDD0'onChange"],{"\uFDD0'onChange":(function (element,checked){
if(cljs.core.truth_(checked))
{return $.ajax.call(null,"/get-historical-data",cljs.core.clj__GT_js.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'data","\uFDD0'complete"],{"\uFDD0'data":cljs.core.ObjMap.fromObject(["\uFDD0'stock-selection","\uFDD0'time-duration","\uFDD0'time-interval"],{"\uFDD0'stock-selection":element.val(),"\uFDD0'time-duration":"300 S","\uFDD0'time-interval":"1 secs"}),"\uFDD0'complete":(function (jqXHR,status){
console.log([cljs.core.str(".multiselect-historical > jqXHR["),cljs.core.str(jqXHR),cljs.core.str("] / status["),cljs.core.str(status),cljs.core.str("]")].join(''));
var result_data = cljs.reader.read_string.call(null,jqXHR.responseText);
var parsed_result_map = edgar.parse_result_data.call(null,result_data);
var increment_QMARK_ = false;
console.log([cljs.core.str("BB-1["),cljs.core.str((new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))),cljs.core.str("]")].join(''));
console.log([cljs.core.str("BB-2["),cljs.core.str((new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,parsed_result_map)),cljs.core.str("]")].join(''));
return edgar.render_stock_graph.call(null,"#historical-stock-graph",cljs.core.PersistentVector.fromArray([(new cljs.core.Keyword("\uFDD0'local-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'sma-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'ema-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,parsed_result_map)], true),(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,parsed_result_map),increment_QMARK_);
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
return edgar.render_stock_graph.call(null,"#live-stock-graph",cljs.core.PersistentVector.fromArray([(new cljs.core.Keyword("\uFDD0'local-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'sma-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'ema-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,parsed_result_map)], true),(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,parsed_result_map),increment_QMARK_);
}));
