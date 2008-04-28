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

package org.metawidget.test.util;

import junit.framework.TestCase;

import org.metawidget.util.StringUtils;

/**
 * @author Richard Kennard
 */

public class StringUtilsTest
	extends TestCase
{
	//
	//
	// Public statics
	//
	//

	public static void testStringUtils()
		throws Exception
	{
		assertTrue( false == StringUtils.isFirstLetterUppercase( "" ));
		assertTrue( null == StringUtils.uncamelCase( null ));
		assertTrue( "Camel cased".equals( StringUtils.uncamelCase( "camelCased" )));
		assertTrue( "Camel CASED".equals( StringUtils.uncamelCase( "camelCASED" )));
	}

	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public StringUtilsTest( String name )
	{
		super( name );
	}
}
