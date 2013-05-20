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
}),cljs.core.with_meta(cljs.core.list(null),cljs.core.hash_map("\uFDD0'line",61,"\uFDD0'column",27)),cljs.core.reverse.call(null,cljs.core.partition.call(null,2,1,tick_list)));
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
