/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.resources;

import nro.consts.Cmd;
import nro.data.DataGame;
import nro.resources.entity.EffectData;
import nro.resources.entity.ImageByName;
import nro.resources.entity.MobData;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.utils.FileUtils;
import nro.utils.Log;
import nro.utils.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import nro.services.func.SummonDragon;
import nro.utils.FileIO;

/**
 *
 * @author kitakeyos - Hoàng Hữu Dũng
 */
public class Resources {

    private static final Resources instance = new Resources();

    public static Resources getInstance() {
        return instance;
    }

    private final List<AbsResources> resourceses;

    public Resources() {
        resourceses = new ArrayList<>();
        resourceses.add(new RNormal());

        // resourceses.add(new RSpecial());
    }

    public void init() {
        for (AbsResources res : resourceses) {
            res.init();
        }
        initCacheResources();

    }

    public byte[] readAllBytes(byte type, String path) {
        AbsResources resources = find(type);
        return resources.readAllBytes(path);
    }

    public List<String> readAllLines(byte type, String path) {
        AbsResources resources = find(type);
        return resources.readAllLines(path);
    }

    public AbsResources find(int type) {
        if (type < 1 && type >= resourceses.size()) {
            if (type == 5) {
                return resourceses.get(1);
            }
        }
        return resourceses.get(0);
    }

    public static final Map<Integer, Map<String, byte[]>> CachedData = new ConcurrentHashMap<>();

    public void initCacheResources() {
        for (int zoom = 1; zoom <= 4; zoom++) {
            AbsResources res = find(zoom);
            File root = new File(res.getFolder(), "data/" + zoom);
            Map<String, byte[]> map = new ConcurrentHashMap<>();
            ArrayList<File> datas = new ArrayList<>();
            FileUtils.addPath(datas, root);
            for (File file : datas) {
                try {
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    map.put(file.getName().replace(".png", ""), bytes);
                    // System.out.println("initCacheResources >> " + file.getName());

                } catch (IOException ex) {
                }
            }
            CachedData.put(zoom, map);
            //    System.out.println("CachedData " +zoom+"  Size>> " + CachedData.get(zoom).size());

        }

    }

