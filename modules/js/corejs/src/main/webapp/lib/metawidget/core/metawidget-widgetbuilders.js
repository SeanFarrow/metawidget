// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

'use strict';

var metawidget = metawidget || {};

/**
 * @namespace WidgetBuilders.
 */

metawidget.widgetbuilder = metawidget.widgetbuilder || {};

/**
 * @class Delegates widget building to one or more sub-WidgetBuilders.
 * <p>
 * Each sub-WidgetBuilder in the list is invoked, in order, calling its <code>buildWidget</code>
 * method. The first result is returned. If all sub-WidgetBuilders return undefined, undefined is
 * returned (the parent Metawidget will generally instantiate a nested Metawidget in this case).
 * <p>
 * Note: the name <em>Composite</em>WidgetBuilder refers to the Composite design pattern.
 */

metawidget.widgetbuilder.CompositeWidgetBuilder = function( config ) {

	if ( ! ( this instanceof metawidget.widgetbuilder.CompositeWidgetBuilder ) ) {
		throw new Error( 'Constructor called as a function' );
	}

	var _widgetBuilders;

	if ( config.widgetBuilders ) {
		_widgetBuilders = config.widgetBuilders;
	} else {
		_widgetBuilders = config;
	}

	this.onStartBuild = function() {

		for ( var loop = 0, length = _widgetBuilders.length; loop < length; loop++ ) {

			var widgetBuilder = _widgetBuilders[loop];

			if ( widgetBuilder.onStartBuild ) {
				widgetBuilder.onStartBuild();
			}
		}
	};

	this.buildWidget = function( attributes, mw ) {

		for ( var loop = 0, length = _widgetBuilders.length; loop < length; loop++ ) {

			var widget;
			var widgetBuilder = _widgetBuilders[loop];

			if ( widgetBuilder.buildWidget ) {
				widget = widgetBuilder.buildWidget( attributes, mw );
			} else {
				widget = widgetBuilder( attributes, mw );
			}

			if ( widget ) {
				return widget;
			}
		}
	};

	this.onEndBuild = function() {

		for ( var loop = 0, length = _widgetBuilders.length; loop < length; loop++ ) {

			var widgetBuilder = _widgetBuilders[loop];

			if ( widgetBuilder.onEndBuild ) {
				widgetBuilder.onEndBuild();
			}
		}
	};
};

/**
 * @class OverriddenWidgetBuilder.
 * 
 * WidgetBuilder to override widgets based on <tt>mw.overriddenNodes</tt>.
 * <p>
 * Widgets are overridden based on id, not name, because name is not legal
 * syntax for many nodes (e.g. <tt>table</tt>).
 */

metawidget.widgetbuilder.OverriddenWidgetBuilder = function() {

	if ( ! ( this instanceof metawidget.widgetbuilder.OverriddenWidgetBuilder ) ) {
		throw new Error( 'Constructor called as a function' );
	}
};

metawidget.widgetbuilder.OverriddenWidgetBuilder.prototype.buildWidget = function( attributes, mw ) {

	if ( mw.overriddenNodes === undefined ) {
		return;
	}

	var overrideId = metawidget.util.getId( attributes, mw );

	for ( var loop = 0, length = mw.overriddenNodes.length; loop < length; loop++ ) {

		var child = mw.overriddenNodes[loop];
		if ( child.nodeType === 1 && child.getAttribute( 'id' ) === overrideId ) {
			child.overridden = true;
			mw.overriddenNodes.splice( loop, 1 );
			return child;
		}
	}
};

/**
 * @class WidgetBuilder for read-only widgets in HTML 5 environments.
 */

metawidget.widgetbuilder.ReadOnlyWidgetBuilder = function() {

	if ( ! ( this instanceof metawidget.widgetbuilder.ReadOnlyWidgetBuilder ) ) {
		throw new Error( 'Constructor called as a function' );
	}
};

