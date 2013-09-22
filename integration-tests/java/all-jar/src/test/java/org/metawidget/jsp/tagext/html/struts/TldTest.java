// Metawidget (licensed under LGPL)
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

package org.metawidget.jsp.tagext.html.struts;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.metawidget.util.IOUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TldTest
	extends TestCase {

	//
	// Public methods
	//

	public void testTld()
		throws Exception {

		InputStream in = ClassLoader.getSystemResourceAsStream( "META-INF/metawidget-struts.tld" );
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOUtils.streamBetween( in, out );
		Document document = XmlUtils.documentFromString( out.toString() );

		Element root = document.getDocumentElement();
		assertEquals( root.getNodeName(), "taglib" );

		// org.metawidget.jsp.tagext.html.struts.StrutsMetawidgetTag

		Element tag = XmlUtils.getChildNamed( root, "tag" );
		Element tagclass = XmlUtils.getChildNamed( tag, "tagclass" );
		assertEquals( tagclass.getTextContent(), "org.metawidget.jsp.tagext.html.struts.StrutsMetawidgetTag" );

		Element bodycontent = XmlUtils.getSiblingNamed( tagclass, "bodycontent" );
		assertEquals( bodycontent.getTextContent(), "JSP" );

		// org.metawidget.jsp.tagext.FacetTag

		tag = XmlUtils.getSiblingNamed( tag, "tag" );
		tagclass = XmlUtils.getChildNamed( tag, "tagclass" );
		assertEquals( tagclass.getTextContent(), "org.metawidget.jsp.tagext.FacetTag" );

		bodycontent = XmlUtils.getSiblingNamed( tagclass, "bodycontent" );
		assertEquals( bodycontent.getTextContent(), "JSP" );

		// org.metawidget.jsp.tagext.html.struts.StrutsStubTag

		tag = XmlUtils.getSiblingNamed( tag, "tag" );
		tagclass = XmlUtils.getChildNamed( tag, "tagclass" );
		assertEquals( tagclass.getTextContent(), "org.metawidget.jsp.tagext.html.struts.StrutsStubTag" );

		bodycontent = XmlUtils.getSiblingNamed( tagclass, "bodycontent" );
		assertEquals( bodycontent.getTextContent(), "JSP" );
	}
}