goog.provide('edgar');
goog.require('cljs.core');
goog.require('jayq.core');
goog.require('cljs.reader');
goog.require('jayq.core');
goog.require('jayq.core');
edgar.render_stock_graph = (function render_stock_graph(selector,tlist,label,increment){
if(cljs.core.not.call(null,increment))
{return jayq.core.$.call(null,selector).highcharts("StockChart",cljs.core.clj__GT_js.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'rangeSelector","\uFDD0'title","\uFDD0'chart","\uFDD0'navigator","\uFDD0'series"],{"\uFDD0'rangeSelector":cljs.core.ObjMap.fromObject(["\uFDD0'selected"],{"\uFDD0'selected":1}),"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":label}),"\uFDD0'chart":cljs.core.ObjMap.fromObject(["\uFDD0'zoomType"],{"\uFDD0'zoomType":"x"}),"\uFDD0'navigator":cljs.core.ObjMap.fromObject(["\uFDD0'adaptToUpdatedData"],{"\uFDD0'adaptToUpdatedData":true}),"\uFDD0'series":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'data","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":label,"\uFDD0'data":tlist,"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})})], true)})));
} else
{return cljs.core.first.call(null,jayq.core.$.call(null,selector).highcharts().series).addPoint(cljs.core.first.call(null,tlist),true,false);
}
});
edgar.tick_list = cljs.core.clj__GT_js.call(null,cljs.core.PersistentVector.fromArray([cljs.core.PersistentVector.fromArray([1368215573010,203.98], true),cljs.core.PersistentVector.fromArray([1368215576331,203.99], true),cljs.core.PersistentVector.fromArray([1368215576857,203.99], true),cljs.core.PersistentVector.fromArray([1368215577765,203.99], true),cljs.core.PersistentVector.fromArray([1368215578769,204.0], true),cljs.core.PersistentVector.fromArray([1368215579272,204.01], true),cljs.core.PersistentVector.fromArray([1368215579517,204.02], true),cljs.core.PersistentVector.fromArray([1368215581769,204.02], true),cljs.core.PersistentVector.fromArray([1368215583602,204.01], true),cljs.core.PersistentVector.fromArray([1368215585650,204.02], true),cljs.core.PersistentVector.fromArray([1368215586060,204.02], true),cljs.core.PersistentVector.fromArray([1368215587029,204.01], true),cljs.core.PersistentVector.fromArray([1368215588318,204.02], true),cljs.core.PersistentVector.fromArray([1368215589335,204.01], true),cljs.core.PersistentVector.fromArray([1368215589536,204.01], true),cljs.core.PersistentVector.fromArray([1368215589846,204.0], true),cljs.core.PersistentVector.fromArray([1368215591079,203.99], true),cljs.core.PersistentVector.fromArray([1368215591789,203.99], true),cljs.core.PersistentVector.fromArray([1368215592104,203.98], true),cljs.core.PersistentVector.fromArray([1368215592615,203.98], true),cljs.core.PersistentVector.fromArray([1368215592758,203.99], true),cljs.core.PersistentVector.fromArray([1368215594039,203.97], true),cljs.core.PersistentVector.fromArray([1368215597119,203.98], true),cljs.core.PersistentVector.fromArray([1368215597632,203.97], true),cljs.core.PersistentVector.fromArray([1368215599396,203.97], true),cljs.core.PersistentVector.fromArray([1368215603876,203.96], true),cljs.core.PersistentVector.fromArray([1368215606059,203.96], true),cljs.core.PersistentVector.fromArray([1368215610316,203.95], true),cljs.core.PersistentVector.fromArray([1368215610634,203.95], true),cljs.core.PersistentVector.fromArray([1368215610813,203.93], true),cljs.core.PersistentVector.fromArray([1368215612886,203.95], true),cljs.core.PersistentVector.fromArray([1368215615858,203.94], true),cljs.core.PersistentVector.fromArray([1368215618621,203.94], true),cljs.core.PersistentVector.fromArray([1368215619138,203.96], true),cljs.core.PersistentVector.fromArray([1368215623846,203.94], true),cljs.core.PersistentVector.fromArray([1368215632669,203.94], true),cljs.core.PersistentVector.fromArray([1368215634709,203.92], true),cljs.core.PersistentVector.fromArray([1368215636587,203.93], true),cljs.core.PersistentVector.fromArray([1368215636952,203.94], true),cljs.core.PersistentVector.fromArray([1368215638328,203.93], true)], true));
edgar.populate_multiselect = (function populate_multiselect(selector,options){
var G__4260 = (new cljs.core.Keyword("\uFDD0'bind")).call(null,jayq.core.deferred_m);
var G__4261 = (new cljs.core.Keyword("\uFDD0'return")).call(null,jayq.core.deferred_m);
var G__4262 = (new cljs.core.Keyword("\uFDD0'zero")).call(null,jayq.core.deferred_m);
return G__4260.call(null,$.ajax.call(null,"/list-filtered-input"),(function (filtered_input){
return G__4261.call(null,(function (){var multiselect = jayq.core.$.call(null,selector);
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
{return $.ajax.call(null,"/get-historical-data",cljs.core.clj__GT_js.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'data","\uFDD0'complete"],{"\uFDD0'data":cljs.core.ObjMap.fromObject(["\uFDD0'stock-selection","\uFDD0'time-duration","\uFDD0'time-interval"],{"\uFDD0'stock-selection":element.val(),"\uFDD0'time-duration":"60 S","\uFDD0'time-interval":"1 secs"}),"\uFDD0'complete":(function (jqXHR,status){
console.log([cljs.core.str(".multiselect-historical > jqXHR["),cljs.core.str(jqXHR),cljs.core.str("] / status["),cljs.core.str(status),cljs.core.str("]")].join(''));
var result_data = cljs.reader.read_string.call(null,jqXHR.responseText);
var local_list = (new cljs.core.Keyword("\uFDD0'stock-list")).call(null,result_data);
var stock_name = (new cljs.core.Keyword("\uFDD0'stock-name")).call(null,result_data);
return edgar.render_stock_graph.call(null,"#historical-stock-graph",local_list,stock_name,false);
})})));
} else
{return null;
}
})}));
edgar.livesource = (new window.EventSource("/get-streaming-stock-data"));
edgar.livesource.addEventListener("stream-live",(function (e){
console.log([cljs.core.str("GET:: get-streaming-live-data > e["),cljs.core.str(e),cljs.core.str("]")].join(''));
var result_data = cljs.reader.read_string.call(null,e.data);
var local_list = cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt(cljs.core.first.call(null,ech)),window.parseFloat(cljs.core.second.call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.into_array.call(null,(new cljs.core.Keyword("\uFDD0'stock-list")).call(null,result_data))));
var stock_name = (new cljs.core.Keyword("\uFDD0'stock-name")).call(null,result_data);
var increment_QMARK_ = cljs.core._EQ_.call(null,stock_name,jayq.core.$.call(null,"#live-stock-graph").highcharts("StockChart").title.text);
return edgar.render_stock_graph.call(null,"#live-stock-graph",local_list,stock_name,increment_QMARK_);
}));
