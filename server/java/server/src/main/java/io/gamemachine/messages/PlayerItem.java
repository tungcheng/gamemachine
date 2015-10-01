
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


import java.lang.reflect.Field;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSelection;
import akka.pattern.AskableActorSelection;

import akka.util.Timeout;
import java.util.concurrent.TimeUnit;

import io.gamemachine.core.GameMachineLoader;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.actor.ActorSystem;
import org.javalite.activejdbc.Errors;


import io.gamemachine.core.ActorUtil;

import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;

import io.gamemachine.core.PersistableMessage;


import io.gamemachine.objectdb.Cache;
import io.gamemachine.core.CacheUpdate;

@SuppressWarnings("unused")
public final class PlayerItem implements Externalizable, Message<PlayerItem>, Schema<PlayerItem>, PersistableMessage{



    public static Schema<PlayerItem> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static PlayerItem getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final PlayerItem DEFAULT_INSTANCE = new PlayerItem();
    static final String defaultScope = PlayerItem.class.getSimpleName();

    			public String id;
	    
        			public String name;
	    
        			public Integer quantity;
	    
        			public String color;
	    
        			public Boolean weapon;
	    
        			public Cost cost;
	    
        			public String playerId;
	    
        			public Integer recordId;
	    
        			public String icon;
	    
        			public Integer harvestable;
	    
        			public Integer craftingResource;
	    
        			public Integer craftable;
	    
        			public Boolean isConsumable;
	    
        			public Integer type;
	    
        			public Integer maxHealth;
	    
        			public Integer health;
	    
        			public Integer level;
	    
        			public String characterId;
	    
        			public String containerId;
	    
        			public Integer updatedAt;
	    
        			public String category;
	    
        			public String locationId;
	    
        			public Integer slotCount;
	    
        			public Boolean stackable;
	    
        			public String locationType;
	    
        			public Integer stackMax;
	    
        			public Integer containerSlot;
	    
        			public String icon_uuid;
	    
        			public String icon_path;
	    
        			public String referenceId;
	    
        			public Boolean hidden;
	    
        			public Integer maxQuantity;
	    
        			public Boolean active;
	    
        			public Float weight;
	    
        			public Integer templateBlockId;
	    
        
	public static PlayerItemCache cache() {
		return PlayerItemCache.getInstance();
	}
	
	public static PlayerItemStore store() {
		return PlayerItemStore.getInstance();
	}


    public PlayerItem()
    {
        
    }

	static class CacheRef {
	
		private final CacheUpdate cacheUpdate;
		private final String id;
		
		public CacheRef(CacheUpdate cacheUpdate, String id) {
			this.cacheUpdate = cacheUpdate;
			this.id = id;
		}
		
		public void send() {
			ActorSelection sel = ActorUtil.findLocalDistributed("cacheUpdateHandler", id);
			sel.tell(cacheUpdate,null);
		}
		
