goog.provide('shoreleave.remotes.xhr');
goog.require('cljs.core');
goog.require('shoreleave.remotes.common');
goog.require('goog.events');
goog.require('goog.net.XhrIo');
/**
* @param {...*} var_args
*/
shoreleave.remotes.xhr.xhr = (function() { 
var xhr__delegate = function (route,opts){
var req = (new goog.net.XhrIo());
var vec__3950 = shoreleave.remotes.common.parse_route.call(null,route);
var method = cljs.core.nth.call(null,vec__3950,0,null);
var uri = cljs.core.nth.call(null,vec__3950,1,null);
var map__3951 = cljs.core.apply.call(null,cljs.core.hash_map,opts);
var map__3951__$1 = ((cljs.core.seq_QMARK_.call(null,map__3951))?cljs.core.apply.call(null,cljs.core.hash_map,map__3951):map__3951);
var headers = cljs.core._lookup.call(null,map__3951__$1,"\uFDD0'headers",null);
var content = cljs.core._lookup.call(null,map__3951__$1,"\uFDD0'content",null);
var on_error = cljs.core._lookup.call(null,map__3951__$1,"\uFDD0'on-error",null);
var on_success = cljs.core._lookup.call(null,map__3951__$1,"\uFDD0'on-success",null);
var content__$1 = shoreleave.remotes.common.csrf_protected.call(null,content,method);
var data = shoreleave.remotes.common.__GT_data_str.call(null,content__$1);
var suc_callback = shoreleave.remotes.common.__GT_simple_callback.call(null,on_success);
var err_callback = shoreleave.remotes.common.__GT_simple_callback.call(null,(function (){var or__3824__auto__ = on_error;
if(cljs.core.truth_(or__3824__auto__))
{return or__3824__auto__;
} else
{return (function (p1__3947_SHARP_){
return console.log([cljs.core.str("XHR ERROR: "),cljs.core.str(p1__3947_SHARP_)].join(''));
});
}
})());
if(cljs.core.truth_(suc_callback))
{goog.events.listen(req,shoreleave.remotes.common.event_types.call(null,"\uFDD0'on-success"),(function (){
return suc_callback.call(null,req);
}));
goog.events.listen(req,shoreleave.remotes.common.event_types.call(null,"\uFDD0'on-error"),(function (){
return err_callback.call(null,req);
}));
} else
{}
return req.send(uri,method,data,(cljs.core.truth_(headers)?cljs.core.clj__GT_js.call(null,headers):null));
};
var xhr = function (route,var_args){
var opts = null;
if (goog.isDef(var_args)) {
  opts = cljs.core.array_seq(Array.prototype.slice.call(arguments, 1),0);
} 
return xhr__delegate.call(this, route, opts);
};
xhr.cljs$lang$maxFixedArity = 1;
xhr.cljs$lang$applyTo = (function (arglist__3952){
var route = cljs.core.first(arglist__3952);
var opts = cljs.core.rest(arglist__3952);
return xhr__delegate(route, opts);
});
xhr.cljs$lang$arity$variadic = xhr__delegate;
return xhr;
})()
;
