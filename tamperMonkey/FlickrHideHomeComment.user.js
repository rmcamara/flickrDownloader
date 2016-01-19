// ==UserScript==
// @name        FlickrHideHomeComment
// @description Shows when a photo was added to a group pool
// @namespace   http://thecamaras.net
// @require     http://code.jquery.com/jquery-1.11.1.min.js
// @include     http*://www.flickr.com/
// @grant       GM_addStyle
// @version     1
// ==/UserScript==

jQuery.noConflict();


GM_addStyle("#activityFeed .card.micro-card.card-comment {display:none !important;}");
GM_addStyle("#activityFeed .card.micro-card.card-comment[data-photo-owner=\"26636301@N02\"]  {display:block !important;}");

