goog.provide('shoreleave.remotes.common');
goog.require('cljs.core');
goog.require('shoreleave.remotes.protocols');
goog.require('shoreleave.browser.cookies');
goog.require('goog.net.EventType');
goog.require('goog.string');
goog.require('goog.structs');
goog.require('goog.Uri.QueryData');
goog.require('clojure.string');
shoreleave.remotes.common.event_types = cljs.core.ObjMap.fromObject(["\uFDD0'on-complete","\uFDD0'on-success","\uFDD0'on-error","\uFDD0'on-timeout","\uFDD0'on-ready"],{"\uFDD0'on-complete":goog.net.EventType.COMPLETE,"\uFDD0'on-success":goog.net.EventType.SUCCESS,"\uFDD0'on-error":goog.net.EventType.ERROR,"\uFDD0'on-timeout":goog.net.EventType.TIMEOUT,"\uFDD0'on-ready":goog.net.EventType.READY});
shoreleave.remotes.common._STAR_csrf_token_name_STAR_ = "\uFDD0'__anti-forgery-token";
/**
* Generate a random string that is suitable for request IDs
*/
shoreleave.remotes.common.rand_id_str = (function rand_id_str(){
return goog.string.getRandomString();
});
/**
* Given the keyword form of a request method (`:post`),
* return Closure acceptable form (an upper-cased string)
*/
shoreleave.remotes.common.__GT_url_method = (function __GT_url_method(m){
return clojure.string.upper_case.call(null,cljs.core.name.call(null,m));
});
/**
* Shape the routes accordingly for Closure's XHR calls
*/
shoreleave.remotes.common.parse_route = (function parse_route(route){
if(cljs.core.string_QMARK_.call(null,route))
{return cljs.core.PersistentVector.fromArray(["GET",route], true);
} else
{if(cljs.core.vector_QMARK_.call(null,route))
{var vec__3954 = route;
var m = cljs.core.nth.call(null,vec__3954,0,null);
var u = cljs.core.nth.call(null,vec__3954,1,null);
return cljs.core.PersistentVector.fromArray([shoreleave.remotes.common.__GT_url_method.call(null,m),u], true);
} else
{if("\uFDD0'else")
{return cljs.core.PersistentVector.fromArray(["GET",route], true);
} else
{return null;
}
}
}
});
/**
* Liberate all client-side developers!
* Given a simple callback function, automatically pass it the response
* content from a remote call
*/
shoreleave.remotes.common.__GT_simple_callback = (function __GT_simple_callback(callback){
if(cljs.core.truth_(callback))
{return (function (req){
var data = req.getResponseText();
return callback.call(null,data);
});
} else
{return null;
}
});
/**
* For all POST requests, if ring-anti-forgery is used, pack the CSRF token
* into the content being sent to the server.
* Content is always sent to the server as a map (that later gets converted accordingly)
*/
shoreleave.remotes.common.csrf_protected = (function csrf_protected(content_map,method){
var temp__3971__auto__ = (function (){var and__3822__auto__ = cljs.core._EQ_.call(null,method,"POST");
if(and__3822__auto__)
{return shoreleave.remotes.common._STAR_csrf_token_name_STAR_.call(null,shoreleave.browser.cookies.cookies);
} else
{return and__3822__auto__;
}
})();
if(cljs.core.truth_(temp__3971__auto__))
{var anti_forgery_token = temp__3971__auto__;
return cljs.core.merge.call(null,content_map,cljs.core.PersistentArrayMap.fromArrays([shoreleave.remotes.common._STAR_csrf_token_name_STAR_],[anti_forgery_token]));
} else
{return content_map;
}
});
(shoreleave.remotes.protocols.ITransportData["_"] = true);
(shoreleave.remotes.protocols._data_str["_"] = (function (t){
return [cljs.core.str(goog.Uri.QueryData.createFromMap((new goog.structs.Map(cljs.core.clj__GT_js.call(null,t)))))].join('');
}));
cljs.core.PersistentTreeMap.prototype.shoreleave$remotes$protocols$ITransportData$ = true;
cljs.core.PersistentTreeMap.prototype.shoreleave$remotes$protocols$ITransportData$_data_str$arity$1 = (function (t){
return [cljs.core.str(goog.Uri.QueryData.createFromMap((new goog.structs.Map(cljs.core.clj__GT_js.call(null,t)))))].join('');
});
cljs.core.PersistentHashMap.prototype.shoreleave$remotes$protocols$ITransportData$ = true;
cljs.core.PersistentHashMap.prototype.shoreleave$remotes$protocols$ITransportData$_data_str$arity$1 = (function (t){
return [cljs.core.str(goog.Uri.QueryData.createFromMap((new goog.structs.Map(cljs.core.clj__GT_js.call(null,t)))))].join('');
});
cljs.core.PersistentArrayMap.prototype.shoreleave$remotes$protocols$ITransportData$ = true;
cljs.core.PersistentArrayMap.prototype.shoreleave$remotes$protocols$ITransportData$_data_str$arity$1 = (function (t){
return [cljs.core.str(goog.Uri.QueryData.createFromMap((new goog.structs.Map(cljs.core.clj__GT_js.call(null,t)))))].join('');
});
(shoreleave.remotes.protocols.ITransportData["string"] = true);
(shoreleave.remotes.protocols._data_str["string"] = (function (t){
return t;
}));
/**
* Generate a query-data-string, given Clojure data (usually a hash-map or string)
*/
shoreleave.remotes.common.__GT_data_str = (function __GT_data_str(d){
return shoreleave.remotes.protocols._data_str.call(null,d);
});
