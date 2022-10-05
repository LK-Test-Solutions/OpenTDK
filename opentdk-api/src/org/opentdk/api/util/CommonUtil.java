/* 
 * BSD 2-Clause License
 * 
 * Copyright (c) 2022, LK Test Solutions GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.opentdk.api.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import org.opentdk.api.logger.MLogger;

/**
 * Class with common methods that get directly used in a static way. For example the determination
 * of the current operating system.
 * 
 * @author LK Test Solutions
 *
 */
public class CommonUtil {

	/**
	 * @return true if the current operating system is Windows.
	 */
	public static final boolean isWindows() {
		return getOSName().toLowerCase().startsWith("win");
	}

	/**
	 * @return true if the current operating system is macOS.
	 */
	public static final boolean isMac() {
		return getOSName().toLowerCase().startsWith("mac");
	}

	/**
	 * @return true if the current operating system is Unix.
	 */
	public static final boolean isUnix() {
		return getOSName().toLowerCase().startsWith("nix") || getOSName().toLowerCase().startsWith("nux") || getOSName().toLowerCase().startsWith("aix");
	}

	/**
	 * @return the system property os.name e.g. 'Windows 10'.
	 */
	public static final String getOSName() {
		return System.getProperty("os.name");
	}

	/**
	 * @return the system property os.version e.g. '10.0' of Windows 10.
	 */
	public static final String getOSVersion() {
		return System.getProperty("os.version");
	}

	/**
	 * @return The {@link java.net.InetAddress} object for the local host for further operations.
	 */
	public static final InetAddress getInetAdress() {
		InetAddress i = null;
		try {
			i = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		return i;
	}

	/**
	 * @return The name of the local host machine.
	 */
	public static final String getComputername() {
		return getInetAdress().getHostName();
	}

	/**
	 * @return The IP address of the local host machine.
	 */
	public static final String getIPAdress() {
		return getInetAdress().getHostAddress();
	}

	/**
	 * @return The MAC address of the local host machine.
	 */
	public static final String getMacAddress() {
		String result = "";
		try {
			for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				byte[] hardwareAddress = ni.getHardwareAddress();
				if (hardwareAddress != null && hardwareAddress.length > 0) {
					for (int i = 0; i < hardwareAddress.length; i++)
						result += String.format((i == 0 ? "" : "-") + "%02X", hardwareAddress[i]);
					return result;
				}
			}
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * Executes any command via Runtime.getRuntime.exec.
	 */
	public static int exeucteCommand(String command) {
		int ret = -1;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			ret = process.onExit().get().exitValue();
			if(ret == 1) {
				MLogger.getInstance().log(Level.SEVERE, "Execute command failed for ==> " + command);
			}						
		} catch(IOException | InterruptedException | ExecutionException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		} finally {
			if(process != null) {
				process.destroy();
			}
		}
		return ret;
	}

}
