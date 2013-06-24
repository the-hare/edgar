goog.provide('edgar');
goog.require('cljs.core');
goog.require('jayq.core');
goog.require('ui.components');
goog.require('ui.graph');
goog.require('server.handler');
goog.require('cljs.reader');
goog.require('jayq.core');
goog.require('jayq.core');
edgar.livesource = (new window.EventSource("/get-streaming-stock-data"));
edgar.livesource.addEventListener("stream-live",(function (e){
var result_data = cljs.reader.read_string.call(null,e.data);
var parsed_result_map = server.handler.parse_result_data.call(null,result_data);
var increment_QMARK_ = (function (){var and__3949__auto__ = !((jayq.core.$.call(null,"#live-stock-graph").highcharts("StockChart") == null));
if(and__3949__auto__)
{var and__3949__auto____$1 = !((jayq.core.$.call(null,"#live-stock-graph").highcharts("StockChart").title == null));
if(and__3949__auto____$1)
{return cljs.core._EQ_.call(null,(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,parsed_result_map),jayq.core.$.call(null,"#live-stock-graph").highcharts("StockChart").title.text);
} else
{return and__3949__auto____$1;
}
} else
{return and__3949__auto__;
}
})();
if((function (){var and__3949__auto__ = !((jayq.core.$.call(null,"#live-stock-graph").highcharts("StockChart").title.text == null));
if(and__3949__auto__)
{return cljs.core._EQ_.call(null,(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,parsed_result_map),jayq.core.$.call(null,"#live-stock-graph").highcharts("StockChart").title.text);
} else
{return and__3949__auto__;
}
})())
{return ui.graph.render_stock_graph.call(null,"#live-stock-graph",cljs.core.PersistentVector.fromArray([(new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'local-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'sma-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'ema-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-price-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-signal-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'macd-histogram-list")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stochastic-k")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stochastic-d")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'obv")).call(null,parsed_result_map)], true),(new cljs.core.Keyword("\uFDD0'signals")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'strategies")).call(null,parsed_result_map),(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,parsed_result_map),increment_QMARK_);
} else
{return null;
}
}));
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
