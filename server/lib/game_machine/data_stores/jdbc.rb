module GameMachine
  module DataStores
    class Jdbc

      def dbname
        'game_machine'
      end

      def connect
        @pool ||= GameMachine::JavaLib::DbConnectionPool.getInstance
        unless @pool.connect(
          dbname,
          Application.config.jdbc_url,
          Application.config.jdbc_driver,
          Application.config.jdbc_username,
          Application.config.jdbc_password || ''
        )
          GameMachine.logger.error "Unable to establish database connection, exiting"
          System.exit 1
        end
        @pool
      end

      # type 0 = string, 1 = byte array
      def set(id,value)
        connection = @pool.get_connection(dbname)
        if value.is_a?(String)
          value = value.to_java_bytes
          type = 0
        else
          type = 1
        end
        s = connection.prepare_statement("INSERT INTO entities (id,value,datatype) VALUES (?,?,?) ON DUPLICATE KEY UPDATE value=VALUES(value)")
        s.setString(1,id.to_java_string)
        s.setBytes(2,value)
        s.setInt(3,type)
        s.execute_update
        s.close
        connection.close
      end

      def delete(id)
        connection = @pool.get_connection(dbname)
        s = connection.prepare_statement("DELETE from entities where id = ?")
        s.setString(1,id.to_java_string)
        s.execute_update
        s.close
        connection.close
      end

      def get(key)
        value = nil
        type = nil
        connection = @pool.get_connection(dbname)
        s = connection.create_statement
        res = s.execute_query("SELECT value,datatype from entities where id = '#{key}' LIMIT 1")
        if res.first
          value = res.get_bytes('value')
          type = res.get_int("datatype")
        end

        s.close
        connection.close

        if type == 0
          value.to_s
        else
          value
        end
      end

    end
  end
end
