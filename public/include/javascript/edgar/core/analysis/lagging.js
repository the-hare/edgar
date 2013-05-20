goog.provide('edgar.core.analysis.lagging');
goog.require('cljs.core');
/**
* Takes the tick-list, and moves back as far as the tick window will take it.
* 
* Returns a list, equal in length to the tick-list, but only with slots filled,
* where preceding tick-list allows.
* 
* Options are:
* :input - input key function will look for (defaults to :last-trade-price)
* :output - output key function will emit (defaults to :last-trade-price-average)
* :etal - other keys to emit in each result map
*/
edgar.core.analysis.lagging.simple_moving_average = (function simple_moving_average(options,tick_window,tick_list){
var start_index = tick_window;
var ma_list = cljs.core.into.call(null,cljs.core.List.EMPTY,cljs.core.repeat.call(null,tick_window,null));
var map__4209 = options;
var map__4209__$1 = ((cljs.core.seq_QMARK_.call(null,map__4209))?cljs.core.apply.call(null,cljs.core.hash_map,map__4209):map__4209);
var input_key = cljs.core._lookup.call(null,map__4209__$1,"\uFDD0'input","\uFDD0'last-trade-price");
var output_key = cljs.core._lookup.call(null,map__4209__$1,"\uFDD0'output","\uFDD0'last-trade-price-average");
var etal_keys = cljs.core._lookup.call(null,map__4209__$1,"\uFDD0'etal",cljs.core.PersistentVector.fromArray(["\uFDD0'last-trade-price","\uFDD0'last-trade-time"], true));
return cljs.core.reduce.call(null,(function (rslt,ech){
var tsum = cljs.core.reduce.call(null,(function (rslt__$1,inp){
var ltprice = input_key.call(null,inp);
return (((cljs.core.string_QMARK_.call(null,ltprice))?edgar.core.analysis.lagging.read_string.call(null,ltprice):ltprice) + rslt__$1);
}),0,ech);
var taverage = (tsum / cljs.core.count.call(null,ech));
return cljs.core.cons.call(null,cljs.core.merge.call(null,cljs.core.zipmap.call(null,etal_keys,cljs.core.map.call(null,(function (p1__4206_SHARP_){
return p1__4206_SHARP_.call(null,cljs.core.first.call(null,ech));
}),etal_keys)),cljs.core.PersistentArrayMap.fromArrays([output_key,"\uFDD0'population"],[taverage,ech])),rslt);
}),ma_list,cljs.core.reverse.call(null,cljs.core.partition.call(null,tick_window,1,tick_list)));
});
/**
* From a tick-list, generates an accompanying exponential moving average list.
* 
* EMA = price(today) * k + EMA(yesterday) * (1 - k)
* k = 2 / N + 1
* N = number of days
* 
* Returns a list, equal in length to the tick-list, but only with slots filled,
* where preceding tick-list allows.
* 
* Options are:
* :input - input key function will look for (defaults to :last-trade-price)
* :output - output key function will emit (defaults to :last-trade-price-exponential)
* :etal - other keys to emit in each result map
*/
edgar.core.analysis.lagging.exponential_moving_average = (function() {
var exponential_moving_average = null;
var exponential_moving_average__3 = (function (options,tick_window,tick_list){
return exponential_moving_average.call(null,options,tick_window,tick_list,edgar.core.analysis.lagging.simple_moving_average.call(null,null,tick_window,tick_list));
});
var exponential_moving_average__4 = (function (options,tick_window,tick_list,sma_list){
var k = (2 / (tick_window + 1));
var ema_list = cljs.core.into.call(null,cljs.core.List.EMPTY,cljs.core.repeat.call(null,tick_window,null));
var map__4211 = options;
var map__4211__$1 = ((cljs.core.seq_QMARK_.call(null,map__4211))?cljs.core.apply.call(null,cljs.core.hash_map,map__4211):map__4211);
var input_key = cljs.core._lookup.call(null,map__4211__$1,"\uFDD0'input","\uFDD0'last-trade-price");
var output_key = cljs.core._lookup.call(null,map__4211__$1,"\uFDD0'output","\uFDD0'last-trade-price-exponential");
var etal_keys = cljs.core._lookup.call(null,map__4211__$1,"\uFDD0'etal",cljs.core.PersistentVector.fromArray(["\uFDD0'last-trade-price","\uFDD0'last-trade-time"], true));
return cljs.core.reduce.call(null,(function (rslt,ech){
var ltprice = input_key.call(null,ech);
var ema_last = (cljs.core.truth_(output_key.call(null,cljs.core.first.call(null,rslt)))?output_key.call(null,cljs.core.first.call(null,rslt)):input_key.call(null,ech));
var ema_now = ((k * ((cljs.core.string_QMARK_.call(null,ltprice))?edgar.core.analysis.lagging.read_string.call(null,ltprice):ltprice)) + (((cljs.core.string_QMARK_.call(null,ema_last))?edgar.core.analysis.lagging.read_string.call(null,ema_last):ema_last) * (1 - k)));
return cljs.core.cons.call(null,cljs.core.merge.call(null,cljs.core.zipmap.call(null,etal_keys,cljs.core.map.call(null,(function (p1__4207_SHARP_){
return p1__4207_SHARP_.call(null,ech);
}),etal_keys)),cljs.core.PersistentArrayMap.fromArrays([output_key],[ema_now])),rslt);
}),ema_list,cljs.core.reverse.call(null,cljs.core.remove.call(null,cljs.core.nil_QMARK_,sma_list)));
});
exponential_moving_average = function(options,tick_window,tick_list,sma_list){
switch(arguments.length){
case 3:
return exponential_moving_average__3.call(this,options,tick_window,tick_list);
case 4:
return exponential_moving_average__4.call(this,options,tick_window,tick_list,sma_list);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
exponential_moving_average.cljs$lang$arity$3 = exponential_moving_average__3;
exponential_moving_average.cljs$lang$arity$4 = exponential_moving_average__4;
return exponential_moving_average;
})()
;
/**
* From a tick-list, generates an accompanying list with upper-band and lower-band
* 
* Upper Band: K times an N-period standard deviation above the moving average (MA + Kσ)
* Lower Band: K times an N-period standard deviation below the moving average (MA − Kσ)
* K: number of standard deviations
* N: period, or tick-window we are looking at
* 
* Returns a list, equal in length to the tick-list, but only with slots filled,
* where preceding tick-list allows.
*/
edgar.core.analysis.lagging.bollinger_band = (function() {
var bollinger_band = null;
var bollinger_band__2 = (function (tick_window,tick_list){
return bollinger_band.call(null,tick_window,tick_list,edgar.core.analysis.lagging.simple_moving_average.call(null,null,tick_window,tick_list));
});
var bollinger_band__3 = (function (tick_window,tick_list,sma_list){
var bollinger_list = cljs.core.into.call(null,cljs.core.List.EMPTY,cljs.core.repeat.call(null,tick_window,null));
return cljs.core.reduce.call(null,(function (rslt,ech){
var ma = (new cljs.core.Keyword("\uFDD0'last-trade-price-average")).call(null,ech);
var mean = (cljs.core.reduce.call(null,(function (rslt__$1,ech__$1){
return (((cljs.core.string_QMARK_.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,ech__$1)))?edgar.core.analysis.lagging.read_string.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,ech__$1)):(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,ech__$1)) + rslt__$1);
}),0,(new cljs.core.Keyword("\uFDD0'population")).call(null,ech)) / cljs.core.count.call(null,(new cljs.core.Keyword("\uFDD0'population")).call(null,ech)));
var sq_diff_list = cljs.core.map.call(null,(function (ech__$1){
var diff = (mean - ((cljs.core.string_QMARK_.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,ech__$1)))?edgar.core.analysis.lagging.read_string.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,ech__$1)):(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,ech__$1)));
return (diff * diff);
}),(new cljs.core.Keyword("\uFDD0'population")).call(null,ech));
var variance = (cljs.core.reduce.call(null,cljs.core._PLUS_,sq_diff_list) / cljs.core.count.call(null,(new cljs.core.Keyword("\uFDD0'population")).call(null,ech)));
var standard_deviation = edgar.core.analysis.lagging.Math.sqrt(variance);
return cljs.core.cons.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'last-trade-price","\uFDD0'last-trade-time","\uFDD0'upper-band","\uFDD0'lower-band"],{"\uFDD0'last-trade-price":(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,ech),"\uFDD0'last-trade-time":(new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech),"\uFDD0'upper-band":(ma + (2 * standard_deviation)),"\uFDD0'lower-band":(ma - (2 * standard_deviation))}),rslt);
}),bollinger_list,cljs.core.reverse.call(null,cljs.core.remove.call(null,cljs.core.nil_QMARK_,sma_list)));
});
bollinger_band = function(tick_window,tick_list,sma_list){
switch(arguments.length){
case 2:
return bollinger_band__2.call(this,tick_window,tick_list);
case 3:
return bollinger_band__3.call(this,tick_window,tick_list,sma_list);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
bollinger_band.cljs$lang$arity$2 = bollinger_band__2;
bollinger_band.cljs$lang$arity$3 = bollinger_band__3;
return bollinger_band;
})()
;
