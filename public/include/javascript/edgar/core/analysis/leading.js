goog.provide('edgar.core.analysis.leading');
goog.require('cljs.core');
goog.require('edgar.core.analysis.lagging');
/**
* The MACD 'oscillator' or 'indicator' is a collection of three signals (or computed data-series), calculated from historical price data. These three signal lines are:
* 
* i) the MACD line: difference between the 12 and 26 days EMAs
* MACD = EMA[stockPrices,12] – EMA[stockPrices,26]
* 
* ii) the signal line (or average line): 9 EMA of the MACD line
* signal = EMA[MACD,9]
* 
* iii) and the difference (or divergence): difference between the blue and red lines
* histogram = MACD – signal
* 
* Options are:
* :macd-window-fast (default is 12)
* :macd-window-slow (default is 26)
* :signal-window (default is 9)
* 
* ** This function assumes the latest tick is on the left**
*/
edgar.core.analysis.leading.macd = (function() {
var macd = null;
var macd__3 = (function (options,tick_window,tick_list){
return macd.call(null,options,tick_window,tick_list,edgar.core.analysis.lagging.simple_moving_average.call(null,null,tick_window,tick_list));
});
var macd__4 = (function (options,tick_window,tick_list,sma_list){
var map__13716 = options;
var map__13716__$1 = ((cljs.core.seq_QMARK_.call(null,map__13716))?cljs.core.apply.call(null,cljs.core.hash_map,map__13716):map__13716);
var macd_fast = cljs.core._lookup.call(null,map__13716__$1,"\uFDD0'macd-window-fast",12);
var macd_slow = cljs.core._lookup.call(null,map__13716__$1,"\uFDD0'macd-window-slow",26);
var signal_window = cljs.core._lookup.call(null,map__13716__$1,"\uFDD0'signal-window",9);
var ema_short = edgar.core.analysis.lagging.exponential_moving_average.call(null,null,macd_fast,tick_list,sma_list);
var ema_long = edgar.core.analysis.lagging.exponential_moving_average.call(null,null,macd_slow,tick_list,sma_list);
var macd__$1 = cljs.core.map.call(null,(function (e1,e2){
if((function (){var and__3949__auto__ = !((e1 == null));
if(and__3949__auto__)
{return !((e2 == null));
} else
{return and__3949__auto__;
}
})())
{return cljs.core.ObjMap.fromObject(["\uFDD0'last-trade-price","\uFDD0'last-trade-time","\uFDD0'last-trade-macd"],{"\uFDD0'last-trade-price":(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,e1),"\uFDD0'last-trade-time":(new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,e1),"\uFDD0'last-trade-macd":((new cljs.core.Keyword("\uFDD0'last-trade-price-exponential")).call(null,e1) - (new cljs.core.Keyword("\uFDD0'last-trade-price-exponential")).call(null,e2))});
} else
{return null;
}
}),ema_short,ema_long);
var ema_signal = edgar.core.analysis.lagging.exponential_moving_average.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'input","\uFDD0'output","\uFDD0'etal"],{"\uFDD0'input":"\uFDD0'last-trade-macd","\uFDD0'output":"\uFDD0'ema-signal","\uFDD0'etal":cljs.core.PersistentVector.fromArray(["\uFDD0'last-trade-price","\uFDD0'last-trade-time"], true)}),signal_window,null,macd__$1);
return cljs.core.map.call(null,(function (e_macd,e_ema){
if((function (){var and__3949__auto__ = !((e_macd == null));
if(and__3949__auto__)
{return !((e_ema == null));
} else
{return and__3949__auto__;
}
})())
{return cljs.core.ObjMap.fromObject(["\uFDD0'last-trade-price","\uFDD0'last-trade-time","\uFDD0'last-trade-macd","\uFDD0'ema-signal","\uFDD0'histogram"],{"\uFDD0'last-trade-price":(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,e_macd),"\uFDD0'last-trade-time":(new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,e_macd),"\uFDD0'last-trade-macd":(new cljs.core.Keyword("\uFDD0'last-trade-macd")).call(null,e_macd),"\uFDD0'ema-signal":(new cljs.core.Keyword("\uFDD0'ema-signal")).call(null,e_ema),"\uFDD0'histogram":((new cljs.core.Keyword("\uFDD0'last-trade-macd")).call(null,e_macd) - (new cljs.core.Keyword("\uFDD0'ema-signal")).call(null,e_ema))});
} else
{return null;
}
}),macd__$1,ema_signal);
});
macd = function(options,tick_window,tick_list,sma_list){
switch(arguments.length){
case 3:
return macd__3.call(this,options,tick_window,tick_list);
case 4:
return macd__4.call(this,options,tick_window,tick_list,sma_list);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
macd.cljs$lang$arity$3 = macd__3;
macd.cljs$lang$arity$4 = macd__4;
return macd;
})()
;
/**
* The stochastic oscillator is a momentum indicator. According to George C. Lane (the inventor), it 'doesn't follow price, it doesn't follow volume or anything like that. It follows the speed or the momentum of price. As a rule, the momentum changes direction before price'. A 3-line Stochastics will give an anticipatory signal in %K, a signal in the turnaround of %D at or before a bottom, and a confirmation of the turnaround in %D-Slow. Smoothing the indicator over 3 periods is standard. Returns a list, equal in length to the tick-list, but only with slots filled, where preceding tick-list allows.
* 
* i) last-price:
* the last closing price
* 
* ii) %K:
* (last-price - low-price / high-price - low-price) * 100
* 
* iii) %D:
* 3-period exponential moving average of %K
* 
* iv)  %D-Slow
* 3-period exponential moving average of %D
* 
* v) low-price:
* the lowest price over the last N periods
* 
* vi) high-price:
* the highest price over the last N periods
* 
* 
* The inputs to this function are:
* 
* tick-window: the length of Stochastic, or number of ticks under observation (defaults to 14)
* trigger-window: the smoothing line (defaults to 3)
* trigger-line: (defaults to 3)
* tick-list: the input time series (in last trade price)
* 
* ** This function assumes the latest tick is on the left**
*/
edgar.core.analysis.leading.stochastic_oscillator = (function stochastic_oscillator(tick_window,trigger_window,trigger_line,tick_list){
var stochastic_list = cljs.core.reduce.call(null,(function (rslt,ech){
var last_time = (new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,cljs.core.first.call(null,ech));
var last_price = (new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.first.call(null,ech));
var last_price_list = cljs.core.map.call(null,(function (p1__13714_SHARP_){
if(cljs.core.string_QMARK_.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,p1__13714_SHARP_)))
{return edgar.core.analysis.leading.read_string.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,p1__13714_SHARP_));
} else
{return (new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,p1__13714_SHARP_);
}
}),ech);
var highest_price = cljs.core.apply.call(null,cljs.core.max,last_price_list);
var lowest_price = cljs.core.apply.call(null,cljs.core.min,last_price_list);
var _PERCENT_K = (function (){try{return ((last_price - lowest_price) / (highest_price - lowest_price));
}catch (e13718){if(cljs.core.instance_QMARK_.call(null,edgar.core.analysis.leading.Exception,e13718))
{var e = e13718;
return 0;
} else
{if("\uFDD0'else")
{throw e13718;
} else
{return null;
}
}
}})();
return cljs.core.cons.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'last-trade-price","\uFDD0'last-trade-time","\uFDD0'highest-price","\uFDD0'lowest-price","\uFDD0'K"],{"\uFDD0'last-trade-price":last_price,"\uFDD0'last-trade-time":last_time,"\uFDD0'highest-price":highest_price,"\uFDD0'lowest-price":lowest_price,"\uFDD0'K":_PERCENT_K}),rslt);
}),cljs.core.into.call(null,cljs.core.List.EMPTY,cljs.core.repeat.call(null,tick_window,null)),cljs.core.reverse.call(null,cljs.core.partition.call(null,tick_window,1,tick_list)));
var d_list = cljs.core.reduce.call(null,(function (rslt,ech){
var e_list = edgar.core.analysis.lagging.exponential_moving_average.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'input","\uFDD0'output","\uFDD0'etal"],{"\uFDD0'input":"\uFDD0'K","\uFDD0'output":"\uFDD0'D","\uFDD0'etal":cljs.core.PersistentVector.fromArray(["\uFDD0'last-trade-time","\uFDD0'last-trade-price","\uFDD0'highest-price","\uFDD0'lowest-price","\uFDD0'K"], true)}),3,null,ech);
return cljs.core.cons.call(null,cljs.core.first.call(null,e_list),rslt);
}),cljs.core.into.call(null,cljs.core.List.EMPTY,cljs.core.repeat.call(null,tick_window,null)),cljs.core.reverse.call(null,cljs.core.partition.call(null,trigger_window,1,cljs.core.remove.call(null,cljs.core.nil_QMARK_,stochastic_list))));
return d_list;
});
