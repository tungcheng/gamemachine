
package io.gamemachine.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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





import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;



@SuppressWarnings("unused")
public final class TrackEntity implements Externalizable, Message<TrackEntity>, Schema<TrackEntity>{



    public static Schema<TrackEntity> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static TrackEntity getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final TrackEntity DEFAULT_INSTANCE = new TrackEntity();
    static final String defaultScope = TrackEntity.class.getSimpleName();

    	
							    public boolean value= false;
		    			    
		
    
        	
							    public boolean internal= false;
		    			    
		
    
        	
					public TrackData trackData = null;
			    
		
    
        


    public TrackEntity()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("track_entity_value",null);
    	    	    	    	    	    	model.set("track_entity_internal",null);
    	    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (value != null) {
    	       	    	model.setBoolean("track_entity_value",value);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (internal != null) {
    	       	    	model.setBoolean("track_entity_internal",internal);
    	        		
    	//}
    	    	    	    }
    
	public static TrackEntity fromModel(Model model) {
		boolean hasFields = false;
    	TrackEntity message = new TrackEntity();
    	    	    	    	    	
    	    	    	Boolean valueTestField = model.getBoolean("track_entity_value");
    	if (valueTestField != null) {
    		boolean valueField = valueTestField;
    		message.setValue(valueField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	Boolean internalTestField = model.getBoolean("track_entity_internal");
    	if (internalTestField != null) {
    		boolean internalField = internalTestField;
    		message.setInternal(internalField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public boolean getValue() {
		return value;
	}
	
	public TrackEntity setValue(boolean value) {
		this.value = value;
		return this;	}
	
		            
		public boolean getInternal() {
		return internal;
	}
	
	public TrackEntity setInternal(boolean internal) {
		this.internal = internal;
		return this;	}
	
		            
		public TrackData getTrackData() {
		return trackData;
	}
	
	public TrackEntity setTrackData(TrackData trackData) {
		this.trackData = trackData;
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

    public Schema<TrackEntity> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public TrackEntity newMessage()
    {
        return new TrackEntity();
    }

    public Class<TrackEntity> typeClass()
    {
        return TrackEntity.class;
    }

    public String messageName()
    {
        return TrackEntity.class.getSimpleName();
    }

    public String messageFullName()
    {
        return TrackEntity.class.getName();
    }

    public boolean isInitialized(TrackEntity message)
    {
        return true;
    }

    public void mergeFrom(Input input, TrackEntity message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.value = input.readBool();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.internal = input.readBool();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.trackData = input.mergeObject(message.trackData, TrackData.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, TrackEntity message) throws IOException
    {
    	    	
    	    	//if(message.value == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (Boolean)message.value != null) {
            output.writeBool(1, message.value, false);
        }
    	    	
    	            	
    	    	
    	    	    	if( (Boolean)message.internal != null) {
            output.writeBool(2, message.internal, false);
        }
    	    	
    	            	
    	    	
    	    	    	if(message.trackData != null)
    		output.writeObject(3, message.trackData, TrackData.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START TrackEntity");
    	    	//if(this.value != null) {
    		System.out.println("value="+this.value);
    	//}
    	    	//if(this.internal != null) {
    		System.out.println("internal="+this.internal);
    	//}
    	    	//if(this.trackData != null) {
    		System.out.println("trackData="+this.trackData);
    	//}
    	    	System.out.println("END TrackEntity");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "value";
        	        	case 2: return "internal";
        	        	case 3: return "trackData";
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
    	    	__fieldMap.put("value", 1);
    	    	__fieldMap.put("internal", 2);
    	    	__fieldMap.put("trackData", 3);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = TrackEntity.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static TrackEntity parseFrom(byte[] bytes) {
	TrackEntity message = new TrackEntity();
	ProtobufIOUtil.mergeFrom(bytes, message, TrackEntity.getSchema());
	return message;
}

public static TrackEntity parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	TrackEntity message = new TrackEntity();
	JsonIOUtil.mergeFrom(bytes, message, TrackEntity.getSchema(), false);
	return message;
}

public TrackEntity clone() {
	byte[] bytes = this.toByteArray();
	TrackEntity trackEntity = TrackEntity.parseFrom(bytes);
	return trackEntity;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, TrackEntity.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<TrackEntity> schema = TrackEntity.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, TrackEntity.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, TrackEntity.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
