/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2002 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive-support@inria.fr
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://www.inria.fr/oasis/ProActive/contacts.html
 *  Contributor(s):
 *
 * ################################################################
 */
package org.objectweb.proactive.core.group.spmd;

import java.io.Serializable;

/**
 * @author Laurent Baduel
 */
public class MethodBarrier implements Serializable {

	private String[] methodNames;
	private boolean[] arrivedMethods;

	/**
	 * Constructor
	 * @param methodNames - the names of the methods awaited to release the barrier
	 */
	public MethodBarrier(String[] methodNames) {
		this.methodNames = methodNames;
		this.arrivedMethods = new boolean[this.methodNames.length];
		for (int i = 0 ; i < this.arrivedMethods.length ; i++) {
			this.arrivedMethods[i] = false;
		}
	}

	/**
	 * Tests if the barrier is achived or not.
	 * @return <code>true</code> if the barrier is ready to be released
	 */
	public boolean barrierOver () {
		boolean result = true;
		for (int i = 0; ((i<this.arrivedMethods.length) && (result)); i++) {
			result &= this.arrivedMethods[i];
		}
		return result;
	}

	/**
	 * Tests if this barrier wait for the specified method. In that case tag,
	 * the method no more awaited and return <code>true</code>.
	 * @param methodName a method name
	 * @return <code>true</code> if the method was awaited by this barrier,
	 * else <code>false</code>
	 */
	public boolean checkMethod(String methodName) {
		int index = this.indexOf(methodName);
		if (index<0 || this.arrivedMethods[index]) {
			return false;
		}
		else {
			this.arrivedMethods[index] = true;
			return true;
		}
	}
	
	/**
	 * Returns the index of the specified method
	 * @param methodName - the name of the method
	 * @return the index of the method, <code>-1</code> if the method is not
	 * member of this barrier 
	 */
	private int indexOf(String methodName) {
		boolean find = false;
		int i;
		for (i = 0; ((i<this.methodNames.length) && (!find)); i++) {
			find = this.methodNames[i].equals(methodName);
		}
		if (find) {
			return i-1;
		}
		else {
			return -1;
		}
	}
}
