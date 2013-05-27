goog.provide('edgar');
goog.require('cljs.core');
goog.require('jayq.core');
goog.require('cljs.reader');
goog.require('jayq.core');
goog.require('jayq.core');
edgar.render_stock_graph = (function render_stock_graph(selector,tlist,label){
return jayq.core.$.call(null,selector).highcharts("StockChart",cljs.core.clj__GT_js.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'rangeSelector","\uFDD0'title","\uFDD0'series"],{"\uFDD0'rangeSelector":cljs.core.ObjMap.fromObject(["\uFDD0'selected"],{"\uFDD0'selected":1}),"\uFDD0'title":cljs.core.ObjMap.fromObject(["\uFDD0'text"],{"\uFDD0'text":label}),"\uFDD0'series":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'name","\uFDD0'data","\uFDD0'marker","\uFDD0'shadow","\uFDD0'tooltip"],{"\uFDD0'name":label,"\uFDD0'data":tlist,"\uFDD0'marker":cljs.core.ObjMap.fromObject(["\uFDD0'enabled","\uFDD0'radius"],{"\uFDD0'enabled":true,"\uFDD0'radius":3}),"\uFDD0'shadow":true,"\uFDD0'tooltip":cljs.core.ObjMap.fromObject(["\uFDD0'valueDecimals"],{"\uFDD0'valueDecimals":2})})], true)})));
});
edgar.tick_list = cljs.core.clj__GT_js.call(null,cljs.core.PersistentVector.fromArray([cljs.core.PersistentVector.fromArray([1368215573010,203.98], true),cljs.core.PersistentVector.fromArray([1368215576331,203.99], true),cljs.core.PersistentVector.fromArray([1368215576857,203.99], true),cljs.core.PersistentVector.fromArray([1368215577765,203.99], true),cljs.core.PersistentVector.fromArray([1368215578769,204.0], true),cljs.core.PersistentVector.fromArray([1368215579272,204.01], true),cljs.core.PersistentVector.fromArray([1368215579517,204.02], true),cljs.core.PersistentVector.fromArray([1368215581769,204.02], true),cljs.core.PersistentVector.fromArray([1368215583602,204.01], true),cljs.core.PersistentVector.fromArray([1368215585650,204.02], true),cljs.core.PersistentVector.fromArray([1368215586060,204.02], true),cljs.core.PersistentVector.fromArray([1368215587029,204.01], true),cljs.core.PersistentVector.fromArray([1368215588318,204.02], true),cljs.core.PersistentVector.fromArray([1368215589335,204.01], true),cljs.core.PersistentVector.fromArray([1368215589536,204.01], true),cljs.core.PersistentVector.fromArray([1368215589846,204.0], true),cljs.core.PersistentVector.fromArray([1368215591079,203.99], true),cljs.core.PersistentVector.fromArray([1368215591789,203.99], true),cljs.core.PersistentVector.fromArray([1368215592104,203.98], true),cljs.core.PersistentVector.fromArray([1368215592615,203.98], true),cljs.core.PersistentVector.fromArray([1368215592758,203.99], true),cljs.core.PersistentVector.fromArray([1368215594039,203.97], true),cljs.core.PersistentVector.fromArray([1368215597119,203.98], true),cljs.core.PersistentVector.fromArray([1368215597632,203.97], true),cljs.core.PersistentVector.fromArray([1368215599396,203.97], true),cljs.core.PersistentVector.fromArray([1368215603876,203.96], true),cljs.core.PersistentVector.fromArray([1368215606059,203.96], true),cljs.core.PersistentVector.fromArray([1368215610316,203.95], true),cljs.core.PersistentVector.fromArray([1368215610634,203.95], true),cljs.core.PersistentVector.fromArray([1368215610813,203.93], true),cljs.core.PersistentVector.fromArray([1368215612886,203.95], true),cljs.core.PersistentVector.fromArray([1368215615858,203.94], true),cljs.core.PersistentVector.fromArray([1368215618621,203.94], true),cljs.core.PersistentVector.fromArray([1368215619138,203.96], true),cljs.core.PersistentVector.fromArray([1368215623846,203.94], true),cljs.core.PersistentVector.fromArray([1368215632669,203.94], true),cljs.core.PersistentVector.fromArray([1368215634709,203.92], true),cljs.core.PersistentVector.fromArray([1368215636587,203.93], true),cljs.core.PersistentVector.fromArray([1368215636952,203.94], true),cljs.core.PersistentVector.fromArray([1368215638328,203.93], true)], true));
edgar.render_stock_graph.call(null,"#live-stock-graph",edgar.tick_list,"IBM");
edgar.render_stock_graph.call(null,"#historical-stock-graph",edgar.tick_list,"AAPL");
edgar.populate_multiselect = (function populate_multiselect(selector,options){
var G__5114 = (new cljs.core.Keyword("\uFDD0'bind")).call(null,jayq.core.deferred_m);
var G__5115 = (new cljs.core.Keyword("\uFDD0'return")).call(null,jayq.core.deferred_m);
var G__5116 = (new cljs.core.Keyword("\uFDD0'zero")).call(null,jayq.core.deferred_m);
return G__5114.call(null,$.ajax.call(null,"/list-filtered-input"),(function (filtered_input){
return G__5115.call(null,(function (){var multiselect = jayq.core.$.call(null,selector);
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
{return $.post.call(null,"/get-streaming-stock-data",(function (data){
return console.log([cljs.core.str("POST:: get-streaming-stock-data > data["),cljs.core.str(data),cljs.core.str("]")].join(''));
}));
} else
{return null;
}
})}));
edgar.livesource = (new window.EventSource("/get-streaming-stock-data"));
edgar.livesource.addEventListener("stream-live",(function (e){
return console.log([cljs.core.str("GET:: get-streaming-live-data > e["),cljs.core.str(e),cljs.core.str("]")].join(''));
}));
