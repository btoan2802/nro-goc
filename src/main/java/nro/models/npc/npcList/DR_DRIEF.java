package nro.models.npc.npcList;

import nro.consts.ConstMap;
import nro.consts.ConstNpc;
import nro.consts.ConstPlayer;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.NpcMethod;
import nro.services.NpcService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineServiceNew;
import nro.services.func.ShopService;

public class DR_DRIEF extends Npc {

    public DR_DRIEF(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    @Override
    public void openBaseMenu(Player pl) {
        if (canOpenNpc(pl)) {
            switch (mapId) {
                case 84:
                    this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                            "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                            pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất"
                                    : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    break;
                case 153:
                    Clan clan = pl.clan;
                    ClanMember cm = pl.clanMember;
                    if (cm.role == Clan.LEADER) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Cần 1000 capsule bang [đang có " + clan.clanPoint
                                + " capsule bang] để nâng cấp bang hội lên cấp "
                                + (clan.level++) + "\n"
                                + "+1 tối đa số lượng thành viên",
                                "Về\nĐảoKame", "Góp " + cm.memberPoint + " capsule", "Nâng cấp",
                                "Từ chối");
                    } else {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Bạn đang có " + cm.memberPoint
                                + " capsule bang,bạn có muốn đóng góp toàn bộ cho bang hội của mình không ?",
                                "Về\nĐảoKame", "Đồng ý", "Từ chối");
                    }
                    break;
                case 187:
                    this.createOtherMenu(pl, ConstNpc.BASE_MENU, "|2|Chào con  !\n"
                            + "|4|Con có muốn thử 1 chút với nâng cấp Linh thú không ?",
                            "Pha lê hóa\n Linh thú", "Ép sao\n Linh Thú",
                            "Đóng");
                    break;
                default:
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar,
                                    "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                    break;
            }

        }
    }

    @Override
    public void confirmMenu(Player player, int select) {

        if (canOpenNpc(player)) {
            switch (player.iDMark.getIndexMenu()) {
                case ConstNpc.BASE_MENU:
                    switch (mapId) {
                        case 187:
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHA_LE_HOA_LINH_THU);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.EP_PHA_LE_LINH_THU);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 84:
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                            break;
                        case 153:
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, ConstMap.DAO_KAME, -1, 1059, 408);
                                    break;
                                case 1:
                                    Clan clan = player.clan;
                                    ClanMember cm = player.clanMember;
                                    if (select == 1) {
                                        player.clan.clanPoint += cm.memberPoint;
                                        cm.clanPoint += cm.memberPoint;
                                        cm.memberPoint = 0;
                                        Service.getInstance().sendThongBao(player, "Đóng góp thành công");
                                    } else if (select == 2 && cm.role == Clan.LEADER) {
                                        if (clan.level >= 5) {
                                            Service.getInstance().sendThongBao(player,
                                                    "Bang hội của bạn đã đạt cấp tối đa");
                                            return;
                                        }
                                        if (clan.clanPoint < 1000) {
                                            Service.getInstance().sendThongBao(player, "Không đủ capsule");
                                            return;
                                        }
                                        clan.level++;
                                        clan.maxMember++;
                                        clan.clanPoint -= 1000;
                                        Service.getInstance().sendThongBao(player,
                                                "Bang hội của bạn đã được nâng cấp lên cấp " + clan.level);
                                    }
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                            break;
                    }
                    break;
                case ConstNpc.MENU_START_COMBINE:
                    NpcMethod.gI().startCombine(player, select);
                    break;
                default:
                    break;
            }
        }
    }
};
