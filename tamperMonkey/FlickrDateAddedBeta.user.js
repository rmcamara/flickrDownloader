// ==UserScript==
// @name        FlickrDateAddedBeta
// @description Shows when a photo was added to a group pool
// @namespace   http://vispillo.org
// @require     http://code.jquery.com/jquery-1.11.1.min.js
// @include     http*://www.flickr.com/groups/*/pool/*
// @include     http*://www.flickr.com/groups/*
// @grant       GM_addStyle
// @version     2
// ==/UserScript==

jQuery.noConflict();

function getJSVariable (regex) {
    // Credit for this function goes to Alesa Dam
    // Some slight modifications for use with jQuery
    var retval;
    jQuery('script').each( function (i,script) {
        if (retval != undefined) {
            return;
        }
        var html = script.innerHTML;
        try {
            retval = html.match(regex)[1];
        } catch (e) {
        }
    });
    return retval;
}

function addGlobalStyle(css) {
    var head, style;
    head = document.getElementsByTagName('head')[0];
    if (!head) { return; }
    style = document.createElement('style');
    style.type = 'text/css';
    style.innerHTML = css;
    head.appendChild(style);
}

function formatDate(seconds) {
    var now = new Date();
    now = now.getTime(); //+now.getTimezoneOffset() * 60*1000;
    var when = (now/1000) - seconds;
    var days = parseInt(when/(3600*24));
    if(days > 4) {
        var date = new Date(seconds*1000);
        return date.toLocaleDateString();
    } else {
        var ret = '';
        if(days > 0)
            ret = days + ' day'+((days>1)?'s':'');
        var rest = when - days*3600*24;
        if(when%(3600*24) > 0) {
            if(rest/3600 > 1) {
                if(ret) ret += ', ';
                ret += parseInt(rest/3600)+' h';
            } else if(days < 1)
                ret = "less than an hour";
                } else if(days < 1)
                    ret = "less than an hour";
            return ret +' ago';
    }
}

function getPage (page) {
    if (document.location.href.indexOf('/pool/') == -1) {
        page = 1;
    }
    else if (document.location.href.indexOf('pool/page') != -1 ) {
        page = document.location.href.match(/pool\/page(\d+)/)[1];
    }
    
	jQuery.getJSON('https://www.flickr.com/services/rest/?method=flickr.groups.pools.getPhotos&group_id='+gid+'&api_key='+key+'&per_page='+perpage+'&page='+page+'&format=json&nojsoncallback=1&hermes=1&hermesClient=1&csrf='+authhash,function (data) {
    var lastITem
    jQuery.each(data.photos.photo,function(i,item) {
        jQuery('a[href*="'+item.id+'"].overlay').before('<div class="dateoverlay">'+formatDate(item.dateadded)+' P:' + page + '</div>');
    });
//    yPos = jQuery('#photo_'+data.photos.photo[0].id+' > div > span.photo_container').offset().top;
//    scroll(0, yPos-2300);
  });
}

GM_addStyle("div.dateoverlay {color:white;font-size:10px;display: block; height: 14px; left: 5px; position: absolute; top: 8px; width: 40px; text-shadow: 1px 1px 0 black; z-index:9000}");

var key = YUI_config.flickr.api.site_key;
var mode = jQuery('#thumb-options ul li a.selected').text();
var gid = getJSVariable(/groupPoolId":"(.*?)",/);

var uid = document.location.href.match(/pool\/([0-9A-Z\@]*?)\//);
if (uid && uid.length > 1){
    gid = gid.split("-")[0];
    gid += "&user_id=" + uid[1];
}   

var authhash = getJSVariable(/csrf":"(.*?)",/);
authhash += "&reqId=" + reqId;

var page = 1;
var perpage = 100;
var str = '<br />';


getPage(page);
jQuery("body").keypress(function(e) {
	if (e.which == 61) {
        getPage(page);
	}    
});

jQuery(".nav-menu").after("<a href=\"" + document.location.href + "26636301@N02/\">My Photos</a>")
window = getPage;
jQuery(".nav-menu").after("<a id=\"dateLink\" href=\"#\">Update Dates</a>");
jQuery("#dateLink").click(function(e){
    e.preventDefault();
    getPage(page);
});

addGlobalStyle('.ui-display-tile .meta-bar{ opacity: 100 !important; }');
//addGlobalStyle('.ui-display-widget-meta .title{ opacity: 100 !important; }');
addGlobalStyle('.ui-display-widget-meta .attribution{ opacity: 100 !important; color: rgba(255,255,255,0.8) !important;}');
addGlobalStyle('.ui-display-widget-meta .attribution a{ color: rgba(255,255,255,0.9) !important;}');
addGlobalStyle('.photo-list-photo-view .interaction-view{ opacity: 100 !important; }');