metawidget.widgetbuilder.ReadOnlyWidgetBuilder.prototype.buildWidget = function( attributes, mw ) {

	// Not read-only?

	if ( attributes.readOnly !== 'true' ) {
		return;
	}

	// Hidden

	if ( attributes.hidden === 'true' || attributes.type === 'function' ) {
		return document.createElement( 'stub' );
	}

	if ( attributes.lookup !== undefined || attributes.type === 'string' || attributes.type === 'boolean' || attributes.type === 'number' || attributes.type === 'date' ) {

		if ( attributes.masked === 'true' ) {

			// Masked (return a couple of nested Stubs, so that we DO still
			// render a label)

			var stub = document.createElement( 'stub' );
			stub.appendChild( document.createElement( 'stub' ) );
			return stub;
		}

		return document.createElement( 'output' );
	}

	// Not simple, but don't expand

	if ( attributes.dontExpand === 'true' ) {
		return document.createElement( 'output' );
	}
};

/**
 * WidgetBuilder for pure JavaScript environments.
 * <p>
 * Creates native HTML 5 widgets, such as <code>input</code> and
 * <code>select</code>, to suit the inspected fields.
 */

metawidget.widgetbuilder.HtmlWidgetBuilder = function() {

	if ( ! ( this instanceof metawidget.widgetbuilder.HtmlWidgetBuilder ) ) {
		throw new Error( 'Constructor called as a function' );
	}
};

metawidget.widgetbuilder.HtmlWidgetBuilder.prototype.buildWidget = function( attributes, mw ) {

	// Hidden

	if ( attributes.hidden === 'true' ) {
		return document.createElement( 'stub' );
	}

	// Select box

	if ( attributes.lookup !== undefined && attributes.lookup !== '' ) {
		var select = document.createElement( 'select' );

		if ( !attributes.required || attributes.required === 'false' ) {
			select.appendChild( document.createElement( 'option' ) );
		}

		var lookupSplit = attributes.lookup.split( ',' );

		for ( var loop = 0, length = lookupSplit.length; loop < length; loop++ ) {
			var option = document.createElement( 'option' );

			// HtmlUnit needs an 'option' to have a 'value', even if the same as
			// the innerHTML

			option.setAttribute( 'value', lookupSplit[loop] );

			if ( attributes.lookupLabels !== undefined && attributes.lookupLabels != '' ) {
				option.innerHTML = attributes.lookupLabels.split( ',' )[loop];
			} else {
				option.innerHTML = lookupSplit[loop];
			}

			select.appendChild( option );
		}
		return select;
	}

	// Action

	if ( attributes.type === 'function' ) {
		var button = document.createElement( 'button' );

		if ( attributes.label ) {
			button.innerHTML = attributes.label;
		} else {
			button.innerHTML = metawidget.util.uncamelCase( attributes.name );
		}
		return button;
	}

	// Number

	if ( attributes.type === 'number' ) {

		if ( attributes.minimumValue && attributes.maximumValue ) {
			var range = document.createElement( 'input' );
			range.setAttribute( 'type', 'range' );
			range.setAttribute( 'min', attributes.minimumValue );
			range.setAttribute( 'max', attributes.maximumValue );
			return range;
		}

		var number = document.createElement( 'input' );
		number.setAttribute( 'type', 'number' );
		return number;
	}

	// Boolean

	if ( attributes.type === 'boolean' ) {
		var checkbox = document.createElement( 'input' );
		checkbox.setAttribute( 'type', 'checkbox' );
		return checkbox;
	}

	// Date

	if ( attributes.type === 'date' ) {
		var date = document.createElement( 'input' );
		date.setAttribute( 'type', 'date' );
		return date;
	}

	// String

	if ( attributes.type === 'string' ) {

		if ( attributes.masked === 'true' ) {
			var password = document.createElement( 'input' );
			password.setAttribute( 'type', 'password' );

			if ( attributes.maximumLength !== undefined ) {
				password.setAttribute( 'maxlength', attributes.maximumLength );
			}

			return password;
		}

		if ( attributes.large === 'true' ) {
			return document.createElement( 'textarea' );
		}

		var text = document.createElement( 'input' );
		text.setAttribute( 'type', 'text' );

		if ( attributes.maximumLength !== undefined ) {
			text.setAttribute( 'maxlength', attributes.maximumLength );
		}

		return text;
	}

	// Not simple, but don't expand

	if ( attributes.dontExpand === 'true' ) {
		var text = document.createElement( 'input' );
		text.setAttribute( 'type', 'text' );
		return text;
	}
};
