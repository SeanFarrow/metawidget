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

import org.metawidget.util.ClassUtils;

/**
 * @author Richard Kennard
 */

public class ClassUtilsTest
	extends TestCase
{
	//
	//
	// Public statics
	//
	//

	public static void testObjectPrimitive()
		throws Exception
	{
		assertTrue( ClassUtils.isObjectPrimitive( Long.class ) );
		assertTrue( ClassUtils.isObjectPrimitive( Boolean.class ) );
		assertTrue( !ClassUtils.isObjectPrimitive( String.class ) );
	}

	public static void testProperties()
		throws Exception
	{
		Foo foo = new Foo();
		Baz baz = new Baz();

		ClassUtils.setProperty( foo, "bar", baz );
		assertTrue( ClassUtils.getProperty( foo, "bar" ).equals( baz ));

		try
		{
			ClassUtils.setProperty( foo, "bar1", baz );
			assertTrue( false );
		}
		catch( Exception e )
		{
			assertTrue( "No such method setBar1( org.metawidget.test.util.ClassUtilsTest$Baz ) on class org.metawidget.test.util.ClassUtilsTest$Foo".equals( e.getCause().getMessage() ));
		}

		try
		{
			ClassUtils.getProperty( foo, "bar1" );
			assertTrue( false );
		}
		catch( Exception e )
		{
			assertTrue( "No such method getBar1() or isBar1() on class org.metawidget.test.util.ClassUtilsTest$Foo".equals( e.getCause().getMessage() ));
		}
	}

	//
	//
	// Inner class
	//
	//

	protected static class Foo
	{
		//
		//
		// Private members
		//
		//

		private Bar mBar;

		//
		//
		// Public methods
		//
		//

		public Bar getBar()
		{
			return mBar;
		}

		public void setBar( Bar bar )
		{
			mBar = bar;
		}
	}

	static interface Bar
	{
		// Just a marker interface
	}

	static class Baz
		implements Bar
	{
		// Just an empty class
	}

	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public ClassUtilsTest( String name )
	{
		super( name );
	}
}
