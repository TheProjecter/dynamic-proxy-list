/**
 * 
 */
package ch.fhnw.i4ds.dynamicproxy;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

/**
 * container of proxy lists
 * @author pdeboer
 * 
 */
public class ProxyListContainer {
	/**
	 * max consequent failures until proxy is removed
	 */
	private final int deleteFailureThreshold;
	private final Set<ProxyEntry> container = new HashSet<ProxyEntry>();
	private final Set<ProxyEntry> deleted = new HashSet<ProxyEntry>();
	private Queue<ProxyEntry> queue = new ConcurrentLinkedQueue<ProxyEntry>();
	private final ProxyListReader plr;
	private final Logger logger = Logger.getLogger(ProxyListContainer.class);

	/**
	 * 
	 * @param filename
	 * @param threshold
	 */
	public ProxyListContainer(String filename, int threshold) {
		this.deleteFailureThreshold = threshold;
		plr = new ProxyListReader(filename);

		mergeLists(this.plr.listProxies());
		
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				mergeLists(ProxyListContainer.this.plr.listProxies());
			}
		}, 10000);
	}

	/**
	 * 
	 * @param initial set of proxies
	 */
	public ProxyListContainer(Set<ProxyEntry> initial) {
		plr = null;
		container.addAll(initial);
		deleteFailureThreshold = 2;
	}

	/**
	 * performs clean-up of proxy-list
	 */
	public void cleanup() {
		Set<ProxyEntry> toDelete = new HashSet<ProxyEntry>(), copy;
		synchronized (container) {
			copy = new HashSet<ProxyEntry>(container);
		}

		// avoid concurrent modification exception
		for (ProxyEntry pe : copy) {
			if (pe.getNumFailures() >= deleteFailureThreshold)
				toDelete.add(pe);
		}

		synchronized (container) {
			container.removeAll(toDelete);
		}
		synchronized (toDelete) {
			deleted.addAll(toDelete);
		}

		logger.info("performed cleanup. " + toDelete.size()
				+ " entries deleted");
	}

	/**
	 * gets proxy from queue
	 * 
	 * @return
	 */
	public synchronized ProxyEntry getNextProxy() {
		ProxyEntry ret = queue.poll();
		if (ret == null) {
			logger.info("no more proxies in queue. generating new queue..");
			cleanup();
			synchronized (container) {
				queue.addAll(container);
			}
			ret = queue.poll();
		}
		logger.info("proxy requested answered with " + ret);
		return ret;
	}

	/**
	 * merges proxies with parameters. previously deleted ones are excluded
	 * 
	 * @param set
	 */
	public void mergeLists(Set<ProxyEntry> set) {
		synchronized (container) {
			set.removeAll(container);
			synchronized (deleted) {
				set.removeAll(deleted);
			}
			container.addAll(set);
			logger.info("added " + set.size() + " new proxies");
		}
	}
}
