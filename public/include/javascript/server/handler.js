goog.provide('server.handler');
goog.require('cljs.core');
server.handler.pull_out_signals = (function pull_out_signals(result_data,tag){
return cljs.core.map.call(null,(function (p1__101708_SHARP_){
return cljs.core.first.call(null,p1__101708_SHARP_);
}),cljs.core.remove.call(null,cljs.core.empty_QMARK_,cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.map.call(null,(function (inp){
return cljs.core.ObjMap.fromObject(["\uFDD0'x","\uFDD0'title","\uFDD0'text"],{"\uFDD0'x":window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),"\uFDD0'title":(new cljs.core.Keyword("\uFDD0'signal")).call(null,inp),"\uFDD0'text":[cljs.core.str("Why: "),cljs.core.str((new cljs.core.Keyword("\uFDD0'why")).call(null,inp))].join('')});
}),(new cljs.core.Keyword("\uFDD0'signals")).call(null,ech)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,tag.call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data)))))));
});
server.handler.pull_out_strategies = (function pull_out_strategies(result_data,tag){
var result_strategies = cljs.core.map.call(null,(function (p1__101709_SHARP_){
return cljs.core.first.call(null,p1__101709_SHARP_);
}),cljs.core.remove.call(null,cljs.core.empty_QMARK_,cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.map.call(null,(function (inp){
return cljs.core.ObjMap.fromObject(["\uFDD0'x","\uFDD0'title","\uFDD0'text"],{"\uFDD0'x":window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),"\uFDD0'title":(new cljs.core.Keyword("\uFDD0'signal")).call(null,inp),"\uFDD0'text":[cljs.core.str("Why: "),cljs.core.str((new cljs.core.Keyword("\uFDD0'why")).call(null,inp))].join('')});
}),(new cljs.core.Keyword("\uFDD0'strategies")).call(null,ech)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,tag.call(null,(new cljs.core.Keyword("\uFDD0'strategies")).call(null,result_data)))))));
return result_strategies;
});
server.handler.parse_result_data = (function parse_result_data(result_data){
return cljs.core.ObjMap.fromObject(["\uFDD0'local-list","\uFDD0'macd-price-list","\uFDD0'stock-name","\uFDD0'sma-list","\uFDD0'macd-histogram-list","\uFDD0'ema-list","\uFDD0'stochastic-d","\uFDD0'bollinger-band","\uFDD0'obv","\uFDD0'strategies","\uFDD0'stochastic-k","\uFDD0'macd-signal-list","\uFDD0'signals"],{"\uFDD0'local-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt(cljs.core.first.call(null,ech)),window.parseFloat(cljs.core.second.call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.into_array.call(null,(new cljs.core.Keyword("\uFDD0'stock-list")).call(null,result_data)))),"\uFDD0'macd-price-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'last-trade-macd")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'macd")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'stock-name":(new cljs.core.Keyword("\uFDD0'stock-name")).call(null,result_data),"\uFDD0'sma-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt(cljs.core.first.call(null,ech)),window.parseFloat(cljs.core.second.call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,(function (p1__101710_SHARP_){
return (cljs.core.first.call(null,p1__101710_SHARP_) == null);
}),cljs.core.into_array.call(null,(new cljs.core.Keyword("\uFDD0'sma-list")).call(null,result_data))))),"\uFDD0'macd-histogram-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'histogram")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'macd")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'ema-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt(cljs.core.first.call(null,ech)),window.parseFloat(cljs.core.second.call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,(function (p1__101711_SHARP_){
return (cljs.core.first.call(null,p1__101711_SHARP_) == null);
}),cljs.core.into_array.call(null,(new cljs.core.Keyword("\uFDD0'ema-list")).call(null,result_data))))),"\uFDD0'stochastic-d":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'D")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'stochastic-oscillator")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'bollinger-band":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'lower-band")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'upper-band")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'bollinger-band")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'obv":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseInt((new cljs.core.Keyword("\uFDD0'obv")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'obv")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'strategies":cljs.core.ObjMap.fromObject(["\uFDD0'strategy-A","\uFDD0'strategy-B"],{"\uFDD0'strategy-A":server.handler.pull_out_strategies.call(null,result_data,"\uFDD0'strategy-A"),"\uFDD0'strategy-B":server.handler.pull_out_strategies.call(null,result_data,"\uFDD0'strategy-B")}),"\uFDD0'stochastic-k":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'K")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'stochastic-oscillator")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'macd-signal-list":cljs.core.into_array.call(null,cljs.core.reduce.call(null,(function (rslt,ech){
return cljs.core.conj.call(null,rslt,cljs.core.into_array.call(null,cljs.core.PersistentVector.fromArray([window.parseInt((new cljs.core.Keyword("\uFDD0'last-trade-time")).call(null,ech)),window.parseFloat((new cljs.core.Keyword("\uFDD0'ema-signal")).call(null,ech))], true)));
}),cljs.core.PersistentVector.EMPTY,cljs.core.remove.call(null,cljs.core.nil_QMARK_,(new cljs.core.Keyword("\uFDD0'macd")).call(null,(new cljs.core.Keyword("\uFDD0'signals")).call(null,result_data))))),"\uFDD0'signals":cljs.core.ObjMap.fromObject(["\uFDD0'moving-average","\uFDD0'bollinger-band","\uFDD0'macd","\uFDD0'stochastic-oscillator","\uFDD0'obv"],{"\uFDD0'moving-average":server.handler.pull_out_signals.call(null,result_data,"\uFDD0'moving-average"),"\uFDD0'bollinger-band":server.handler.pull_out_signals.call(null,result_data,"\uFDD0'bollinger-band"),"\uFDD0'macd":server.handler.pull_out_signals.call(null,result_data,"\uFDD0'macd"),"\uFDD0'stochastic-oscillator":server.handler.pull_out_signals.call(null,result_data,"\uFDD0'stochastic-oscillator"),"\uFDD0'obv":server.handler.pull_out_signals.call(null,result_data,"\uFDD0'obv")})});
});
