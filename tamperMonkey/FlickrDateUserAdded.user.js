// ==UserScript==
// @name        FlickrDateUserAdded
// @description Shows when a photo was added to a group pool
// @namespace   http://thecamaras.net
// @require     http://code.jquery.com/jquery-1.11.1.min.js
// @require     https://rawgit.com/notifyjs/notifyjs/master/dist/notify.js
// @include     http*://www.flickr.com/photos/*
// @grant       GM_addStyle
// @grant       GM_xmlhttpRequest
// @connect     localhost
// @version     1
// ==/UserScript==

jQuery.noConflict();

function getJSVariable (regex) {
    // Credit for this function goes to Alesa Dam
    // Some slight modifications for use with jQuery
    var retval;
    jQuery('script').each( function (i,script) {
        if (retval !== undefined) {
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


function formatDate(seconds) {
    var now = new Date();
    now = now.getTime();//+now.getTimezoneOffset() * 60*1000;
    var when = (now/1000) - seconds;
    var days = parseInt(when/(3600*24));
    if(days > 10) {
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
    jQuery.getJSON('https://www.flickr.com/services/rest/?method=flickr.people.getPhotos&user_id='+uid+'&api_key='+key+'&per_page='+perpage+'&page='+page+'&extras=date_upload&format=json&nojsoncallback=1&csrf='+authhash,function (data) {
        jQuery.each(data.photos.photo,function(i,item) {
            jQuery('div[style*="'+item.id +'_' + item.secret +'"] > div').before('<div class="dateoverlay">'+formatDate(item.dateupload)+'</div>');
        });
    });
}

GM_addStyle("div.dateoverlay {color:white;font-size:10px;display: block; height: 14px; left: 5px; position: absolute; top: 8px; width: 40px; text-shadow: 1px 1px 0 black; z-index:9000}");
GM_addStyle(".comment .comment-content img {display:none !important;}");
GM_addStyle(".notifyjs-corner {z-index: 4050 !important;}");

var key = YUI_config.flickr.api.site_key;
var uid = getJSVariable(/nsid":"(.*?)",/);
var photoId = getJSVariable(/photoId":"(.*?)",/);

var authhash = getJSVariable(/csrf":"(.*?)",/);
authhash += "&reqId=" + reqId;

var page = 1;
var perpage = 200;
if (document.location.href.indexOf('/page') != -1 ) {
    var newpage = document.location.href.match(/\/page(\d+)/)[1];
    if (newpage) {
        page = newpage*1.0-1;
    }
}


getPage(page);
jQuery(document).bind("DOMNodeInserted", function(e) {
    if (jQuery(e.target).is('div.photo-display-container')) {
        setTimeout(function () {getPage(1);}, 4000);
    }
});
window = getPage;
jQuery("body").append("<div id=\"camaraLink\" style=\"position: fixed; top: 0px; z-index: 3000;\"></div>");
jQuery("ul.nav-menu").append("<li role=\"menuitem\"><a id=\"dateLink\" href=\"#\">Update Dates</a></li>");
jQuery("ul.nav-menu").append("<li role=\"menuitem\"><a href=\"http://localhost:8888/download/user/" + uid +"\">Download All</a></li>");
jQuery("#camaraLink").append("<a id=\"camaraDown\" href=\"#\">Download</a>");

jQuery("#dateLink").click(function(e){
    e.preventDefault();
    getPage(page);
});

function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}
jQuery("#camaraDown").click(function(e){
    e.preventDefault();
    var cPhotoId = jQuery('a.currentImage[data-photo-id]').attr("data-photo-id");
    if (!cPhotoId){
        return;
    }
    jQuery("#camaraDown").css("color", getRandomColor());

    var downloadUrl = "http://localhost:8888/download/photo/";
    downloadUrl += cPhotoId;
    if (e.ctrlKey){
        downloadUrl += "?useCommon=false";
    }
    if (e.altKey){
        if (e.ctrlKey) {
            downloadUrl += "&";
        }
        else {
            downloadUrl += "?";
        }
        downloadUrl += "ignoreSize=true";
    }

    GM_xmlhttpRequest({
        method: "GET",
        url: downloadUrl,
        onload: function(response) {
            jQuery.notify(response.response, {
                className: "success"
            });
        }
    });
});
jQuery("body").keypress(function(e) {
    if (e.which == 61) {
        getPage(page);
    }
});
