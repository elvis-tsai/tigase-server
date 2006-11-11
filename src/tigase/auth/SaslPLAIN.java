/*  Package Jabber Server
 *  Copyright (C) 2001, 2002, 2003, 2004, 2005
 *  "Artur Hefczyc" <kobit@users.sourceforge.net>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software Foundation,
 *  Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * $Rev$
 * Last modified by $Author$
 * $Date$
 */
package tigase.auth;

import java.util.Arrays;
import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

/**
 * Describe class SaslPLAIN here.
 *
 *
 * Created: Mon Nov  6 09:02:31 2006
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev$
 */
public class SaslPLAIN implements SaslServer {

	private static final String MECHANISM = "PLAIN";

	private Map<? super String,?> props = null;
	private CallbackHandler callbackHandler = null;

	private boolean auth_ok = false;

	/**
	 * Creates a new <code>SaslPLAIN</code> instance.
	 *
	 */
	public SaslPLAIN() {}

	protected SaslPLAIN(Map<? super String,?> props,
		CallbackHandler callbackHandler) {
		this.props = props;
		this.callbackHandler = callbackHandler;
	}

	// Implementation of javax.security.sasl.SaslServer

	/**
	 * Describe <code>unwrap</code> method here.
	 *
	 * @param byteArray a <code>byte[]</code> value
	 * @param n an <code>int</code> value
	 * @param n1 an <code>int</code> value
	 * @return a <code>byte[]</code> value
	 * @exception SaslException if an error occurs
	 */
	public byte[] unwrap(final byte[] byteArray, final int n, final int n1)
		throws SaslException {
		return null;
	}

	/**
	 * Describe <code>evaluateResponse</code> method here.
	 *
	 * @param byteArray a <code>byte[]</code> value
	 * @return a <code>byte[]</code> value
	 * @exception SaslException if an error occurs
	 */
	public byte[] evaluateResponse(final byte[] byteArray) throws SaslException {

		StringBuilder authz = new StringBuilder();
		int auth_idx = 0;
		while (byteArray[auth_idx] != 0 && auth_idx < byteArray.length)
		{ ++auth_idx;	}
		String authoriz = new String(byteArray, 0, auth_idx);
		int user_idx = ++auth_idx;
		while (byteArray[user_idx] != 0 && user_idx < byteArray.length)
		{ ++user_idx;	}
		String user_id = new String(byteArray, auth_idx, user_idx - auth_idx);
		++user_idx;
		String passwd =
			new String(byteArray, user_idx, byteArray.length - user_idx);

		if (callbackHandler == null) {
			throw new SaslException("Error: no CallbackHandler available.");
		}
		Callback[] callbacks = new Callback[3];
		NameCallback nc = new NameCallback("User name", user_id);
		PasswordCallback pc = new PasswordCallback("User password", false);
		AuthorizeCallback ac = new AuthorizeCallback(user_id, authoriz);
		callbacks[0] = nc;
		callbacks[1] = pc;
		callbacks[2] = ac;
		try {
			callbackHandler.handle(callbacks);
			if (ac.isAuthorized()) {
				char[] real_password = pc.getPassword();
				if (Arrays.equals(real_password, passwd.toCharArray())) {
					auth_ok = true;
				} else {
					throw new SaslException("Password missmatch.");
				}
			} else {
				throw new SaslException("Not authorized.");
			} // end of else
		} catch (Exception e) {
			throw new SaslException("Authorization error.", e);
		} // end of try-catch

		return null;
	}

	/**
	 * Describe <code>getAuthorizationID</code> method here.
	 *
	 * @return a <code>String</code> value
	 */
	public String getAuthorizationID() {
		return null;
	}

	/**
	 * Describe <code>getMechanismName</code> method here.
	 *
	 * @return a <code>String</code> value
	 */
	public String getMechanismName() {
		return MECHANISM;
	}

	/**
	 * Describe <code>getNegotiatedProperty</code> method here.
	 *
	 * @param string a <code>String</code> value
	 * @return an <code>Object</code> value
	 */
	public Object getNegotiatedProperty(final String string) {
		return null;
	}

	/**
	 * Describe <code>isComplete</code> method here.
	 *
	 * @return a <code>boolean</code> value
	 */
	public boolean isComplete() {
		return auth_ok;
	}

	/**
	 * Describe <code>wrap</code> method here.
	 *
	 * @param byteArray a <code>byte[]</code> value
	 * @param n an <code>int</code> value
	 * @param n1 an <code>int</code> value
	 * @return a <code>byte[]</code> value
	 * @exception SaslException if an error occurs
	 */
	public byte[] wrap(final byte[] byteArray, final int n, final int n1)
		throws SaslException {
		return null;
	}

	/**
	 * Describe <code>dispose</code> method here.
	 *
	 * @exception SaslException if an error occurs
	 */
	public void dispose() throws SaslException {
		props = null;
		callbackHandler = null;
	}

} // SaslPLAIN
