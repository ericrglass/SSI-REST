/**
* Html5 Placeholder Polyfill - v2.0.7 - 2013-06-08
* @requires jQuery - tested with 1.6.2 but might as well work with older versions
* 
* current code: https://github.com/ericrglass/HTML5-placeholder-polyfill.git
* 
* original code: https://github.com/ginader/HTML5-placeholder-polyfill
* please report issues at: https://github.com/ginader/HTML5-placeholder-polyfill/issues
*
* Copyright (c) 2012 Dirk Ginader (ginader.de)
* Dual licensed under the MIT and GPL licenses:
* http://www.opensource.org/licenses/mit-license.php
* http://www.gnu.org/licenses/gpl.html
* 
*/

(function($) {
    var debug = false;
    function showPlaceholderIfEmpty(input,options) {
        if( input.val() === '' ){
            input.data('placeholder').removeClass(options.hideClass);
        }else{
            input.data('placeholder').addClass(options.hideClass);
        }
    }
    function hidePlaceholder(input,options){
        input.data('placeholder').addClass(options.hideClass);
    }
    function positionPlaceholder(placeholder,input){
        var ta  = input.is('textarea');
        var pt = parseFloat(input.css('padding-top'));
        var pl = parseFloat(input.css('padding-left'));

        // Determine if we need to shift the header down more.
        var offset = input.offset();
        if (pt) {
            offset.top += pt;
        }
        if (pl) {
            offset.left += pl;
        }

        placeholder.css({
            width : input.innerWidth()-(ta ? 20 : 4),
            height : input.innerHeight()-6,
            lineHeight : input.css('line-height'),
            whiteSpace : ta ? 'normal' : 'nowrap',
            overflow : 'hidden'
        }).offset(offset);
    }
    function startFilledCheckChange(input,options){
      var val = input.val();
      var chkOptions = options;
      input.bind('keyup.placeHolder_polyfill', function(event) {
        if($(this).val() !== val){
          hidePlaceholder($(this),chkOptions);
          $(this).unbind('keyup.placeHolder_polyfill');
          $(this).bind('keyup.placeHolder_polyfill', function(event) {
            if( $(this).val() === '' ){
              showPlaceholderIfEmpty($(this),chkOptions);
              $(this).unbind('keyup.placeHolder_polyfill');
              startFilledCheckChange($(this),chkOptions);
            }
          });
        }
      });
    }
    function stopCheckChange(input){
      input.unbind('keyup.placeHolder_polyfill');
    }
    function log(msg){
        if(debug && window.console && window.console.log){
            window.console.log(msg);
        }
    }

    $.fn.placeHolder = function(config) {
        log('init placeHolder');
        var o = this;
        this.options = $.extend({
            className: 'placeholder', // css class that is used to style the placeholder
            visibleToScreenreaders : true, // expose the placeholder text to screenreaders or not
            visibleToScreenreadersHideClass : 'placeholder-hide-except-screenreader', // css class is used to visually hide the placeholder
            visibleToNoneHideClass : 'placeholder-hide', // css class used to hide the placeholder for all
            hideOnFocus : false, // either hide the placeholder on focus or on type
            removeLabelClass : 'visuallyhidden', // remove this class from a label (to fix hidden labels)
            hiddenOverrideClass : 'visuallyhidden-with-placeholder', // replace the label above with this class
            forceHiddenOverride : true, // allow the replace of the removeLabelClass with hiddenOverrideClass or not
            forceApply : false, // apply the polyfill even for browser with native support
            autoInit : true // init automatically or not
        }, config);
        this.options.hideClass = this.options.visibleToScreenreaders ? this.options.visibleToScreenreadersHideClass : this.options.visibleToNoneHideClass;
        if((typeof $.attrHooks !== 'undefined') || (typeof $.propHooks !== 'undefined')){
          var ph_hooks = {
            get: function(elem) {
                if (elem.nodeName.toLowerCase() === 'input' || elem.nodeName.toLowerCase() === 'textarea') {
                    if( $(elem).data('placeholder') ){
                        // has been polyfilled
                        return $( $(elem).data('placeholder') ).text();
                    }else{
                        // native / not yet polyfilled
                        return $(elem)[0].placeholder;
                    }
                }else{
                    return undefined;
                }
            },
            set: function(elem, value){
                return $( $(elem).data('placeholder') ).text(value);
            }
          };
          if(typeof $.attrHooks !== 'undefined'){
            $.attrHooks.placeholder = ph_hooks;
          }
          if(typeof $.propHooks !== 'undefined'){
        	$.propHooks.placeholder = ph_hooks;
          }
        }
        if(typeof $.valHooks !== 'undefined'){
          var val_hooks = {
            get: function(elem) {
              return elem.value;
            },
            set: function(elem, value) {
			  elem.value = value;
              if( !$(elem).data('placeholder') ){
                return;
              }
              if(value == ''){
                $(elem).triggerHandler('placeholderForceShow');
              } else {
                $(elem).triggerHandler('placeholderForceHide');
              }
            }
          };
          $.valHooks.input = val_hooks;
          $.valHooks.textarea = val_hooks;
        }
        $('form').bind('reset', function(event) {
          $(this).find('input, textarea').each(function() {
            if( !$(this).data('placeholder') ){
              return;
            }
            $(this).triggerHandler('placeholderForceShow');
          });
        });
        return $(this).each(function(index) {
            var input = $(this),
                text = input.attr('placeholder'),
                id = input.attr('id'),
                label,placeholder,titleNeeded,polyfilled;

            function onFocusIn() {
                if(!o.options.hideOnFocus){
                    startFilledCheckChange(input,o.options);
                }else{
                    hidePlaceholder(input,o.options);
                }
            }

            if(text === "" || text === undefined) {
              text = input[0].attributes["placeholder"].value;
            }
            label = input.closest('label');
            input.removeAttr('placeholder');
            if(!label.length && !id){
                log('the input element with the placeholder needs an id!');
                return;
            }
            label = label.length ? label : $('label[for="'+id+'"]').first();
            if(!label.length){
                log('the input element with the placeholder needs a label!');
                return;
            }
            polyfilled = $(label).find('.placeholder');
            if(polyfilled.length) {
                //log('the input element already has a polyfilled placeholder!');
                positionPlaceholder(polyfilled,input);
                polyfilled.text(text);
                return input;
            }
            if(label.hasClass(o.options.removeLabelClass)){
                label.removeClass(o.options.removeLabelClass)
                     .addClass(o.options.hiddenOverrideClass);
            }

            placeholder = $('<span>').addClass(o.options.className).text(text).appendTo(label);

            titleNeeded = (placeholder.width() > input.width());
            if(titleNeeded){
                placeholder.attr('title',text);
            }
            positionPlaceholder(placeholder,input);
            input.data('placeholder',placeholder);
            placeholder.data('input',input);
            placeholder.click(function(){
                $(this).data('input').focus();
            });
            input.focusin(onFocusIn);
            input.focusout(function(){
                showPlaceholderIfEmpty($(this),o.options);
                if(!o.options.hideOnFocus){
                  stopCheckChange($(this));
                }
            });
            input.bind('placeholderForceHide', function(){
              $(this).data('placeholder').addClass(o.options.hideClass);
            });
            input.bind('placeholderForceShow', function(){
              $(this).data('placeholder').removeClass(o.options.hideClass);
            });
            showPlaceholderIfEmpty(input,o.options);

            // reformat on window resize and optional reformat on font resize - requires: http://www.tomdeater.com/jquery/onfontresize/
            $(document).bind("fontresize resize", function(){
                positionPlaceholder(placeholder,input);
            });

            // optional reformat when a textarea is being resized - requires http://benalman.com/projects/jquery-resize-plugin/
            if($.event.special.resize){
                $("textarea").bind("resize", function(){
                    positionPlaceholder(placeholder,input);
                });
            }else{
                // we simply disable the resizeablilty of textareas when we can't react on them resizing
                $("textarea").css('resize','none');
            }

            if (input.is(":focus")) {
                onFocusIn();
            }
        });
    };
    $(function(){
        var config = window.placeHolderConfig || {};
        if(config.autoInit === false){
            log('placeholder:abort because autoInit is off');
            return;
        }
        if(('placeholder' in $('<input>')[0] || 'placeHolder' in $('<input>')[0]) && !config.forceApply){ // don't run the polyfill when the browser has native support
            log('placeholder:abort because browser has native support');
            return;
        }
        $('input[placeholder], textarea[placeholder]').placeHolder(config);
    });
}(jQuery));
