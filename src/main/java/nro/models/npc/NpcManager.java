package nro.models.npc;

import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.TaskService;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 💖 em Hoàng 💖
 * @copyright 💖 em Thưởng 💖
 *
 */
public class NpcManager {

    public static Npc getByIdAndMap(int id, int mapId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == id && npc.mapId == mapId) {
                return npc;
            }
        }
        return null;
    }

    public static Npc getNpc(byte tempId) {
        for (Npc npc : Manager.NPCS) {
            if (npc.tempId == tempId) {
                return npc;
            }
        }
        return null;
    }

    public static List<Npc> getNpcsByMapPlayer(Player player) {
        List<Npc> list = new ArrayList<>();
        if (player.zone != null) {
            for (Npc npc : player.zone.map.npcs) {

                if (npc.tempId == ConstNpc.NOI_BANH && Manager.EVENT_SEVER != 4) {
                    continue;
                }
                if (npc.tempId == ConstNpc.QUA_TRUNG && player.mabuEgg == null && player.zone.map.mapId == (player.gender + 21)) {
                    continue;
                }
                if (npc.tempId == ConstNpc.QUA_TRUNG && player.kaminEgg == null && player.zone.map.mapId == (player.gender + 202)) {
                    continue;
                } else if (npc.tempId == ConstNpc.CALICK && TaskService.gI().getIdTask(player) < ConstTask.TASK_21_0) {
                    continue;
                } else if (npc.tempId == ConstNpc.THAN_MEO_KARIN && (player.istrain || player.IsTraing_type2) && player.zone.map.mapId == 46 || npc.tempId == ConstNpc.THAN_MEO_KARIN && (player.isTrainning || player.IsTraing_type2 || player.isChallenge || player.ischallenge_type2)) {
                    continue;
                }
                list.add(npc);
            }
        }
        return list;
    }
}
