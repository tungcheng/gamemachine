package com.game_machine.test;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.persistence.WriteBehindHandler;
import com.game_machine.entity_system.generated.Entity;

public class WriteBehindHandlerTest {

	public static ActorSystem system;
	
	@BeforeClass
	public void setup() {
		system = ActorUtil.createSystem("system");
	}
	
	@AfterClass
	public void teardown() {
		system.shutdown();
	}
	
	public void sleep(Integer ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void firstWriteShouldBeTrue() {
		
		WriteBehindHandler handler = getHandler("test1",2000,50);
		Entity message = new Entity(1);
		assertThat(handler.write(message)).isTrue();
	}
	
	@Test
	public void shouldOnlyWriteSameKeyOnceEveryInterval() {
		WriteBehindHandler handler = getHandler("test2",1000,50);
		Entity message = new Entity(1);
		Integer count = 0;
		for(int i=0;i<210;i++) {
			if (handler.write(message)) {
				count++;
			}
			sleep(10);
		}
		assertThat(count).isEqualTo(3);
	}
	
	@Test
	public void shouldLimitTotalWrites() {
		WriteBehindHandler handler = getHandler("test3",2000,50);
		Entity message = new Entity(1);
		Integer count = 0;
		for(int i=0;i<100;i++) {
			message = new Entity(i);
			if (handler.write(message)) {
				count++;
			}
			sleep(10);
		}
		
		assertThat(count).isEqualTo(50);
		
		
		
	}
	
	public WriteBehindHandler getHandler(String name, Integer writeInterval, Integer maxWritesPerSecond) {
		Props props = Props.create(WriteBehindHandler.class, writeInterval, maxWritesPerSecond);
		TestActorRef<WriteBehindHandler> ref = TestActorRef.create(system, props, name);
		return ref.underlyingActor();
	}
	
	
	// @Test
	public static void example() {
		try {
			ActorSystem system = ActorUtil.createSystem("system");
			final Props props = Props.create(WriteBehindHandler.class);
			final TestActorRef<WriteBehindHandler> ref = TestActorRef.create(system, props, "testA");
			final WriteBehindHandler actor = ref.underlyingActor();
			
			Entity o = new Entity(1);
			final Future<Object> future = akka.pattern.Patterns.ask(ref, o, 3000);

			assertThat(future.isCompleted()).isFalse();
			// assertTrue(future.isCompleted());
			Await.result(future, Duration.create(5, TimeUnit.SECONDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
