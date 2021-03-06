/* JNativeHook: Global keyboard and mouse hooking for Java.
 * Copyright (C) 2006-2016 Alexander Barker.  All Rights Received.
 * https://github.com/kwhat/jnativehook/
 *
 * JNativeHook is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JNativeHook is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jnativehook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A small class to determine the native system's operating system family and
 * architecture. The class is only used to determine which native library to
 * unpack and load at runtime. This class is never used if the native library
 * is loaded using the <code>java.library.path</code> property.
 *
 * @author	Alexander Barker (<a href="mailto:alex@1stleg.com">alex@1stleg.com</a>)
 * @since	1.0
 * @version	2.1
 */
public class NativeSystem {

	/**
	 * The operating system family enum.
	 *
	 * @see NativeSystem
	 */
	public enum Family {
		/** The FreeBSD operating system family. */
		FREEBSD,

		/** The OpenBSD operating system family. */
		OPENBSD,

		/** The Apple OS X operating system family. */
		DARWIN,

		/** The Solaris operating system family. */
		SOLARIS,

		/** The Linux operating system family. */
		LINUX,

		/** The Windows operating system family. */
		WINDOWS,

		/** Any unsupported operating system family. */
		UNSUPPORTED;
	}

	/**
	 * The system architecture enum.
	 *
	 * @see NativeSystem
	 */
	public enum Arch {
		/** The arm6j architecture. */
		ARM6,

		/** The arm7a architecture. */
		ARM7,

		/** The sparc architecture. */
		SPARC,

		/** The sparc64 architecture. */
		SPARC64,

		/** The ppc architecture. */
		PPC,

		/** The ppc64 architecture. */
		PPC64,

		/** The x86 architecture. */
		x86,

		/** The amd64 architecture. */
		x86_64,

		/** Any unsupported system architecture. */
		UNSUPPORTED;
	}

	/**
	 * Determines the current operating system family.
	 *
	 * @return The current operating system family enum item.
	 */
	public static Family getFamily() {
		String osName = System.getProperty("os.name");
		Family family;

		if (osName.equalsIgnoreCase("freebsd")) {
			family = Family.FREEBSD;
		}
		else if (osName.equalsIgnoreCase("openbsd")) {
			family = Family.OPENBSD;
		}
		else if (osName.equalsIgnoreCase("mac os x")) {
			family = Family.DARWIN;
		}
		else if (osName.equalsIgnoreCase("solaris") ||
				osName.equalsIgnoreCase("sunos")) {
			family = Family.SOLARIS;
		}
		else if (osName.equalsIgnoreCase("linux")) {
			family = Family.LINUX;
		}
		else if (osName.toLowerCase().startsWith("windows")) {
			family = Family.WINDOWS;
		}
		else {
			family = Family.UNSUPPORTED;
		}

		return family;
	}

	/**
	 * Determines the current system architecture.
	 *
	 * @return The current system architecture.
	 */
	public static Arch getArchitecture() {
		String osArch = System.getProperty("os.arch");
		Arch arch;

		if (osArch.equalsIgnoreCase("arm")) {
			arch = Arch.ARM6;

			// Arm7 is not supported by Java... Yes, seriously.
			if (NativeSystem.getFamily() == Family.LINUX) {
				File fin = new File("/proc/cpuinfo");
				if (fin.exists() && fin.canRead()) {
					Pattern p = Pattern.compile("ARMv7", Pattern.CASE_INSENSITIVE);

					try {
						BufferedReader br = new BufferedReader(new FileReader(fin));

						String line = null;
						while ((line = br.readLine()) != null) {
							Matcher m = p.matcher(line);

							if (m.find()) {
								arch = Arch.ARM7;
								break;
							}
						}

						br.close();
					}
					catch (IOException e) {
						// Assume Arm6.
					}
				}
			}
		}
		else if (osArch.equalsIgnoreCase("sparc")) {
			arch = Arch.SPARC;
		}
		else if (osArch.equalsIgnoreCase("sparc64")) {
			arch = Arch.SPARC64;
		}
		else if (osArch.equalsIgnoreCase("ppc") ||
				osArch.equalsIgnoreCase("powerpc")) {
			arch = Arch.PPC;
		}
		else if (osArch.equalsIgnoreCase("ppc64") ||
				osArch.equalsIgnoreCase("powerpc64")) {
			arch = Arch.PPC64;
		}
		else if (osArch.equalsIgnoreCase("x86") ||
				osArch.equalsIgnoreCase("i386") ||
				osArch.equalsIgnoreCase("i486") ||
				osArch.equalsIgnoreCase("i586") ||
				osArch.equalsIgnoreCase("i686")) {
			arch = Arch.x86;
		}
		else if (osArch.equalsIgnoreCase("x86_64") ||
				osArch.equalsIgnoreCase("amd64") ||
				osArch.equalsIgnoreCase("k8")) {
			arch = Arch.x86_64;
		}

		else {
			arch = Arch.UNSUPPORTED;
		}

		return arch;
	}
}
