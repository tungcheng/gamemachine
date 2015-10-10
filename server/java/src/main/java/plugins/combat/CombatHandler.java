package plugins.combat;

import com.google.common.base.Strings;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.Attack;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GmVector3;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Character;

public class CombatHandler extends GameMessageActor {

	public static String name = CombatHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	@Override
	public void awake() {
	}

	@Override
	public void onTick(String message) {

	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (exactlyOnce(gameMessage)) {
			if (gameMessage.attack != null) {
				doAttack(gameMessage.attack);
				setReply(gameMessage);
			} else if (gameMessage.dataRequest != null) {
				// effectHandler.tell(gameMessage.dataRequest, getSelf());
			}
		}
	}

	private void sendAttack(Attack attack) {
		GameMessage msg = new GameMessage();

		Grid grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), "default", playerId);
		for (TrackData trackData : grid.getAll()) {
			if (!playerId.equals(trackData.id)) {
				msg.attack = attack;
				PlayerCommands.sendGameMessage(msg, trackData.id);
			}
		}
	}
	
	private void doAttack(Attack attack) {
		boolean sendToObjectGrid = false;
		boolean sendToDefaultGrid = false;
		
		int zone = GameGrid.getEntityZone(playerId);
		if (attack.playerSkill == null) {
			logger.warning("Attack without player skill, ignoring");
			return;
		}

		if (Strings.isNullOrEmpty(attack.attackerCharacterId)) {
			logger.warning("Attack without attackerCharacterId, ignoring");
			return;
		}
		
		logger.warning("Attack " + attack.attackerCharacterId + " " + attack.targetId + " skill " + attack.playerSkill.id);
		
		StatusEffectTarget statusEffectTarget = new StatusEffectTarget();
		
		statusEffectTarget.attack = attack;
		
		statusEffectTarget.originCharacterId = attack.attackerCharacterId;
		statusEffectTarget.originEntityId = playerId;
		
		if (attack.playerSkill.skillType.equals(PlayerSkill.SkillType.Passive.toString())) {
			logger.warning("Set passive flags");
			statusEffectTarget.action = StatusEffectTarget.Action.Apply;
			statusEffectTarget.passiveFlag = StatusEffectTarget.PassiveFlag.AutoRemove;
		} else {
			statusEffectTarget.action = StatusEffectTarget.Action.Apply;
			statusEffectTarget.passiveFlag = StatusEffectTarget.PassiveFlag.NA;
		}
		
		if (attack.playerSkill.damageType.equals(PlayerSkill.DamageType.SingleTarget.toString())) {
			if (Strings.isNullOrEmpty(attack.targetId)) {
				logger.warning("SingleTarget with no targetId");
				return;
			} else {
				Character character = CharacterService.instance().find(attack.targetId);
				
				// No character = Object/vehicle/etc..
				if (character == null) {
					statusEffectTarget.targetEntityId = attack.targetId;
					sendToObjectGrid = true;
				} else {
					statusEffectTarget.targetEntityId = character.playerId;
					sendToDefaultGrid = true;
				}
			}
			
		} else if (attack.playerSkill.damageType.equals(PlayerSkill.DamageType.Self.toString())) {
			statusEffectTarget.targetEntityId = playerId;
			sendToDefaultGrid = true;
			
		} else if (attack.playerSkill.damageType.equals(PlayerSkill.DamageType.Aoe.toString())) {
			if (attack.targetLocation == null) {
				logger.warning("Aoe without targetLocation");
				return;
			}
			
			statusEffectTarget.location = attack.targetLocation;
			sendToObjectGrid = true;
			sendToDefaultGrid = true;
			
		} else if (attack.playerSkill.damageType.equals(PlayerSkill.DamageType.Pbaoe.toString())) {
			
			Grid grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), "default", zone);
			
			TrackData td = grid.get(playerId);
			if (td == null) {
				logger.warning("TrackData not found for "+playerId);
				return;
			}
			
			statusEffectTarget.location = new GmVector3();
			statusEffectTarget.location.xi = td.x;
			statusEffectTarget.location.yi = td.y;
			statusEffectTarget.location.zi = td.z;
			
			sendToObjectGrid = true;
			sendToDefaultGrid = true;
		} else {
			logger.warning("Invalid damage type");
			return;
		}
		
		if (sendToDefaultGrid) {
			StatusEffectHandler.tell("default", zone, statusEffectTarget.clone(), getSelf());
		}
		
		if (sendToObjectGrid) {
			StatusEffectHandler.tell("build_objects", zone, statusEffectTarget.clone(), getSelf());
		}
		
		sendAttack(attack);
	}

}
