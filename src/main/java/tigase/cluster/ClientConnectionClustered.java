/*
 *   Tigase Jabber/XMPP Server
 *  Copyright (C) 2004-2008 "Artur Hefczyc" <artur.hefczyc@tigase.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://www.gnu.org/licenses/.
 *
 * $Rev$
 * Last modified by $Author$
 * $Date$
 */

package tigase.cluster;

//~--- non-JDK imports --------------------------------------------------------

import tigase.cluster.api.ClusterControllerIfc;
import tigase.cluster.api.ClusteredComponentIfc;
import tigase.server.ServiceChecker;
import tigase.server.xmppclient.ClientConnectionManager;
import tigase.server.xmppclient.SeeOtherHostIfc;

import tigase.xmpp.JID;
import tigase.xmpp.XMPPIOService;
import tigase.xmpp.BareJID;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Collections;

//~--- classes ----------------------------------------------------------------

/**
 * Describe class ClientConnectionClustered here.
 * 
 * 
 * Created: Sat Jun 21 22:23:18 2008
 * 
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev$
 */
public class ClientConnectionClustered extends ClientConnectionManager implements
		ClusteredComponentIfc {

	/**
	 * Variable <code>log</code> is a class logger.
	 */
	private static final Logger log = Logger.getLogger(ClientConnectionClustered.class
			.getName());

	private SeeOtherHostIfc see_other_host_strategy = null;

	final ArrayList<BareJID> connectedNodes = new ArrayList<BareJID>() {
		{
			add(getDefHostName());
		}
	};

	// ~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param node
	 */
	@Override
	public void nodeConnected(String node) {
		BareJID nodeJID = BareJID.bareJIDInstanceNS(null, node);

		if (!connectedNodes.contains(nodeJID)) {
			connectedNodes.add(nodeJID);

			Collections.sort(connectedNodes);
		}
	}

	/**
	 * 
	 * @param node
	 */
	@Override
	public void nodeDisconnected(String node) {
		if (log.isLoggable(Level.FINEST)) {
			log.log(Level.FINEST, "Disconnected nodes: {0}", node);
		}

		BareJID nodeJID = BareJID.bareJIDInstanceNS(null, node);

		if (connectedNodes.contains(nodeJID)) {
			connectedNodes.remove(nodeJID);
		}

		final String hostname = node;

		doForAllServices(new ServiceChecker<XMPPIOService<Object>>() {
			@Override
			public void check(XMPPIOService<Object> service) {
				JID dataReceiver = service.getDataReceiver();

				if (log.isLoggable(Level.FINEST)) {
					log.log(Level.FINEST, "Checking service for dataReceiver: {0}", dataReceiver);
				}

				if ((dataReceiver != null) && dataReceiver.getDomain().equals(hostname)) {
					if (log.isLoggable(Level.FINEST)) {
						log.finest("Stopping service because corresponding cluster node stopped.");
					}

					service.stop();
				}
			}
		});
	}

	@Override
	public SeeOtherHostIfc getSeeOtherHostInstance(String see_other_host_class) {

		if (see_other_host_strategy == null) {
			if (see_other_host_class == null) {
				see_other_host_class =
						SeeOtherHostIfc.CM_SEE_OTHER_HOST_CLASS_PROP_DEF_VAL_CLUSTER;
			}
			try {
				see_other_host_strategy =
						(SeeOtherHostIfc) Class.forName(see_other_host_class).newInstance();
			} catch (Exception e) {
				log.log(Level.SEVERE, "Can not instantiate see_other_host strategy for class: "
						+ see_other_host_class, e);
			}

			see_other_host_strategy.setNodes(connectedNodes);
		}
		return see_other_host_strategy;
	}

	// ~--- set methods ----------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param cl_controller
	 */
	@Override
	public void setClusterController(ClusterControllerIfc cl_controller) {
	}
}

// ~ Formatted in Sun Code Convention

// ~ Formatted by Jindent --- http://www.jindent.com
