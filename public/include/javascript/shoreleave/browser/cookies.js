goog.provide('shoreleave.browser.cookies');
goog.require('cljs.core');
goog.require('goog.string');
goog.require('goog.net.Cookies');
goog.net.Cookies.prototype.cljs$core$IHash$ = true;
goog.net.Cookies.prototype.cljs$core$IHash$_hash$arity$1 = (function (c){
return cljs.core._hash.call(null,cljs.core._persistent_BANG_.call(null,c));
});
goog.net.Cookies.prototype.cljs$core$ILookup$ = true;
goog.net.Cookies.prototype.cljs$core$ILookup$_lookup$arity$2 = (function (c,k){
return cljs.core._lookup.call(null,c,k,null);
});
goog.net.Cookies.prototype.cljs$core$ILookup$_lookup$arity$3 = (function (c,k,not_found){
var v = c.get(cljs.core.name.call(null,k),not_found);
if(cljs.core.string_QMARK_.call(null,v))
{return goog.string.urlDecode(v);
} else
{return v;
}
});
goog.net.Cookies.prototype.cljs$core$IAssociative$ = true;
goog.net.Cookies.prototype.cljs$core$IAssociative$_assoc$arity$3 = (function (c,k,v){
return cljs.core._assoc.call(null,cljs.core._persistent_BANG_.call(null,c),k,v);
});
goog.net.Cookies.prototype.cljs$core$IAssociative$_contains_key_QMARK_$arity$2 = (function (c,k){
return c.containsKey(cljs.core.name.call(null,k));
});
goog.net.Cookies.prototype.cljs$core$IFn$ = true;
goog.net.Cookies.prototype.call = (function() {
var G__3958 = null;
var G__3958__2 = (function (self__,k){
var self____$1 = this;
var c = self____$1;
return cljs.core._lookup.call(null,c,k);
});
var G__3958__3 = (function (self__,k,not_found){
var self____$1 = this;
var c = self____$1;
return cljs.core._lookup.call(null,c,k,not_found);
});
G__3958 = function(self__,k,not_found){
switch(arguments.length){
case 2:
return G__3958__2.call(this,self__,k);
case 3:
return G__3958__3.call(this,self__,k,not_found);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
return G__3958;
})()
;
goog.net.Cookies.prototype.apply = (function (self__,args3955){
return self__.call.apply(self__,[self__].concat(args3955.slice()));
});
goog.net.Cookies.prototype.cljs$core$IPrintWithWriter$ = true;
goog.net.Cookies.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (c,writer,opts){
return cljs.core._write.call(null,writer,cljs.core._persistent_BANG_.call(null,c));
});
goog.net.Cookies.prototype.cljs$core$ITransientMap$ = true;
goog.net.Cookies.prototype.cljs$core$ITransientMap$_dissoc_BANG_$arity$4 = (function() { 
var G__3959__delegate = function (c,k,opts){
var temp__3974__auto__ = (function (){var and__3822__auto__ = c.isValidName(cljs.core.name.call(null,k));
if(cljs.core.truth_(and__3822__auto__))
{return cljs.core.name.call(null,k);
} else
{return and__3822__auto__;
}
})();
if(cljs.core.truth_(temp__3974__auto__))
{var k__$1 = temp__3974__auto__;
var map__3956 = cljs.core.apply.call(null,cljs.core.hash_map,opts);
var map__3956__$1 = ((cljs.core.seq_QMARK_.call(null,map__3956))?cljs.core.apply.call(null,cljs.core.hash_map,map__3956):map__3956);
var domain = cljs.core._lookup.call(null,map__3956__$1,"\uFDD0'domain",null);
var path = cljs.core._lookup.call(null,map__3956__$1,"\uFDD0'path",null);
return c.remove(k__$1,path,domain);
} else
{return null;
}
};
var G__3959 = function (c,k,var_args){
var opts = null;
if (goog.isDef(var_args)) {
  opts = cljs.core.array_seq(Array.prototype.slice.call(arguments, 2),0);
} 
return G__3959__delegate.call(this, c, k, opts);
};
G__3959.cljs$lang$maxFixedArity = 2;
G__3959.cljs$lang$applyTo = (function (arglist__3960){
var c = cljs.core.first(arglist__3960);
var k = cljs.core.first(cljs.core.next(arglist__3960));
var opts = cljs.core.rest(cljs.core.next(arglist__3960));
return G__3959__delegate(c, k, opts);
});
G__3959.cljs$lang$arity$variadic = G__3959__delegate;
return G__3959;
})()
;
goog.net.Cookies.prototype.cljs$core$ISeqable$ = true;
goog.net.Cookies.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (c){
return cljs.core.map.call(null,cljs.core.vector,c.getKeys(),c.getValues());
});
goog.net.Cookies.prototype.cljs$core$ICounted$ = true;
goog.net.Cookies.prototype.cljs$core$ICounted$_count$arity$1 = (function (c){
return c.getCount();
});
goog.net.Cookies.prototype.cljs$core$ITransientCollection$ = true;
goog.net.Cookies.prototype.cljs$core$ITransientCollection$_persistent_BANG_$arity$1 = (function (c){
return shoreleave.browser.cookies.as_hash_map.call(null,c);
});
goog.net.Cookies.prototype.cljs$core$ITransientAssociative$ = true;
goog.net.Cookies.prototype.cljs$core$ITransientAssociative$_assoc_BANG_$arity$5 = (function() { 
var G__3961__delegate = function (c,k,v,opts){
var temp__3974__auto__ = (function (){var and__3822__auto__ = c.isValidName(cljs.core.name.call(null,k));
if(cljs.core.truth_(and__3822__auto__))
{return cljs.core.name.call(null,k);
} else
{return and__3822__auto__;
}
})();
if(cljs.core.truth_(temp__3974__auto__))
{var k__$1 = temp__3974__auto__;
var map__3957 = cljs.core.apply.call(null,cljs.core.hash_map,opts);
var map__3957__$1 = ((cljs.core.seq_QMARK_.call(null,map__3957))?cljs.core.apply.call(null,cljs.core.hash_map,map__3957):map__3957);
var secure_QMARK_ = cljs.core._lookup.call(null,map__3957__$1,"\uFDD0'secure?",null);
var domain = cljs.core._lookup.call(null,map__3957__$1,"\uFDD0'domain",null);
var path = cljs.core._lookup.call(null,map__3957__$1,"\uFDD0'path",null);
var max_age = cljs.core._lookup.call(null,map__3957__$1,"\uFDD0'max-age",null);
return c.set(k__$1,v,max_age,path,domain,secure_QMARK_);
} else
{return null;
}
};
var G__3961 = function (c,k,v,var_args){
var opts = null;
if (goog.isDef(var_args)) {
  opts = cljs.core.array_seq(Array.prototype.slice.call(arguments, 3),0);
} 
return G__3961__delegate.call(this, c, k, v, opts);
};
G__3961.cljs$lang$maxFixedArity = 3;
G__3961.cljs$lang$applyTo = (function (arglist__3962){
var c = cljs.core.first(arglist__3962);
var k = cljs.core.first(cljs.core.next(arglist__3962));
var v = cljs.core.first(cljs.core.next(cljs.core.next(arglist__3962)));
var opts = cljs.core.rest(cljs.core.next(cljs.core.next(arglist__3962)));
return G__3961__delegate(c, k, v, opts);
});
G__3961.cljs$lang$arity$variadic = G__3961__delegate;
return G__3961;
})()
;
shoreleave.browser.cookies.cookies = (new goog.net.Cookies(document));
shoreleave.browser.cookies.as_hash_map = (function() {
var as_hash_map = null;
var as_hash_map__0 = (function (){
return as_hash_map.call(null,shoreleave.browser.cookies.cookies);
});
var as_hash_map__1 = (function (cks){
return cljs.core.zipmap.call(null,cks.getKeys(),cks.getValues());
});
as_hash_map = function(cks){
switch(arguments.length){
case 0:
return as_hash_map__0.call(this);
case 1:
return as_hash_map__1.call(this,cks);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
as_hash_map.cljs$lang$arity$0 = as_hash_map__0;
as_hash_map.cljs$lang$arity$1 = as_hash_map__1;
return as_hash_map;
})()
;
/**
* Returns a boolean, true if cookies are currently enabled for the browser
*/
shoreleave.browser.cookies.cookies_enabled_QMARK_ = (function() {
var cookies_enabled_QMARK_ = null;
var cookies_enabled_QMARK___0 = (function (){
return cookies_enabled_QMARK_.call(null,shoreleave.browser.cookies.cookies);
});
var cookies_enabled_QMARK___1 = (function (cks){
return cks.isEnabled();
});
cookies_enabled_QMARK_ = function(cks){
switch(arguments.length){
case 0:
return cookies_enabled_QMARK___0.call(this);
case 1:
return cookies_enabled_QMARK___1.call(this,cks);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cookies_enabled_QMARK_.cljs$lang$arity$0 = cookies_enabled_QMARK___0;
cookies_enabled_QMARK_.cljs$lang$arity$1 = cookies_enabled_QMARK___1;
return cookies_enabled_QMARK_;
})()
;
shoreleave.browser.cookies.empty_BANG_ = (function empty_BANG_(cks){
return cks.clear();
});
