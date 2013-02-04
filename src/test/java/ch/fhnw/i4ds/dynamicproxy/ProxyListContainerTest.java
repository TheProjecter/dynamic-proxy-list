/**
 * 
 */
package ch.fhnw.i4ds.dynamicproxy;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author pdeboer
 *
 */
public class ProxyListContainerTest {
	@Test
	public void testCleanup() {
		Set<ProxyEntry> set = new HashSet<ProxyEntry>();
		set.add(ProxyListReader.parse("SOCKS,1.2.3.4:66"));
		set.add(ProxyListReader.parse("HTTP,1.2.3.4:66"));
		
		ProxyListContainer plc = new ProxyListContainer(set);
		ProxyEntry pe2 = plc.getNextProxy();
		Assert.assertTrue(set.contains(pe2));
		ProxyEntry pe3 = plc.getNextProxy();
		Assert.assertFalse(pe2.equals(pe3));
		Assert.assertTrue(set.contains(pe3));
		
		//report 2 failures
		pe3.reportProxyUsage(false);
		pe3.reportProxyUsage(false);
		pe3.reportProxyUsage(false);
		
		ProxyEntry pe4 = plc.getNextProxy();
		Assert.assertSame(pe2, pe4);
		ProxyEntry pe5 = plc.getNextProxy();
		Assert.assertSame(pe2, pe5);
	}

}
