goog.provide('shoreleave.remotes.http_rpc');
goog.require('cljs.core');
goog.require('cljs.reader');
goog.require('shoreleave.remotes.xhr');
shoreleave.remotes.http_rpc._STAR_remote_uri_STAR_ = "/_shoreleave";
/**
* Call a remote-callback on the server.
* Arguments:
* remote - a string, the name of the remote on the server (eg. specified with a defremote)
* params - a vector, the parameters to pass to the remote function
* callback - a map or a function.  The map specifies {:on-success some-f, :on-error another-f}
* otherwise, just a single function that will be called with on-complete is triggered
* extra-content - varlist of key-value pairs, extra-content to merge into the payload/content map.
* @param {...*} var_args
*/
shoreleave.remotes.http_rpc.remote_callback = (function() { 
var remote_callback__delegate = function (remote,params,callback,extra_content){
if(cljs.core.map_QMARK_.call(null,callback))
{var map__3945 = callback;
var map__3945__$1 = ((cljs.core.seq_QMARK_.call(null,map__3945))?cljs.core.apply.call(null,cljs.core.hash_map,map__3945):map__3945);
var on_error = cljs.core._lookup.call(null,map__3945__$1,"\uFDD0'on-error",null);
var on_success = cljs.core._lookup.call(null,map__3945__$1,"\uFDD0'on-success",null);
return shoreleave.remotes.xhr.xhr.call(null,cljs.core.PersistentVector.fromArray(["\uFDD0'post",shoreleave.remotes.http_rpc._STAR_remote_uri_STAR_], true),"\uFDD0'content",cljs.core.merge.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'remote","\uFDD0'params"],{"\uFDD0'remote":remote,"\uFDD0'params":cljs.core.pr_str.call(null,params)}),cljs.core.apply.call(null,cljs.core.hash_map,extra_content)),"\uFDD0'on-success",(cljs.core.truth_(on_success)?(function (data){
var data__$1 = ((cljs.core._EQ_.call(null,data,""))?"nil":data);
return on_success.call(null,cljs.reader.read_string.call(null,data__$1));
}):null),"\uFDD0'on-error",(cljs.core.truth_(on_error)?(function (data){
var data__$1 = ((cljs.core._EQ_.call(null,data,""))?"nil":data);
return on_error.call(null,cljs.reader.read_string.call(null,data__$1));
}):null));
} else
{return shoreleave.remotes.xhr.xhr.call(null,cljs.core.PersistentVector.fromArray(["\uFDD0'post",shoreleave.remotes.http_rpc._STAR_remote_uri_STAR_], true),"\uFDD0'content",cljs.core.merge.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'remote","\uFDD0'params"],{"\uFDD0'remote":remote,"\uFDD0'params":cljs.core.pr_str.call(null,params)}),cljs.core.apply.call(null,cljs.core.hash_map,extra_content)),"\uFDD0'on-success",(cljs.core.truth_(callback)?(function (data){
var data__$1 = ((cljs.core._EQ_.call(null,data,""))?"nil":data);
return callback.call(null,cljs.reader.read_string.call(null,data__$1));
}):null));
}
};
var remote_callback = function (remote,params,callback,var_args){
var extra_content = null;
if (goog.isDef(var_args)) {
  extra_content = cljs.core.array_seq(Array.prototype.slice.call(arguments, 3),0);
} 
return remote_callback__delegate.call(this, remote, params, callback, extra_content);
};
remote_callback.cljs$lang$maxFixedArity = 3;
remote_callback.cljs$lang$applyTo = (function (arglist__3946){
var remote = cljs.core.first(arglist__3946);
var params = cljs.core.first(cljs.core.next(arglist__3946));
var callback = cljs.core.first(cljs.core.next(cljs.core.next(arglist__3946)));
var extra_content = cljs.core.rest(cljs.core.next(cljs.core.next(arglist__3946)));
return remote_callback__delegate(remote, params, callback, extra_content);
});
remote_callback.cljs$lang$arity$variadic = remote_callback__delegate;
return remote_callback;
})()
;
