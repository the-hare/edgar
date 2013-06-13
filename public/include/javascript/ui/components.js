goog.provide('ui.components');
goog.require('cljs.core');
goog.require('jayq.core');
goog.require('cljs.reader');
goog.require('jayq.core');
goog.require('jayq.core');
ui.components.populate_multiselect = (function populate_multiselect(selector,options){
var G__103825 = (new cljs.core.Keyword("\uFDD0'bind")).call(null,jayq.core.deferred_m);
var G__103826 = (new cljs.core.Keyword("\uFDD0'return")).call(null,jayq.core.deferred_m);
var G__103827 = (new cljs.core.Keyword("\uFDD0'zero")).call(null,jayq.core.deferred_m);
return G__103825.call(null,$.ajax.call(null,"/list-filtered-input"),(function (filtered_input){
return G__103826.call(null,(function (){var multiselect = jayq.core.$.call(null,selector);
cljs.core.reduce.call(null,(function (rslt,inp){
var option_value = cljs.core.second.call(null,inp);
var option_label = cljs.core.nth.call(null,inp,2);
var price_difference = cljs.core.first.call(null,inp).toFixed(2);
return multiselect.append([cljs.core.str("<option value='"),cljs.core.str(option_value),cljs.core.str("'>"),cljs.core.str(option_label),cljs.core.str(" ("),cljs.core.str(price_difference),cljs.core.str(")</option>")].join(''));
}),null,cljs.core.into_array.call(null,cljs.reader.read_string.call(null,filtered_input)));
return jayq.core.$.call(null,selector).multiselect(cljs.core.clj__GT_js.call(null,cljs.core.merge.call(null,cljs.core.ObjMap.fromObject(["\uFDD0'enableFiltering"],{"\uFDD0'enableFiltering":true}),options)));
})());
}));
});
