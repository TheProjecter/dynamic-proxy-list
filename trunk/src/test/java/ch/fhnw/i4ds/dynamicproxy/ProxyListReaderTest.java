/**
 * 
 */
package ch.fhnw.i4ds.dynamicproxy;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author pdeboer
 * 
 */
public class ProxyListReaderTest {
	@Test
	public void testListProxies() {
		List<String> lines = new LinkedList<String>();
		lines.add("SOCKS,187.22.33.44:627");
		lines.add("HTTP,11.22.33.44:80");
		lines.add("HTTP,11.22.33.44:80"); //twice the same. 
		
		Set<ProxyEntry> pe = new ProxyListReader("").listProxies(lines);
		
		Assert.assertTrue(pe.size() == 2);
		Assert.assertTrue(pe.contains(new ProxyEntry(new Proxy(Type.SOCKS, new InetSocketAddress(
				"187.22.33.44", 627)))));
		
		Assert.assertTrue(pe.contains(new ProxyEntry(new Proxy(Type.HTTP, new InetSocketAddress(
				"11.22.33.44", 80)))));
	}

	@Test
	public void testParseLineSocks() {
		String line = "SOCKS,187.22.33.44:627";
		ProxyEntry res = ProxyListReader.parse(line);
		Assert.assertEquals(new Proxy(Type.SOCKS, new InetSocketAddress(
				"187.22.33.44", 627)), res.getProxy());

		Assert.assertFalse(new Proxy(Type.SOCKS, new InetSocketAddress(
				"187.21.33.44", 627)).equals(res.getProxy()));

		Assert.assertEquals(0, res.getNumFailures());
		Assert.assertNull(res.getLastTest());

	}

	@Test
	public void testParseLineHttp() {
		String line = "HttP,187.22.33.44:627";
		ProxyEntry res = ProxyListReader.parse(line);
		Assert.assertEquals(new Proxy(Type.HTTP, new InetSocketAddress(
				"187.22.33.44", 627)), res.getProxy());
	}

	@Test
	public void testParseLineInvalidIP() {
		String line = "HttP,187.22.33.48dd84:627";
		ProxyEntry res = ProxyListReader.parse(line);
		Assert.assertNull(res);
	}
}
