
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
public final class Consumable implements Externalizable, Message<Consumable>, Schema<Consumable>{



    public static Schema<Consumable> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Consumable getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Consumable DEFAULT_INSTANCE = new Consumable();
    static final String defaultScope = Consumable.class.getSimpleName();

    	
							    public String type= null;
		    			    
		
    
        	
							    public String size= null;
		    			    
		
    
        


    public Consumable()
    {
        
    }


	


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("consumable_type",null);
    	    	    	    	    	    	model.set("consumable_size",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	//if (type != null) {
    	       	    	model.setString("consumable_type",type);
    	        		
    	//}
    	    	    	    	    	
    	    	    	//if (size != null) {
    	       	    	model.setString("consumable_size",size);
    	        		
    	//}
    	    	    }
    
	public static Consumable fromModel(Model model) {
		boolean hasFields = false;
    	Consumable message = new Consumable();
    	    	    	    	    	
    	    	    	String typeTestField = model.getString("consumable_type");
    	if (typeTestField != null) {
    		String typeField = typeTestField;
    		message.setType(typeField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	    	    	    	
    	    	    	String sizeTestField = model.getString("consumable_size");
    	if (sizeTestField != null) {
    		String sizeField = sizeTestField;
    		message.setSize(sizeField);
    		hasFields = true;
    	}
    	
    	    	
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	            
		public String getType() {
		return type;
	}
	
	public Consumable setType(String type) {
		this.type = type;
		return this;	}
	
		            
		public String getSize() {
		return size;
	}
	
	public Consumable setSize(String size) {
		this.size = size;
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

    public Schema<Consumable> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Consumable newMessage()
    {
        return new Consumable();
    }

    public Class<Consumable> typeClass()
    {
        return Consumable.class;
    }

    public String messageName()
    {
        return Consumable.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Consumable.class.getName();
    }

    public boolean isInitialized(Consumable message)
    {
        return true;
    }

    public void mergeFrom(Input input, Consumable message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.type = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.size = input.readString();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Consumable message) throws IOException
    {
    	    	
    	    	//if(message.type == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.type != null) {
            output.writeString(1, message.type, false);
        }
    	    	
    	            	
    	    	//if(message.size == null)
        //    throw new UninitializedMessageException(message);
    	    	
    	    	    	if( (String)message.size != null) {
            output.writeString(2, message.size, false);
        }
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Consumable");
    	    	//if(this.type != null) {
    		System.out.println("type="+this.type);
    	//}
    	    	//if(this.size != null) {
    		System.out.println("size="+this.size);
    	//}
    	    	System.out.println("END Consumable");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "type";
        	        	case 2: return "size";
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
    	    	__fieldMap.put("type", 1);
    	    	__fieldMap.put("size", 2);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Consumable.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Consumable parseFrom(byte[] bytes) {
	Consumable message = new Consumable();
	ProtobufIOUtil.mergeFrom(bytes, message, Consumable.getSchema());
	return message;
}

public static Consumable parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Consumable message = new Consumable();
	JsonIOUtil.mergeFrom(bytes, message, Consumable.getSchema(), false);
	return message;
}

public Consumable clone() {
	byte[] bytes = this.toByteArray();
	Consumable consumable = Consumable.parseFrom(bytes);
	return consumable;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Consumable.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Consumable> schema = Consumable.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, Consumable.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, Consumable.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}
