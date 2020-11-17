import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

public class LeaderSelector {
    private Logger logger = LoggerFactory.getLogger(LeaderSelector.class);

    private String zookeeperServer = "localhost:2181,localhost:2182,localhost:2183";

    private String leaderName = "leader-example";

    private AtomicBoolean isMaster = new AtomicBoolean(false);

    /**
     * 开始Leader选举
     */
    public void leader() {
        LeaderLatchListener leaderLatchListener = new LeaderLatchListener() {
            @Override
            public void isLeader() {
                isMaster.set(true);
                logger.info("Running as master");
                //
                logger.info("Starting master jobs");
            }

            @Override
            public void notLeader() {
                isMaster.set(false);
                logger.info("Running as slave");
                //
                logger.info("Stopping master jobs");
            }
        };

        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperServer, 5000, 5000, new ExponentialBackoffRetry(1000, 5));
        client.start();

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String localHost = inetAddress.getHostAddress();
            logger.info("LocalHost:{}", localHost);

            LeaderLatch leaderLatch = new LeaderLatch(client, leaderName, localHost);
            leaderLatch.addListener(leaderLatchListener);

            leaderLatch.start();
        } catch (UnknownHostException e) {
//            logger.error(ExceptionUtil.getExceptionStack(e));
        } catch (Exception e) {
//            logger.error(ExceptionUtil.getExceptionStack(e));
        }
    }

    /**
     * 返回Master状态
     *
     * @return
     */
    public boolean isMaster() {
        return isMaster.get();
    }
}
