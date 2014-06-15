package com.game_machine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import com.game_machine.core.NetMessage;

public final class UdpServer implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(UdpServer.class);
	private static Thread serverThread;

	private static UdpServer udpServer;

	private final String hostname;
	private final int port;

	private final UdpServerHandler handler;


	public static UdpServer getUdpServer() {
		return udpServer;
	}

	public static void start(String host, Integer port) {

		// Don't try to start an already running server
		if (udpServer != null) {
			return;
		}

		udpServer = new UdpServer(host, port);
		serverThread = new Thread(udpServer);
		serverThread.start();
	}

	public static void stop() {
		log.info("Stopping UDP server");
		// Don't try to stop a server that's not running
		if (udpServer == null) {
			return;
		}

	}

	public UdpServerHandler getHandler() {
		return this.handler;
	}

	public UdpServer(final String hostname, final int port) {
		this.port = port;
		this.hostname = hostname;
		this.handler = new UdpServerHandler();
	}

	public void run() {
		log.info("Starting UdpServer port=" + this.port + " hostname=" + this.hostname);
		Thread.currentThread().setName("udp-server");
		EventLoopGroup group = new NioEventLoopGroup();
		try {
		Bootstrap boot = new Bootstrap();
		boot.channel(NioDatagramChannel.class);
		boot.group(group);
		boot.option(ChannelOption.SO_BROADCAST, false);
		boot.handler(new UdpServerHandler());
		
		boot.bind(this.port).sync().channel().closeFuture().await();
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			 group.shutdownGracefully();
		}
	}

	public void sendToClient(byte[] bytes, String host, int port, ChannelHandlerContext ctx) {
		handler.send(bytes, host, port, ctx);
	}

	

}