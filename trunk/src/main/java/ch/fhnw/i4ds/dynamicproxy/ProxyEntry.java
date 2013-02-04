/**
 * 
 */
package ch.fhnw.i4ds.dynamicproxy;

import java.net.Proxy;
import java.util.Date;

/**
 * model for proxy entries
 * @author pdeboer
 * 
 */
public class ProxyEntry {
	private Proxy proxy;
	private Date lastTest;
	private boolean lastTestSuccessful = true;
	private int numFailures = 0;

	/**
	 * after usage of proxy, use this method to set how happy you were
	 * 
	 * @param successful
	 */
	public void reportProxyUsage(boolean successful) {
		if (successful) {
			numFailures = 0;
		} else {
			++numFailures;
		}
		lastTestSuccessful = successful;
		lastTest = new Date();
	}

	/**
	 * 
	 */
	public ProxyEntry(Proxy p) {
		this.proxy = p;
	}

	/**
	 * @return the proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * @param proxy
	 *            the proxy to set
	 */
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * @return the lastTest
	 */
	public Date getLastTest() {
		return lastTest;
	}

	/**
	 * @return the lastTestSuccessful
	 */
	public boolean isLastTestSuccessful() {
		return lastTestSuccessful;
	}

	/**
	 * @return the numFailures
	 */
	public int getNumFailures() {
		return numFailures;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((proxy == null) ? 0 : proxy.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProxyEntry other = (ProxyEntry) obj;
		if (proxy == null) {
			if (other.proxy != null)
				return false;
		} else if (!proxy.equals(other.proxy))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return proxy.toString();
	}
}
