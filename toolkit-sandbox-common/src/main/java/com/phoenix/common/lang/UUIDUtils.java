package com.phoenix.common.lang;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UUIDUtils {
	private static Logger log = LoggerFactory.getLogger(UUIDUtils.class);

	static boolean nativeInitialized = false;

	static UUIDGenerator ug;

	static org.safehaus.uuid.EthernetAddress eAddr;

	static {
		// Try loading the EthernetAddress library. If this fails, then fallback
		// to
		// using another method for generating UUID's.
		/*
		 * This is always going to fail at the moment try {
		 * System.loadLibrary("EthernetAddress"); //$NON-NLS-1$
		 * nativeInitialized = true; } catch (Throwable t) { //
		 * log.warn(Messages.getErrorString("UUIDUtil.ERROR_0001_LOADING_ETHERNET_ADDRESS") );
		 * //$NON-NLS-1$ //$NON-NLS-2$ // Ignore for now. }
		 */
		ug = UUIDGenerator.getInstance();
		if (nativeInitialized) {
			try {
				com.ccg.net.ethernet.EthernetAddress ea = com.ccg.net.ethernet.EthernetAddress
						.getPrimaryAdapter();
				eAddr = new org.safehaus.uuid.EthernetAddress(ea.getBytes());
			} catch (Exception ex) {
				log.error(UUIDUtils.class.getName(), ex); //$NON-NLS-1$
			} catch (UnsatisfiedLinkError ule) {
				log.error(UUIDUtils.class.getName(), ule); //$NON-NLS-1$
				nativeInitialized = false;
			}
		}

		/*
		 * Add support for running in clustered environments. In this way, the
		 * MAC address of the running server can be added to the environment
		 * with a -DMAC_ADDRESS=00:50:56:C0:00:01
		 */
		if (eAddr == null) {
			String macAddr = System.getProperty("MAC_ADDRESS"); //$NON-NLS-1$
			if (macAddr != null) {
				// On Windows machines, people would be inclined to get the MAC
				// address with ipconfig /all. The format of this would be
				// something like 00-50-56-C0-00-08. So, replace '-' with ':'
				// before
				// creating the address.
				// 
				macAddr = macAddr.replace('-', ':');
				eAddr = new org.safehaus.uuid.EthernetAddress(macAddr);
			}
		}

		if (eAddr == null) {
			// Still don't have an Ethernet Address - generate a dummy one.
			eAddr = ug.getDummyAddress();
		}

		// Generate a UUID to make sure everything is running OK.
		UUID olduuId = ug.generateTimeBasedUUID(eAddr);
		if (olduuId == null) {
			log.error("olduuId is null!");
		}

	}

	public static String getUUIDAsString() {
		return getUUID().toString();
	}

	public static UUID getUUID() {
		UUID uuId = ug.generateTimeBasedUUID(eAddr);
		// while (uuId.toString().equals(olduuId.toString())) {
		// uuId = ug.generateTimeBasedUUID(eAddr);
		// }
		// olduuId = uuId;
		return uuId;
	}

	public static void main(String[] args) {
		System.out.println(UUIDUtils.getUUIDAsString());
	}
}
