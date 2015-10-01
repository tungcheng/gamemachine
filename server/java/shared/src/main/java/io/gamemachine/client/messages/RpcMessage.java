
package io.gamemachine.client.messages;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

import io.protostuff.ByteString;
import io.protostuff.GraphIOUtil;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import io.protostuff.JsonIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;


import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class RpcMessage implements Externalizable, Message<RpcMessage>, Schema<RpcMessage>{

	public enum MessageType implements io.protostuff.EnumLite<MessageType>
    {
    	
    	    	NONE(0),    	    	TEST(1);    	        
        public final int number;
        
        private MessageType (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static MessageType valueOf(int number)
        {
            switch(number) 
            {
            	    			case 0: return (NONE);
    			    			case 1: return (TEST);
    			                default: return null;
            }
        }
    }


    public static Schema<RpcMessage> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static RpcMessage getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final RpcMessage DEFAULT_INSTANCE = new RpcMessage();

    			public MessageType messageType; // = NONE:0;
	    
        			public GameMessage gameMessage;
	    
        			public Long messageId;
	    
        			public String playerId;
	    
      
    public RpcMessage()
    {
        
    }


	

	    
    public Boolean hasMessageType()  {
        return messageType == null ? false : true;
    }
        
		public MessageType getMessageType() {
		return messageType;
	}
	
	public RpcMessage setMessageType(MessageType messageType) {
		this.messageType = messageType;
		return this;	}
	
		    
    public Boolean hasGameMessage()  {
        return gameMessage == null ? false : true;
    }
        
		public GameMessage getGameMessage() {
		return gameMessage;
	}
	
	public RpcMessage setGameMessage(GameMessage gameMessage) {
		this.gameMessage = gameMessage;
		return this;	}
	
		    
    public Boolean hasMessageId()  {
        return messageId == null ? false : true;
    }
        
		public Long getMessageId() {
		return messageId;
	}
	
	public RpcMessage setMessageId(Long messageId) {
		this.messageId = messageId;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public RpcMessage setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
	
  
    // java serialization

    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    // message method

    public Schema<RpcMessage> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public RpcMessage newMessage()
    {
        return new RpcMessage();
    }

    public Class<RpcMessage> typeClass()
    {
        return RpcMessage.class;
    }

    public String messageName()
    {
        return RpcMessage.class.getSimpleName();
    }

    public String messageFullName()
    {
        return RpcMessage.class.getName();
    }

    public boolean isInitialized(RpcMessage message)
    {
        return true;
    }

    public void mergeFrom(Input input, RpcMessage message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                    message.messageType = MessageType.valueOf(input.readEnum());
                    break;
                	                	
                            	            	case 2:
            	                	                	message.gameMessage = input.mergeObject(message.gameMessage, GameMessage.getSchema());
                    break;
                                    	
                            	            	case 3:
            	                	                	message.messageId = input.readInt64();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, RpcMessage message) throws IOException
    {
    	    	
    	    	if(message.messageType == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.messageType != null)
    	 	output.writeEnum(1, message.messageType.number, false);
    	    	
    	            	
    	    	
    	    	    	if(message.gameMessage != null)
    		output.writeObject(2, message.gameMessage, GameMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.messageId != null)
            output.writeInt64(3, message.messageId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(4, message.playerId, false);
    	    	
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "messageType";
        	        	case 2: return "gameMessage";
        	        	case 3: return "messageId";
        	        	case 4: return "playerId";
        	            default: return null;
        }
    }

    public int getFieldNumber(String name)
    {
        final Integer number = __fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }

    private static final java.util.HashMap<String,Integer> __fieldMap = new java.util.HashMap<String,Integer>();
    static
    {
    	    	__fieldMap.put("messageType", 1);
    	    	__fieldMap.put("gameMessage", 2);
    	    	__fieldMap.put("messageId", 3);
    	    	__fieldMap.put("playerId", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = RpcMessage.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static RpcMessage parseFrom(byte[] bytes) {
	RpcMessage message = new RpcMessage();
	ProtobufIOUtil.mergeFrom(bytes, message, RpcMessage.getSchema());
	return message;
}

public static RpcMessage parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	RpcMessage message = new RpcMessage();
	JsonIOUtil.mergeFrom(bytes, message, RpcMessage.getSchema(), false);
	return message;
}

public RpcMessage clone() {
	byte[] bytes = this.toByteArray();
	RpcMessage rpcMessage = RpcMessage.parseFrom(bytes);
	return rpcMessage;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, RpcMessage.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<RpcMessage> schema = RpcMessage.getSchema();
    
	final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ProtobufOutput output = new ProtobufOutput(buffer);
    try
    {
    	schema.writeTo(output, this);
        final int size = output.getSize();
        ProtobufOutput.writeRawVarInt32Bytes(out, size);
        final int msgSize = LinkedBuffer.writeTo(out, buffer);
        assert size == msgSize;
        
        buffer.clear();
        return out.toByteArray();
    }
    catch (IOException e)
    {
        throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                "(should never happen).", e);
    }
 
}

public byte[] toProtobuf() {
	LinkedBuffer buffer = LocalLinkedBuffer.get();
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, RpcMessage.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}