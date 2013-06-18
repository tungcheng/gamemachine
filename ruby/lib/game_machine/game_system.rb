
java_import 'akka.util.Timeout'

module GameMachine

  class DuplicateHashringError < StandardError;end
  class MissingHashringError < StandardError;end

  class GameSystem < UntypedActor

    class << self
      alias_method :apply, :new
      alias_method :create, :new

      def systems
        [GameMachine::CommandRouter,GameMachine::LocalEcho,GameMachine::ConnectionManager].freeze
      end

      def components
        []
      end

      def reset_hashrings
        @@hashrings = nil
      end

      def hashrings
        @@hashrings ||= java.util.concurrent.ConcurrentHashMap.new
      end

      def hashring(name)
        hashrings.fetch(name,nil)
      end

      def add_hashring(name,hashring)
        if hashring(name)
          raise DuplicateHashringError, "name=#{name}"
        end
        hashrings[name] = hashring
      end

      def actor_system
        GameMachineLoader.get_actor_system
      end

      def remote_base_uri(server)
        "akka.tcp://system@#{Settings.servers.send(server).akka.host}:#{Settings.servers.send(server).akka.port}"
      end

      def remote_path(server,name)
        "#{remote_base_uri(server)}/user/#{name}"
      end

      def distributed_path(id,name)
        server = hashring(name).server_for(id)
        bucket = hashring(name).bucket_for(id)
        remote_path(server,bucket)
      end

      def local_path(name)
        "/user/#{name}"
      end

      def make_path(options)
        name = options[:name] || self.name
        if options[:key]
          if hashring(name)
            distributed_path(options[:key], name)
          else
            raise MissingHashringError
          end
        elsif options[:server]
          remote_path(options[:server],name)
        else
          local_path(name)
        end
      end

      def actor_selection(options)
        actor_system.actor_selection(make_path(options))
      end

      def tell(message,options={})
        actor_selection(options).tell(message,options[:sender])
      end

      def ask(message,options={})
        options = {:timeout => 100}.merge(options)
        duration = Duration.create(options[:timeout], TimeUnit::MILLISECONDS)
        timeout = Timeout.new(duration)
        ref = AskableActorSelection.new(actor_selection(options))
        future = ref.ask(message,timeout)
        Await.result(future, duration)
      rescue Java::JavaUtilConcurrent::TimeoutException => e
        GameMachine.logger.warn("TimeoutException caught in ask (timeout = #{options[:timeout]})")
      end

    end

    def onReceive(message)
      on_receive(message)
    end

    def on_receive(message)
      unhandled(message)
    end

  end
end
