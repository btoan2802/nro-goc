package nro.models.boss.robotsatthu;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.ServerNotify;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.Util;

public class Android20 extends Boss {

    public Android20() {
        super(BossFactory.ANDROID_20, BossData.ANDROID_20);
    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            short listBossTogether[] = {BossFactory.ANDROID_19};
            int x = Util.nextInt(100, zone.map.mapWidth - 100);
            CreatBossTogether(zone, listBossTogether, x);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }

    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        try {
            TaskService.gI().checkDoneTaskKillBoss(pl, this);
            if (!generalRewards(pl, (byte) 10, (byte) 25)) {
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void doneChatS() {
        ChangeToAttackTogether(BossFactory.ANDROID_19);
    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"|-2|Chào anh! em đứng đây từ chiều",
            "|-1|Quái lạ! Sao chúng biết rõ tung tích của bọn ta thế nhỉ?",
            "|-1|Chúng còn biết chính xác ta sẽ xuất hiện ở đây để đón đánh nữa!",
            "|-1|Chúng mày là ai từ đâu tới?Cho tao xin cái địa chỉ",
            "|-2|Điều ấy biết hay không cũng không còn quan trọng nữa",
            "|-1|Ừ bọn bây chỉ là hạng tôm tép ta chẳng cần biết tên làm gì!",
            "|-1|Số 19! Xuất chiêu đi nào",
            "|-3|Okê đại ca, em sẽ xử lý bọn này trong vòng 2 tiếng."
        };
        this.textTalkMidle = new String[]{"|-1|Oải rồi hả?",
            "|-1|Ê cố lên nhóc",
            "|-1|Chán",
            "|-1|Mi khá đấy, nhưng so với ta cũng chỉ là hạng tôm tép",
            "|-1|Lôi Công Trảo",
            "|-1|Cho dù ngươi có mạnh đến đâu.. thì cũng không đánh bại được rôbốt bọn ta",
            "|-2|Lão già khôn thật!!",
            "|-2|Hừ! Lão già khốn kiếp!",};

    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null) {
            switch (plAtt.playerSkill.skillSelect.template.id) {
                case Skill.KAMEJOKO:
                case Skill.MASENKO:
                case Skill.ANTOMIC:
                    PlayerService.gI().hoiPhuc(this, damage, 0);
                    return 0;
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

}