    public void downloadResources(Session session, Message ms) {
        DataInputStream reader = null;
        try {
            reader = ms.reader();
            byte type = reader.readByte();
            if (type == 1) {
                //  System.out.println("Session zoomLevel>>> " +session.zoomLevel);
                Map<String, byte[]> datas = CachedData.get((int) session.zoomLevel);
                sendNumberOfFiles(session, (short) datas.size());
                for (Map.Entry<String, byte[]> entry : datas.entrySet()) {
                    fileTransfer(session, entry.getKey(), entry.getValue());
                }
                fileTransferCompleted(session);

            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(DataGame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    java.util.logging.Logger.getLogger(DataGame.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    public void sendNumberOfFiles(Session session, short size) {
        Message msg = null;
        try {
            msg = new Message(Cmd.GET_IMAGE_SOURCE);
            msg.writer().writeByte(1);
            msg.writer().writeShort(size);
            session.sendMessage(msg);

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup(); // Đảm bảo tài nguyên được giải phóng.
            }
        }
    }

    public void fileTransferCompleted(Session session) {
        AbsResources res = find(session.typeClient);
        if (res != null) {
            int[] version = res.getDataVersion();
            Message msg = null;
            try {
                msg = new Message(Cmd.GET_IMAGE_SOURCE);
                msg.writer().writeByte(3);
                msg.writer().writeInt(version[session.zoomLevel - 1]);
                session.sendMessage(msg);
            } catch (Exception e) {
                Log.error(DataGame.class, e);
            } finally {
                if (msg != null) {
                    msg.cleanup(); // Đảm bảo tài nguyên được giải phóng.
                }
            }
        }
    }

    public void sendResVersion(Session session) {
        Message mss = null;
        DataOutputStream ds = null;
        try {
            AbsResources res = find(session.typeClient);
            if (res != null) {
                int[] version = res.getDataVersion();
                mss = new Message(Cmd.GET_IMAGE_SOURCE);
                ds = mss.writer();
                ds.writeByte(0);
                ds.writeInt(version[session.zoomLevel - 1]);
                ds.flush();
                session.sendMessage(mss);
            }
        } catch (IOException ex) {
            ex.printStackTrace(); // Ghi log lỗi để dễ dàng theo dõi và khắc phục.
        } finally {
            if (mss != null) {
                mss.cleanup(); // Đảm bảo tài nguyên được giải phóng.
            }
            // Không cần đóng ds vì nó được đóng tự động khi mss.cleanup() được gọi.
        }
    }

    public void fileTransfer(Session session, String name, byte[] data) {
        Message mss = null;
        DataOutputStream ds = null;
        try {

            mss = new Message(Cmd.GET_IMAGE_SOURCE);
            ds = mss.writer();
            ds.writeByte(2);
            ds.writeUTF(name);

            ds.writeInt(data.length);
            ds.write(data);
            ds.flush();

            session.sendMessage(mss);
        } catch (IOException ex) {
            ex.printStackTrace(); // Ghi log lỗi để dễ dàng theo dõi và khắc phục.
        } finally {
            if (mss != null) {
                mss.cleanup(); // Đảm bảo tài nguyên được giải phóng.
            }
        }
    }

    public void downloadIconData(Session session, int id) {
        Message msg = null;
        try {
            AbsResources res = find(session.typeClient);
            if (res != null) {
                byte[] data = res.getRawIconData(session.zoomLevel, id);
                msg = new Message(Cmd.REQUEST_ICON);
                DataOutputStream ds = msg.writer();
                ds.writeInt(id);
                ds.writeInt(data.length);
                ds.write(data);
                ds.flush();
                session.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Ghi log lỗi để dễ dàng theo dõi và khắc phục.
        } finally {
            if (msg != null) {
                msg.cleanup(); // Đảm bảo tài nguyên được giải phóng.
            }
        }
    }

    public void downloadBGTemplate(Session session, int id) {
        Message msg = null;
        try {
            AbsResources res = find(session.typeClient);
            if (res != null) {
                byte[] data = res.getRawBGData(session.zoomLevel, id);
                msg = new Message(Cmd.BACKGROUND_TEMPLATE);
                DataOutputStream ds = msg.writer();
                ds.writeShort(id);
                ds.writeInt(data.length);
                ds.write(data);
                ds.flush();
                session.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Ghi log lỗi để dễ dàng theo dõi và khắc phục.
        } finally {
            if (msg != null) {
                msg.cleanup(); // Đảm bảo tài nguyên được giải phóng.
            }
        }
    }

    public void sendSmallVersion(Session session) {
        Message ms = null;
        try {
            AbsResources res = find(session.typeClient);
            if (res != null) {
                byte[][] smallVersion = res.getSmallVersion();
                byte[] data = smallVersion[session.zoomLevel - 1];
                ms = new Message(Cmd.SMALLIMAGE_VERSION);
                DataOutputStream ds = ms.writer();
                ds.writeShort(data.length);
                ds.write(data);
                ds.flush();
                session.sendMessage(ms);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Ghi log lỗi để dễ dàng theo dõi và khắc phục.
        } finally {
            if (ms != null) {
                ms.cleanup(); // Đảm bảo tài nguyên được giải phóng.
            }
        }
    }

    public void sendBGVersion(Session session) {
        Message ms = null;
        try {
            AbsResources res = find(session.typeClient);
            if (res != null) {
                byte[][] backgroundVersion = res.getBackgroundVersion();
                byte[] data = backgroundVersion[session.zoomLevel - 1];
                ms = new Message(Cmd.BGITEM_VERSION);
                DataOutputStream ds = ms.writer();
                ds.writeShort(data.length);
                ds.write(data);
                ds.flush();
                session.sendMessage(ms);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Ghi log lỗi để dễ dàng theo dõi và khắc phục.
        } finally {
            if (ms != null) {
                ms.cleanup(); // Đảm bảo tài nguyên được giải phóng.
            }
        }
    }

    public static void requestMobTemplate(Session session, int id) {
        Message msg = null;
        try {
            byte[] mob = FileIO.readFile("image/data/data_mob/x" + session.zoomLevel + "/" + id);
            msg = new Message(11);
            DataOutputStream ds = msg.writer();

            if (id != 86 && id != 87 && id != 88 && id != 89 && id != 85 && id != 94) {
                ds.writeByte(id);
            }
            if (id == 83 || id == 84 || id == 95 || id == 96 || id == 97 || id == 98) {
                ds.writeByte(0);
            }
            ds.write(mob);
            ds.flush();
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace(); // Ghi log lỗi để dễ dàng theo dõi và khắc phục.
        } finally {
            if (msg != null) {
                msg.cleanup(); // Đảm bảo tài nguyên được giải phóng.
            }
        }
    }

    public void downloadIBN(Session session, String filename) {
        Message msg = null;
        try {
            AbsResources res = find(session.typeClient);
            ImageByName ibn = res.getIBN(filename);

            if (ibn != null) {
                byte[] data = res.getRawIBNData(session.zoomLevel, filename);
                msg = new Message(Cmd.GET_IMG_BY_NAME);
                DataOutputStream ds = msg.writer();
                ds.writeUTF(ibn.getFilename());
                ds.writeByte(ibn.getNFame());
                ds.writeInt(data.length);
                ds.write(data);
                ds.flush();
                session.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Ghi log lỗi để dễ dàng theo dõi và khắc phục.
        } finally {
            if (msg != null) {
                msg.cleanup(); // Đảm bảo tài nguyên được giải phóng.
            }
        }
    }

    public void loadMoData(Session session, int id) {
        Message ms = null;
        try {
            AbsResources res = find(session.typeClient);
            if (res != null) {
                MobData mob = res.getMobData(id);
                if (mob != null) {
                    byte[] data = mob.getDataMob();
                    byte[] imgData = res.getRawMobData(session.zoomLevel, id);
                    ms = new Message(Cmd.REQUEST_NPCTEMPLATE);
                    DataOutputStream ds = ms.writer();
                    ds.writeByte(mob.getId());
                    ds.writeByte(mob.getType());
                    ds.writeInt(data.length);
                    ds.write(data);
                    ds.writeInt(imgData.length);
                    ds.write(imgData);
                    ds.writeByte(mob.getTypeData());

                    if (mob.getTypeData() == 1 || mob.getTypeData() == 2) {
                        byte[][] frameBoss = mob.getFrameBoss();
                        ds.writeByte(frameBoss.length);
                        for (byte[] frame : frameBoss) {
                            ds.writeByte(frame.length);
                            ds.write(frame);
                        }
                    }
                    ds.flush();
                    session.sendMessage(ms);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Ghi log lỗi để dễ dàng theo dõi và khắc phục.
        } finally {
            if (ms != null) {
                ms.cleanup(); // Đảm bảo tài nguyên được giải phóng.
            }
        }
    }

    public static void effData(Session session, int id, int... idtemp) {
        int idT = id;
        if (idtemp.length > 0 && idtemp[0] != 0) {
            idT = idtemp[0];
        }

        Message msg = null;
        try {
//            byte[] effData = FileIO.readFile("Eff/effect/x" + session.zoomLevel + "/data/DataEffect_" + idT);
//            byte[] effImg = FileIO.readFile("Eff/effect/x" + session.zoomLevel + "/img/ImgEffect_" + idT + ".png");
String base = "image/"; // hoặc "" nếu bạn đã đặt root đúng
byte[] effData = FileIO.readFile(base + "Eff/effect/x" + session.zoomLevel + "/data/DataEffect_" + idT);
byte[] effImg  = FileIO.readFile(base + "Eff/effect/x" + session.zoomLevel + "/img/ImgEffect_" + idT + ".png");

            if (effData != null && effImg != null) {
                msg = new Message(-66);
                DataOutputStream ds = msg.writer();
                ds.writeShort(id);
                ds.writeInt(effData.length);
                ds.write(effData);
                ds.writeByte(0);
                ds.writeInt(effImg.length);
                ds.write(effImg);
                ds.flush();
                session.sendMessage(msg);
            }
        } catch (Exception e) {
            Logger.logException(Resources.class, e, "Loi keo Res");
        } finally {
            if (msg != null) {
                msg.cleanup(); // Đảm bảo tài nguyên được giải phóng.
            }
        }
    }

    public void loadEffectData(Session session, int id) {
        AbsResources res = find(session.typeClient);
        if (id >= 205 && id < 222) {
            sendEffectTemplate(session, id);
            return;
        }

        if (res != null) {
            Message ms = null;
            try {
                int effId = id;
                if (id == 25 && session.player != null && session.player.zone != null) {
                    byte effDragon = session.player.zone.effDragon;
                    if (effDragon != -1) {
                        effId = effDragon;
                        if (effId == 60 && !session.isVersionAbove(220)) {
                            effId = 61;
                        }
                    }
                    if (SummonDragon.gI().isIcecShenronAppear) {
                        effId = 59;
                    }
                }
                EffectData eff = res.getEffectData(effId);
                if (eff != null) {
                    byte[] data = eff.getData(session.version);
                    byte[] imgData = res.getRawEffectData(session.zoomLevel, effId);
                    ms = new Message(Cmd.GET_EFFDATA);
                    DataOutputStream ds = ms.writer();
                    ds.writeShort(id);
                    ds.writeInt(data.length);
                    ds.write(data);
                    if (session.isVersionAbove(220)) {
                        ds.writeByte(eff.getType());
                    }
                    ds.writeInt(imgData.length);
                    ds.write(imgData);
                    ds.flush();
                    session.sendMessage(ms);
                }
            } catch (Exception e) {
                Log.error(Resources.class, e, "Error loading effect data"); // Cung cấp thông tin lỗi rõ ràng hơn.
            } finally {
                if (ms != null) {
                    ms.cleanup(); // Đảm bảo tài nguyên được giải phóng.
                }
            }
        }
    }

    public static void sendEffectTemplate(Session session, int id) {
        Message msg = null;
        try {
            // Đọc dữ liệu từ tệp
            byte[] effData = FileIO.readFile("image/data/effdata/x" + session.zoomLevel + "/" + id);

            // Tạo đối tượng Message và ghi dữ liệu vào đó
            msg = new Message(-66);
            DataOutputStream ds = msg.writer();
            ds.write(effData);
            ds.flush();

            // Gửi tin nhắn
            session.sendMessage(msg);
        } catch (Exception e) {
            // Ghi log lỗi để dễ dàng theo dõi và xử lý sự cố
            Logger.logException(Resources.class, e, "Error sending effect template data");
        } finally {
            // Đảm bảo tài nguyên của Message được giải phóng
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

}
