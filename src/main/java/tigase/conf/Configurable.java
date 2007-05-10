/*
 *  Package Tigase XMPP/Jabber Server
 *  Copyright (C) 2004, 2005, 2006
 *  "Artur Hefczyc" <artur.hefczyc@tigase.org>
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

package tigase.conf;

import java.util.Map;
import tigase.server.ServerComponent;

/**
 * Interface Configurable
 *
 * Objects inheriting this interface can be configured. In Tigase system object
 * can't request configuration properties. Configuration of the object is passed
 * to it at some time. Actually it can be passed at any time. This allows
 * dynamic system reconfiguration at runtime.
 *
 * Created: Tue Nov 22 07:07:11 2005
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev$
 */
public interface Configurable extends ServerComponent {

	public static final String GEN_CONFIG = "--gen-config";
	public static final String GEN_CONFIG_ALL = "--gen-config-all";
	public static final String GEN_CONFIG_SM = "--gen-config-sm";
	public static final String GEN_CONFIG_CS = "--gen-config-cs";
	public static final String GEN_CONFIG_DEF = "--gen-config-default";

	public static final String GEN_TEST = "--test";
	public static final String GEN_EXT_COMP = "--ext-comp";
	public static final String GEN_USER_DB = "--user-db";
	public static final String GEN_AUTH_DB = "--auth-db";
	public static final String GEN_USER_DB_URI = "--user-db-uri";
	public static final String GEN_AUTH_DB_URI = "--auth-db-uri";
	public static final String GEN_ADMINS = "--admins";
	public static final String GEN_VIRT_HOSTS = "--virt-hosts";
	public static final String GEN_DEBUG = "--debug";

	/**
	 * Get object name. This name corresponds to section in configuration.
	 *
	 * @return a <code>String</code> value of object name.
	 */
	String getName();

  /**
   * Sets all configuration properties for the object.
   */
	void setProperties(Map<String, Object> properties);

  /**
   * Returns defualt configuration settings for this object.
   */
	Map<String, Object> getDefaults(Map<String, Object> params);

}
