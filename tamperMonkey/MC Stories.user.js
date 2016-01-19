// ==UserScript==
// @name       MC Stories
// @namespace  http://use.i.E.your.homepage/
// @version    0.1
// @description  enter something useful
// @match      http://www.mcstories.com/*
// @match      http://mcstories.com/*
// @copyright  2012+, You
// ==/UserScript==

function addGlobalStyle(css) {
    var head, style;
    head = document.getElementsByTagName('head')[0];
    if (!head) { return; }
    style = document.createElement('style');
    style.type = 'text/css';
    style.innerHTML = css;
    head.appendChild(style);
}

addGlobalStyle('body { background: white !important; }');
addGlobalStyle('article{ background: #FFFFFF !important; }');
addGlobalStyle('article{ max-width: 50em !important; }');