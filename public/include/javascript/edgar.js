goog.provide('edgar');
goog.require('cljs.core');
goog.require('jayq.core');
goog.require('ui.components');
goog.require('ui.graph');
goog.require('server.handler');
goog.require('cljs.reader');
goog.require('jayq.core');
goog.require('jayq.core');
edgar.add_signals = (function add_signals(initial_list,signal_map){
return cljs.core.reduce.call(null,(function (rslt,ech){
var default_entry = (function (eF){
return cljs.core.ObjMap.fromObject(["\uFDD0'type","\uFDD0'data","\uFDD0'color","\uFDD0'fillColor","\uFDD0'width","\uFDD0'style","\uFDD0'states"],{"\uFDD0'type":"flags","\uFDD0'data":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'x","\uFDD0'title","\uFDD0'text"],{"\uFDD0'x":(new cljs.core.Keyword("\uFDD0'x")).call(null,eF),"\uFDD0'title":(new cljs.core.Keyword("\uFDD0'title")).call(null,eF),"\uFDD0'text":(new cljs.core.Keyword("\uFDD0'text")).call(null,eF)})], true),"\uFDD0'color":"#5F86B3","\uFDD0'fillColor":"#5F86B3","\uFDD0'width":16,"\uFDD0'style":cljs.core.ObjMap.fromObject(["\uFDD0'color"],{"\uFDD0'color":"white"}),"\uFDD0'states":cljs.core.ObjMap.fromObject(["\uFDD0'hover"],{"\uFDD0'hover":cljs.core.ObjMap.fromObject(["\uFDD0'fillColor"],{"\uFDD0'fillColor":"#395C84"})})});
});
var G__107811 = cljs.core.first.call(null,ech);
if(cljs.core._EQ_.call(null,"default",G__107811))
{return rslt;
} else
{if(cljs.core._EQ_.call(null,"\uFDD0'obv",G__107811))
{return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.assoc.call(null,default_entry.call(null,eF),"\uFDD0'onSeries","obv-list"));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
} else
{if(cljs.core._EQ_.call(null,"\uFDD0'stochastic-oscillator",G__107811))
{return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.assoc.call(null,default_entry.call(null,eF),"\uFDD0'onSeries","k-list"));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
} else
{if(cljs.core._EQ_.call(null,"\uFDD0'macd",G__107811))
{return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.assoc.call(null,default_entry.call(null,eF),"\uFDD0'onSeries","macd-price-list"));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
} else
{if(cljs.core._EQ_.call(null,"\uFDD0'bollinger-band",G__107811))
{return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.assoc.call(null,default_entry.call(null,eF),"\uFDD0'onSeries","bollinger-list"));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
} else
{if(cljs.core._EQ_.call(null,"\uFDD0'moving-average",G__107811))
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
edgar.add_strategies = (function add_strategies(initial_list,strategy_map){
return cljs.core.reduce.call(null,(function (rslt,ech){
var default_entry = (function (eF){
return cljs.core.ObjMap.fromObject(["\uFDD0'type","\uFDD0'data","\uFDD0'color","\uFDD0'fillColor","\uFDD0'width","\uFDD0'style","\uFDD0'states"],{"\uFDD0'type":"flags","\uFDD0'data":cljs.core.PersistentVector.fromArray([cljs.core.ObjMap.fromObject(["\uFDD0'x","\uFDD0'title","\uFDD0'text"],{"\uFDD0'x":(new cljs.core.Keyword("\uFDD0'x")).call(null,eF),"\uFDD0'title":(new cljs.core.Keyword("\uFDD0'title")).call(null,eF),"\uFDD0'text":(new cljs.core.Keyword("\uFDD0'text")).call(null,eF)})], true),"\uFDD0'color":"#5F86B3","\uFDD0'fillColor":"#5F86B3","\uFDD0'width":16,"\uFDD0'style":cljs.core.ObjMap.fromObject(["\uFDD0'color"],{"\uFDD0'color":"white"}),"\uFDD0'states":cljs.core.ObjMap.fromObject(["\uFDD0'hover"],{"\uFDD0'hover":cljs.core.ObjMap.fromObject(["\uFDD0'fillColor"],{"\uFDD0'fillColor":"#395C84"})})});
});
return cljs.core.concat.call(null,rslt,cljs.core.reduce.call(null,(function (rF,eF){
return cljs.core.conj.call(null,rF,cljs.core.assoc.call(null,default_entry.call(null,eF),"\uFDD0'onSeries","tick-list"));
}),cljs.core.PersistentVector.EMPTY,cljs.core.second.call(null,ech)));
}),initial_list,cljs.core.seq.call(null,strategy_map));
});
ui.components.populate_multiselect.call(null,".multiselect-live",cljs.core.ObjMap.fromObject(["\uFDD0'onChange"],{"\uFDD0'onChange":(function (element,checked){
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
ui.components.populate_multiselect.call(null,".multiselect-historical",cljs.core.ObjMap.fromObject(["\uFDD0'onChange"],{"\uFDD0'onChange":(function (element,checked){
if(cljs.core.truth_(checked))
{return $.ajax.call(null,"/get-historical-data",cljs.core.clj__GT_js.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'data","\uFDD0'complete"],{"\uFDD0'data":cljs.core.ObjMap.fromObject(["\uFDD0'stock-selection","\uFDD0'time-duration","\uFDD0'time-interval"],{"\uFDD0'stock-selection":element.val(),"\uFDD0'time-duration":"300 S","\uFDD0'time-interval":"1 secs"}),"\uFDD0'complete":(function (jqXHR,status){
console.log([cljs.core.str(".multiselect-historical > jqXHR["),cljs.core.str(jqXHR),cljs.core.str("] / status["),cljs.core.str(status),cljs.core.str("]")].join(''));
var result_data = cljs.reader.read_string.call(null,jqXHR.responseText);
var parsed_result_map = server.handler.parse_result_data.call(null,result_data);
var increment_QMARK_ = false;
console.log([cljs.core.str("... generated signal-map["),cljs.core.str((new cljs.core.Keyword("\uFDD0'strategies")).call(null,result_data)),cljs.core.str("]")].join(''));
return ui.graph.render_stock_graph.call(null,"#historical-stock-graph",cljs.core.PersistentVector.fromArray([(new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'local-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'sma-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'ema-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-price-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-signal-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-histogram-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stochastic-k")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stochastic-d")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'obv")).call(null,parsed_result_map)], true),(new cljs.core.Keyword("\uFDD0'signals")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'strategies")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,parsed_result_map),increment_QMARK_);
})})));
} else
{return null;
}
})}));
edgar.livesource = (new window.EventSource("/get-streaming-stock-data"));
edgar.livesource.addEventListener("stream-live",(function (e){
var result_data = cljs.reader.read_string.call(null,e.data);
var parsed_result_map = server.handler.parse_result_data.call(null,result_data);
var increment_QMARK_ = (function (){var and__3949__auto__ = !((jayq.core.$.call(null,"#live-stock-graph").highcharts("StockChart") == null));
if(and__3949__auto__)
{return cljs.core._EQ_.call(null,(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,parsed_result_map),jayq.core.$.call(null,"#live-stock-graph").highcharts("StockChart").title.text);
} else
{return and__3949__auto__;
}
})();
return ui.graph.render_stock_graph.call(null,"#live-stock-graph",cljs.core.PersistentVector.fromArray([(new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'local-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'sma-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'ema-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-price-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-signal-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-histogram-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stochastic-k")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stochastic-d")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'obv")).call(null,parsed_result_map)], true),(new cljs.core.Keyword("\uFDD0'signals")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'strategies")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,parsed_result_map),increment_QMARK_);
}));
