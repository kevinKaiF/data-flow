package com.github.dataflow.node;

import com.github.dataflow.common.utils.PropertyUtil;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;
import java.util.Properties;

/**
 * server启动入口
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/5/31
 */
public class NodeLauncher {
    private static Logger logger = LoggerFactory.getLogger(NodeLauncher.class);

    public static void main(String[] args) {
        Server server = new Server();
        try {
            configureServer(server);
            server.setHandler(getSpringHandler());
            server.start();
            logger.info("Startup Jetty Server on port : {}.", server.getConnectors()[0].getPort());
            server.join();
        } catch (Exception e) {
            logger.error("Jetty Server happened exception, detail : ", e);
            System.exit(-1);
        } finally {
            server.destroy();
            logger.info("Stop Jetty Server successful.");
        }
    }

    private static void configureServer(Server server) throws IOException {
        Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
        // configure connector
        Connector connector = new SocketConnector();
        connector.setStatsOn(PropertyUtil.getBoolean(properties, "jetty.connector.statsOn", "false"));
        connector.setMaxIdleTime(PropertyUtil.getInt(properties, "jetty.connector.maxIdleTime", "600000"));
        connector.setPort(PropertyUtil.getInt(properties, "jetty.connector.port", "600000"));
        server.setConnectors(new Connector[]{connector});

        // configure threadPool
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMinThreads(PropertyUtil.getInt(properties, "jetty.threadPool.minThreads", "1"));
        threadPool.setMaxThreads(PropertyUtil.getInt(properties, "jetty.threadPool.maxThreads", "100"));
        server.setThreadPool(threadPool);

        // configure other params
        server.setSendServerVersion(PropertyUtil.getBoolean(properties, "jetty.sendServerVersion", "false"));
        server.setSendDateHeader(PropertyUtil.getBoolean(properties, "jetty.sendDateHeader", "true"));
        server.setGracefulShutdown(PropertyUtil.getInt(properties, "jetty.gracefulShutdown", "1000"));
        server.setStopAtShutdown(PropertyUtil.getBoolean(properties, "jetty.stopAtShutdown", "true"));
    }

    private static Handler getSpringHandler() {
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/node");
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocations(new String[]{"classpath:application-context.xml"});
        handler.addEventListener(new ContextLoaderListener(context));
        handler.addServlet(new ServletHolder(new DispatcherServlet(context)), "/*");
        return handler;
    }
}
