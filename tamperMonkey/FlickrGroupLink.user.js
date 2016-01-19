// ==UserScript==
// @name        FlickrGroupLink
// @description Shows when a photo was added to a group pool
// @namespace   http://vispillo.org
// @require     http://code.jquery.com/jquery-1.11.1.min.js
// @include     http*://www.flickr.com/groups
// @grant       GM_addStyle
// @version     1
// ==/UserScript==

jQuery.noConflict();

jQuery("body").keypress(function(e) {
    if (e.which == 61) {
        jQuery('td a[href^="/groups/"]').attr("href", function(index, href){
            return href + "pool/";
        }); 
    }
});