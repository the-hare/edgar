goog.provide('edgar.core.analysis.confirming');
goog.require('cljs.core');
/**
* On Balance Volume (OBV) measures buying and selling pressure as a cumulative indicator that i) adds volume on up days and ii) subtracts volume on down days. We'll look for divergences between OBV and price to predict price movements or use OBV to confirm price trends.
* 
* The On Balance Volume (OBV) line is a running total of positive and negative volume. i) A tick's volume is positive when the close is above the prior close. Or ii) a tick's volume is negative when the close is below the prior close.
* 
* If closing price is above prior:
* Current OBV = Previous OBV + Current Volume
* 
* If closing price is below prior:
* Current OBV = Previous OBV  -  Current Volume
* 
* If closing price equals prior:
* Current OBV = Previous OBV (no change)
* 
* ** The first OBV value is the first period's positive/negative volume.
* ** This function assumes the latest tick is on the left**
*/
edgar.core.analysis.confirming.on_balance_volume = (function on_balance_volume(latest_tick,tick_list){
var obv_list = cljs.core.reduce.call(null,(function (rslt,ech){
var temp__4098__auto__ = (new cljs.core.Keyword("\uFDD0'obv")).call(null,cljs.core.first.call(null,rslt));
if(cljs.core.truth_(temp__4098__auto__))
{var prev_obv = temp__4098__auto__;
var current_price = ((cljs.core.string_QMARK_.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.first.call(null,ech))))?edgar.core.analysis.confirming.read_string.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.first.call(null,ech))):(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.first.call(null,ech)));
var prev_price = ((cljs.core.string_QMARK_.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.second.call(null,ech))))?edgar.core.analysis.confirming.read_string.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.second.call(null,ech))):(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.second.call(null,ech)));
var current_volume = ((cljs.core.string_QMARK_.call(null,(new cljs.core.Keyword("\uFDD0'total-volume")).call(null,cljs.core.first.call(null,ech))))?edgar.core.analysis.confirming.read_string.call(null,(new cljs.core.Keyword("\uFDD0'total-volume")).call(null,cljs.core.first.call(null,ech))):(new cljs.core.Keyword("\uFDD0'total-volume")).call(null,cljs.core.first.call(null,ech)));
var obv = ((cljs.core._EQ_.call(null,current_price,prev_price))?prev_obv:(((current_price > prev_price))?(prev_obv + current_volume):(prev_obv - current_volume)));
return cljs.core.cons.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'obv","\uFDD0'total-volume","\uFDD0'last-trade-price","\uFDD0'last-trade-time"],{"\uFDD0'obv":obv,"\uFDD0'total-volume":(new cljs.core.Keyword("\uFDD0'total-volume")).call(null,cljs.core.first.call(null,ech)),"\uFDD0'last-trade-price":(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.first.call(null,ech)),"\uFDD0'last-trade-time":(new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,cljs.core.first.call(null,ech))}),rslt);
} else
{return cljs.core.cons.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'obv","\uFDD0'total-volume","\uFDD0'last-trade-price","\uFDD0'last-trade-time"],{"\uFDD0'obv":(new cljs.core.Keyword("\uFDD0'total-volume")).call(null,cljs.core.first.call(null,ech)),"\uFDD0'total-volume":(new cljs.core.Keyword("\uFDD0'total-volume")).call(null,cljs.core.first.call(null,ech)),"\uFDD0'last-trade-price":(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.first.call(null,ech)),"\uFDD0'last-trade-time":(new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,cljs.core.first.call(null,ech))}),rslt);
}
}),cljs.core.with_meta(cljs.core.list(null),cljs.core.hash_map("\uFDD0'line",62,"\uFDD0'column",27)),cljs.core.reverse.call(null,cljs.core.partition.call(null,2,1,tick_list)));
if(cljs.core.truth_(latest_tick))
{var cprice = ((cljs.core.string_QMARK_.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,latest_tick)))?edgar.core.analysis.confirming.read_string.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,latest_tick)):(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,latest_tick));
var pprice = ((cljs.core.string_QMARK_.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.first.call(null,obv_list))))?edgar.core.analysis.confirming.read_string.call(null,(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.first.call(null,obv_list))):(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.first.call(null,obv_list)));
var cvolume = ((cljs.core.string_QMARK_.call(null,(new cljs.core.Keyword("\uFDD0'total-volume")).call(null,latest_tick)))?edgar.core.analysis.confirming.read_string.call(null,(new cljs.core.Keyword("\uFDD0'total-volume")).call(null,latest_tick)):(new cljs.core.Keyword("\uFDD0'total-volume")).call(null,latest_tick));
var pobv = (new cljs.core.Keyword("\uFDD0'obv")).call(null,cljs.core.first.call(null,obv_list));
var cobv = ((cljs.core._EQ_.call(null,cprice,pprice))?pobv:(((cprice > pprice))?(pobv + cvolume):(pobv - cvolume)));
return cljs.core.cons.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'obv","\uFDD0'total-volume","\uFDD0'last-trade-price","\uFDD0'last-trade-time"],{"\uFDD0'obv":cobv,"\uFDD0'total-volume":(new cljs.core.Keyword("\uFDD0'total-volume")).call(null,latest_tick),"\uFDD0'last-trade-price":(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,latest_tick),"\uFDD0'last-trade-time":(new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,latest_tick)}),obv_list);
} else
{return obv_list;
}
});
/**
* The Relative Strength Index (RSI) is a momentum oscillator that measures the speed and change of price movements. It oscillates between zero and 100.
* 
* If no 'tick-window' is given, it defaults to 14
* 
* ** This function assumes the latest tick is on the left**
*/
edgar.core.analysis.confirming.relative_strength_index = (function relative_strength_index(tick_window,tick_list){
var twindow = (cljs.core.truth_(tick_window)?tick_window:14);
var window_list = cljs.core.partition.call(null,twindow,1,tick_list);
return cljs.core.reduce.call(null,(function (rslt,ech){
var pass_one = cljs.core.reduce.call(null,(function (rslt__$1,ech__$1){
var fst = (new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.first.call(null,ech__$1));
var snd = (new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.second.call(null,ech__$1));
var up_QMARK_ = (fst > snd);
var down_QMARK_ = (fst < snd);
var sideways_QMARK_ = (function (){var and__3949__auto__ = !(up_QMARK_);
if(and__3949__auto__)
{return !(down_QMARK_);
} else
{return and__3949__auto__;
}
})();
if((function (){var or__3951__auto__ = up_QMARK_;
if(or__3951__auto__)
{return or__3951__auto__;
} else
{return down_QMARK_;
}
})())
{if(up_QMARK_)
{return cljs.core.conj.call(null,rslt__$1,cljs.core.assoc.call(null,cljs.core.first.call(null,ech__$1),"\uFDD0'signal","\uFDD0'up"));
} else
{return cljs.core.conj.call(null,rslt__$1,cljs.core.assoc.call(null,cljs.core.first.call(null,ech__$1),"\uFDD0'signal","\uFDD0'down"));
}
} else
{return cljs.core.conj.call(null,rslt__$1,cljs.core.assoc.call(null,cljs.core.first.call(null,ech__$1),"\uFDD0'signal","\uFDD0'sideways"));
}
}),cljs.core.PersistentVector.EMPTY,cljs.core.partition.call(null,2,1,cljs.core.remove.call(null,cljs.core.nil_QMARK_,ech)));
var up_list = (new cljs.core.Keyword("\uFDD0'up")).call(null,cljs.core.group_by.call(null,"\uFDD0'signal",pass_one));
var down_list = (new cljs.core.Keyword("\uFDD0'down")).call(null,cljs.core.group_by.call(null,"\uFDD0'signal",pass_one));
var avg_gains = (cljs.core.apply.call(null,cljs.core._PLUS_,cljs.core.map.call(null,"\uFDD0'last-trade-price",up_list)) / tick_window);
var avg_losses = (cljs.core.apply.call(null,cljs.core._PLUS_,cljs.core.map.call(null,"\uFDD0'last-trade-price",down_list)) / tick_window);
var rs = ((!(cljs.core._EQ_.call(null,0,avg_losses)))?(avg_gains / avg_losses):0);
var rsi = (100 - (100 / (1 + rs)));
return cljs.core.conj.call(null,rslt,cljs.core.ObjMap.fromObject(["\uFDD0'last-trade-time","\uFDD0'last-trade-price","\uFDD0'rs","\uFDD0'rsi"],{"\uFDD0'last-trade-time":(new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,cljs.core.first.call(null,ech)),"\uFDD0'last-trade-price":(new cljs.core.Keyword("\uFDD0'last-trade-price")).call(null,cljs.core.first.call(null,ech)),"\uFDD0'rs":rs,"\uFDD0'rsi":rsi}));
}),cljs.core.PersistentVector.EMPTY,window_list);
});