		public PlayerItem result(int timeout) {
			ActorSelection sel = ActorUtil.findLocalDistributed("cacheUpdateHandler", id);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Future<Object> future = askable.ask(cacheUpdate, t);
			try {
				Await.result(future, t.duration());
				return cache().getCache().get(id);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static class PlayerItemCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, PlayerItem> cache = new Cache<String, PlayerItem>(120, 5000);
		
		private PlayerItemCache() {
		}
		
		private static class LazyHolder {
			private static final PlayerItemCache INSTANCE = new PlayerItemCache();
		}
	
		public static PlayerItemCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, PlayerItem>(expiration, size);
		}
	
		public Cache<String, PlayerItem> getCache() {
			return cache;
		}
		
		public CacheRef setField(String id, String field, Object value) {
			return updateField(id, field, value, CacheUpdate.SET);
		}
		
		public CacheRef incrementField(String id, String field, Object value) {
			return updateField(id, field, value, CacheUpdate.INCREMENT);
		}
		
		public CacheRef decrementField(String id, String field, Object value) {
			return updateField(id, field, value, CacheUpdate.DECREMENT);
		}
		
		private CacheRef updateField(String id, String field, Object value, int updateType) {
			CacheUpdate cacheUpdate = new CacheUpdate(PlayerItemCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(PlayerItem message) {
			CacheUpdate cacheUpdate = new CacheUpdate(PlayerItemCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public PlayerItem get(String id, int timeout) {
			PlayerItem message = cache.get(id);
			if (message == null) {
				message = PlayerItem.store().get(id, timeout);
			}
			return message;
		}
			
		public static PlayerItem setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			PlayerItem message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (PlayerItem) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				PlayerItem.store().set(message);
			} else {
				message = PlayerItem.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, PlayerItem.class.getField(field));
					} catch (NoSuchFieldException e) {
						throw new RuntimeException("No such field "+field);
					} catch (SecurityException e) {
						throw new RuntimeException("Security Exception accessing field "+field);
					}
	        	}
				Field f = cachefields.get(field);
				Class<?> klass = f.getType();
				if (cacheUpdate.getUpdateType() == CacheUpdate.SET) {
					f.set(message, klass.cast(cacheUpdate.getFieldValue()));
				} else {
					int updateType = cacheUpdate.getUpdateType();
					Object value = cacheUpdate.getFieldValue();
					if (klass == Integer.TYPE || klass == Integer.class) {
						Integer i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Integer)f.get(message) + (Integer) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Integer)f.get(message) - (Integer) value;
							f.set(message, klass.cast(i));
						}
					} else if (klass == Long.TYPE || klass == Long.class) {
						Long i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Long)f.get(message) + (Long) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Long)f.get(message) - (Long) value;
							f.set(message, klass.cast(i));
						}
					} else if (klass == Double.TYPE || klass == Double.class) {
						Double i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Double)f.get(message) + (Double) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Double)f.get(message) - (Double) value;
							f.set(message, klass.cast(i));
						}
					} else if (klass == Float.TYPE || klass == Float.class) {
						Float i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Float)f.get(message) + (Float) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Float)f.get(message) - (Float) value;
							f.set(message, klass.cast(i));
						}
					}
				}
				cache.set(message.id, message);
				PlayerItem.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class PlayerItemStore {
	
		private PlayerItemStore() {
		}
		
		private static class LazyHolder {
			private static final PlayerItemStore INSTANCE = new PlayerItemStore();
		}
	
		public static PlayerItemStore getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public static String scopeId(String playerId, String id) {
    		return playerId + "##" + id;
    	}
    
	    public static String unscopeId(String id) {
	    	if (id.contains("##")) {
	    		String[] parts = id.split("##");
	        	return parts[1];
	    	} else {
	    		throw new RuntimeException("Expected "+id+" to contain ##");
	    	}
	    }
	    
	    public static String defaultScope() {
	    	return defaultScope;
	    }
		
	    public void set(PlayerItem message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public PlayerItem get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, PlayerItem message) {
	    	PlayerItem clone = message.clone();
			clone.id = scopeId(scope,message.id);
			ActorSelection sel = ActorUtil.findDistributed("object_store", clone.id);
			sel.tell(clone, null);
		}
			
		public void delete(String scope, String id) {
			String scopedId = scopeId(scope,id);
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			ObjectdbDel del = new ObjectdbDel().setEntityId(scopedId);
			sel.tell(del, null);
		}
			
		public PlayerItem get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("PlayerItem");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			PlayerItem message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof PlayerItem) {
					message = (PlayerItem)result;
				} else if (result instanceof ObjectdbStatus) {
					return null;
				}
			} catch (Exception e) {
				throw new RuntimeException("Operation timed out");
			}
			if (message == null) {
				return null;
			}
			message.id = unscopeId(message.id);
			return message;
		}
		
	}
	

	

	public static PlayerItemDb db() {
		return PlayerItemDb.getInstance();
	}
	
	public interface PlayerItemAsyncDb {
		void save(PlayerItem message);
		void delete(int recordId);
		void deleteWhere(String query, Object ... params);
	}
	
	public static class PlayerItemAsyncDbImpl implements PlayerItemAsyncDb {
	
		public void save(PlayerItem message) {
			PlayerItem.db().save(message, false);
	    }
	    
	    public void delete(int recordId) {
	    	PlayerItem.db().delete(recordId);
	    }
	    
	    public void deleteWhere(String query, Object ... params) {
	    	PlayerItem.db().deleteWhere(query,params);
	    }
	    
	}
	
	public static class PlayerItemDb {
	
		public Errors dbErrors;
		private PlayerItemAsyncDb asyncDb = null;
		
		private PlayerItemDb() {
			start();
		}
		
		public void start() {
			if (asyncDb == null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				asyncDb = TypedActor.get(system).typedActorOf(new TypedProps<PlayerItemAsyncDbImpl>(PlayerItemAsyncDb.class, PlayerItemAsyncDbImpl.class));
			}
		}
		
		public void stop() {
			if (asyncDb != null) {
				ActorSystem system = GameMachineLoader.getActorSystem();
				TypedActor.get(system).stop(asyncDb);
				asyncDb = null;
			}
		}
		
		private static class LazyHolder {
			private static final PlayerItemDb INSTANCE = new PlayerItemDb();
		}
	
		public static PlayerItemDb getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public void saveAsync(PlayerItem message) {
			asyncDb.save(message);
	    }
	    
	    public void deleteAsync(int recordId) {
	    	asyncDb.delete(recordId);
	    }
	    
	    public void deleteWhereAsync(String query, Object ... params) {
	    	asyncDb.deleteWhere(query,params);
	    }
	    		        
	    public Boolean save(PlayerItem message) {
	    	return save(message, false);
	    }
	        
	    public Boolean save(PlayerItem message, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.PlayerItem.open();
	    	}
	    	
	    	io.gamemachine.orm.models.PlayerItem model = null;
	    	if (message.hasRecordId()) {
	    		model = io.gamemachine.orm.models.PlayerItem.findFirst("id = ?", message.recordId);
	    	}
	    	
	    	if (model == null) {
	    		model = new io.gamemachine.orm.models.PlayerItem();
	    		message.toModel(model);
	    	} else {
	    		message.toModel(model);
	    	}
	    		    	if (message.hasCost()) {
	    		message.cost.toModel(model);
	    	} else {
	    		Cost.clearModel(model);
	    	}
	    		    	
	    	Boolean res = model.save();
	    	if (res) {
	    		message.setRecordId(model.getInteger("id"));
	    	} else {
	    		dbErrors = model.errors();
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.PlayerItem.close();
	    	}
	    	return res;
	    }
	    
	    public Boolean delete(int recordId) {
	    	Boolean result;
	    	io.gamemachine.orm.models.PlayerItem.open();
	    	int deleted = io.gamemachine.orm.models.PlayerItem.delete("id = ?", recordId);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.PlayerItem.close();
	    	return result;
	    }
	    
	    public Boolean deleteWhere(String query, Object ... params) {
	    	Boolean result;
	    	io.gamemachine.orm.models.PlayerItem.open();
	    	int deleted = io.gamemachine.orm.models.PlayerItem.delete(query,params);
	    	if (deleted >= 1) {
	    		result = true;
	    	} else {
	    		result = false;
	    	}
	    	io.gamemachine.orm.models.PlayerItem.close();
	    	return result;
	    }
	    
	    public PlayerItem find(int recordId) {
	    	return find(recordId, false);
	    }
	    
	    public PlayerItem find(int recordId, boolean inTransaction) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.PlayerItem.open();
	    	}
	    	
	    	io.gamemachine.orm.models.PlayerItem model = io.gamemachine.orm.models.PlayerItem.findFirst("id = ?", recordId);
	    	
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.PlayerItem.close();
	    	}
	    	
	    	if (model == null) {
	    		return null;
	    	} else {
	    		PlayerItem playerItem = fromModel(model);
	    			    		playerItem.cost = Cost.fromModel(model);
	    			    		return playerItem;
	    	}
	    }
	    
	    public PlayerItem findFirst(String query, Object ... params) {
	    	io.gamemachine.orm.models.PlayerItem.open();
	    	io.gamemachine.orm.models.PlayerItem model = io.gamemachine.orm.models.PlayerItem.findFirst(query, params);
	    	io.gamemachine.orm.models.PlayerItem.close();
	    	if (model == null) {
	    		return null;
	    	} else {
	    		PlayerItem playerItem = fromModel(model);
	    			    		playerItem.cost = Cost.fromModel(model);
	    			    		return playerItem;
	    	}
	    }
	    
	    public List<PlayerItem> findAll() {
	    	io.gamemachine.orm.models.PlayerItem.open();
	    	List<io.gamemachine.orm.models.PlayerItem> models = io.gamemachine.orm.models.PlayerItem.findAll();
	    	List<PlayerItem> messages = new ArrayList<PlayerItem>();
	    	for (io.gamemachine.orm.models.PlayerItem model : models) {
	    		PlayerItem playerItem = fromModel(model);
	    			    		playerItem.cost = Cost.fromModel(model);
	    			    		messages.add(playerItem);
	    	}
	    	io.gamemachine.orm.models.PlayerItem.close();
	    	return messages;
	    }
	    
	    public List<PlayerItem> where(String query, Object ... params) {
	    	return where(false,query,params);
	    }
	    
	    public List<PlayerItem> where(boolean inTransaction, String query, Object ... params) {
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.PlayerItem.open();
	    	}
	    	List<io.gamemachine.orm.models.PlayerItem> models = io.gamemachine.orm.models.PlayerItem.where(query, params);
	    	List<PlayerItem> messages = new ArrayList<PlayerItem>();
	    	for (io.gamemachine.orm.models.PlayerItem model : models) {
	    		PlayerItem playerItem = fromModel(model);
	    			    		playerItem.cost = Cost.fromModel(model);
	    			    		messages.add(playerItem);
	    	}
	    	if (!inTransaction) {
	    		io.gamemachine.orm.models.PlayerItem.close();
	    	}
	    	return messages;
	    }
    }
    


	public static void clearModel(Model model) {
    	    	    	    	    	    	model.set("player_item_id",null);
    	    	    	    	    	    	model.set("player_item_name",null);
    	    	    	    	    	    	model.set("player_item_quantity",null);
    	    	    	    	    	    	model.set("player_item_color",null);
    	    	    	    	    	    	model.set("player_item_weapon",null);
    	    	    	    	    	    	    	model.set("player_item_player_id",null);
    	    	    	    	    	    	    	    	    	model.set("player_item_icon",null);
    	    	    	    	    	    	model.set("player_item_harvestable",null);
    	    	    	    	    	    	model.set("player_item_crafting_resource",null);
    	    	    	    	    	    	model.set("player_item_craftable",null);
    	    	    	    	    	    	model.set("player_item_is_consumable",null);
    	    	    	    	    	    	model.set("player_item_type",null);
    	    	    	    	    	    	model.set("player_item_max_health",null);
    	    	    	    	    	    	model.set("player_item_health",null);
    	    	    	    	    	    	model.set("player_item_level",null);
    	    	    	    	    	    	model.set("player_item_character_id",null);
    	    	    	    	    	    	model.set("player_item_container_id",null);
    	    	    	    	    	    	model.set("player_item_updated_at",null);
    	    	    	    	    	    	model.set("player_item_category",null);
    	    	    	    	    	    	model.set("player_item_location_id",null);
    	    	    	    	    	    	model.set("player_item_slot_count",null);
    	    	    	    	    	    	model.set("player_item_stackable",null);
    	    	    	    	    	    	model.set("player_item_location_type",null);
    	    	    	    	    	    	model.set("player_item_stack_max",null);
    	    	    	    	    	    	model.set("player_item_container_slot",null);
    	    	    	    	    	    	model.set("player_item_icon_uuid",null);
    	    	    	    	    	    	model.set("player_item_icon_path",null);
    	    	    	    	    	    	model.set("player_item_reference_id",null);
    	    	    	    	    	    	model.set("player_item_hidden",null);
    	    	    	    	    	    	model.set("player_item_max_quantity",null);
    	    	    	    	    	    	model.set("player_item_active",null);
    	    	    	    	    	    	model.set("player_item_weight",null);
    	    	    	    	    	    	model.set("player_item_template_block_id",null);
    	    }
    
	public void toModel(Model model) {
    	    	    	    	
    	    	    	if (id != null) {
    	       	    	model.setString("player_item_id",id);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (name != null) {
    	       	    	model.setString("player_item_name",name);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (quantity != null) {
    	       	    	model.setInteger("player_item_quantity",quantity);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (color != null) {
    	       	    	model.setString("player_item_color",color);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (weapon != null) {
    	       	    	model.setBoolean("player_item_weapon",weapon);
    	        		
    	}
    	    	    	    	    	    	
    	    	    	if (playerId != null) {
    	       	    	model.setString("player_item_player_id",playerId);
    	        		
    	}
    	    	    	    	    	
    	    	    	model.setInteger("id",recordId);
    	    	    	    	    	
    	    	    	if (icon != null) {
    	       	    	model.setString("player_item_icon",icon);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (harvestable != null) {
    	       	    	model.setInteger("player_item_harvestable",harvestable);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (craftingResource != null) {
    	       	    	model.setInteger("player_item_crafting_resource",craftingResource);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (craftable != null) {
    	       	    	model.setInteger("player_item_craftable",craftable);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (isConsumable != null) {
    	       	    	model.setBoolean("player_item_is_consumable",isConsumable);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (type != null) {
    	       	    	model.setInteger("player_item_type",type);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (maxHealth != null) {
    	       	    	model.setInteger("player_item_max_health",maxHealth);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (health != null) {
    	       	    	model.setInteger("player_item_health",health);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (level != null) {
    	       	    	model.setInteger("player_item_level",level);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (characterId != null) {
    	       	    	model.setString("player_item_character_id",characterId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (containerId != null) {
    	       	    	model.setString("player_item_container_id",containerId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (updatedAt != null) {
    	       	    	model.setInteger("player_item_updated_at",updatedAt);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (category != null) {
    	       	    	model.setString("player_item_category",category);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (locationId != null) {
    	       	    	model.setString("player_item_location_id",locationId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (slotCount != null) {
    	       	    	model.setInteger("player_item_slot_count",slotCount);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (stackable != null) {
    	       	    	model.setBoolean("player_item_stackable",stackable);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (locationType != null) {
    	       	    	model.setString("player_item_location_type",locationType);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (stackMax != null) {
    	       	    	model.setInteger("player_item_stack_max",stackMax);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (containerSlot != null) {
    	       	    	model.setInteger("player_item_container_slot",containerSlot);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (icon_uuid != null) {
    	       	    	model.setString("player_item_icon_uuid",icon_uuid);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (icon_path != null) {
    	       	    	model.setString("player_item_icon_path",icon_path);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (referenceId != null) {
    	       	    	model.setString("player_item_reference_id",referenceId);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (hidden != null) {
    	       	    	model.setBoolean("player_item_hidden",hidden);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (maxQuantity != null) {
    	       	    	model.setInteger("player_item_max_quantity",maxQuantity);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (active != null) {
    	       	    	model.setBoolean("player_item_active",active);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (weight != null) {
    	       	    	model.setFloat("player_item_weight",weight);
    	        		
    	}
    	    	    	    	    	
    	    	    	if (templateBlockId != null) {
    	       	    	model.setInteger("player_item_template_block_id",templateBlockId);
    	        		
    	}
    	    	    }
    
	public static PlayerItem fromModel(Model model) {
		boolean hasFields = false;
    	PlayerItem message = new PlayerItem();
    	    	    	    	    	
    	    	    	String idField = model.getString("player_item_id");
    	    	
    	if (idField != null) {
    		message.setId(idField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String nameField = model.getString("player_item_name");
    	    	
    	if (nameField != null) {
    		message.setName(nameField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer quantityField = model.getInteger("player_item_quantity");
    	    	
    	if (quantityField != null) {
    		message.setQuantity(quantityField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String colorField = model.getString("player_item_color");
    	    	
    	if (colorField != null) {
    		message.setColor(colorField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean weaponField = model.getBoolean("player_item_weapon");
    	    	
    	if (weaponField != null) {
    		message.setWeapon(weaponField);
    		hasFields = true;
    	}
    	    	    	    	    	    	    	
    	    	    	String playerIdField = model.getString("player_item_player_id");
    	    	
    	if (playerIdField != null) {
    		message.setPlayerId(playerIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	if (model.getInteger("id") != null) {
    		message.setRecordId(model.getInteger("id"));
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String iconField = model.getString("player_item_icon");
    	    	
    	if (iconField != null) {
    		message.setIcon(iconField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer harvestableField = model.getInteger("player_item_harvestable");
    	    	
    	if (harvestableField != null) {
    		message.setHarvestable(harvestableField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer craftingResourceField = model.getInteger("player_item_crafting_resource");
    	    	
    	if (craftingResourceField != null) {
    		message.setCraftingResource(craftingResourceField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer craftableField = model.getInteger("player_item_craftable");
    	    	
    	if (craftableField != null) {
    		message.setCraftable(craftableField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean isConsumableField = model.getBoolean("player_item_is_consumable");
    	    	
    	if (isConsumableField != null) {
    		message.setIsConsumable(isConsumableField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer typeField = model.getInteger("player_item_type");
    	    	
    	if (typeField != null) {
    		message.setType(typeField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer maxHealthField = model.getInteger("player_item_max_health");
    	    	
    	if (maxHealthField != null) {
    		message.setMaxHealth(maxHealthField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer healthField = model.getInteger("player_item_health");
    	    	
    	if (healthField != null) {
    		message.setHealth(healthField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer levelField = model.getInteger("player_item_level");
    	    	
    	if (levelField != null) {
    		message.setLevel(levelField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String characterIdField = model.getString("player_item_character_id");
    	    	
    	if (characterIdField != null) {
    		message.setCharacterId(characterIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String containerIdField = model.getString("player_item_container_id");
    	    	
    	if (containerIdField != null) {
    		message.setContainerId(containerIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer updatedAtField = model.getInteger("player_item_updated_at");
    	    	
    	if (updatedAtField != null) {
    		message.setUpdatedAt(updatedAtField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String categoryField = model.getString("player_item_category");
    	    	
    	if (categoryField != null) {
    		message.setCategory(categoryField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String locationIdField = model.getString("player_item_location_id");
    	    	
    	if (locationIdField != null) {
    		message.setLocationId(locationIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer slotCountField = model.getInteger("player_item_slot_count");
    	    	
    	if (slotCountField != null) {
    		message.setSlotCount(slotCountField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean stackableField = model.getBoolean("player_item_stackable");
    	    	
    	if (stackableField != null) {
    		message.setStackable(stackableField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String locationTypeField = model.getString("player_item_location_type");
    	    	
    	if (locationTypeField != null) {
    		message.setLocationType(locationTypeField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer stackMaxField = model.getInteger("player_item_stack_max");
    	    	
    	if (stackMaxField != null) {
    		message.setStackMax(stackMaxField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer containerSlotField = model.getInteger("player_item_container_slot");
    	    	
    	if (containerSlotField != null) {
    		message.setContainerSlot(containerSlotField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String icon_uuidField = model.getString("player_item_icon_uuid");
    	    	
    	if (icon_uuidField != null) {
    		message.setIcon_uuid(icon_uuidField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String icon_pathField = model.getString("player_item_icon_path");
    	    	
    	if (icon_pathField != null) {
    		message.setIcon_path(icon_pathField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	String referenceIdField = model.getString("player_item_reference_id");
    	    	
    	if (referenceIdField != null) {
    		message.setReferenceId(referenceIdField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean hiddenField = model.getBoolean("player_item_hidden");
    	    	
    	if (hiddenField != null) {
    		message.setHidden(hiddenField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer maxQuantityField = model.getInteger("player_item_max_quantity");
    	    	
    	if (maxQuantityField != null) {
    		message.setMaxQuantity(maxQuantityField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Boolean activeField = model.getBoolean("player_item_active");
    	    	
    	if (activeField != null) {
    		message.setActive(activeField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Float weightField = model.getFloat("player_item_weight");
    	    	
    	if (weightField != null) {
    		message.setWeight(weightField);
    		hasFields = true;
    	}
    	    	    	    	    	    	
    	    	    	Integer templateBlockIdField = model.getInteger("player_item_template_block_id");
    	    	
    	if (templateBlockIdField != null) {
    		message.setTemplateBlockId(templateBlockIdField);
    		hasFields = true;
    	}
    	    	    	if (hasFields) {
    		return message;
    	} else {
    		return null;
    	}
    }


	    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public PlayerItem setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasName()  {
        return name == null ? false : true;
    }
        
		public String getName() {
		return name;
	}
	
	public PlayerItem setName(String name) {
		this.name = name;
		return this;	}
	
		    
    public Boolean hasQuantity()  {
        return quantity == null ? false : true;
    }
        
		public Integer getQuantity() {
		return quantity;
	}
	
	public PlayerItem setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;	}
	
		    
    public Boolean hasColor()  {
        return color == null ? false : true;
    }
        
		public String getColor() {
		return color;
	}
	
	public PlayerItem setColor(String color) {
		this.color = color;
		return this;	}
	
		    
    public Boolean hasWeapon()  {
        return weapon == null ? false : true;
    }
        
		public Boolean getWeapon() {
		return weapon;
	}
	
	public PlayerItem setWeapon(Boolean weapon) {
		this.weapon = weapon;
		return this;	}
	
		    
    public Boolean hasCost()  {
        return cost == null ? false : true;
    }
        
		public Cost getCost() {
		return cost;
	}
	
	public PlayerItem setCost(Cost cost) {
		this.cost = cost;
		return this;	}
	
		    
    public Boolean hasPlayerId()  {
        return playerId == null ? false : true;
    }
        
		public String getPlayerId() {
		return playerId;
	}
	
	public PlayerItem setPlayerId(String playerId) {
		this.playerId = playerId;
		return this;	}
	
		    
    public Boolean hasRecordId()  {
        return recordId == null ? false : true;
    }
        
		public Integer getRecordId() {
		return recordId;
	}
	
	public PlayerItem setRecordId(Integer recordId) {
		this.recordId = recordId;
		return this;	}
	
		    
    public Boolean hasIcon()  {
        return icon == null ? false : true;
    }
        
		public String getIcon() {
		return icon;
	}
	
	public PlayerItem setIcon(String icon) {
		this.icon = icon;
		return this;	}
	
		    
    public Boolean hasHarvestable()  {
        return harvestable == null ? false : true;
    }
        
		public Integer getHarvestable() {
		return harvestable;
	}
	
	public PlayerItem setHarvestable(Integer harvestable) {
		this.harvestable = harvestable;
		return this;	}
	
		    
    public Boolean hasCraftingResource()  {
        return craftingResource == null ? false : true;
    }
        
		public Integer getCraftingResource() {
		return craftingResource;
	}
	
	public PlayerItem setCraftingResource(Integer craftingResource) {
		this.craftingResource = craftingResource;
		return this;	}
	
		    
    public Boolean hasCraftable()  {
        return craftable == null ? false : true;
    }
        
		public Integer getCraftable() {
		return craftable;
	}
	
	public PlayerItem setCraftable(Integer craftable) {
		this.craftable = craftable;
		return this;	}
	
		    
    public Boolean hasIsConsumable()  {
        return isConsumable == null ? false : true;
    }
        
		public Boolean getIsConsumable() {
		return isConsumable;
	}
	
	public PlayerItem setIsConsumable(Boolean isConsumable) {
		this.isConsumable = isConsumable;
		return this;	}
	
		    
    public Boolean hasType()  {
        return type == null ? false : true;
    }
        
		public Integer getType() {
		return type;
	}
	
	public PlayerItem setType(Integer type) {
		this.type = type;
		return this;	}
	
		    
    public Boolean hasMaxHealth()  {
        return maxHealth == null ? false : true;
    }
        
		public Integer getMaxHealth() {
		return maxHealth;
	}
	
	public PlayerItem setMaxHealth(Integer maxHealth) {
		this.maxHealth = maxHealth;
		return this;	}
	
		    
    public Boolean hasHealth()  {
        return health == null ? false : true;
    }
        
		public Integer getHealth() {
		return health;
	}
	
	public PlayerItem setHealth(Integer health) {
		this.health = health;
		return this;	}
	
		    
    public Boolean hasLevel()  {
        return level == null ? false : true;
    }
        
		public Integer getLevel() {
		return level;
	}
	
	public PlayerItem setLevel(Integer level) {
		this.level = level;
		return this;	}
	
		    
    public Boolean hasCharacterId()  {
        return characterId == null ? false : true;
    }
        
		public String getCharacterId() {
		return characterId;
	}
	
	public PlayerItem setCharacterId(String characterId) {
		this.characterId = characterId;
		return this;	}
	
		    
    public Boolean hasContainerId()  {
        return containerId == null ? false : true;
    }
        
		public String getContainerId() {
		return containerId;
	}
	
	public PlayerItem setContainerId(String containerId) {
		this.containerId = containerId;
		return this;	}
	
		    
    public Boolean hasUpdatedAt()  {
        return updatedAt == null ? false : true;
    }
        
		public Integer getUpdatedAt() {
		return updatedAt;
	}
	
	public PlayerItem setUpdatedAt(Integer updatedAt) {
		this.updatedAt = updatedAt;
		return this;	}
	
		    
    public Boolean hasCategory()  {
        return category == null ? false : true;
    }
        
		public String getCategory() {
		return category;
	}
	
	public PlayerItem setCategory(String category) {
		this.category = category;
		return this;	}
	
		    
    public Boolean hasLocationId()  {
        return locationId == null ? false : true;
    }
        
		public String getLocationId() {
		return locationId;
	}
	
	public PlayerItem setLocationId(String locationId) {
		this.locationId = locationId;
		return this;	}
	
		    
    public Boolean hasSlotCount()  {
        return slotCount == null ? false : true;
    }
        
		public Integer getSlotCount() {
		return slotCount;
	}
	
	public PlayerItem setSlotCount(Integer slotCount) {
		this.slotCount = slotCount;
		return this;	}
	
		    
    public Boolean hasStackable()  {
        return stackable == null ? false : true;
    }
        
		public Boolean getStackable() {
		return stackable;
	}
	
	public PlayerItem setStackable(Boolean stackable) {
		this.stackable = stackable;
		return this;	}
	
		    
    public Boolean hasLocationType()  {
        return locationType == null ? false : true;
    }
        
		public String getLocationType() {
		return locationType;
	}
	
	public PlayerItem setLocationType(String locationType) {
		this.locationType = locationType;
		return this;	}
	
		    
    public Boolean hasStackMax()  {
        return stackMax == null ? false : true;
    }
        
		public Integer getStackMax() {
		return stackMax;
	}
	
	public PlayerItem setStackMax(Integer stackMax) {
		this.stackMax = stackMax;
		return this;	}
	
		    
    public Boolean hasContainerSlot()  {
        return containerSlot == null ? false : true;
    }
        
		public Integer getContainerSlot() {
		return containerSlot;
	}
	
	public PlayerItem setContainerSlot(Integer containerSlot) {
		this.containerSlot = containerSlot;
		return this;	}
	
		    
    public Boolean hasIcon_uuid()  {
        return icon_uuid == null ? false : true;
    }
        
		public String getIcon_uuid() {
		return icon_uuid;
	}
	
	public PlayerItem setIcon_uuid(String icon_uuid) {
		this.icon_uuid = icon_uuid;
		return this;	}
	
		    
    public Boolean hasIcon_path()  {
        return icon_path == null ? false : true;
    }
        
		public String getIcon_path() {
		return icon_path;
	}
	
	public PlayerItem setIcon_path(String icon_path) {
		this.icon_path = icon_path;
		return this;	}
	
		    
    public Boolean hasReferenceId()  {
        return referenceId == null ? false : true;
    }
        
		public String getReferenceId() {
		return referenceId;
	}
	
	public PlayerItem setReferenceId(String referenceId) {
		this.referenceId = referenceId;
		return this;	}
	
		    
    public Boolean hasHidden()  {
        return hidden == null ? false : true;
    }
        
		public Boolean getHidden() {
		return hidden;
	}
	
	public PlayerItem setHidden(Boolean hidden) {
		this.hidden = hidden;
		return this;	}
	
		    
    public Boolean hasMaxQuantity()  {
        return maxQuantity == null ? false : true;
    }
        
		public Integer getMaxQuantity() {
		return maxQuantity;
	}
	
	public PlayerItem setMaxQuantity(Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
		return this;	}
	
		    
    public Boolean hasActive()  {
        return active == null ? false : true;
    }
        
		public Boolean getActive() {
		return active;
	}
	
	public PlayerItem setActive(Boolean active) {
		this.active = active;
		return this;	}
	
		    
    public Boolean hasWeight()  {
        return weight == null ? false : true;
    }
        
		public Float getWeight() {
		return weight;
	}
	
	public PlayerItem setWeight(Float weight) {
		this.weight = weight;
		return this;	}
	
		    
    public Boolean hasTemplateBlockId()  {
        return templateBlockId == null ? false : true;
    }
        
		public Integer getTemplateBlockId() {
		return templateBlockId;
	}
	
	public PlayerItem setTemplateBlockId(Integer templateBlockId) {
		this.templateBlockId = templateBlockId;
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

    public Schema<PlayerItem> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public PlayerItem newMessage()
    {
        return new PlayerItem();
    }

    public Class<PlayerItem> typeClass()
    {
        return PlayerItem.class;
    }

    public String messageName()
    {
        return PlayerItem.class.getSimpleName();
    }

    public String messageFullName()
    {
        return PlayerItem.class.getName();
    }

    public boolean isInitialized(PlayerItem message)
    {
        return true;
    }

    public void mergeFrom(Input input, PlayerItem message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.name = input.readString();
                	break;
                	                	
                            	            	case 3:
            	                	                	message.quantity = input.readInt32();
                	break;
                	                	
                            	            	case 4:
            	                	                	message.color = input.readString();
                	break;
                	                	
                            	            	case 5:
            	                	                	message.weapon = input.readBool();
                	break;
                	                	
                            	            	case 8:
            	                	                	message.cost = input.mergeObject(message.cost, Cost.getSchema());
                    break;
                                    	
                            	            	case 9:
            	                	                	message.playerId = input.readString();
                	break;
                	                	
                            	            	case 10:
            	                	                	message.recordId = input.readInt32();
                	break;
                	                	
                            	            	case 11:
            	                	                	message.icon = input.readString();
                	break;
                	                	
                            	            	case 12:
            	                	                	message.harvestable = input.readInt32();
                	break;
                	                	
                            	            	case 13:
            	                	                	message.craftingResource = input.readInt32();
                	break;
                	                	
                            	            	case 14:
            	                	                	message.craftable = input.readInt32();
                	break;
                	                	
                            	            	case 16:
            	                	                	message.isConsumable = input.readBool();
                	break;
                	                	
                            	            	case 17:
            	                	                	message.type = input.readInt32();
                	break;
                	                	
                            	            	case 18:
            	                	                	message.maxHealth = input.readInt32();
                	break;
                	                	
                            	            	case 19:
            	                	                	message.health = input.readInt32();
                	break;
                	                	
                            	            	case 20:
            	                	                	message.level = input.readInt32();
                	break;
                	                	
                            	            	case 21:
            	                	                	message.characterId = input.readString();
                	break;
                	                	
                            	            	case 22:
            	                	                	message.containerId = input.readString();
                	break;
                	                	
                            	            	case 23:
            	                	                	message.updatedAt = input.readInt32();
                	break;
                	                	
                            	            	case 24:
            	                	                	message.category = input.readString();
                	break;
                	                	
                            	            	case 25:
            	                	                	message.locationId = input.readString();
                	break;
                	                	
                            	            	case 26:
            	                	                	message.slotCount = input.readInt32();
                	break;
                	                	
                            	            	case 27:
            	                	                	message.stackable = input.readBool();
                	break;
                	                	
                            	            	case 28:
            	                	                	message.locationType = input.readString();
                	break;
                	                	
                            	            	case 29:
            	                	                	message.stackMax = input.readInt32();
                	break;
                	                	
                            	            	case 30:
            	                	                	message.containerSlot = input.readInt32();
                	break;
                	                	
                            	            	case 31:
            	                	                	message.icon_uuid = input.readString();
                	break;
                	                	
                            	            	case 32:
            	                	                	message.icon_path = input.readString();
                	break;
                	                	
                            	            	case 33:
            	                	                	message.referenceId = input.readString();
                	break;
                	                	
                            	            	case 34:
            	                	                	message.hidden = input.readBool();
                	break;
                	                	
                            	            	case 35:
            	                	                	message.maxQuantity = input.readInt32();
                	break;
                	                	
                            	            	case 36:
            	                	                	message.active = input.readBool();
                	break;
                	                	
                            	            	case 37:
            	                	                	message.weight = input.readFloat();
                	break;
                	                	
                            	            	case 38:
            	                	                	message.templateBlockId = input.readInt32();
                	break;
                	                	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, PlayerItem message) throws IOException
    {
    	    	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(1, message.id, false);
    	    	
    	            	
    	    	if(message.name == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.name != null)
            output.writeString(2, message.name, false);
    	    	
    	            	
    	    	if(message.quantity == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.quantity != null)
            output.writeInt32(3, message.quantity, false);
    	    	
    	            	
    	    	
    	    	    	if(message.color != null)
            output.writeString(4, message.color, false);
    	    	
    	            	
    	    	
    	    	    	if(message.weapon != null)
            output.writeBool(5, message.weapon, false);
    	    	
    	            	
    	    	
    	    	    	if(message.cost != null)
    		output.writeObject(8, message.cost, Cost.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerId != null)
            output.writeString(9, message.playerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.recordId != null)
            output.writeInt32(10, message.recordId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon != null)
            output.writeString(11, message.icon, false);
    	    	
    	            	
    	    	
    	    	    	if(message.harvestable != null)
            output.writeInt32(12, message.harvestable, false);
    	    	
    	            	
    	    	
    	    	    	if(message.craftingResource != null)
            output.writeInt32(13, message.craftingResource, false);
    	    	
    	            	
    	    	
    	    	    	if(message.craftable != null)
            output.writeInt32(14, message.craftable, false);
    	    	
    	            	
    	    	
    	    	    	if(message.isConsumable != null)
            output.writeBool(16, message.isConsumable, false);
    	    	
    	            	
    	    	
    	    	    	if(message.type != null)
            output.writeInt32(17, message.type, false);
    	    	
    	            	
    	    	
    	    	    	if(message.maxHealth != null)
            output.writeInt32(18, message.maxHealth, false);
    	    	
    	            	
    	    	
    	    	    	if(message.health != null)
            output.writeInt32(19, message.health, false);
    	    	
    	            	
    	    	
    	    	    	if(message.level != null)
            output.writeInt32(20, message.level, false);
    	    	
    	            	
    	    	
    	    	    	if(message.characterId != null)
            output.writeString(21, message.characterId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.containerId != null)
            output.writeString(22, message.containerId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.updatedAt != null)
            output.writeInt32(23, message.updatedAt, false);
    	    	
    	            	
    	    	
    	    	    	if(message.category != null)
            output.writeString(24, message.category, false);
    	    	
    	            	
    	    	
    	    	    	if(message.locationId != null)
            output.writeString(25, message.locationId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.slotCount != null)
            output.writeInt32(26, message.slotCount, false);
    	    	
    	            	
    	    	
    	    	    	if(message.stackable != null)
            output.writeBool(27, message.stackable, false);
    	    	
    	            	
    	    	
    	    	    	if(message.locationType != null)
            output.writeString(28, message.locationType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.stackMax != null)
            output.writeInt32(29, message.stackMax, false);
    	    	
    	            	
    	    	
    	    	    	if(message.containerSlot != null)
            output.writeInt32(30, message.containerSlot, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon_uuid != null)
            output.writeString(31, message.icon_uuid, false);
    	    	
    	            	
    	    	
    	    	    	if(message.icon_path != null)
            output.writeString(32, message.icon_path, false);
    	    	
    	            	
    	    	
    	    	    	if(message.referenceId != null)
            output.writeString(33, message.referenceId, false);
    	    	
    	            	
    	    	if(message.hidden == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.hidden != null)
            output.writeBool(34, message.hidden, false);
    	    	
    	            	
    	    	
    	    	    	if(message.maxQuantity != null)
            output.writeInt32(35, message.maxQuantity, false);
    	    	
    	            	
    	    	if(message.active == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.active != null)
            output.writeBool(36, message.active, false);
    	    	
    	            	
    	    	if(message.weight == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.weight != null)
            output.writeFloat(37, message.weight, false);
    	    	
    	            	
    	    	
    	    	    	if(message.templateBlockId != null)
            output.writeInt32(38, message.templateBlockId, false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START PlayerItem");
    	    	if(this.id != null) {
    		System.out.println("id="+this.id);
    	}
    	    	if(this.name != null) {
    		System.out.println("name="+this.name);
    	}
    	    	if(this.quantity != null) {
    		System.out.println("quantity="+this.quantity);
    	}
    	    	if(this.color != null) {
    		System.out.println("color="+this.color);
    	}
    	    	if(this.weapon != null) {
    		System.out.println("weapon="+this.weapon);
    	}
    	    	if(this.cost != null) {
    		System.out.println("cost="+this.cost);
    	}
    	    	if(this.playerId != null) {
    		System.out.println("playerId="+this.playerId);
    	}
    	    	if(this.recordId != null) {
    		System.out.println("recordId="+this.recordId);
    	}
    	    	if(this.icon != null) {
    		System.out.println("icon="+this.icon);
    	}
    	    	if(this.harvestable != null) {
    		System.out.println("harvestable="+this.harvestable);
    	}
    	    	if(this.craftingResource != null) {
    		System.out.println("craftingResource="+this.craftingResource);
    	}
    	    	if(this.craftable != null) {
    		System.out.println("craftable="+this.craftable);
    	}
    	    	if(this.isConsumable != null) {
    		System.out.println("isConsumable="+this.isConsumable);
    	}
    	    	if(this.type != null) {
    		System.out.println("type="+this.type);
    	}
    	    	if(this.maxHealth != null) {
    		System.out.println("maxHealth="+this.maxHealth);
    	}
    	    	if(this.health != null) {
    		System.out.println("health="+this.health);
    	}
    	    	if(this.level != null) {
    		System.out.println("level="+this.level);
    	}
    	    	if(this.characterId != null) {
    		System.out.println("characterId="+this.characterId);
    	}
    	    	if(this.containerId != null) {
    		System.out.println("containerId="+this.containerId);
    	}
    	    	if(this.updatedAt != null) {
    		System.out.println("updatedAt="+this.updatedAt);
    	}
    	    	if(this.category != null) {
    		System.out.println("category="+this.category);
    	}
    	    	if(this.locationId != null) {
    		System.out.println("locationId="+this.locationId);
    	}
    	    	if(this.slotCount != null) {
    		System.out.println("slotCount="+this.slotCount);
    	}
    	    	if(this.stackable != null) {
    		System.out.println("stackable="+this.stackable);
    	}
    	    	if(this.locationType != null) {
    		System.out.println("locationType="+this.locationType);
    	}
    	    	if(this.stackMax != null) {
    		System.out.println("stackMax="+this.stackMax);
    	}
    	    	if(this.containerSlot != null) {
    		System.out.println("containerSlot="+this.containerSlot);
    	}
    	    	if(this.icon_uuid != null) {
    		System.out.println("icon_uuid="+this.icon_uuid);
    	}
    	    	if(this.icon_path != null) {
    		System.out.println("icon_path="+this.icon_path);
    	}
    	    	if(this.referenceId != null) {
    		System.out.println("referenceId="+this.referenceId);
    	}
    	    	if(this.hidden != null) {
    		System.out.println("hidden="+this.hidden);
    	}
    	    	if(this.maxQuantity != null) {
    		System.out.println("maxQuantity="+this.maxQuantity);
    	}
    	    	if(this.active != null) {
    		System.out.println("active="+this.active);
    	}
    	    	if(this.weight != null) {
    		System.out.println("weight="+this.weight);
    	}
    	    	if(this.templateBlockId != null) {
    		System.out.println("templateBlockId="+this.templateBlockId);
    	}
    	    	System.out.println("END PlayerItem");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "id";
        	        	case 2: return "name";
        	        	case 3: return "quantity";
        	        	case 4: return "color";
        	        	case 5: return "weapon";
        	        	case 8: return "cost";
        	        	case 9: return "playerId";
        	        	case 10: return "recordId";
        	        	case 11: return "icon";
        	        	case 12: return "harvestable";
        	        	case 13: return "craftingResource";
        	        	case 14: return "craftable";
        	        	case 16: return "isConsumable";
        	        	case 17: return "type";
        	        	case 18: return "maxHealth";
        	        	case 19: return "health";
        	        	case 20: return "level";
        	        	case 21: return "characterId";
        	        	case 22: return "containerId";
        	        	case 23: return "updatedAt";
        	        	case 24: return "category";
        	        	case 25: return "locationId";
        	        	case 26: return "slotCount";
        	        	case 27: return "stackable";
        	        	case 28: return "locationType";
        	        	case 29: return "stackMax";
        	        	case 30: return "containerSlot";
        	        	case 31: return "icon_uuid";
        	        	case 32: return "icon_path";
        	        	case 33: return "referenceId";
        	        	case 34: return "hidden";
        	        	case 35: return "maxQuantity";
        	        	case 36: return "active";
        	        	case 37: return "weight";
        	        	case 38: return "templateBlockId";
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
    	    	__fieldMap.put("id", 1);
    	    	__fieldMap.put("name", 2);
    	    	__fieldMap.put("quantity", 3);
    	    	__fieldMap.put("color", 4);
    	    	__fieldMap.put("weapon", 5);
    	    	__fieldMap.put("cost", 8);
    	    	__fieldMap.put("playerId", 9);
    	    	__fieldMap.put("recordId", 10);
    	    	__fieldMap.put("icon", 11);
    	    	__fieldMap.put("harvestable", 12);
    	    	__fieldMap.put("craftingResource", 13);
    	    	__fieldMap.put("craftable", 14);
    	    	__fieldMap.put("isConsumable", 16);
    	    	__fieldMap.put("type", 17);
    	    	__fieldMap.put("maxHealth", 18);
    	    	__fieldMap.put("health", 19);
    	    	__fieldMap.put("level", 20);
    	    	__fieldMap.put("characterId", 21);
    	    	__fieldMap.put("containerId", 22);
    	    	__fieldMap.put("updatedAt", 23);
    	    	__fieldMap.put("category", 24);
    	    	__fieldMap.put("locationId", 25);
    	    	__fieldMap.put("slotCount", 26);
    	    	__fieldMap.put("stackable", 27);
    	    	__fieldMap.put("locationType", 28);
    	    	__fieldMap.put("stackMax", 29);
    	    	__fieldMap.put("containerSlot", 30);
    	    	__fieldMap.put("icon_uuid", 31);
    	    	__fieldMap.put("icon_path", 32);
    	    	__fieldMap.put("referenceId", 33);
    	    	__fieldMap.put("hidden", 34);
    	    	__fieldMap.put("maxQuantity", 35);
    	    	__fieldMap.put("active", 36);
    	    	__fieldMap.put("weight", 37);
    	    	__fieldMap.put("templateBlockId", 38);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = PlayerItem.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static PlayerItem parseFrom(byte[] bytes) {
	PlayerItem message = new PlayerItem();
	ProtobufIOUtil.mergeFrom(bytes, message, PlayerItem.getSchema());
	return message;
}

public static PlayerItem parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	PlayerItem message = new PlayerItem();
	JsonIOUtil.mergeFrom(bytes, message, PlayerItem.getSchema(), false);
	return message;
}

public PlayerItem clone() {
	byte[] bytes = this.toByteArray();
	PlayerItem playerItem = PlayerItem.parseFrom(bytes);
	return playerItem;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, PlayerItem.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<PlayerItem> schema = PlayerItem.getSchema();
    
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
		bytes = ProtobufIOUtil.toByteArray(this, PlayerItem.getSchema(), buffer);
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
		ProtobufIOUtil.writeTo(buffer, this, PlayerItem.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}