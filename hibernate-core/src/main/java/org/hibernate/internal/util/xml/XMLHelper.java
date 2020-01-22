/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.internal.util.xml;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;

/**
 * Small helper class that lazily loads DOM and SAX reader and keep them for fast use afterwards.
 *
 * @deprecated Currently only used for integration with HCANN.  The rest of Hibernate uses StAX now
 * for XML processing.  See {@link org.hibernate.boot.jaxb.internal.stax}
 */
@Deprecated
public final class XMLHelper {
	private final DocumentFactory documentFactory;

	public XMLHelper() {
		PrivilegedAction<DocumentFactory> action = new PrivilegedAction<DocumentFactory>() {
			public DocumentFactory run() {
				final ClassLoader originalTccl = Thread.currentThread().getContextClassLoader();
				try {
					// We need to make sure we get DocumentFactory
					// loaded from the same ClassLoader that loads
					// Hibernate classes, to make sure we get the
					// proper version of DocumentFactory, This class
					// is "internal", and should only be used for XML
					// files generated by Envers.

					// Using the (Hibernate) ClassLoader that loads
					// this Class will avoid collisions in the case
					// that DocumentFactory can be loaded from,
					// for example, the application ClassLoader.
					Thread.currentThread().setContextClassLoader( this.getClass().getClassLoader() );
					return DocumentFactory.getInstance();
				}
				finally {
					Thread.currentThread().setContextClassLoader( originalTccl );
				}
			}
		};

		this.documentFactory = System.getSecurityManager() != null
				? AccessController.doPrivileged( action )
				: action.run();
	}

	public DocumentFactory getDocumentFactory() {
		return documentFactory;
	}

	public SAXReader createSAXReader(ErrorLogger errorLogger, EntityResolver entityResolver) {
		SAXReader saxReader = new SAXReader();
		saxReader.setMergeAdjacentText( true );
		saxReader.setValidation( true );
		saxReader.setErrorHandler( errorLogger );
		saxReader.setEntityResolver( entityResolver );

		return saxReader;
	}
}
