/**
 * 
 */
package ch.fhnw.i4ds.dynamicproxy;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * reads proxy list text file. Format:
 * 
 * <pre>
 * TYPE,IP:PORT    <- 1 per line.
 * </pre>
 * 
 * Example:
 * 
 * <pre>
 * SOCKS,8.8.8.8:1234
 * HTTP,8.8.4.4:1343
 * </pre>
 * 
 * @author pdeboer
 * 
 */
public class ProxyListReader {
	private File file;
	private static Logger logger = Logger.getLogger(ProxyListReader.class);

	/**
	 * 
	 */
	public ProxyListReader(String fname) {
		this.file = new File(fname);
		logger.info("loading file " + file.getAbsolutePath());
	}

	/**
	 * get proxies out of supplied lines-list
	 * 
	 * @param lines
	 * @return
	 */
	public Set<ProxyEntry> listProxies(List<String> lines) {
		Set<ProxyEntry> ret = new HashSet<ProxyEntry>();

		for (String l : lines) {
			ProxyEntry pe = parse(l);
			if (pe != null)
				ret.add(pe);
		}

		return ret;
	}

	/**
	 * read file and list proxies
	 * 
	 * @return
	 */
	public Set<ProxyEntry> listProxies() {
		try {
			Set<ProxyEntry> ret = listProxies(FileUtils.readLines(file));
			logger.info("loaded " + ret.size());
			return ret;
		} catch (IOException e) {
			logger.error("couldnt read proxy file", e);
		}
		return null;
	}

	/**
	 * parses a line of format "SOCKS,1.2.3.4:66"
	 * 
	 * @param line
	 * @return
	 */
	public static ProxyEntry parse(String line) {
		try {
			String[] cells = line.split(",");
			String[] ip = cells[1].split(":");
			String[] ipCells = ip[0].split("\\.");
			byte[] ipConverted = new byte[4];
			for (int i = 0; i < 4; i++) {
				ipConverted[i] = (byte) Integer.parseInt(ipCells[i]);
			}
			Proxy p = new Proxy(
					cells[0].toLowerCase().equals("http") ? Type.HTTP
							: Type.SOCKS, new InetSocketAddress(
							InetAddress.getByAddress(ipConverted),
							Integer.parseInt(ip[1])));
			return new ProxyEntry(p);
		} catch (Exception e) {
			logger.error("couldnt parse line: " + line, e);
			return null;
		}
	}
}
